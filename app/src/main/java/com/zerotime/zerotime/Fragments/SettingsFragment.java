package com.zerotime.zerotime.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.zerotime.zerotime.BuildConfig;
import com.zerotime.zerotime.Login;
import com.zerotime.zerotime.R;
import com.zerotime.zerotime.databinding.UserFragmentSettingsBinding;

import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.content.Context.MODE_PRIVATE;


public class SettingsFragment extends Fragment {

    UserFragmentSettingsBinding binding;
    SharedPreferences.Editor editor;
    Context context;
    View view;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = UserFragmentSettingsBinding.inflate(inflater, container, false);
        view = binding.getRoot();
        context = container.getContext();


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            String unique = bundle.getString("UniqueID", "Not Found");
            Toast.makeText(context, unique, Toast.LENGTH_SHORT).show();
        }

        //Complaints Button
        binding.SettingsComplainsBtn.setOnClickListener(view1 -> {
            FragmentManager fragmentManager = getFragmentManager();
            ComplaintsFragment fragment = new ComplaintsFragment();
            assert fragmentManager != null;
            fragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left,
                            R.anim.enter_left_to_right, R.anim.exit_left_to_right)
                    .replace(R.id.Frame_Content, fragment)
                    .addToBackStack("SettingsFragment")
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();
        });

        // share app btn
        binding.SettingsShareAppBtn.setOnClickListener(view -> {

            try {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Zero Time");
                String shareMessage = "\nLet me recommend you this application\n\n";
                shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "choose one"));
            } catch (Exception e) {
                //e.toString();
            }


        });


        //Log Out Button
        binding.SettingsLogoutBtn.setOnClickListener(view1 -> {
            SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
            pDialog.
                    setTitleText("هل انت متأكد ؟")
                    //.setContentText("هل تريد تسجيل الخروج ؟")
                    .setConfirmText("نعم ، متأكد")

                    .setConfirmClickListener(sweetAlertDialog -> {
                        editor = Objects.requireNonNull(getContext()).getSharedPreferences("UserState", MODE_PRIVATE).edit();
                        editor.putString("isLogged", "null");
                        editor.apply();

                        sweetAlertDialog.cancel();

                        Intent intent = new Intent(context, Login.class);
                        startActivity(intent);
                        ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        ((Activity) context).finish();
                    })

                    .setCancelText("التراجع")

                    .setCancelClickListener(SweetAlertDialog::cancel);

            pDialog.setCancelable(false);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.show();


        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            String unique = bundle.getString("UniqueID", "Not Found");
            Toast.makeText(context, unique, Toast.LENGTH_SHORT).show();
        }else Toast.makeText(context, "null bundle", Toast.LENGTH_SHORT).show();
        /*if (getArguments() != null){

            String bundle = getArguments().getString("UniqueID");
            Toast.makeText(context, bundle, Toast.LENGTH_SHORT).show();

        }else Toast.makeText(context, "Argument Null", Toast.LENGTH_SHORT).show();*/

        /*try {

            new Handler().postDelayed(() -> {

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

            }, 1000000);
        } catch (Exception e) {
            Toast.makeText(context, "Try Catch", Toast.LENGTH_SHORT).show();
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }*/
    }
}