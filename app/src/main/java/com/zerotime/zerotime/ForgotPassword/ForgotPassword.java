package com.zerotime.zerotime.ForgotPassword;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zerotime.zerotime.MyBroadCast;
import com.zerotime.zerotime.No_Internet_Connection;
import com.zerotime.zerotime.R;
import com.zerotime.zerotime.User.Login;
import com.zerotime.zerotime.databinding.ActivityForgotPasswordBinding;

import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class ForgotPassword extends AppCompatActivity {
    String primaryNumber, secondaryNumber;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    private ActivityForgotPasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //animation
        inAnimation = new AlphaAnimation(0f, 2f);
        outAnimation = new AlphaAnimation(2f, 0f);
        animation();

    }

    @Override
    protected void onStart() {
        super.onStart();
        binding.forgotNextBtn.setOnClickListener(view1 -> {

            // Check Internet State
            if (!haveNetworkConnection()) {
                goToNoConnection();
            }
            checkInternetConnection();
            //-----------------------------------

            // data validation
            validateData();

        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goToLogin();
    }

    private void validateData() {
        //Primary Phone Validation
        if (TextUtils.isEmpty(binding.forgotPhoneEdt.getText())) {
            binding.forgotPhoneEdt.setError("ادخل رقم الهاتف الاول من فضلك !");
            binding.forgotPhoneEdt.requestFocus();
            return;
        }
        if (Objects.requireNonNull(binding.forgotPhoneEdt.getText()).length() != 11) {
            binding.forgotPhoneEdt.setError("رقم الهاتف يجب ان يتكون من 11 رقم فقط !");
            binding.forgotPhoneEdt.requestFocus();
            return;
        }


        //Secondary Phone Validation
        if (TextUtils.isEmpty(binding.forgotPhone2Edt.getText())) {
            binding.forgotPhone2Edt.setError("ادخل رقم الهاتف الثانى من فضلك !");
            binding.forgotPhone2Edt.requestFocus();
            return;
        }
        if (Objects.requireNonNull(binding.forgotPhone2Edt.getText()).length() != 11) {
            binding.forgotPhone2Edt.setError("رقم الهاتف يجب ان يتكون من 11 رقم فقط !");
            binding.forgotPhone2Edt.requestFocus();
            return;
        }

        //different numbers validation
        String primaryPhone = binding.forgotPhoneEdt.getText().toString();
        String secondaryPhone = binding.forgotPhone2Edt.getText().toString();
        if (primaryPhone.equals(secondaryPhone)) {
            binding.forgotPhone2Edt.setError("من فضلك قم بإدخال رقمين مختلفين !");
            binding.forgotPhone2Edt.requestFocus();
            return;
        }

        nextStep();

    }

    private void nextStep() {
        //Progress bar
        binding.forgetProgress.setAnimation(inAnimation);
        binding.forgetProgress.setVisibility(View.VISIBLE);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        primaryNumber = Objects.requireNonNull(binding.forgotPhoneEdt.getText()).toString();
        secondaryNumber = Objects.requireNonNull(binding.forgotPhone2Edt.getText()).toString();

        //check weather user exist or not in DB
        DatabaseReference user = FirebaseDatabase.getInstance().getReference("Users");
        user.child(primaryNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    if (snapshot.hasChildren()) {
                        String secondaryPhone = snapshot.child("UserSecondaryPhone").getValue(String.class);

                        assert secondaryPhone != null;
                        if (!secondaryPhone.equals(secondaryNumber)) {
                            //clear progress
                            binding.forgetProgress.setVisibility(View.GONE);
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                            binding.forgotPhone2Edt.setError("رقم هاتفك الثاني غير صحيح !");
                            binding.forgotPhone2Edt.requestFocus();

                        } else {
                            binding.forgotPhoneEdt.setError(null);
                            //clear progress
                            binding.forgetProgress.setVisibility(View.GONE);
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                            goToSetNewPassword();

                        }
                    }
                }else {
                    //clear progress
                    binding.forgetProgress.setVisibility(View.GONE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                    binding.forgotPhoneEdt.setError("لا يوجد مستخدم بهذا الرقم !");
                    binding.forgotPhoneEdt.requestFocus();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
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
        registerReceiver(broadCast, intentFilter);

    }

    private void goToLogin() {
        Intent intent = new Intent(ForgotPassword.this, Login.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

    private void goToSetNewPassword() {
        Intent intent = new Intent(ForgotPassword.this, SetNewPassword.class);
        intent.putExtra("phoneNo", primaryNumber);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

    private void goToNoConnection() {
        Intent i = new Intent(ForgotPassword.this, No_Internet_Connection.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("UniqueID", "ForgotPassword");
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

    private void animation() {
        inAnimation.setDuration(200);
        outAnimation.setDuration(200);

        // forgot lock image animation
        binding.imgforgot.setTranslationX(400f);
        binding.imgforgot.setAlpha(0f);
        binding.imgforgot.animate().translationX(0f).alpha(1f).setDuration(600).setStartDelay(500).start();


        // forgot lock image animation
        binding.fogotPasswordLockImg.setTranslationX(600f);
        binding.fogotPasswordLockImg.setAlpha(0f);
        binding.fogotPasswordLockImg.animate().translationX(0f).alpha(1f).setDuration(800).setStartDelay(500).start();
        //---------------------------------------------------------------------
        // primary phone animation
        binding.forgotPhoneEdt.setTranslationX(800f);
        binding.forgotPhoneEdt.setAlpha(0f);
        binding.forgotPhoneEdt.animate().translationX(0f).alpha(1f).setDuration(1000).setStartDelay(500).start();
        //---------------------------------------------------------------------
        // secondary phone animation
        binding.forgotPhone2Edt.setTranslationX(1000f);
        binding.forgotPhone2Edt.setAlpha(0f);
        binding.forgotPhone2Edt.animate().translationX(0f).alpha(1f).setDuration(1200).setStartDelay(500).start();
        //---------------------------------------------------------------------
        // forgot password btn animation
        binding.forgotNextBtn.setTranslationX(1200f);
        binding.forgotNextBtn.setAlpha(0f);
        binding.forgotNextBtn.animate().translationX(0f).alpha(1f).setDuration(1400).setStartDelay(500).start();
        //---------------------------------------------------------------------

    }

}