package com.zerotime.zerotime.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zerotime.zerotime.History;
import com.zerotime.zerotime.R;
import com.zerotime.zerotime.databinding.UserFragmentProfileBinding;


public class ProfileFragment extends Fragment {

    UserFragmentProfileBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = UserFragmentProfileBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        binding.profileOrdersHistoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), History.class);
                startActivity(intent);
            }
        });


        binding.profileUpdateUserDataBtn.setOnClickListener(view1 -> {
            Fragment newFragment = new UpdateUserDataFragment();
            assert getFragmentManager() != null;
            FragmentTransaction transaction = getFragmentManager().beginTransaction();

            transaction.replace(R.id.Frame_Content, newFragment);
            transaction.addToBackStack(null);

            transaction.commit();
        });
        binding.profileOrderProgressBtn.setOnClickListener(view1 -> {
            Fragment newFragment = new FollowMyOrdersFragment();
            assert getFragmentManager() != null;
            FragmentTransaction transaction = getFragmentManager().beginTransaction();

            transaction.replace(R.id.Frame_Content, newFragment);
            transaction.addToBackStack(null);

            transaction.commit();
        });
        
        return view;
    }
}