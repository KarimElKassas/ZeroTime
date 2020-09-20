package com.zerotime.zerotime.Secretary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zerotime.zerotime.Login;
import com.zerotime.zerotime.Moderator.ModeratorHome;
import com.zerotime.zerotime.Moderator.ModeratorViewClerks;
import com.zerotime.zerotime.R;
import com.zerotime.zerotime.Secretary.Pojos.SecretaryChatPojo;
import com.zerotime.zerotime.databinding.ActivitySecretaryHomeBinding;
import com.zerotime.zerotime.myBroadCast;

public class SecretaryHome extends AppCompatActivity {
    private ActivitySecretaryHomeBinding binding;
    private DatabaseReference chatRef;
    public int notificationID;
    NotificationManager notificationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySecretaryHomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        checkInternetConnection();

        chatRef = FirebaseDatabase.getInstance().getReference("Chats");
        chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int unread = 0;
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    SecretaryChatPojo chat = ds.getValue(SecretaryChatPojo.class);
                    if (chat.getReceiver().equals("Zero Time") && !chat.isSeen()) {
                        unread++;
                    }

                }
                if (unread != 0) {
                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

                        String CHANNEL_ID = "my_channel_01";
                        CharSequence name = "my_channel";
                        String Description = "This is my channel";
                        int importance = NotificationManager.IMPORTANCE_HIGH;
                        NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
                        mChannel.setDescription(Description);
                        mChannel.enableLights(true);
                        mChannel.setLightColor(Color.RED);
                        mChannel.enableVibration(true);
                        mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                        mChannel.setShowBadge(false);
                        notificationManager.createNotificationChannel(mChannel);
                    }
                    notificationID = (int) System.currentTimeMillis();
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(SecretaryHome.this,"my_channel_01")
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setVibrate(new long[]{1000, 1000})
                            .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .setContentTitle("لديك رسالة جديدة")
                            .setContentText("لديك رسالة جديدة من احد العملاء");


                    notificationManager.notify(notificationID, builder.build());


                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        binding.secretaryHomeOrdersBtn.setOnClickListener(view1 -> {
            Intent intent = new Intent(SecretaryHome.this,FollowingTheOrderState.class);
           intent.putExtra("from","S");
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();

        });
        binding.secretaryHomeChatsBtn.setOnClickListener(view1 -> {
            Intent intent = new Intent(SecretaryHome.this, SecretaryDisplayChats.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();

        });



    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(SecretaryHome.this, Login.class);
        startActivity(i);
        finish();
    }
    private void checkInternetConnection(){
        myBroadCast broadCast=new myBroadCast();
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(broadCast,intentFilter);
    }
}