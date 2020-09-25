package com.zerotime.zerotime.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zerotime.zerotime.R;
import com.zerotime.zerotime.databinding.UserFragmentHomeBinding;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import es.dmoral.toasty.Toasty;


public class HomeFragment extends Fragment {
    UserFragmentHomeBinding binding;
    Context context;
    View view;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = UserFragmentHomeBinding.inflate(inflater, container, false);
        view = binding.getRoot();
        context = container.getContext();


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Our Products Button
        binding.homeFragmentOurProductsBtn.setOnClickListener(view1 -> Toasty.info(context, "قريباً", Toasty.LENGTH_SHORT, true).show());

        //Month Offers Button
        binding.homeFragmentMonthOffersBtn.setOnClickListener(view1 -> {
            FragmentManager fragmentManager = getFragmentManager();
            DisplayOffersFragment fragment = new DisplayOffersFragment();
            assert fragmentManager != null;
            fragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left,
                            R.anim.enter_left_to_right, R.anim.exit_left_to_right)
                    .replace(R.id.Frame_Content, fragment)
                    .addToBackStack("HomeFragment")
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();

        });
        //Add Order Button
        binding.homeFragmentAddOrderBtn.setOnClickListener(view1 -> {
            // Create new fragment and transaction
            FragmentManager fragmentManager = getFragmentManager();
            AddOrderFragment fragment = new AddOrderFragment();
            assert fragmentManager != null;
            fragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left,
                            R.anim.enter_left_to_right, R.anim.exit_left_to_right)
                    .replace(R.id.Frame_Content, fragment)
                    .addToBackStack("HomeFragment")
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();
        });
        //About Us Button
        binding.homeFragmentAboutUsBtn.setOnClickListener(view1 -> {
            // Create new fragment and transaction
            FragmentManager fragmentManager = getFragmentManager();
            AboutUsFragment fragment = new AboutUsFragment();
            assert fragmentManager != null;
            fragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left,
                            R.anim.enter_left_to_right, R.anim.exit_left_to_right)
                    .replace(R.id.Frame_Content, fragment)
                    .addToBackStack("HomeFragment")
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();
        });
    }

    @Override
    public void onResume() {

        super.onResume();
        try {


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

        } catch (Exception e) {
            Toast.makeText(context, "Try Catch", Toast.LENGTH_SHORT).show();
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }


    }
}