package com.zerotime.zerotime.Secretary.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zerotime.zerotime.R;
import com.zerotime.zerotime.Secretary.Pojos.Chat;
import com.zerotime.zerotime.Secretary.Pojos.Users;

import java.util.List;

public class DisplayChatsAdapter extends RecyclerView.Adapter<DisplayChatsAdapter.ViewHolder> {
    private Context context;
    private List<Users> mUsers;
    private String theLastMessage;

    public DisplayChatsAdapter(Context context, List<Users> mUsers) {
        this.context = context;
        this.mUsers = mUsers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.display_users_row, parent, false);

        return new DisplayChatsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Users user = mUsers.get(position);
        holder.userName.setText(user.getUserName());
        lastMessage(user.getUserPrimaryPhone(), holder.lastMsg);
        holder.chatCard.setOnClickListener(v -> {

            /*Intent intent = new Intent(context, ChatAct.class);
            intent.putExtra("UniqueID","from_DisplayChatsAdapter");
            intent.putExtra("CustomerID",user.getUser_ID());
            context.startActivity(intent);
            ((Activity)context).overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);*/
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView userName, lastMsg;
        CardView chatCard;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.display_users_row_user_name);
            lastMsg = itemView.findViewById(R.id.display_users_row_last_message);
            chatCard = itemView.findViewById(R.id.display_users_row_card);
        }
    }

    private void lastMessage(final String userPrimaryPhone, final TextView last_msg) {
        theLastMessage = "default";
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");

        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = new Chat();
                    chat.setSender(snapshot.child("Sender").getValue(String.class));
                    chat.setReceiver(snapshot.child("Receiver").getValue(String.class));
                    chat.setMessage(snapshot.child("Message").getValue(String.class));

                    if (chat.getSender().equals("Zero Time") && chat.getReceiver().equals(userPrimaryPhone) ||
                            chat.getSender().equals(userPrimaryPhone) && chat.getReceiver().equals("Zero Time")) {
                        theLastMessage = chat.getMessage();

                    }
                }

                if ("default".equals(theLastMessage)) {
                    last_msg.setText("لا توجد رسائل");
                } else {
                    last_msg.setText(theLastMessage);
                }

                theLastMessage = "default";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
