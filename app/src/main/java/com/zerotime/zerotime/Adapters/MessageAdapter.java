package com.zerotime.zerotime.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.zerotime.zerotime.Pojos.ChatPojo;
import com.zerotime.zerotime.R;
import com.zerotime.zerotime.Secretary.Pojos.SecretaryChatPojo;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
            Glide.with(context.getApplicationContext()).load(mchat.getMessage())
                    .apply(new RequestOptions()
                            .fitCenter()
                            .format(DecodeFormat.PREFER_ARGB_8888)
                            .override(1000,1000))
                    .fitCenter()
                    .placeholder(R.drawable.ic_dots)
                    .error(R.drawable.ic_warning)
                    .into(holder.showMessageImage);
        }
        holder.showMessageImage.setOnClickListener(view -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View dialoglayout = inflater.inflate(R.layout.dialog_chat_image, null);

            builder.setView(dialoglayout);

            ImageView image = dialoglayout.findViewById(R.id.dialog_chat_image_imageView);
            try {
                Glide.with(context.getApplicationContext()).load(mchat.getMessage())
                        .apply(new RequestOptions()
                                .fitCenter()
                                .format(DecodeFormat.PREFER_ARGB_8888)
                                .override(Target.SIZE_ORIGINAL))
                        .placeholder(R.drawable.avatar1)
                        .into(image);

            } catch (Exception e) {
                Toast.makeText(context, "catch\n" + e.getMessage(), Toast.LENGTH_LONG).show();
            }
            builder.show();


        });

        if (position == ChatPojos.size() - 1){

            if (mchat.isSeen()){
                holder.seen.setText("تم العرض");
            }else holder.seen.setText("تم الارسال");

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

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
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