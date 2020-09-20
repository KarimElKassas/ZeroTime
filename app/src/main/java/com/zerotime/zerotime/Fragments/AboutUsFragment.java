package com.zerotime.zerotime.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zerotime.zerotime.databinding.UserFragmentAboutUsBinding;

import java.util.Objects;


public class AboutUsFragment extends Fragment {
    private UserFragmentAboutUsBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = UserFragmentAboutUsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();




        return view;
    }
    @Override
    public void onResume() {

        super.onResume();

        Objects.requireNonNull(getView()).setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener((v, keyCode, event) -> {

            if( keyCode == KeyEvent.KEYCODE_BACK )
            {
                assert getFragmentManager() != null;
                getFragmentManager().popBackStackImmediate();
                return true;
            }

            return false;
        });
    }
}