package com.example.severin.kersti;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class UserSettings extends AppCompatActivity {

    private TextView username;
    private CircleImageView profilePic;
    private Button changePic;
    private Button tags;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private static final int GALLERY_PICK = 1;
    private StorageReference storageReference;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);
        getSupportActionBar().setTitle("Dein Profil");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        username = (TextView) findViewById(R.id.us_name);
        profilePic = (CircleImageView) findViewById(R.id.user_profilepic);
        changePic = (Button) findViewById(R.id.us_changeButton);
        tags = (Button) findViewById(R.id.us_tagsButton);
        user = FirebaseAuth.getInstance().getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();
        String user_id = user.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
        databaseReference.keepSynced(true);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                final String pic = dataSnapshot.child("profilepic").getValue().toString();
                //String thumb = dataSnapshot.child("thumbnail").getValue().toString();
                //String tags = dataSnapshot.child("tags").getValue().toString();
                username.setText(name);
                if(!pic.equals("link")) {
                    Picasso.with(UserSettings.this).load(pic).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.placeholder).into(profilePic, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            Picasso.with(UserSettings.this).load(pic).placeholder(R.drawable.placeholder).into(profilePic);
                        }
                    });
                }
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
        tags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserSettings.this, TagActivity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GALLERY_PICK) {
            if (resultCode == RESULT_OK) {
                progressDialog = new ProgressDialog(UserSettings.this);
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
