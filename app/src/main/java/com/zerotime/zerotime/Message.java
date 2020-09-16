package com.zerotime.zerotime;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Gallery;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.zerotime.zerotime.Adapters.MessageAdapter;
import com.zerotime.zerotime.Pojos.ChatPojo;
import com.zerotime.zerotime.Secretary.Pojos.SecretaryChatPojo;
import com.zerotime.zerotime.databinding.ActivityMessageBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Message extends AppCompatActivity {
    private ActivityMessageBinding binding;
    SharedPreferences prefs;
    MessageAdapter adapter;
    List<ChatPojo> chatPojos;
    DatabaseReference reference;
    ValueEventListener seenListener;
    String userId, intentFrom;

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

        if (getIntent().getStringExtra("UniqueID") != null) {
            if (Objects.requireNonNull(getIntent().getStringExtra("UniqueID")).equals("ContactFragment")) {
                userId = getIntent().getStringExtra("UserID");
                intentFrom = "ContactFragment";

            }
        }


        prefs = getSharedPreferences("UserState", MODE_PRIVATE);
        binding.messageSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = binding.messageWriteMSGEdt.getText().toString();
                if (!msg.equals("")) {
                    if (intentFrom != null) {
                        if (intentFrom.equals("ContactFragment")) {
                            sendMessage(userId
                                    , "Zero Time", msg);

                        }
                    }

                } else
                    Toast.makeText(Message.this, "You Cant't Sent Empty Message!!", Toast.LENGTH_SHORT).show();
                binding.messageWriteMSGEdt.setText("");
            }
        });
        ReadMessages();

        seenMessage(userId);
    }

    private void seenMessage(final String userid) {
        reference = FirebaseDatabase.getInstance().getReference("Chats");
        seenListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ChatPojo chat = snapshot.getValue(ChatPojo.class);
                    assert chat != null;
                    if (chat.getReceiver().equals(userid) && chat.getSender().equals("Zero Time")) {
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("isSeen", true);
                        snapshot.getRef().updateChildren(hashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void sendMessage(String Sender, String Receiver, String Message) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("Sender", Sender);
        hashMap.put("Receiver", Receiver);
        hashMap.put("Message", Message);
        hashMap.put("isSeen", false);
        reference.child("Chats").push().setValue(hashMap);

    }

    private void ReadMessages() {
        chatPojos = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatPojos.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ChatPojo mChatPojo = dataSnapshot.getValue(ChatPojo.class);
                    if (mChatPojo != null && (mChatPojo.getReceiver().equals("Zero Time") && mChatPojo.getSender().equals(userId) ||
                            mChatPojo.getReceiver().equals(userId) && mChatPojo.getSender().equals("Zero Time"))) {
                        chatPojos.add(mChatPojo);
                    }
                    adapter = new MessageAdapter(Message.this, chatPojos);
                    binding.messageRecycler.setAdapter(adapter);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        final DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("ChatList")
                .child(Objects.requireNonNull(userId))
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
                .child(userId);
        chatRefReceiver.child("Receiver_ID").setValue(userId);

    }

    @Override
    protected void onPause() {
        super.onPause();
        reference.removeEventListener(seenListener);
    }
}