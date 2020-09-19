package com.zerotime.zerotime;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

import com.gauravk.bubblenavigation.listener.BubbleNavigationChangeListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zerotime.zerotime.Fragments.ContactFragment;
import com.zerotime.zerotime.Fragments.HomeFragment;
import com.zerotime.zerotime.Fragments.ProfileFragment;
import com.zerotime.zerotime.Fragments.SettingsFragment;
import com.zerotime.zerotime.Secretary.Pojos.SecretaryChatPojo;
import com.zerotime.zerotime.databinding.ActivityHomeBinding;

public class Home extends AppCompatActivity {
    private ActivityHomeBinding binding;
    private DatabaseReference chatRef;
    public int notificationID;
    NotificationManager notificationManager;
    String userId;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (binding.bottomNav.getCurrentActiveItemPosition() == 0){
            this.finish();
            return;
        }
        if (binding.bottomNav.getCurrentActiveItemPosition() == 1) {
            binding.bottomNav.setCurrentActiveItem(0);
            return;
        }
        if (binding.bottomNav.getCurrentActiveItemPosition() == 2) {
            binding.bottomNav.setCurrentActiveItem(0);
            return;
        }
        if (binding.bottomNav.getCurrentActiveItemPosition() == 3) {
            binding.bottomNav.setCurrentActiveItem(0);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);





        SharedPreferences prefs = getSharedPreferences("UserState", MODE_PRIVATE);
        userId = prefs.getString("isLogged", "");

        chatRef = FirebaseDatabase.getInstance().getReference("Chats");
        chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int unread = 0;
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    SecretaryChatPojo chat = ds.getValue(SecretaryChatPojo.class);
                    if (chat.getReceiver().equals(userId) && !chat.isSeen()) {
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
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(Home.this, "my_channel_01")
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setVibrate(new long[]{1000, 1000})
                            .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .setContentTitle("رسالة جديدة")
                            .setContentText("لديك رساله جديدة من الإدارة");

                    /*Intent resultIntent = new Intent(ctx, MainActivity.class);
                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(ctx);
                    stackBuilder.addParentStack(MainActivity.class);
                    stackBuilder.addNextIntent(resultIntent);
                    PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

                    builder.setContentIntent(resultPendingIntent);*/

                    notificationManager.notify(notificationID, builder.build());

                    /*if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        String CHANNEL_ID = "my_channel_02";
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

                        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(Navigation.this, "02")
                                .setSmallIcon(R.drawable.ic_launcher_background)
                                .setVibrate(new long[]{1000, 1000})
                                .setContentTitle("New Message !")
                                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                                .setContentText("You May Have new Messages")
                                .setStyle(new NotificationCompat.BigTextStyle().bigText("You May Have new Messages"))
                                .setPriority(NotificationCompat.PRIORITY_MAX)
                                .setWhen(0);

                        NotificationID = (int) System.currentTimeMillis();
                        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                        manager.notify(NotificationID, mBuilder.build());
                    */
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // Default Fragment To Open
        getSupportFragmentManager().beginTransaction().replace(R.id.Frame_Content, new HomeFragment()).commit();

        //Attach Listener To Bottom Nav
        //binding.bottomNav.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);

        binding.bottomNav.setNavigationChangeListener(new BubbleNavigationChangeListener() {
            @Override
            public void onNavigationChanged(View view, int position) {
                //navigation changed, do something
                switch (position) {
                    case 0:
                        Fragment newFragment = new HomeFragment();
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.Frame_Content, newFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();

                        break;
                    case 1:
                        ProfileFragment fragment2 = new ProfileFragment();
                        FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction2.replace(R.id.Frame_Content, fragment2);
                        fragmentTransaction2.addToBackStack(null);
                        fragmentTransaction2.commit();
                        break;
                    case 2:
                        ContactFragment fragment3 = new ContactFragment();
                        FragmentTransaction fragmentTransaction3 = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction3.replace(R.id.Frame_Content, fragment3);
                        fragmentTransaction3.addToBackStack(null);
                        fragmentTransaction3.commit();
                        break;
                    case 3:
                        SettingsFragment fragment4 = new SettingsFragment();
                        FragmentTransaction fragmentTransaction4 = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction4.replace(R.id.Frame_Content, fragment4);
                        fragmentTransaction4.addToBackStack(null);
                        fragmentTransaction4.commit();
                        break;
                }
            }
        });
    }

}