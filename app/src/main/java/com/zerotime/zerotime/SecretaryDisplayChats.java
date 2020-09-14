package com.zerotime.zerotime;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zerotime.zerotime.Secretary.Adapters.DisplayChatsAdapter;
import com.zerotime.zerotime.Secretary.Pojos.ChatList;
import com.zerotime.zerotime.Secretary.Pojos.Users;
import com.zerotime.zerotime.databinding.ActivitySecretaryDisplayChatsBinding;

import java.util.ArrayList;
import java.util.List;

public class SecretaryDisplayChats extends AppCompatActivity {
    private ActivitySecretaryDisplayChatsBinding binding;

    private DisplayChatsAdapter userAdapter;
    private List<Users> mUsers;
    private List<ChatList> userList;
    private DatabaseReference chatListRef1,chatListRef2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySecretaryDisplayChatsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.secretaryDisplayChatsRecycler.setHasFixedSize(true);
        binding.secretaryDisplayChatsRecycler.setLayoutManager(new LinearLayoutManager(this));

        userList = new ArrayList<>();

        chatListRef1 = FirebaseDatabase.getInstance().getReference("ChatList").child("Zero Time");
        chatListRef2 = FirebaseDatabase.getInstance().getReference("ChatList");

        chatListRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String userPrimaryPhone = ds.child("Receiver").getValue(String.class);
                    ChatList chatList = new ChatList();
                    chatList.setUserPrimaryPhone(userPrimaryPhone);
                    userList.add(chatList);

                }
                //Toast.makeText(context,String.valueOf(userList.size()),Toast.LENGTH_LONG).show();

                chatList();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    private void chatList() {
        mUsers = new ArrayList<>();
        chatListRef1 = FirebaseDatabase.getInstance().getReference("Users");
        chatListRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mUsers.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    String userPrimaryPhone = ds.child("UserPrimaryPhone").getValue(String.class);
                    String userName = ds.child("UserName").getValue(String.class);

                    Users users = new Users();
                    users.setUserPrimaryPhone(userPrimaryPhone);
                    users.setUserName(userName);

                    for (ChatList chatList : userList){
                        //Toast.makeText(context,users.getUser_ID() + "\n" + chatList.getUser_ID(),Toast.LENGTH_SHORT).show();

                        if (users.getUserPrimaryPhone().equals(chatList.getUserPrimaryPhone())){
                            mUsers.add(users);
                        }
                    }
                }
                userAdapter = new DisplayChatsAdapter(SecretaryDisplayChats.this,mUsers);
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
                    String userPrimaryPhone = ds.child("Receiver").getValue(String.class);
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
}