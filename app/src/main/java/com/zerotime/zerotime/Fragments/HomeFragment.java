package com.zerotime.zerotime.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zerotime.zerotime.R;
import com.zerotime.zerotime.databinding.FragmentHomeBinding;


public class HomeFragment extends Fragment {
    FragmentHomeBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater,container,false);
        View view =  binding.getRoot();

        // Our Products Button
        binding.homeFragmentOurProductsBtn.setOnClickListener(view1 -> {
            Toast.makeText(getContext(),"قريباً",Toast.LENGTH_SHORT).show();
        });
        //Month Offers Button
        binding.homeFragmentMonthOffersBtn.setOnClickListener(view1 -> {
            Fragment newFragment = new DisplayOffersFragment();
            assert getFragmentManager() != null;
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.Frame_Content, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });
        //Add Order Button
        binding.homeFragmentAddOrderBtn.setOnClickListener(view1 -> {
            // Create new fragment and transaction
            Fragment newFragment = new AddOrderFragment();
            assert getFragmentManager() != null;
            FragmentTransaction transaction = getFragmentManager().beginTransaction();

            transaction.replace(R.id.Frame_Content, newFragment);
            transaction.addToBackStack(null);

            transaction.commit();
        });


        return view;
    }


}