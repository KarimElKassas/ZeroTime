package com.zerotime.zerotime;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.View;

import com.ismaeldivita.chipnavigation.ChipNavigationBar;
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
        binding.bottomNavMenu.setItemSelected(R.id.bottom_nav_home,true);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();

        bottomMenu();
    }

    private void bottomMenu() {
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
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
            }
        });




    }
}