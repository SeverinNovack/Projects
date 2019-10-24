
package com.example.severin.kersti;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity2 extends AppCompatActivity {

    private CircleImageView profilePic;
    private Button changePic;
    private Button next;
    private Button tags;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReference2;
    private FirebaseUser user;
    private static final int GALLERY_PICK = 1;
    private StorageReference storageReference;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);
        getSupportActionBar().setTitle("Profilbild");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        user = FirebaseAuth.getInstance().getCurrentUser();
        String user_id = user.getUid();
        databaseReference2 = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id).child("Tags");
        HashMap<String, Boolean> map = createMap();
        databaseReference2.setValue(map);
        profilePic = (CircleImageView) findViewById(R.id.reg2_profilepic);
        tags = (Button) findViewById(R.id.reg2_tagButton);
        tags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity2.this, RegisterActivity3.class);
                startActivity(intent);
            }
        });
        changePic = (Button) findViewById(R.id.reg2_changeButton);
        next = (Button) findViewById(R.id.reg2_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity2.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        storageReference = FirebaseStorage.getInstance().getReference();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                String pic = dataSnapshot.child("profilepic").getValue().toString();
                //String thumb = dataSnapshot.child("thumbnail").getValue().toString();
                //String tags = dataSnapshot.child("tags").getValue().toString();
                Picasso.with(RegisterActivity2.this).load(pic).into(profilePic);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        changePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/+");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Bild auswählen"), GALLERY_PICK);


            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GALLERY_PICK) {
            if (resultCode == RESULT_OK) {
                progressDialog = new ProgressDialog(RegisterActivity2.this);
                progressDialog.setTitle("Bild wird hochgeladen");
                progressDialog.setMessage("Dies könnte einen Moment dauern");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                Uri resultUri = data.getData();
                CropImage.activity(resultUri).setAspectRatio(1,1).start(this);
                StorageReference path = storageReference.child("profile_pics").child(user.getUid() + ".jpg");
                path.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()) {

                            String url = task.getResult().getDownloadUrl().toString();
                            databaseReference.child("profilepic").setValue(url).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        progressDialog.dismiss();
                                    }
                                }
                            });
                        }
                    }
                });
            }
        }
    }
    private HashMap<String,Boolean> createMap() {
        HashMap<String, Boolean> map = new HashMap<>();
        map.put("Gaming", false);
        map.put("Kochen", false);
        map.put("Zeichnen", false);
        map.put("Fantasy", false);
        map.put("Sci-Fi", false);
        map.put("Sport", false);
        map.put("Roleplay", false);
        map.put("Pen and Paper", false);
        map.put("Cosplay", false);
        map.put("Game of Thrones", false);

        return map;
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


