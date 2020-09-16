package com.zerotime.zerotime.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.zerotime.zerotime.Pojos.ChatPojo;
import com.zerotime.zerotime.R;
import com.zerotime.zerotime.Secretary.Pojos.SecretaryChatPojo;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    public static final int msgTypeLeft = 0;
    public static final int msgTypeRight = 1;
    Context context;
    private List<ChatPojo> ChatPojos;

    public MessageAdapter(Context context, List<ChatPojo> ChatPojos) {
        this.context = context;
        this.ChatPojos = ChatPojos;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == msgTypeRight){
            View ItemView = LayoutInflater.from(context).inflate(R.layout.item_chat_right,parent,false);
            return new MessageViewHolder(ItemView);
        }
        else{
            View ItemView = LayoutInflater.from(context).inflate(R.layout.item_chat_left,parent,false);
            return new MessageViewHolder(ItemView);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        ChatPojo mchat = ChatPojos.get(position);

        if (mchat.getType().equals("Text")){
            holder.showMessage.setText(mchat.getMessage());
        }else {
            holder.showMessage.setVisibility(View.GONE);
            holder.showMessageImageCard.setVisibility(View.VISIBLE);
            Glide.with(context.getApplicationContext())
                    .load(mchat.getMessage())
                    .into(holder.showMessageImage);
        }

        if (position == ChatPojos.size() - 1){

            if (mchat.isSeen()){
                holder.seen.setText("Seen");
            }else holder.seen.setText("Delivered");

        }else holder.seen.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        return ChatPojos.size();
    }

    public void setList(List<ChatPojo> moviesList) {
        this.ChatPojos = moviesList;
        notifyDataSetChanged();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView showMessage,seen;
        ImageView showMessageImage;
        CardView showMessageImageCard;
        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            showMessage = itemView.findViewById(R.id.showMessage);
            showMessageImage = itemView.findViewById(R.id.showMessageImage);
            showMessageImageCard = itemView.findViewById(R.id.showMessageImageCard);
            seen = itemView.findViewById(R.id.isSeen_text_view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (ChatPojos.get(position).getSender().equals("Zero Time")) {
            return msgTypeLeft;
        } else return msgTypeRight;

    }
}