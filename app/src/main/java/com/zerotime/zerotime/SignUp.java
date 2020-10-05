package com.zerotime.zerotime;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.zerotime.zerotime.databinding.ActivitySignUpBinding;

import java.util.HashMap;
import java.util.Objects;

public class SignUp extends AppCompatActivity {

    private ActivitySignUpBinding binding;

    private DatabaseReference usersRef;

    private String userToken = "";
    private HashMap<String, Object> usersMap;


    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //Check Internet State
        if (!haveNetworkConnection()) {
            Intent i = new Intent(SignUp.this, No_Internet_Connection.class);
            //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("UniqueID","SignUp");
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
        }
        checkInternetConnection();
        //---------------------------------------

        usersRef = FirebaseDatabase.getInstance().getReference("Users");
        usersMap = new HashMap<>();

        //animation
        inAnimation = new AlphaAnimation(0f, 2f);
        outAnimation = new AlphaAnimation(2f, 0f);
        animation();

        //get user token id
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                userToken = Objects.requireNonNull(task.getResult()).getToken();
            }
        });
        //Sign In Text
        binding.signUpLoginTextView.setOnClickListener(view1 -> goToLogin());
        //Sign Up Button
        binding.signUpSignUpBtn.setOnClickListener(view12 -> checkData());

    }


    private void checkData() {
        //User Name Validation
        if (TextUtils.isEmpty(binding.signUpUserNameEditTxt.getText())) {
            binding.signUpUserNameEditTxt.setError("ادخل الاسم من فضلك !");
            binding.signUpUserNameEditTxt.requestFocus();
            return;
        }
        //User Password Validation
        if (TextUtils.isEmpty(binding.signUpUserPasswordEditTxt.getText())) {
            binding.signUpUserPasswordEditTxt.setError("ادخل كلمة السر من فضلك !");
            binding.signUpUserPasswordEditTxt.requestFocus();
            return;
        }
        if (Objects.requireNonNull(binding.signUpUserPasswordEditTxt.getText()).length() < 8) {
            binding.signUpUserPasswordEditTxt.setError("كلمة السر يجب ان تكون اكثر من او تساوي 8 حروف او ارقام !");
            binding.signUpUserPasswordEditTxt.requestFocus();
            return;
        }
        //Primary Phone Validation
        if (TextUtils.isEmpty(binding.signUpUserPrimaryPhoneEditTxt.getText())) {
            binding.signUpUserPrimaryPhoneEditTxt.setError("ادخل رقم الهاتف الاول من فضلك !");
            binding.signUpUserPrimaryPhoneEditTxt.requestFocus();
            return;
        }
        if (Objects.requireNonNull(binding.signUpUserPrimaryPhoneEditTxt.getText()).length() != 11) {
            binding.signUpUserPrimaryPhoneEditTxt.setError("رقم الهاتف يجب ان يتكون من 11 رقم فقط !");
            binding.signUpUserPrimaryPhoneEditTxt.requestFocus();
            return;
        }
        if (!binding.signUpUserPrimaryPhoneEditTxt.getText().toString().startsWith("01")) {
            binding.signUpUserPrimaryPhoneEditTxt.setError("رقم الهاتف يجب ان يبدأ بـ 01 !");
            binding.signUpUserPrimaryPhoneEditTxt.requestFocus();
            return;
        }
        //Secondary Phone Validation
        if (TextUtils.isEmpty(binding.signUpUserSecondaryPhoneEditTxt.getText())) {
            binding.signUpUserSecondaryPhoneEditTxt.setError("ادخل رقم الهاتف الثانى من فضلك !");
            binding.signUpUserSecondaryPhoneEditTxt.requestFocus();
            return;
        }
        if (Objects.requireNonNull(binding.signUpUserSecondaryPhoneEditTxt.getText()).length() != 11) {
            binding.signUpUserSecondaryPhoneEditTxt.setError("رقم الهاتف يجب ان يتكون من 11 رقم فقط !");
            binding.signUpUserSecondaryPhoneEditTxt.requestFocus();
            return;
        }
        if (!binding.signUpUserSecondaryPhoneEditTxt.getText().toString().startsWith("01")) {
            binding.signUpUserSecondaryPhoneEditTxt.setError("رقم الهاتف يجب ان يبدأ بـ 01 !");
            binding.signUpUserSecondaryPhoneEditTxt.requestFocus();
            return;
        }
        String primaryPhone = binding.signUpUserPrimaryPhoneEditTxt.getText().toString();
        String secondaryPhone = binding.signUpUserSecondaryPhoneEditTxt.getText().toString();
        if (primaryPhone.equals(secondaryPhone)) {
            binding.signUpUserSecondaryPhoneEditTxt.setError("من فضلك قم باختيار رقمين مختلفين !");
            binding.signUpUserSecondaryPhoneEditTxt.requestFocus();
            return;
        }
        //User Address Validation
        if (TextUtils.isEmpty(binding.signUpUserAddressEditTxt.getText())) {
            binding.signUpUserAddressEditTxt.setError("ادخل العنوان بالتفصيل من فضلك !");
            binding.signUpUserAddressEditTxt.requestFocus();
            return;
        }

        createNewUser();
    }

    private void createNewUser() {

        //Progress Bar
        binding.signUpProgressBarHolder.setAnimation(inAnimation);
        binding.signUpProgressBarHolder.setVisibility(View.VISIBLE);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        usersMap.put("UserName", Objects.requireNonNull(binding.signUpUserNameEditTxt.getText()).toString());
        usersMap.put("UserPrimaryPhone", Objects.requireNonNull(binding.signUpUserPrimaryPhoneEditTxt.getText()).toString());
        usersMap.put("UserSecondaryPhone", Objects.requireNonNull(binding.signUpUserSecondaryPhoneEditTxt.getText()).toString());
        usersMap.put("UserPassword", Objects.requireNonNull(binding.signUpUserPasswordEditTxt.getText()).toString());
        usersMap.put("UserAddress", Objects.requireNonNull(binding.signUpUserAddressEditTxt.getText()).toString());
        usersMap.put("UserToken", userToken);
        usersMap.put("UserId", Objects.requireNonNull(binding.signUpUserPrimaryPhoneEditTxt.getText()).toString());

        usersRef.child(binding.signUpUserPrimaryPhoneEditTxt.getText().toString())
                .setValue(usersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    //clear progress bar
                    binding.signUpProgressBarHolder.setAnimation(outAnimation);
                    binding.signUpProgressBarHolder.setVisibility(View.GONE);

                    Toast.makeText(SignUp.this.getApplicationContext(), "Sign Up Successfully", Toast.LENGTH_SHORT).show();
                    SignUp.this.goToLogin();
                } else {
                    //clear progress bar
                    binding.signUpProgressBarHolder.setAnimation(outAnimation);
                    binding.signUpProgressBarHolder.setVisibility(View.GONE);

                    Toast.makeText(SignUp.this.getApplicationContext(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }
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
        Intent intent = new Intent(SignUp.this, Login.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        this.finish();
    }

    private void animation() {
        inAnimation.setDuration(200);
        outAnimation.setDuration(200);

        // app logo animation
        binding.signUpAppLogoImageView.setTranslationX(400f);
        binding.signUpAppLogoImageView.setAlpha(0f);
        binding.signUpAppLogoImageView.animate().translationX(0f).alpha(1f).setDuration(600).setStartDelay(500).start();
        //---------------------------------------------------------------------
        // user name animation
        binding.signUpUserNameEditTxt.setTranslationX(600f);
        binding.signUpUserNameEditTxt.setAlpha(0f);
        binding.signUpUserNameEditTxt.animate().translationX(0f).alpha(1f).setDuration(800).setStartDelay(500).start();
        //---------------------------------------------------------------------
        // user password animation
        binding.signUpUserPasswordEditTxt.setTranslationX(800f);
        binding.signUpUserPasswordEditTxt.setAlpha(0f);
        binding.signUpUserPasswordEditTxt.animate().translationX(0f).alpha(1f).setDuration(1000).setStartDelay(500).start();
        //---------------------------------------------------------------------
        // user primary phone animation
        binding.signUpUserPrimaryPhoneEditTxt.setTranslationX(1000f);
        binding.signUpUserPrimaryPhoneEditTxt.setAlpha(0f);
        binding.signUpUserPrimaryPhoneEditTxt.animate().translationX(0f).alpha(1f).setDuration(1200).setStartDelay(500).start();
        //---------------------------------------------------------------------
        // user secondary phone animation
        binding.signUpUserSecondaryPhoneEditTxt.setTranslationX(1200f);
        binding.signUpUserSecondaryPhoneEditTxt.setAlpha(0f);
        binding.signUpUserSecondaryPhoneEditTxt.animate().translationX(0f).alpha(1f).setDuration(1400).setStartDelay(500).start();
        //---------------------------------------------------------------------
        // user address animation
        binding.signUpUserAddressEditTxt.setTranslationX(1400f);
        binding.signUpUserAddressEditTxt.setAlpha(0f);
        binding.signUpUserAddressEditTxt.animate().translationX(0f).alpha(1f).setDuration(1600).setStartDelay(500).start();
        //---------------------------------------------------------------------
        // user sign up button animation
        binding.signUpSignUpBtn.setTranslationX(1600f);
        binding.signUpSignUpBtn.setAlpha(0f);
        binding.signUpSignUpBtn.animate().translationX(0f).alpha(1f).setDuration(1800).setStartDelay(500).start();
        //---------------------------------------------------------------------
        // user login text animation
        binding.signUpLoginTextView.setTranslationX(1800);
        binding.signUpLoginTextView.setAlpha(0f);
        binding.signUpLoginTextView.animate().translationX(0f).alpha(1f).setDuration(2000).setStartDelay(500).start();
        //---------------------------------------------------------------------

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }
}