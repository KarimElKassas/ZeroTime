package com.zerotime.zerotime;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.zerotime.zerotime.Fragments.AboutUsFragment;
import com.zerotime.zerotime.Fragments.AddOrderFragment;
import com.zerotime.zerotime.Fragments.ContactFragment;
import com.zerotime.zerotime.Fragments.DisplayOffersFragment;
import com.zerotime.zerotime.Fragments.HomeFragment;
import com.zerotime.zerotime.Fragments.ProfileFragment;
import com.zerotime.zerotime.Fragments.SettingsFragment;
import com.zerotime.zerotime.databinding.UserActivityHomeBinding;

public class Home extends AppCompatActivity {
    String userId;
    private UserActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = UserActivityHomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // Check Internet State
        if (!haveNetworkConnection()) {
            Intent i = new Intent(Home.this, No_Internet_Connection.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
        }
        checkInternetConnection();
        //-----------------------------------

        SharedPreferences prefs = getSharedPreferences("UserState", MODE_PRIVATE);
        userId = prefs.getString("isLogged", "");

        // Default Fragment To Open
        getSupportFragmentManager().beginTransaction().replace(R.id.Frame_Content, new HomeFragment()).commit();

        //Attach Listener To Bottom Nav
        binding.bottomNav.setCurrentActiveItem(3);
        binding.bottomNav.setNavigationChangeListener((view1, position) -> {

            //navigation changed, do something

            switch (position) {
                case 3:
                    Fragment newFragment = new HomeFragment();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.Frame_Content, newFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                    break;

                case 2:
                    ProfileFragment fragment2 = new ProfileFragment();
                    FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction2.replace(R.id.Frame_Content, fragment2);
                    fragmentTransaction2.addToBackStack(null);
                    fragmentTransaction2.commit();
                    break;

                case 1:
                    ContactFragment fragment3 = new ContactFragment();
                    FragmentTransaction fragmentTransaction3 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction3.replace(R.id.Frame_Content, fragment3);
                    fragmentTransaction3.addToBackStack(null);
                    fragmentTransaction3.commit();
                    break;

                case 0:
                    SettingsFragment fragment4 = new SettingsFragment();
                    FragmentTransaction fragmentTransaction4 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction4.replace(R.id.Frame_Content, fragment4);
                    fragmentTransaction4.addToBackStack(null);
                    fragmentTransaction4.commit();
                    break;

            }
        });
    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    private void checkInternetConnection() {
        MyBroadCast broadCast = new MyBroadCast();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(broadCast, intentFilter);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (binding.bottomNav.getCurrentActiveItemPosition() == 3) {

            this.finish();
            return;
        }
        if (binding.bottomNav.getCurrentActiveItemPosition() == 2) {

            binding.bottomNav.setCurrentActiveItem(3);
            return;
        }
        if (binding.bottomNav.getCurrentActiveItemPosition() == 1) {
            binding.bottomNav.setCurrentActiveItem(3);
            return;
        }
        if (binding.bottomNav.getCurrentActiveItemPosition() == 0) {
            binding.bottomNav.setCurrentActiveItem(3);
        }

    }

}