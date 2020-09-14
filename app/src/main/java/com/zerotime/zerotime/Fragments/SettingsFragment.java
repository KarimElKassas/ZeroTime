package com.zerotime.zerotime.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.zerotime.zerotime.ComplaintsFragment;
import com.zerotime.zerotime.Login;
import com.zerotime.zerotime.R;
import com.zerotime.zerotime.databinding.FragmentAddOrderBinding;
import com.zerotime.zerotime.databinding.FragmentSettingsBinding;

import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;


public class SettingsFragment extends Fragment {

    FragmentSettingsBinding binding;
    SharedPreferences.Editor editor;
    Context context;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSettingsBinding.inflate(inflater,container,false);
        View view =  binding.getRoot();
        context = container.getContext();

        //Complaints Button
        binding.SettingsComplainsBtn.setOnClickListener(view1 -> {
            Fragment newFragment = new ComplaintsFragment();
            assert getFragmentManager() != null;
            FragmentTransaction transaction = getFragmentManager().beginTransaction();

            transaction.replace(R.id.Frame_Content, newFragment);
            transaction.addToBackStack(null);

            transaction.commit();
        });
        //Log Out Button
        binding.SettingsLogoutBtn.setOnClickListener(view1 -> {
            editor = Objects.requireNonNull(getContext()).getSharedPreferences("UserState", MODE_PRIVATE).edit();
            editor.putString("isLogged", "null");
            editor.apply();
            Intent intent = new Intent(getActivity(), Login.class);
            startActivity(intent);
            ((Activity)context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            ((Activity)context).finish();
        });
        return view;
    }
}