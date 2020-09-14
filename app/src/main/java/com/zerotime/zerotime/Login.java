package com.zerotime.zerotime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.zerotime.zerotime.Secretary.FollowingTheOrderState;
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
    public UserState userState ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        //animation
        inAnimation = new AlphaAnimation(0f,2f);
        outAnimation = new AlphaAnimation(2f,0f);
        // Initialize User State
        userState = new UserState();
        usersRef = FirebaseDatabase.getInstance().getReference("Users");
        editor = getSharedPreferences("UserState", MODE_PRIVATE).edit();
        editor.putString("isLogged", "null");
        editor.apply();

        binding.loginLoginBtn.setOnClickListener(view1 -> checkData());
        //Return To Sign Up
        binding.loginSignUpTextView.setOnClickListener(view12 -> goToSignUp());
    }
    private void checkData() {
        //Moderator Case
        if (Objects.requireNonNull(binding.loginUserPhoneEditTxt.getText()).toString().equals("0")
                && Objects.requireNonNull(binding.loginUserPasswordEditTxt.getText()).toString().equals("0")){
            Intent intent = new Intent(Login.this, ModeratorHome.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            this.finish();
            return;
        }
        //Secretary Case
        if (Objects.requireNonNull(binding.loginUserPhoneEditTxt.getText()).toString().equals("1")
                && Objects.requireNonNull(binding.loginUserPasswordEditTxt.getText()).toString().equals("1")){

            editor.putString("UserType", "secretary");
            editor.apply();

            Intent intent = new Intent(Login.this, SecretaryHome.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            this.finish();
            return;
        }
        //Phone Validation
        if (TextUtils.isEmpty(binding.loginUserPhoneEditTxt.getText())){
            binding.loginUserPhoneEditTxt.setError("ادخل رقم الهاتف الاول من فضلك !");
            binding.loginUserPhoneEditTxt.requestFocus();
            return;
        }
        if (Objects.requireNonNull(binding.loginUserPhoneEditTxt.getText()).length() != 11){
            binding.loginUserPhoneEditTxt.setError("رقم الهاتف يجب ان يتكون من 11 رقم فقط !");
            binding.loginUserPhoneEditTxt.requestFocus();
            return;
        }
        if (!binding.loginUserPhoneEditTxt.getText().toString().startsWith("01")){
            binding.loginUserPhoneEditTxt.setError("رقم الهاتف يجب ان يبدأ بـ 01 !");
            binding.loginUserPhoneEditTxt.requestFocus();
            return;
        }
        //User Password Validation
        if (TextUtils.isEmpty(binding.loginUserPasswordEditTxt.getText())){
            binding.loginUserPasswordEditTxt.setError("ادخل كلمة السر من فضلك !");
            binding.loginUserPasswordEditTxt.requestFocus();
            return;
        }
        if (Objects.requireNonNull(binding.loginUserPasswordEditTxt.getText()).length() < 8){
            binding.loginUserPasswordEditTxt.setError("كلمة السر يجب ان تكون اكثر من 8 حروف او ارقام !");
            binding.loginUserPasswordEditTxt.requestFocus();
            return;
        }

        signIn();
    }
    private void signIn(){
        //Progress Bar
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
                if (snapshot.exists()){
                    if (snapshot.hasChildren()){
                        String userPassword = snapshot.child("UserPassword").getValue(String.class);
                        String userPhone = snapshot.child("UserPrimaryPhone").getValue(String.class);
                        assert userPassword != null;
                        if (userPassword.equals(Objects.requireNonNull(binding.loginUserPasswordEditTxt.getText()).toString())){
                            //clear progress bar
                            binding.loginProgressBarHolder.setAnimation(outAnimation);
                            binding.loginProgressBarHolder.setVisibility(View.GONE);
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            //Save User State
                            editor.putString("isLogged",userPhone);
                            editor.apply();
                            //Go To Home Activity
                            goToHome();

                        }else {
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
    public void goToHome(){
        Intent intent = new Intent(Login.this,Home.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        this.finish();
    }
    public void goToSignUp(){
        Intent intent = new Intent(Login.this,SignUp.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        this.finish();
    }
}