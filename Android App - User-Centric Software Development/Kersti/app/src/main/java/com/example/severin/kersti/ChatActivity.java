package com.example.severin.kersti;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    private String partnerID;
    private DatabaseReference userBase;
    private DatabaseReference chatBase;
    private DatabaseReference msgBase;
    private FirebaseAuth firebaseAuth;
    private String user_id;
    private ImageButton addButton;
    private ImageButton sendButton;
    private TextView msg;
    private RecyclerView recyclerView;
    private final List<Messages> messagesList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private MessageAdapter messageAdapter;
    private String partnerName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        sendButton = (ImageButton) findViewById(R.id.chat_sendButton);
        msg = (TextView) findViewById(R.id.chat_msg);
        partnerID = getIntent().getStringExtra("user_id");
        userBase = FirebaseDatabase.getInstance().getReference().child("Users").child(partnerID);
        chatBase = FirebaseDatabase.getInstance().getReference().child("Chats");
        msgBase = FirebaseDatabase.getInstance().getReference().child("Messages");
        firebaseAuth = FirebaseAuth.getInstance();
        user_id = firebaseAuth.getCurrentUser().getUid();
        messageAdapter = new MessageAdapter(messagesList);
        recyclerView = (RecyclerView) findViewById(R.id.chat_view);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(messageAdapter);
        loadMessages();
        userBase.child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                partnerName = dataSnapshot.getValue().toString();
                getSupportActionBar().setTitle(partnerName);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        chatBase.child(user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChild(partnerID)) {

                    chatBase.child(user_id).child(partnerID).child("timestamp").setValue(ServerValue.TIMESTAMP);
                    chatBase.child(partnerID).child(user_id).child("timestamp").setValue(ServerValue.TIMESTAMP);
                    Map chatMap = new HashMap();
                    chatMap.put("timestamp", ServerValue.TIMESTAMP);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String m = msg.getText().toString();
                if(!TextUtils.isEmpty(m)){
                    String push_id = msgBase.child(user_id).child(partnerID).push().getKey();
                    Map ownMap = new HashMap();
                    ownMap.put("inhalt", m);
                    ownMap.put("typ", "text");
                    ownMap.put("timestamp", ServerValue.TIMESTAMP);
                    ownMap.put("from", user_id);

                    Map otherMap = new HashMap();
                    otherMap.put("inhalt", m);
                    otherMap.put("typ", "text");
                    otherMap.put("timestamp", ServerValue.TIMESTAMP);
                    otherMap.put("from", user_id);
                    msgBase.child(user_id).child(partnerID).child(push_id).setValue(ownMap);
                    msgBase.child(partnerID).child(user_id).child(push_id).setValue(otherMap);
                    /*
                    msgBase.child(user_id).child(partnerID).child(push_id).child("inhalt").setValue(m);
                    msgBase.child(user_id).child(partnerID).child(push_id).child("typ").setValue("text");
                    msgBase.child(user_id).child(partnerID).child(push_id).child("timestamp").setValue(ServerValue.TIMESTAMP);
                    msgBase.child(user_id).child(partnerID).child(push_id).child("from").setValue(user_id);
                    msgBase.child(partnerID).child(user_id).child(push_id).child("inhalt").setValue(m);
                    msgBase.child(partnerID).child(user_id).child(push_id).child("typ").setValue("text");
                    msgBase.child(partnerID).child(user_id).child(push_id).child("timestamp").setValue(ServerValue.TIMESTAMP);
                    msgBase.child(partnerID).child(user_id).child(push_id).child("from").setValue(user_id);
                    */
                    msg.setText("");
                }
            }
        });
    }

    private void loadMessages() {
        msgBase.child(user_id).child(partnerID).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Messages messages = dataSnapshot.getValue(Messages.class);
                messagesList.add(messages);
                messageAdapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(messagesList.size()-1);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}
