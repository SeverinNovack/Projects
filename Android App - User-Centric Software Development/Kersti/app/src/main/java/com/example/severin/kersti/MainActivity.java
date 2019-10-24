package com.example.severin.kersti;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private RecyclerView recyclerView;
    private DatabaseReference chatBase;
    private DatabaseReference msgBase;
    private DatabaseReference userBase;
    private StorageReference storageReference;
    private static int count = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        //mAuth.signOut();
        getSupportActionBar().setTitle("Kersti");
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView = (NavigationView) findViewById(R.id.nav);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case(R.id.menu_logout): {
                        FirebaseAuth.getInstance().signOut();
                        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(loginIntent);
                        finish();
                        break;
                    }
                    case(R.id.menu_profile): {
                        Intent intent = new Intent(MainActivity.this, UserSettings.class);
                        startActivity(intent);
                        break;
                    }
                    case(R.id.menu_delete): {
                        CharSequence options[] = new CharSequence[]{"Profil löschen", "Nicht löschen"};
                        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("Bist du dir sicher?");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(which == 0) {
                                    user = FirebaseAuth.getInstance().getCurrentUser();
                                    final String user_id = user.getUid();
                                    //User
                                    databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
                                    databaseReference.removeValue();
                                    //Chats
                                    databaseReference = FirebaseDatabase.getInstance().getReference().child("Chats").child(user_id);
                                    databaseReference.removeValue();
                                    final Query chatQuery = FirebaseDatabase.getInstance().getReference().child("Chats").orderByKey();
                                    chatQuery.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                snapshot.getRef().child(user_id).removeValue();
                                            }
                                            chatQuery.removeEventListener(this);
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                    //Messages
                                    databaseReference = FirebaseDatabase.getInstance().getReference().child("Messages").child(user_id);
                                    databaseReference.removeValue();
                                    final Query messageQuery = FirebaseDatabase.getInstance().getReference().child("Messages").orderByKey();
                                    messageQuery.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                snapshot.child(user_id).getRef().removeValue();
                                            }
                                            messageQuery.removeEventListener(this);
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                    //Freunde
                                    databaseReference = FirebaseDatabase.getInstance().getReference().child("Freunde").child(user_id);
                                    databaseReference.removeValue();
                                    final Query friendQuery = FirebaseDatabase.getInstance().getReference().child("Freunde").orderByKey();
                                    friendQuery.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                snapshot.child(user_id).getRef().removeValue();
                                            }
                                            friendQuery.removeEventListener(this);
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                    //Anfragen
                                    databaseReference = FirebaseDatabase.getInstance().getReference().child("Anfragen").child(user_id);
                                    databaseReference.removeValue();
                                    final Query anfragenQuery = FirebaseDatabase.getInstance().getReference().child("Anfragen").orderByKey();
                                    anfragenQuery.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                snapshot.child(user_id).getRef().removeValue();
                                            }
                                            anfragenQuery.removeEventListener(this);
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                    //Bild
                                    storageReference = FirebaseStorage.getInstance().getReference();
                                    StorageReference path = storageReference.child("profile_pics").child(user_id + ".jpg");
                                    path.delete();
                                    mAuth.signOut();
                                    user.delete();
                                    Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                                    startActivity(loginIntent);
                                    finish();
                                }
                                else if(which == 1){

                                }
                            }
                        });
                        builder.show();
                        break;


                    }
                    case(R.id.menu_search): {
                        final ArrayList<String> ownTags = new ArrayList<>();
                        final String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        Query query = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id).child("Tags");
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                HashMap<String, Boolean> map = (HashMap<String, Boolean>) dataSnapshot.getValue();
                                for (HashMap.Entry<String, Boolean> entry : map.entrySet()) {
                                    String key = entry.getKey();
                                    Boolean value = entry.getValue();
                                    if(value == true) {
                                        ownTags.add(key);
                                    }
                                }
                                final ArrayList<String> list = new ArrayList<>();
                                Query query1 = FirebaseDatabase.getInstance().getReference().child("Freunde").child(mAuth.getCurrentUser().getUid());
                                query1.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            list.add(snapshot.getKey());
                                        }
                                        Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                                        intent.putExtra("tags", ownTags);
                                        intent.putExtra("id", mAuth.getCurrentUser().getUid());
                                        intent.putExtra("friendlist", list);
                                        startActivity(intent);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });


                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        break;
                    }
                    case(R.id.menu_friends): {
                        Intent intent = new Intent(MainActivity.this, FriendActivity.class);
                        startActivity(intent);
                        break;
                    }
                    case(R.id.menu_anfragen): {
                        String id = mAuth.getCurrentUser().getUid();
                        Intent intent = new Intent(MainActivity.this, AnfragenActivity.class);
                        intent.putExtra("id", id);
                        startActivity(intent);
                        break;
                    }
                }
            return true;
            }

        });
        recyclerView = (RecyclerView) findViewById(R.id.main_chatview);
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null) {
            Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
        }
        else {
            final String user_id = mAuth.getCurrentUser().getUid();
            databaseReference = FirebaseDatabase.getInstance().getReference().child("Chats");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.hasChild(user_id) || count > 0) {
                        if(!dataSnapshot.hasChild(user_id)) {
                            drawerLayout.openDrawer(Gravity.START);
                        }
                        chatBase = FirebaseDatabase.getInstance().getReference().child("Chats").child(user_id);
                        chatBase.keepSynced(true);
                        msgBase = FirebaseDatabase.getInstance().getReference().child("Messages").child(user_id);
                        msgBase.keepSynced(true);
                        userBase = FirebaseDatabase.getInstance().getReference().child("Users");
                        userBase.keepSynced(true);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
                        linearLayoutManager.setReverseLayout(true);
                        linearLayoutManager.setStackFromEnd(true);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(linearLayoutManager);
                    }
                    else {
                        count++;
                        CharSequence options[] = new CharSequence[]{"Profil bearbeiten","Gleichgesinnte suchen", "Ausloggen"};
                        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("Beginnen");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(which == 0) {
                                    Intent intent = new Intent(MainActivity.this, UserSettings.class);
                                    startActivity(intent);
                                }
                                else if(which == 1){
                                    final ArrayList<String> ownTags = new ArrayList<>();
                                    final String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                    Query query = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id).child("Tags");
                                    query.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            HashMap<String, Boolean> map = (HashMap<String, Boolean>) dataSnapshot.getValue();
                                            for (HashMap.Entry<String, Boolean> entry : map.entrySet()) {
                                                String key = entry.getKey();
                                                Boolean value = entry.getValue();
                                                if(value == true) {
                                                    ownTags.add(key);
                                                }
                                            }
                                            final ArrayList<String> list = new ArrayList<>();
                                            Query query1 = FirebaseDatabase.getInstance().getReference().child("Freunde").child(mAuth.getCurrentUser().getUid());
                                            query1.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                        list.add(snapshot.getKey());
                                                    }
                                                    Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                                                    intent.putExtra("tags", ownTags);
                                                    intent.putExtra("id", mAuth.getCurrentUser().getUid());
                                                    intent.putExtra("friendlist", list);
                                                    startActivity(intent);
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });


                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }
                                else if(which == 2) {
                                    mAuth.signOut();
                                    Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                                    startActivity(loginIntent);
                                    finish();
                                }
                            }
                        });
                        builder.show();
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null) {
            Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
        }
        chatBase =  FirebaseDatabase.getInstance().getReference().child("Chats");
        chatBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(currentUser.getUid())) {
                    Query query = chatBase.orderByChild("timestamp");
                    FirebaseRecyclerAdapter<Chat, ChatViewHolder> chatAdapter = new FirebaseRecyclerAdapter<Chat, ChatViewHolder>(Chat.class, R.layout.chatlistl, ChatViewHolder.class, query) {
                        @Override
                        protected void populateViewHolder(final ChatViewHolder viewHolder, Chat model, int position) {
                            final String id = getRef(position).getKey();
                            Query lastMsg = msgBase.child(id).limitToLast(1);
                            lastMsg.addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                    String data = dataSnapshot.child("inhalt").getValue().toString();
                                    viewHolder.setLast(data);
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
                            Query query1 = FirebaseDatabase.getInstance().getReference().child("Chats").child(currentUser.getUid());
                            query1.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.hasChild(id)) {
                                        userBase.child(id).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                final String name = dataSnapshot.child("name").getValue().toString();
                                                String pic = dataSnapshot.child("profilepic").getValue().toString();
                                                viewHolder.setName(name);
                                                viewHolder.setPic(pic);
                                                viewHolder.setVisibility(true);
                                                viewHolder.view.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                                                        intent.putExtra("user_id", id);
                                                        startActivity(intent);
                                                    }
                                                });
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                    else {
                                        viewHolder.setVisibility(false);
                                    }

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        }
                    };
                    recyclerView.setAdapter(chatAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Zum ausfahren der Seitenleiste
        if(actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        View view;

        public ChatViewHolder(View itemView) {
            super(itemView);
            view = itemView;
        }

        public void setLast(String msg) {
            TextView textView = (TextView) view.findViewById(R.id.chat_last);
            textView.setText(msg);
        }
        public void setName(String name) {
            TextView textView = (TextView) view.findViewById(R.id.chat_name);
            textView.setText(name);
        }
        public void setPic(String pic) {
            CircleImageView circleImageView = (CircleImageView) view.findViewById(R.id.chat_pic);
            Picasso.with(view.getContext()).load(pic).placeholder(R.drawable.placeholder).into(circleImageView);
        }
        public void setVisibility(boolean b) {
            RecyclerView.LayoutParams param = (RecyclerView.LayoutParams)view.getLayoutParams();
            if (b){
                param.height = LinearLayout.LayoutParams.WRAP_CONTENT;
                param.width = LinearLayout.LayoutParams.MATCH_PARENT;
                view.setVisibility(View.VISIBLE);
            }else{
                view.setVisibility(View.GONE);
                param.height = 0;
                param.width = 0;
            }
            view.setLayoutParams(param);

        }

    }
}
