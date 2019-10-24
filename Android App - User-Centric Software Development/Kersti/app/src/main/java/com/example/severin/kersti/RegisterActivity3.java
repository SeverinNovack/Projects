package com.example.severin.kersti;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class RegisterActivity3 extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    ArrayList<String> selectedTags = new ArrayList<>();
    private Button fertigButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register3);
        getSupportActionBar().setTitle("Tags");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        user = FirebaseAuth.getInstance().getCurrentUser();
        String user_id = user.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id).child("Tags");

        fertigButton = (Button) findViewById(R.id.reg3_next);
        ListView lv = (ListView) findViewById(R.id.lview);
        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        final String[] tags = {"Cosplay", "Fantasy", "Game of Thrones", "Gaming", "Kochen", "Pen and Paper", "Roleplay", "Sci-Fi", "Sport", "Zeichnen"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.taglist, R.id.taglistlayout, tags);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = ((TextView)view).getText().toString();
                if(selectedTags.contains(selectedItem)) {
                    selectedTags.remove(selectedItem);
                }
                else
                    selectedTags.add(selectedItem);
            }
        });
        fertigButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedTags.size() > 0) {
                    String[] s = new String[selectedTags.size()];
                    s = selectedTags.toArray(s);
                    for (int i = 0; i < s.length; i++) {
                        String string = s[i];
                        databaseReference.child(string).setValue(true);
                    }
                    finish();
                }
                else
                    finish();
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
