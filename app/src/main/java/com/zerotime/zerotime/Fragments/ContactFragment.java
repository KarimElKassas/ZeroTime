package com.zerotime.zerotime.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zerotime.zerotime.Message;
import com.zerotime.zerotime.R;
import com.zerotime.zerotime.databinding.UserFragmentContactBinding;

import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class ContactFragment extends Fragment {
    UserFragmentContactBinding binding;
    View view;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = UserFragmentContactBinding.inflate(inflater, container, false);
        view = binding.getRoot();
        context = container.getContext();


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SharedPreferences prefs = Objects.requireNonNull(getContext()).getSharedPreferences("UserState", MODE_PRIVATE);


        binding.goToChatBTn.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), Message.class);
            intent.putExtra("UserID", prefs.getString("isLogged", ""));
            intent.putExtra("UniqueID", "ContactFragment");
            startActivity(intent);
        });
        binding.goToCallBTn.setOnClickListener(view1 -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + "01017268676"));
            context.startActivity(intent);
        });
        binding.goToMailBTn.setOnClickListener(view1 -> {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("message/rfc822");
            i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"zerotime336@example.com"});
            i.putExtra(Intent.EXTRA_SUBJECT, "اكتب العنوان هنا");
            i.putExtra(Intent.EXTRA_TEXT   , "اكتب الرساله هنا");
            try {
                startActivity(Intent.createChooser(i, "Send mail..."));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(context, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
            }
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
                        ((Activity)context).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        ((Activity)context).finish();
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