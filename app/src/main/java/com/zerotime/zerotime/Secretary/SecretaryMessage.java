package com.zerotime.zerotime.Secretary;

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
import com.zerotime.zerotime.Adapters.MessageAdapter;
import com.zerotime.zerotime.Secretary.Adapters.SecretaryMessageAdapter;
import com.zerotime.zerotime.Secretary.Pojos.SecretaryChatPojo;
import com.zerotime.zerotime.databinding.ActivitySecretaryMessageBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class SecretaryMessage extends AppCompatActivity {
    private ActivitySecretaryMessageBinding binding;
    SharedPreferences prefs;
    SecretaryMessageAdapter adapter;
    List<SecretaryChatPojo> secretaryChatPojoList;
    DatabaseReference reference;
    ValueEventListener seenListener;
    String userId, intentFrom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySecretaryMessageBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.secretaryMessageRecycler.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        binding.secretaryMessageRecycler.setLayoutManager(linearLayoutManager);

        if (getIntent().getStringExtra("UniqueID") != null) {
            if (Objects.requireNonNull(getIntent().getStringExtra("UniqueID")).equals("DisplayChatsAdapter")) {
                intentFrom = "DisplayChatsAdapter";
                userId = getIntent().getStringExtra("UserID");

            }
        }
        prefs = getSharedPreferences("UserState", MODE_PRIVATE);
        binding.secretaryMessageSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = binding.secretaryMessageWriteMSGEdt.getText().toString();
                if (!msg.equals("")) {
                    if (intentFrom != null) {
                        sendMessage("Zero Time", userId, msg);
                    }


                } else
                    Toast.makeText(SecretaryMessage.this, "You Cant't Sent Empty Message!!", Toast.LENGTH_SHORT).show();
                binding.secretaryMessageWriteMSGEdt.setText("");
            }
        });
        ReadMessages();

        seenMessage(userId);
    }
    private void seenMessage(final String userid){
        reference = FirebaseDatabase.getInstance().getReference("Chats");
        seenListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    SecretaryChatPojo chat = snapshot.getValue(SecretaryChatPojo.class);
                    assert chat != null;
                    if (chat.getReceiver().equals("Zero Time") && chat.getSender().equals(userid)){
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

    /*private void seenMessage(final String userPrimaryPhone) {
        reference = FirebaseDatabase.getInstance().getReference("Chats");
        seenListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    SecretaryChatPojo secretaryChatPojo = snapshot.getValue(SecretaryChatPojo.class);
                    //Secretary Case
                    if (intentFrom.equals("DisplayChatsAdapter")) {
                        assert secretaryChatPojo != null;
                        if (secretaryChatPojo.getReceiver().equals("Zero Time") && secretaryChatPojo.getSender().equals(userPrimaryPhone)) {
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("isSeen", true);
                            snapshot.getRef().updateChildren(hashMap);
                        }

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }*/

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
        secretaryChatPojoList = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                secretaryChatPojoList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    SecretaryChatPojo mSecretaryChatPojo = dataSnapshot.getValue(SecretaryChatPojo.class);
                    if (mSecretaryChatPojo != null && (mSecretaryChatPojo.getReceiver().equals("Zero Time") && mSecretaryChatPojo.getSender().equals(userId) ||
                            mSecretaryChatPojo.getReceiver().equals(userId) && mSecretaryChatPojo.getSender().equals("Zero Time"))) {
                        secretaryChatPojoList.add(mSecretaryChatPojo);
                    }
                    adapter = new SecretaryMessageAdapter(SecretaryMessage.this, secretaryChatPojoList);
                    binding.secretaryMessageRecycler.setAdapter(adapter);
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