package com.zerotime.zerotime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zerotime.MessageAdapter;
import com.zerotime.zerotime.databinding.ActivityMessageBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Message extends AppCompatActivity {
    private ActivityMessageBinding binding;
    SharedPreferences prefs;
    MessageAdapter adapter;
    List<Chat> chatList;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMessageBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.messageRecycler.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        binding.messageRecycler.setLayoutManager(linearLayoutManager);


        prefs = getSharedPreferences("UserState", MODE_PRIVATE);
        binding.messageSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = binding.messageWriteMSGEdt.getText().toString();
                if (!msg.equals("")) {
                    sendMessage(prefs.getString("isLogged", "")
                            , "Zero Time", msg);

                } else
                    Toast.makeText(Message.this, "You Cant't Sent Empty Message!!", Toast.LENGTH_SHORT).show();
                binding.messageWriteMSGEdt.setText("");
            }
        });
ReadMessages(prefs.getString("isLogged", ""),"Zero Time");
    }

    private void sendMessage(String Sender, String Receiver, String Message) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("Sender", Sender);
        hashMap.put("Receiver", Receiver);
        hashMap.put("Message", Message);
        reference.child("Chats").push().setValue(hashMap);

    }

    private void ReadMessages(final String myID, final String userID) {
        chatList = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Chat mChat = dataSnapshot.getValue(Chat.class);
                    if (mChat != null && (mChat.getReceiver().equals("Zero Time") && mChat.getSender().equals(prefs.getString("isLogged", "")) ||
                            mChat.getReceiver().equals(prefs.getString("isLogged", "")) && mChat.getSender().equals("Zero Time"))) {
                        chatList.add(mChat);
                    }
                    adapter = new MessageAdapter(Message.this, chatList);
                    binding.messageRecycler.setAdapter(adapter);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        final DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("ChatList")
                .child(prefs.getString("isLogged", ""))
                .child("Zero Time");

        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    chatRef.child("Receiver_ID").setValue("Zero Time");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final DatabaseReference chatRefReceiver = FirebaseDatabase.getInstance().getReference("ChatList")
                .child("Zero Time")
                .child(prefs.getString("isLogged", ""));
        chatRefReceiver.child("Receiver_ID").setValue(prefs.getString("isLogged", ""));

    }

}