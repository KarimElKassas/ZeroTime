package com.zerotime.zerotime.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zerotime.zerotime.Message;
import com.zerotime.zerotime.databinding.UserFragmentContactBinding;

import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class ContactFragment extends Fragment {
    UserFragmentContactBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = UserFragmentContactBinding.inflate(inflater,container,false);
        View view =  binding.getRoot();

        SharedPreferences prefs = Objects.requireNonNull(getContext()).getSharedPreferences("UserState", MODE_PRIVATE);


        binding.goToChatBTn.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), Message.class);
            intent.putExtra("UserID",prefs.getString("isLogged",""));
            intent.putExtra("UniqueID","ContactFragment");
            startActivity(intent);
        });
        return view;
    }
}