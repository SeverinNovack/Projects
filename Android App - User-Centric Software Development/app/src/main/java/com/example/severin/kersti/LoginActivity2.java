package com.example.severin.kersti;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity2 extends AppCompatActivity {

    private Button logButton;
    private TextView email;
    private TextView pw;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progressDialog = new ProgressDialog(this);
        email = (TextView) findViewById(R.id.log2_mail);
        pw = (TextView) findViewById(R.id.log2_pw);
        logButton = (Button) findViewById(R.id.log2_button);
        mAuth = FirebaseAuth.getInstance();
        logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String semail = email.getText().toString().trim();
                String passwort = pw.getText().toString().trim();

                if(!(TextUtils.isEmpty(semail) || TextUtils.isEmpty(passwort))) {
                    progressDialog.setTitle("Login");
                    progressDialog.setMessage("Benutzer wird eingeloggt");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    loginUser(semail, passwort);
                }
            }
        });
    }

    private void loginUser(String semail, String passwort) {
        mAuth.signInWithEmailAndPassword(semail, passwort).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    progressDialog.dismiss();
                    Intent intent = new Intent(LoginActivity2.this, MainActivity.class);
                    startActivity(intent);
                    LoginActivity.ac1.finish();
                    finish();
                }
                    else {
                    progressDialog.hide();
                    Toast.makeText(LoginActivity2.this, "Login fehlgeschlagen. Bitte überprüfen Sie ihre Eingabe.", Toast.LENGTH_LONG).show();
                }
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

