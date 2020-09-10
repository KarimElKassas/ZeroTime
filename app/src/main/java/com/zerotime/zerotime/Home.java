package com.zerotime.zerotime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.zerotime.zerotime.Fragments.ContactFragment;
import com.zerotime.zerotime.Fragments.HomeFragment;
import com.zerotime.zerotime.Fragments.ProfileFragment;
import com.zerotime.zerotime.Fragments.SettingsFragment;
import com.zerotime.zerotime.databinding.ActivityHomeBinding;
import com.zerotime.zerotime.databinding.ActivitySignUpBinding;

public class Home extends AppCompatActivity {
    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // Default Fragment To Open
        getSupportFragmentManager().beginTransaction().replace(R.id.Frame_Content,new HomeFragment()).commit();

        //Attach Listener To Bottom Nav
        binding.bottomNav.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);

        //bottomMenu();
    }

    /*private void bottomMenu() {
        binding.bottomNavMenu.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment fragment = null;
                switch (i) {
                    case R.id.bottom_nav_home:
                        fragment = new HomeFragment();
                        break;
                    case R.id.bottom_nav_profile:
                        fragment = new ProfileFragment();
                        break;
                        case R.id.bottom_nav_contact:
                        fragment = new ContactFragment();
                        break;
                    case R.id.bottom_nav_settings:
                        fragment = new SettingsFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_content,fragment).commit();
            }
        });

    }*/
    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = menuItem -> {
        switch (menuItem.getItemId()){

            case R.id.bottom_nav_home :
                HomeFragment fragment1 = new HomeFragment();
                FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
                fragmentTransaction1.replace(R.id.Frame_Content,fragment1);
                fragmentTransaction1.commit();
                return true;

            case R.id.bottom_nav_profile :
                ProfileFragment fragment2 = new ProfileFragment();
                FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
                fragmentTransaction2.replace(R.id.Frame_Content,fragment2);
                fragmentTransaction2.commit();
                return true;

            case R.id.bottom_nav_contact :
                ContactFragment fragment3 = new ContactFragment();
                FragmentTransaction fragmentTransaction3 = getSupportFragmentManager().beginTransaction();
                fragmentTransaction3.replace(R.id.Frame_Content,fragment3);
                fragmentTransaction3.commit();
                return true;

            case R.id.bottom_nav_settings :
                SettingsFragment fragment4 = new SettingsFragment();
                FragmentTransaction fragmentTransaction4 = getSupportFragmentManager().beginTransaction();
                fragmentTransaction4.replace(R.id.Frame_Content,fragment4);
                fragmentTransaction4.commit();
                return true;


        }

        return false;
    };

}