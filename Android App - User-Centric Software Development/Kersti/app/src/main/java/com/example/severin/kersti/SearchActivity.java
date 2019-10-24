package com.example.severin.kersti;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    private Button showTags;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        recyclerView = (RecyclerView) findViewById(R.id.search_list);
        getSupportActionBar().setTitle("Gleichgesinnte");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");


    }
    @Override
    public void onStart(){
        super.onStart();
        final ArrayList<String> ownTags = (ArrayList<String>) getIntent().getSerializableExtra("tags");
        Query query = FirebaseDatabase.getInstance().getReference().child("Users").child(getIntent().getStringExtra("id")).child("name");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                name = dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        final ArrayList<String> list = (ArrayList<String>) getIntent().getSerializableExtra("friendlist");
        final ArrayList<String> friendlist = new ArrayList<>();
        String[] s = new String[list.size()];
        s = list.toArray(s);
        for (int i = 0; i < s.length; i++) {
            Query query1 = FirebaseDatabase.getInstance().getReference().child("Users").child(s[i]).child("name");
            query1.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    friendlist.add(dataSnapshot.getValue().toString());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }


        FirebaseRecyclerAdapter<User, UserViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<User, UserViewHolder>(User.class, R.layout.searchlist, UserViewHolder.class, databaseReference) {
            @Override
            protected void populateViewHolder(UserViewHolder viewHolder, User model, int position) {
                viewHolder.compare(model.getTags(), ownTags);
                double percent = viewHolder.getA();
                String per = Double.toString(percent) + " %";
                viewHolder.setName(model.getName());
                viewHolder.setPic(model.getProfilepic());
                viewHolder.setPercent(per);
                showTags = viewHolder.getButton();
                final ArrayList<String> s = model.getTags();
                Boolean b = false;
                Boolean c = false;
                String[] list = new String[friendlist.size()];
                list = friendlist.toArray(list);
                for (int i = 0; i < list.length; i++) {
                    if(model.getName().equals(list[i])) {
                        c = true;
                        break;
                    }
                }
                if(percent > 70 && !(name.equals(model.getName())) && c == false) {
                    b = true;
                }
                viewHolder.setVisibility(b);
                showTags.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        Intent intent = new Intent(SearchActivity.this, ShowTagActivity.class);
                        intent.putExtra("tags", s);
                        startActivity(intent);
                        }
                    });
                final String user_id = getRef(position).getKey();
                viewHolder.view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(SearchActivity.this, ProfilActivity.class);
                            intent.putExtra("user_id", user_id);
                            intent.putExtra("tags", s);
                            startActivity(intent);
                        }
                    });

                }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);

    }

    public static class UserViewHolder extends RecyclerView.ViewHolder{

        View view;
        Button button;
        private ArrayList<String> ownTags = new ArrayList<>();
        private double a;
        public double getA() {
            return a;
        }


        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
        }
        public void setName(String name){
            TextView textView = (TextView) view.findViewById(R.id.chat_name);
            textView.setText(name);
        }
        public void setPic(String profilepic) {
            if(!profilepic.equals("link")) {
                CircleImageView circleImageView = (CircleImageView) view.findViewById(R.id.chat_pic);
                Picasso.with(view.getContext()).load(profilepic).placeholder(R.drawable.placeholder).into(circleImageView);
            }
        }
        public void setPercent(String percent) {
            TextView textView = (TextView) view.findViewById(R.id.searchlist_percent);
            textView.setText(percent);
        }
        public Button getButton() {
            button = (Button) view.findViewById(R.id.search_tags);
            return button;
        }
        public void compare(ArrayList<String> list, ArrayList<String> ownTags) {
            if (ownTags.size() > list.size())
                a = percent(ownTags, list, true);
            else
                a = percent(list, ownTags, false);
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
    static double percent(ArrayList<String> big, ArrayList<String> small, boolean selbst) {
        double a;
        String[] bigs = new String[big.size()];
        bigs = big.toArray(bigs);
        String[] smalls = new String[small.size()];
        smalls = small.toArray(smalls);
        ArrayList<String> same = new ArrayList<>();
        for (int i = 0; i < bigs.length; i++) {
            for (int j = 0; j < smalls.length; j++) {
                if(bigs[i].equals(smalls[j])) {
                    if(selbst == true) {
                        same.add(bigs[i]);
                    }
                    else
                        same.add(smalls[j]);
                }
            }
        }
        String[] sames = new String[same.size()];
        sames = same.toArray(sames);
        if(selbst == true){
            double b = sames.length;
            double c = bigs.length;
            a = b/c;
            a = a * 100;
        }
        else {
            double b = sames.length;
            double c = bigs.length;
            a =  c/b;
            a = a *100;
        }
        if(a > 100) {
            a = 100;
        }
        return a;
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