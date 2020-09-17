package com.zerotime.zerotime;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zerotime.zerotime.databinding.FragmentAboutUsBinding;


public class AboutUsFragment extends Fragment {
    private FragmentAboutUsBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentAboutUsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();




        return view;
    }
}