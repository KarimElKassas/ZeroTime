package com.zerotime.zerotime.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.zerotime.zerotime.Login;
import com.zerotime.zerotime.R;

import java.util.Objects;


public class HomeFragment extends Fragment {

    Button btn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        btn = view.findViewById(R.id.fragment_home_sign_out_btn);
        btn.setOnClickListener(view1 -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(container.getContext(), Login.class);
            startActivity(intent);
            Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            getActivity().finish();
        });
        return view;
    }
}