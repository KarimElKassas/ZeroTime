package com.zerotime;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.zerotime.zerotime.R;
import com.zerotime.zerotime.Chat;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    public static final int msgTypeLeft = 0;
    public static final int msgTypeRight = 1;
    Context context;
    private List<Chat> Chats = new ArrayList<>();

    public MessageAdapter(Context context, List<Chat> Chats) {
        this.context = context;
        this.Chats = Chats;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == msgTypeRight)
            return new MessageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_right, parent, false));
        else
            return new MessageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_left, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Chat mchat = Chats.get(position);
        holder.showMessage.setText(mchat.getMessage());

    }

    @Override
    public int getItemCount() {
        return Chats.size();
    }

    public void setList(List<Chat> moviesList) {
        this.Chats = moviesList;
        notifyDataSetChanged();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView showMessage;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            showMessage = itemView.findViewById(R.id.showMessage);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (Chats.get(position).getReceiver().equals("Zero Time")) {
            return msgTypeRight;
        } else return msgTypeLeft;

    }
}