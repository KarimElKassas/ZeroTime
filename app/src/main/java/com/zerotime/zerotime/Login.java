package com.zerotime.zerotime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.zerotime.zerotime.Moderator.ModeratorHome;
import com.zerotime.zerotime.Room.Data.UserDao;
import com.zerotime.zerotime.Room.UserDataBase;
import com.zerotime.zerotime.Secretary.SecretaryHome;
import com.zerotime.zerotime.databinding.ActivityLoginBinding;

import java.util.Objects;

public class Login extends AppCompatActivity {

    private ActivityLoginBinding binding;
    SharedPreferences.Editor editor;

    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;

    private DatabaseReference usersRef;
    private String userToken = "";

    // Room DB
    UserDao db;
    UserDataBase dataBase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        //Room DB
        dataBase = Room.databaseBuilder(this, UserDataBase.class, "User")
                .allowMainThreadQueries().build();
        db = dataBase.getUserDao();


        //animation
        inAnimation = new AlphaAnimation(0f, 2f);
        outAnimation = new AlphaAnimation(2f, 0f);
        animation();
        // Initialize User State
        usersRef = FirebaseDatabase.getInstance().getReference("Users");
        editor = getSharedPreferences("UserState", MODE_PRIVATE).edit();
        editor.putString("isLogged", "null");
        editor.apply();

        //Login With User
        binding.loginLoginBtn.setOnClickListener(view1 -> checkData());
        //Return To Sign Up
        binding.loginSignUpTextView.setOnClickListener(view12 -> goToSignUp());
    }

    private void checkData() {
        //Moderator Case
        if (Objects.requireNonNull(binding.loginUserPhoneEditTxt.getText()).toString().equals("0")
                && Objects.requireNonNull(binding.loginUserPasswordEditTxt.getText()).toString().equals("0")) {
            editor.putString("UserType", "Moderator");
            editor.apply();
            Intent intent = new Intent(Login.this, ModeratorHome.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            this.finish();
            return;
        }
        //Secretary Case
        if (Objects.requireNonNull(binding.loginUserPhoneEditTxt.getText()).toString().equals("1")
                && Objects.requireNonNull(binding.loginUserPasswordEditTxt.getText()).toString().equals("1")) {
            editor.putString("UserType", "Secretary");
            editor.apply();
            Intent intent = new Intent(Login.this, SecretaryHome.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            this.finish();
            return;
        }
        //Phone Validation
        if (TextUtils.isEmpty(binding.loginUserPhoneEditTxt.getText())) {
            binding.loginUserPhoneEditTxt.setError("ادخل رقم الهاتف الاول من فضلك !");
            binding.loginUserPhoneEditTxt.requestFocus();
            return;
        }
        if (Objects.requireNonNull(binding.loginUserPhoneEditTxt.getText()).length() != 11) {
            binding.loginUserPhoneEditTxt.setError("رقم الهاتف يجب ان يتكون من 11 رقم فقط !");
            binding.loginUserPhoneEditTxt.requestFocus();
            return;
        }
        if (!binding.loginUserPhoneEditTxt.getText().toString().startsWith("01")) {
            binding.loginUserPhoneEditTxt.setError("رقم الهاتف يجب ان يبدأ بـ 01 !");
            binding.loginUserPhoneEditTxt.requestFocus();
            return;
        }
        //User Password Validation
        if (TextUtils.isEmpty(binding.loginUserPasswordEditTxt.getText())) {
            binding.loginUserPasswordEditTxt.setError("ادخل كلمة السر من فضلك !");
            binding.loginUserPasswordEditTxt.requestFocus();
            return;
        }
        if (Objects.requireNonNull(binding.loginUserPasswordEditTxt.getText()).length() < 8) {
            binding.loginUserPasswordEditTxt.setError("كلمة السر يجب ان تكون اكثر من 8 حروف او ارقام !");
            binding.loginUserPasswordEditTxt.requestFocus();
            return;
        }

        signIn();
    }


    private void signIn() {
        //Progress Bar
        editor.putString("UserType", "User");
        editor.apply();

        binding.loginProgressBarHolder.setAnimation(inAnimation);
        binding.loginProgressBarHolder.setVisibility(View.VISIBLE);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);


        Query query;
        query = usersRef.child(Objects.requireNonNull(binding.loginUserPhoneEditTxt.getText()).toString());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    if (snapshot.hasChildren()) {
                        String userPassword = snapshot.child("UserPassword").getValue(String.class);
                        String userPhone = snapshot.child("UserPrimaryPhone").getValue(String.class);
                        assert userPassword != null;
                        if (userPassword.equals(Objects.requireNonNull(binding.loginUserPasswordEditTxt.getText()).toString())) {
                            //clear progress bar
                            binding.loginProgressBarHolder.setAnimation(outAnimation);
                            binding.loginProgressBarHolder.setVisibility(View.GONE);
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            //Save User State
                            editor.putString("isLogged", userPhone);
                            editor.apply();
                            //Go To Home Activity
                            goToHome();

                        } else {
                            //clear progress bar
                            binding.loginProgressBarHolder.setAnimation(outAnimation);
                            binding.loginProgressBarHolder.setVisibility(View.GONE);
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            // Wrong Password Helper
                            binding.loginUserPasswordEditTxt.setError("كلمة المرور غير صحيحة !");
                            binding.loginUserPasswordEditTxt.requestFocus();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void goToHome() {
        Intent intent = new Intent(Login.this, Home.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        this.finish();
    }

    public void goToSignUp() {
        Intent intent = new Intent(Login.this, SignUp.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        this.finish();
    }

    private void animation() {
        inAnimation.setDuration(200);
        outAnimation.setDuration(200);

        // login welcome text animation
        binding.loginLogoImg.setTranslationX(400f);
        binding.loginLogoImg.setAlpha(0f);
        binding.loginLogoImg.animate().translationX(0f).alpha(1f).setDuration(600).setStartDelay(500).start();
        //---------------------------------------------------------------------
        // user phone animation
        binding.loginUserPhoneEditTxt.setTranslationX(600f);
        binding.loginUserPhoneEditTxt.setAlpha(0f);
        binding.loginUserPhoneEditTxt.animate().translationX(0f).alpha(1f).setDuration(800).setStartDelay(500).start();
        //---------------------------------------------------------------------
        // user password animation
        binding.loginUserPasswordEditTxt.setTranslationX(800f);
        binding.loginUserPasswordEditTxt.setAlpha(0f);
        binding.loginUserPasswordEditTxt.animate().translationX(0f).alpha(1f).setDuration(1000).setStartDelay(500).start();
        //---------------------------------------------------------------------
        // forgot password animation
        binding.ForgotPassword.setTranslationX(1000f);
        binding.ForgotPassword.setAlpha(0f);
        binding.ForgotPassword.animate().translationX(0f).alpha(1f).setDuration(1200).setStartDelay(500).start();
        //---------------------------------------------------------------------
        // user login button animation
        binding.loginLoginBtn.setTranslationX(1200f);
        binding.loginLoginBtn.setAlpha(0f);
        binding.loginLoginBtn.animate().translationX(0f).alpha(1f).setDuration(1400).setStartDelay(500).start();
        //---------------------------------------------------------------------
        // create new account animation
        binding.loginSignUpTextView.setTranslationX(1400f);
        binding.loginSignUpTextView.setAlpha(0f);
        binding.loginSignUpTextView.animate().translationX(0f).alpha(1f).setDuration(1600).setStartDelay(500).start();
        //---------------------------------------------------------------------
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}