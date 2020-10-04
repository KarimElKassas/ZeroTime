package com.zerotime.zerotime;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import com.zerotime.zerotime.Fragments.ContactFragment;
import com.zerotime.zerotime.Fragments.HomeFragment;
import com.zerotime.zerotime.Fragments.ProfileFragment;
import com.zerotime.zerotime.Fragments.SettingsFragment;
import com.zerotime.zerotime.databinding.UserActivityHomeBinding;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class Home extends AppCompatActivity {
    private UserActivityHomeBinding binding;
    String userId;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = UserActivityHomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

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
}