package com.zerotime.zerotime.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zerotime.zerotime.R;
import com.zerotime.zerotime.databinding.FragmentProfileBinding;


public class ProfileFragment extends Fragment {

    FragmentProfileBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater,container,false);
        View view =  binding.getRoot();

        binding.profileUpdateUserDataBtn.setOnClickListener(view1 -> {
            Fragment newFragment = new UpdateUserDataFragment();
            assert getFragmentManager() != null;
            FragmentTransaction transaction = getFragmentManager().beginTransaction();

            transaction.replace(R.id.Frame_Content, newFragment);
            transaction.addToBackStack(null);

            transaction.commit();
        });
        return view;
    }
}