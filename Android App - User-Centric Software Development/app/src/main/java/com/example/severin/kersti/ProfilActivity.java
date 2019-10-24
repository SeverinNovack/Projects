package com.example.severin.kersti;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ProfilActivity extends AppCompatActivity {

    private TextView textName;
    private Button tagButton;
    private Button friendButton;
    private Button declineButton;
    private DatabaseReference databaseReference;
    private ImageView imageView;
    private int status;
    private DatabaseReference anfrageBase;
    private FirebaseUser user;
    private DatabaseReference friendBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        final String id = getIntent().getStringExtra("user_id");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        declineButton = (Button) findViewById(R.id.profil_AblehnenButton);
        declineButton.setEnabled(false);
        declineButton.setVisibility(View.INVISIBLE);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(id);
        anfrageBase = FirebaseDatabase.getInstance().getReference().child("Anfragen");
        friendBase = FirebaseDatabase.getInstance().getReference().child("Freunde");
        user = FirebaseAuth.getInstance().getCurrentUser();
        imageView = (ImageView) findViewById(R.id.profile_pic);
        status = 0;
        tagButton = (Button) findViewById(R.id.profile_tagButton);
        tagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> list = (ArrayList<String>) getIntent().getSerializableExtra("tags");
                Intent intent = new Intent(ProfilActivity.this, ProfilTagActivity.class);
                intent.putExtra("tags", list);
                startActivity(intent);
            }
        });
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                String pic = dataSnapshot.child("profilepic").getValue().toString();
                textName = (TextView) findViewById(R.id.profile_name);
                textName.setText(name);
                if(!pic.equals("link"))
                Picasso.with(ProfilActivity.this).load(pic).placeholder(R.drawable.placeholder).into(imageView);
                getSupportActionBar().setTitle(name);
                anfrageBase.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild(id)) {
                            String stat = dataSnapshot.child(id).child("Anfrage").getValue().toString();
                            if(stat.equals("bekommen")) {
                                status = 2;
                                friendButton.setText("Bestätigen");
                                declineButton.setEnabled(true);
                                declineButton.setVisibility(View.VISIBLE);
                            }
                            else if(stat.equals("verschickt")) {
                                status = 0;
                                friendButton.setText("Zurückziehen");
                            }
                        }
                        else {
                            friendBase.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.hasChild(id)) {
                                        status = 3;
                                        friendButton.setText("Freund entfernen");
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
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
        friendButton = (Button) findViewById(R.id.profile_anfrage);
        friendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                friendButton.setEnabled(false);
                if(status == 0) {
                    anfrageBase.child(user.getUid()).child(id).child("Anfrage").setValue("verschickt");
                    anfrageBase.child(id).child(user.getUid()).child("Anfrage").setValue("bekommen").addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            friendButton.setEnabled(true);
                            status = 1;
                            friendButton.setText("Zurückziehen");
                            Toast.makeText(ProfilActivity.this, "Anfrage verschickt", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                if(status == 1) {
                    anfrageBase.child(user.getUid()).child(id).removeValue();
                    anfrageBase.child(id).child(user.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            friendButton.setEnabled(true);
                            status = 0;
                            friendButton.setText("Freundesanfrage");
                            declineButton.setEnabled(false);
                            declineButton.setVisibility(View.INVISIBLE);
                        }
                    });
                }
                if(status == 2) {
                    final String datum = DateFormat.getDateTimeInstance().format(new Date());
                    friendBase.child(user.getUid()).child(id).child("datum").setValue(datum).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            friendBase.child(id).child(user.getUid()).child("datum").setValue(datum).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    anfrageBase.child(user.getUid()).child(id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            anfrageBase.child(id).child(user.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    friendButton.setEnabled(true);
                                                    status = 3;
                                                    friendButton.setText("Freund entfernen");
                                                    declineButton.setEnabled(false);
                                                    declineButton.setVisibility(View.INVISIBLE);
                                                }
                                            });
                                        }
                                    });
                                }
                            });
                        }
                    });
                }
                if(status == 3) {
                    friendBase.child(user.getUid()).child(id).removeValue();
                    friendBase.child(id).child(user.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            friendButton.setEnabled(true);
                            status = 0;
                            friendButton.setText("Freundesanfrage");
                            Toast.makeText(ProfilActivity.this, "Freund entfernt", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anfrageBase.child(user.getUid()).child(id).removeValue();
                anfrageBase.child(id).child(user.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        friendButton.setEnabled(true);
                        status = 0;
                        friendButton.setText("Freundesanfrage");
                        declineButton.setEnabled(false);
                        declineButton.setVisibility(View.INVISIBLE);
                    }
                });
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
