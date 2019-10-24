package com.example.severin.kersti;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ProfilTagActivity extends AppCompatActivity {

    private ListView listView;
    ArrayList<String> list = new ArrayList<>();
    private Button fertig;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_tag);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Tags");
        ArrayList<String> list = (ArrayList<String>) getIntent().getSerializableExtra("tags");
        String[] s = new String[list.size()];
        s = list.toArray(s);
        if((s.length == 0))
            s = new String[] {"Es sind keine Tags vorhanden"};
        listView = (ListView) findViewById(R.id.ProfTags_Listview);
        fertig = (Button) findViewById(R.id.ProfTags_Button);
        fertig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, s);
        listView.setAdapter(arrayAdapter);

    }

}
