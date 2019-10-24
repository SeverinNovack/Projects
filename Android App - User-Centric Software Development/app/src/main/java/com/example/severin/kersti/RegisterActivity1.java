package com.example.severin.kersti;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.theartofdev.edmodo.cropper.CropImageActivity;

import java.util.HashMap;

public class RegisterActivity1 extends AppCompatActivity {
    private TextView username;
    private TextView mail;
    private TextView password;
    private Button nextButton;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReference2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register1);
        username = (TextView) findViewById(R.id.reg_username);
        mail = (TextView) findViewById(R.id.reg_mail);
        password = (TextView) findViewById(R.id.reg_pw);
        nextButton = (Button) findViewById(R.id.reg1_next);
        mAuth = FirebaseAuth.getInstance();
        getSupportActionBar().setTitle("Registrierung");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progressDialog = new ProgressDialog(this);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uname = username.getText().toString().trim();
                String email = mail.getText().toString().trim();
                String pw = password.getText().toString().trim();

                if(TextUtils.isEmpty(uname) || TextUtils.isEmpty(email) || TextUtils.isEmpty(pw)) {

                }
                else {
                    progressDialog.setTitle("Registrierung");
                    progressDialog.setMessage("Benutzer wird erstellt");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    register(uname, email, pw);
                }
            }

            private void register(final String user_name, String email, String pw) {
                mAuth.createUserWithEmailAndPassword(email, pw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {

                            FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                            String user_id = current_user.getUid();
                            databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
                            databaseReference2 = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id).child("Tags");
                            HashMap<String, String> map = new HashMap<>();
                            map.put("name", user_name);
                            map.put("profilepic", "link");
                            //map.put("thumbnail", "link");
                            databaseReference.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        progressDialog.dismiss();
                                        Intent intent = new Intent(RegisterActivity1.this, RegisterActivity2.class);
                                        startActivity(intent);
                                        LoginActivity.ac1.finish();
                                        finish();
                                    }
                                }
                            });


                        }
                        else {
                            progressDialog.hide();
                            Toast.makeText(RegisterActivity1.this, "Registrierung fehlgeschlagen. Überprüfen Sie ihre Eingabe.", Toast.LENGTH_LONG).show();
                        }
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
