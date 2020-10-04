package com.zerotime.zerotime.Secretary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.zerotime.zerotime.Moderator.ModeratorHome;
import com.zerotime.zerotime.Moderator.ModeratorNumberOfOrders;
import com.zerotime.zerotime.Notifications.Token;
import com.zerotime.zerotime.Secretary.Adapters.DisplayChatsAdapter;
import com.zerotime.zerotime.Secretary.Pojos.ChatList;
import com.zerotime.zerotime.Secretary.Pojos.Users;
import com.zerotime.zerotime.databinding.SecretaryActivityDisplayChatsBinding;
import com.zerotime.zerotime.myBroadCast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class SecretaryDisplayChats extends AppCompatActivity {
    private SecretaryActivityDisplayChatsBinding binding;

    private DisplayChatsAdapter userAdapter;
    private List<Users> mUsers;
    private List<ChatList> userList;
    private DatabaseReference chatListRef1, chatListRef2;
    String userToken;
    Random random;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SecretaryActivityDisplayChatsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        checkInternetConnection();
        binding.secretaryDisplayChatsRecycler.setHasFixedSize(true);
        binding.secretaryDisplayChatsRecycler.setLayoutManager(new LinearLayoutManager(this));

        random = new Random();


        userList = new ArrayList<>();

        chatListRef1 = FirebaseDatabase.getInstance().getReference("ChatList").child("Zero Time");
        chatListRef2 = FirebaseDatabase.getInstance().getReference("ChatList");

        chatListRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    String userPrimaryPhone = ds.child("Receiver_ID").getValue(String.class);
                    ChatList chatList = new ChatList();
                    chatList.setUserPrimaryPhone(userPrimaryPhone);
                    userList.add(chatList);

                }

                chatList();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //Update User Token ID
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                userToken = Objects.requireNonNull(task.getResult()).getToken();
                refreshToken(userToken);
            }
        });


    }

    private void refreshToken(String token) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        reference.child("Zero Time").setValue(token1);
    }

    private void chatList() {
        mUsers = new ArrayList<>();
        chatListRef1 = FirebaseDatabase.getInstance().getReference("Users");
        chatListRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mUsers.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String userPrimaryPhone = ds.child("UserPrimaryPhone").getValue(String.class);
                    String userName = ds.child("UserName").getValue(String.class);
                    int rand_int = random.nextInt(10);

                    Users users = new Users();
                    users.setUserPrimaryPhone(userPrimaryPhone);
                    users.setUserName(userName);
                    users.setRandom(rand_int);

                    for (ChatList chatList : userList) {
                        //Toast.makeText(context,users.getUser_ID() + "\n" + chatList.getUser_ID(),Toast.LENGTH_SHORT).show();

                        if (users.getUserPrimaryPhone().equals(chatList.getUserPrimaryPhone())) {
                            mUsers.add(users);
                        }
                    }
                }
                userAdapter = new DisplayChatsAdapter(SecretaryDisplayChats.this, mUsers);
                binding.secretaryDisplayChatsRecycler.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.secretaryDisplayChatsRecycler.setHasFixedSize(true);
        binding.secretaryDisplayChatsRecycler.setLayoutManager(new LinearLayoutManager(this));
        //--------------------------------
        userList = new ArrayList<>();

        chatListRef1 = FirebaseDatabase.getInstance().getReference("ChatList").child("Zero Time");
        chatListRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String userPrimaryPhone = ds.child("Receiver_ID").getValue(String.class);
                    ChatList chatList = new ChatList();
                    chatList.setUserPrimaryPhone(userPrimaryPhone);
                    userList.add(chatList);

                }

                chatList();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void checkInternetConnection() {
        myBroadCast broadCast = new myBroadCast();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(broadCast, intentFilter);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(SecretaryDisplayChats.this, SecretaryHome.class);
        startActivity(i);
        finish();
    }
}