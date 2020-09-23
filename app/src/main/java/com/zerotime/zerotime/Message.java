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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.zerotime.zerotime.Adapters.MessageAdapter;
import com.zerotime.zerotime.Interfaces.ApiService;
import com.zerotime.zerotime.Notifications.Client;
import com.zerotime.zerotime.Notifications.Data;
import com.zerotime.zerotime.Notifications.MyResponse;
import com.zerotime.zerotime.Notifications.Sender;
import com.zerotime.zerotime.Notifications.Token;
import com.zerotime.zerotime.Pojos.ChatPojo;
import com.zerotime.zerotime.databinding.UserActivityMessageBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Message extends AppCompatActivity {
    private UserActivityMessageBinding binding;
    private static final int GALLERY_PICK = 0;
    ApiService apiService;

    SharedPreferences prefs;
    MessageAdapter adapter;
    List<ChatPojo> chatPojos;
    DatabaseReference chatRef,userRef;
    ValueEventListener seenListener;
    String userId, intentFrom,userName;
    boolean notify = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = UserActivityMessageBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        apiService = Client.getClient("https://fcm.googleapis.com/").create(ApiService.class);

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

        binding.secretaryMessageSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notify = true;
                String msg = Objects.requireNonNull(binding.secretaryMessageWriteMSGEdt.getText()).toString();
                if (!msg.equals("")) {
                    if (intentFrom != null) {
                        if (intentFrom.equals("ContactFragment")) {
                            sendMessage(userId
                                    , "Zero Time", msg);

                        }
                    }

                } else
                    Toast.makeText(Message.this, "You Cant't Sent Empty Message!!", Toast.LENGTH_SHORT).show();
                binding.secretaryMessageWriteMSGEdt.setText("");

            }
        });
        binding.secretaryMessageSendImage.setOnClickListener(view1 -> {
            Intent galleryIntent = new Intent();
            galleryIntent.setType("image/*");
            galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(galleryIntent,"SELECT IMAGE"),GALLERY_PICK);
        });
        ReadMessages();

        seenMessage(userId);

    }
    private void sendNotification(final String userName, String message){
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo("Zero Time");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Token token = dataSnapshot.getValue(Token.class);

                    Data data = new Data(userId,userName + ": " + message,"لديك رسالة جديدة",userId,R.mipmap.ic_launcher_round);

                    assert token != null;
                    Sender sender = new Sender(data,token.getToken());
                    apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
                        @Override
                        public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                            if (response.code() == 200){
                                assert response.body() != null;
                                if (response.body().success != 1){
                                    Toast.makeText(Message.this, "Failed !", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<MyResponse> call, Throwable t) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void seenMessage(final String userid){
        chatRef = FirebaseDatabase.getInstance().getReference("Chats");
        seenListener = chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ChatPojo chat = snapshot.getValue(ChatPojo.class);
                    assert chat != null;
                    if (chat.getReceiver().equals(userid) && chat.getSender().equals("Zero Time")){
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
        hashMap.put("Type", "Text");
        reference.child("Chats").push().setValue(hashMap);

        final String msg = Objects.requireNonNull(binding.secretaryMessageWriteMSGEdt.getText()).toString();
        userRef = FirebaseDatabase.getInstance().getReference("Users");
        userRef.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    if (snapshot.hasChildren()){
                        userName = snapshot.child("UserName").getValue(String.class);
                        if (notify){
                            sendNotification(userName,msg);
                        }
                        notify = false;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void ReadMessages() {
        chatPojos = new ArrayList<>();
        chatRef = FirebaseDatabase.getInstance().getReference("Chats");
        chatRef.addValueEventListener(new ValueEventListener() {
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK){
            Uri imageUri = data.getData();

            final String currentUserRef = "messages/" + userId + "/" + "Zero Time";
            final String chatUserRef = "messages/" + "Zero Time" + "/" + userId;

            DatabaseReference userMessagePush = FirebaseDatabase.getInstance()
                    .getReference("Messages").child(userId).child("Zero Time").push();
            final String pushID = userMessagePush.getKey();

            StorageReference filePath = FirebaseStorage.getInstance().getReference("Messages")
                    .child("MessageImages").child(pushID + ".jpg");
            assert imageUri != null;
            filePath.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()){
                        FirebaseStorage.getInstance().getReference("Messages")
                                .child("MessageImages").child(pushID + ".jpg")
                                .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String downloadUrl = uri.toString();
                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("Sender", userId);
                                hashMap.put("Receiver", "Zero Time");
                                hashMap.put("Message", downloadUrl);
                                hashMap.put("isSeen", false);
                                hashMap.put("Type", "Image");
                                chatRef.push().setValue(hashMap);

                            }
                        });
                    }
                }
            });
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        chatRef.removeEventListener(seenListener);
    }
}