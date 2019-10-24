package com.example.severin.kersti;

import android.app.Notification;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<Messages> list;
    private FirebaseAuth firebaseAuth;

    public MessageAdapter(List<Messages> list) {
        this.list = list;

    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.chatlayout, viewGroup, false);
        return new MessageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder messageViewHolder, int i) {
        firebaseAuth = FirebaseAuth.getInstance();
        String user_id = firebaseAuth.getCurrentUser().getUid();
        Messages messages = list.get(i);
        String s = messages.getFrom();
        if(user_id.equals(messages.getFrom()) || s == null) {
            messageViewHolder.msg1.setText(messages.getInhalt());
            messageViewHolder.msg1.setVisibility(View.VISIBLE);
            messageViewHolder.msg.setVisibility(View.INVISIBLE);
        }
        else {
            messageViewHolder.msg.setText(messages.getInhalt());
            messageViewHolder.msg.setVisibility(View.VISIBLE);
            messageViewHolder.msg1.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {
        public TextView msg;
        public TextView msg1;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);

            msg = (TextView) itemView.findViewById(R.id.chatlayout_msg);
            msg1 = (TextView) itemView.findViewById(R.id.chatlayout_msg1);
        }
    }
}

