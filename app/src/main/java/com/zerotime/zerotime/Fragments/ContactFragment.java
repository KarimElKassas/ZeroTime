package com.zerotime.zerotime.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.zerotime.zerotime.MyBroadCast;
import com.zerotime.zerotime.No_Internet_Connection;
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

        // Check Internet State
        if (!haveNetworkConnection()) {
            Intent i = new Intent(context, No_Internet_Connection.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
            ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            ((Activity) context).finish();
        }
        checkInternetConnection();
        //-----------------------------------

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SharedPreferences prefs = Objects.requireNonNull(getContext()).getSharedPreferences("UserState", MODE_PRIVATE);


        binding.goToChatCard.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), Message.class);
            intent.putExtra("UserID", prefs.getString("isLogged", ""));
            intent.putExtra("UniqueID", "ContactFragment");
            startActivity(intent);
        });
        binding.goToCallCard.setOnClickListener(view1 -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + "01017268676"));
            context.startActivity(intent);
        });
        binding.goToMailCard.setOnClickListener(view1 -> {
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
    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }
    private void checkInternetConnection() {
        MyBroadCast broadCast = new MyBroadCast();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        context.registerReceiver(broadCast, intentFilter);

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