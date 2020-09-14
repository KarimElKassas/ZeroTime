package com.zerotime.zerotime.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zerotime.zerotime.Message;
import com.zerotime.zerotime.R;
import com.zerotime.zerotime.databinding.FragmentContactBinding;
import com.zerotime.zerotime.databinding.FragmentHomeBinding;

public class ContactFragment extends Fragment {
    FragmentContactBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentContactBinding.inflate(inflater,container,false);
        View view =  binding.getRoot();
        binding.goToChatBTn.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), Message.class);
            startActivity(intent);
        });
        return view;
    }
}