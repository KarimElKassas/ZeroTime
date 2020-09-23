package com.zerotime.zerotime.Secretary.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.zerotime.zerotime.R;
import com.zerotime.zerotime.Secretary.Pojos.SecretaryChatPojo;

import java.util.List;

public class SecretaryMessageAdapter extends RecyclerView.Adapter<SecretaryMessageAdapter.MessageViewHolder> {
    public static final int msgTypeLeft = 0;
    public static final int msgTypeRight = 1;
    Context context;
    private List<SecretaryChatPojo> secretaryChatPojos;

    public SecretaryMessageAdapter(Context context, List<SecretaryChatPojo> secretaryChatPojos) {
        this.context = context;
        this.secretaryChatPojos = secretaryChatPojos;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == msgTypeRight){
            View ItemView = LayoutInflater.from(context).inflate(R.layout.item_chat_right_red,parent,false);
            return new MessageViewHolder(ItemView);
        }
        else{
            View ItemView = LayoutInflater.from(context).inflate(R.layout.item_chat_left_red,parent,false);
            return new MessageViewHolder(ItemView);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        SecretaryChatPojo mchat = secretaryChatPojos.get(position);
        if (mchat.getType().equals("Text")){
            holder.showMessage.setText(mchat.getMessage());
        }else {
            holder.showMessage.setVisibility(View.GONE);
            holder.showMessageImageCard.setVisibility(View.VISIBLE);
            Glide.with(context.getApplicationContext())
                    .load(mchat.getMessage())
                    .into(holder.showMessageImage);
        }
        if (position == secretaryChatPojos.size() - 1){

            if (mchat.isSeen()){
                holder.seen.setText("Seen");
            }else holder.seen.setText("Delivered");

        }else holder.seen.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return secretaryChatPojos.size();
    }

    public void setList(List<SecretaryChatPojo> moviesList) {
        this.secretaryChatPojos = moviesList;
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
        if (secretaryChatPojos.get(position).getSender().equals("Zero Time")) {
            return msgTypeRight;
        } else return msgTypeLeft;

    }
}