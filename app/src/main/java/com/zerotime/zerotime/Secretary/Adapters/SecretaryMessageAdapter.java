package com.zerotime.zerotime.Secretary.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.zerotime.zerotime.R;
import com.zerotime.zerotime.Secretary.Pojos.SecretaryChatPojo;

import java.util.List;
import java.util.Objects;

public class SecretaryMessageAdapter extends RecyclerView.Adapter<SecretaryMessageAdapter.MessageViewHolder> {
    public static final int msgTypeLeft = 0;
    public static final int msgTypeRight = 1;
    Context context;
    Dialog imageDialog;
    private List<SecretaryChatPojo> secretaryChatPojos;

    public SecretaryMessageAdapter(Context context, List<SecretaryChatPojo> secretaryChatPojos) {
        this.context = context;
        this.secretaryChatPojos = secretaryChatPojos;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == msgTypeRight) {
            View ItemView = LayoutInflater.from(context).inflate(R.layout.item_chat_right_red, parent, false);
            return new MessageViewHolder(ItemView);
        } else {
            View ItemView = LayoutInflater.from(context).inflate(R.layout.item_chat_left_red, parent, false);
            return new MessageViewHolder(ItemView);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        SecretaryChatPojo mchat = secretaryChatPojos.get(position);
        if (mchat.getType().equals("Text")) {
            holder.showMessage.setText(mchat.getMessage());
        } else {
            holder.showMessage.setVisibility(View.GONE);
            holder.showMessageImageCard.setVisibility(View.VISIBLE);
            Glide.with(context.getApplicationContext())
                    .load(mchat.getMessage())
                    .into(holder.showMessageImage);

            holder.showMessageImage.setOnClickListener(view -> {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                final AlertDialog dialog = builder.create();
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dialogLayout = inflater.inflate(R.layout.dialog_chat_image, null);
                dialog.setView(dialogLayout);

                dialog.requestWindowFeature(Window.DECOR_CAPTION_SHADE_DARK);

                ImageView image = dialogLayout.findViewById(R.id.dialog_chat_image_imageView);
                try {
                    Objects.requireNonNull(image).setImageDrawable(holder.showMessageImage.getDrawable());

                } catch (Exception e) {
                    Toast.makeText(context, "catch\n" + e.getMessage(), Toast.LENGTH_LONG).show();
                }

                dialog.show();

               /* dialog.setOnShowListener((DialogInterface.OnShowListener) d -> {
                    Toast.makeText(context, "on show", Toast.LENGTH_SHORT).show();
                    ImageView image = (ImageView) dialog.findViewById(R.id.dialog_chat_image_imageView);
                    if (image == null){
                        Toast.makeText(context, "null image", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(context, "not null image", Toast.LENGTH_SHORT).show();
                        image.setImageDrawable(holder.showMessageImage.getDrawable());
                    }
                });*/
            });
        }
        if (position == secretaryChatPojos.size() - 1) {

            if (mchat.isSeen()) {
                holder.seen.setText("Seen");
            } else holder.seen.setText("Delivered");

        } else holder.seen.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return secretaryChatPojos.size();
    }

    public void setList(List<SecretaryChatPojo> moviesList) {
        this.secretaryChatPojos = moviesList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (secretaryChatPojos.get(position).getSender().equals("Zero Time")) {
            return msgTypeRight;
        } else return msgTypeLeft;

    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView showMessage, seen;
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
}