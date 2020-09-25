package com.zerotime.zerotime.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import com.zerotime.zerotime.History;
import com.zerotime.zerotime.R;
import com.zerotime.zerotime.databinding.UserFragmentProfileBinding;

import java.util.Objects;

import es.dmoral.toasty.Toasty;


public class ProfileFragment extends Fragment {

    UserFragmentProfileBinding binding;
    View view;
    Context context;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = UserFragmentProfileBinding.inflate(inflater, container, false);
        view = binding.getRoot();
        context = container.getContext();


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding.profileOrdersHistoryBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), History.class);
            startActivity(intent);
            ((Activity)context).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        });


        binding.profileUpdateUserDataBtn.setOnClickListener(view1 -> {
            FragmentManager fragmentManager = getFragmentManager();
            UpdateUserDataFragment fragment = new UpdateUserDataFragment();
            assert fragmentManager != null;
            fragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.enter_right_to_left,R.anim.exit_right_to_left,
                            R.anim.enter_left_to_right,R.anim.exit_left_to_right)
                    .replace(R.id.Frame_Content,fragment)
                    .addToBackStack("ProfileFragment")
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();
        });
        binding.profileOrderProgressBtn.setOnClickListener(view1 -> {
            FragmentManager fragmentManager = getFragmentManager();
            FollowMyOrdersFragment fragment = new FollowMyOrdersFragment();
            assert fragmentManager != null;
            fragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.enter_right_to_left,R.anim.exit_right_to_left,
                            R.anim.enter_left_to_right,R.anim.exit_left_to_right)
                    .replace(R.id.Frame_Content,fragment)
                    .addToBackStack("ProfileFragment")
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();
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

                    if( keyCode == KeyEvent.KEYCODE_BACK )
                    {
                        getActivity().onBackPressed();
                        /* FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);


                        ((Activity)context).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        ((Activity)context).finish();*/

                        return true;
                    }

                    return false;
                });

            }, 1000000);
        }catch (Exception e){
            Toast.makeText(context, "Try Catch", Toast.LENGTH_SHORT).show();
            Toast.makeText(context, e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }


}