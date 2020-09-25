package com.zerotime.zerotime.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zerotime.zerotime.Login;
import com.zerotime.zerotime.R;
import com.zerotime.zerotime.databinding.UserFragmentSettingsBinding;

import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;


public class SettingsFragment extends Fragment {

    UserFragmentSettingsBinding binding;
    SharedPreferences.Editor editor;
    Context context;
    View view;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = UserFragmentSettingsBinding.inflate(inflater, container, false);
        view = binding.getRoot();
        context = container.getContext();


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Complaints Button
        binding.SettingsComplainsBtn.setOnClickListener(view1 -> {
            FragmentManager fragmentManager = getFragmentManager();
            ComplaintsFragment fragment = new ComplaintsFragment();
            assert fragmentManager != null;
            fragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.enter_right_to_left,R.anim.exit_right_to_left,
                            R.anim.enter_left_to_right,R.anim.exit_left_to_right)
                    .replace(R.id.Frame_Content,fragment)
                    .addToBackStack("SettingsFragment")
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();
        });

        //Log Out Button
        binding.SettingsLogoutBtn.setOnClickListener(view1 -> {
            editor = Objects.requireNonNull(getContext()).getSharedPreferences("UserState", MODE_PRIVATE).edit();
            editor.putString("isLogged", "null");
            editor.apply();
            Intent intent = new Intent(context, Login.class);
            startActivity(intent);
            ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            ((Activity) context).finish();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        try {

            new Handler().postDelayed(() -> {

                view.setFocusableInTouchMode(true);
                view.requestFocus();
                view.setOnKeyListener((v, keyCode, event) -> {

                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        ((Activity) context).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        ((Activity) context).finish();
                        return true;
                    }

                    return false;
                });

            }, 1000000);
        } catch (Exception e) {
            Toast.makeText(context, "Try Catch", Toast.LENGTH_SHORT).show();
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}