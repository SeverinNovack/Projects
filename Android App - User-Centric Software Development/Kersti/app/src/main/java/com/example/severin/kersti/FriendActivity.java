package com.example.severin.kersti;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    private DatabaseReference userBase;
    private FirebaseAuth firebaseAuth;
    private String user_id;
    private Button showTags;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        getSupportActionBar().setTitle("Freunde");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = (RecyclerView) findViewById(R.id.friend_view);
        firebaseAuth = FirebaseAuth.getInstance();
        user_id = firebaseAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Freunde").child(user_id);
        databaseReference.keepSynced(true);
        userBase = FirebaseDatabase.getInstance().getReference().child("Users");
        userBase.keepSynced(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Friends, FriendsViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Friends, FriendsViewHolder>(Friends.class, R.layout.searchlist, FriendsViewHolder.class, databaseReference) {
            @Override
            protected void populateViewHolder(final FriendsViewHolder viewHolder, Friends model, int position) {
                final String id = getRef(position).getKey();
                userBase.child(id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String name = dataSnapshot.child("name").getValue().toString();
                        String pic = dataSnapshot.child("profilepic").getValue().toString();
                        HashMap<String, Boolean> tags = (HashMap<String, Boolean>) dataSnapshot.child("Tags").getValue();
                        viewHolder.setName(name);
                        viewHolder.setPic(pic);
                        final ArrayList<String> s = viewHolder.getTags(tags);
                        showTags = viewHolder.getButton();
                        showTags.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(FriendActivity.this, ShowTagActivity.class);
                                intent.putExtra("tags", s);
                                startActivity(intent);
                            }
                        });
                        viewHolder.view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CharSequence options[] = new CharSequence[]{"Profil","Nachricht schreiben"};
                                final AlertDialog.Builder builder = new AlertDialog.Builder(FriendActivity.this);
                                builder.setTitle("Optionen");
                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if(which == 0) {
                                            Intent intent = new Intent(FriendActivity.this, ProfilActivity.class);
                                            intent.putExtra("user_id", id);
                                            intent.putExtra("tags", s);
                                            startActivity(intent);
                                        }
                                        else if(which == 1){
                                            Intent intent = new Intent(FriendActivity.this, ChatActivity.class);
                                            intent.putExtra("user_id", id);
                                            startActivity(intent);
                                        }
                                    }
                                });
                                builder.show();
                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);

    }

    public static class FriendsViewHolder extends RecyclerView.ViewHolder {
        View view;
        Button button;
        public FriendsViewHolder(View v) {
            super(v);
            view = v;
        }
        public void setName(String name) {
            TextView textView = (TextView) view.findViewById(R.id.chat_name);
            textView.setText(name);
        }
        public void setPic(String pic) {
            if(!pic.equals("link")) {
                CircleImageView circleImageView = (CircleImageView) view.findViewById(R.id.chat_pic);
                Picasso.with(view.getContext()).load(pic).placeholder(R.drawable.placeholder).into(circleImageView);
            }
        }
        public Button getButton() {
            button = (Button) view.findViewById(R.id.search_tags);
            return button;
        }

        public ArrayList<String> getTags(HashMap<String,Boolean> Tags) {
            HashMap<String, Boolean> map = Tags;
            ArrayList<String> list = new ArrayList<>();
            for (HashMap.Entry<String, Boolean> entry : map.entrySet()) {
                String key = entry.getKey();
                Boolean value = entry.getValue();
                if(value == true) {
                    list.add(key);
                }
            }
            return list;
        }
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
