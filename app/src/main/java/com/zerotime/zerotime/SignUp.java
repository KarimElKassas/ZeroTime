package com.zerotime.zerotime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.zerotime.zerotime.databinding.ActivitySignUpBinding;

import java.util.HashMap;
import java.util.Objects;

public class SignUp extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private ActivitySignUpBinding binding;
    private static final String[] regions = {"القاهرة", "الاسكندرية", "الجيزة"};

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference usersRef;

    private String userToken = "";
    private HashMap<String,Object> usersMap ;

    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        usersRef = FirebaseDatabase.getInstance().getReference("Users");
        usersMap = new HashMap<>();
        //animation
        inAnimation = new AlphaAnimation(0f,2f);
        outAnimation = new AlphaAnimation(2f,0f);
        //Regions Spinner
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(SignUp.this,
                android.R.layout.simple_spinner_item,regions);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.signUpRegionsSpinner.setAdapter(adapter);
        binding.signUpRegionsSpinner.setOnItemSelectedListener(this);

        //get user token id
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (task.isSuccessful()){
                    userToken = Objects.requireNonNull(task.getResult()).getToken();
                }
            }
        });
        //Sign In Text
        binding.signUpLoginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToLogin();
            }
        });
        //Sign Up Button
        binding.signUpSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkData();
            }
        });

    }
    private void checkData(){
        //User Name Validation
        if (TextUtils.isEmpty(binding.signUpUserNameEditTxt.getText())){
            binding.signUpUserNameEditTxt.setError("ادخل الاسم من فضلك !");
            binding.signUpUserNameEditTxt.requestFocus();
            return;
        }
        //User Password Validation
        if (TextUtils.isEmpty(binding.signUpUserPasswordEditTxt.getText())){
            binding.signUpUserPasswordEditTxt.setError("ادخل كلمة السر من فضلك !");
            binding.signUpUserPasswordEditTxt.requestFocus();
            return;
        }
        if (Objects.requireNonNull(binding.signUpUserPasswordEditTxt.getText()).length() < 8){
            binding.signUpUserPasswordEditTxt.setError("كلمة السر يجب ان تكون اكثر من او تساوي 8 حروف او ارقام !");
            binding.signUpUserPasswordEditTxt.requestFocus();
            return;
        }
        //Primary Phone Validation
        if (TextUtils.isEmpty(binding.signUpUserPrimaryPhoneEditTxt.getText())){
            binding.signUpUserPrimaryPhoneEditTxt.setError("ادخل رقم الهاتف الاول من فضلك !");
            binding.signUpUserPrimaryPhoneEditTxt.requestFocus();
            return;
        }
        if (Objects.requireNonNull(binding.signUpUserPrimaryPhoneEditTxt.getText()).length() != 11){
            binding.signUpUserPrimaryPhoneEditTxt.setError("رقم الهاتف يجب ان يتكون من 11 رقم فقط !");
            binding.signUpUserPrimaryPhoneEditTxt.requestFocus();
            return;
        }
        if (!binding.signUpUserPrimaryPhoneEditTxt.getText().toString().startsWith("01")){
            binding.signUpUserPrimaryPhoneEditTxt.setError("رقم الهاتف يجب ان يبدأ بـ 01 !");
            binding.signUpUserPrimaryPhoneEditTxt.requestFocus();
            return;
        }
        //Secondary Phone Validation
        if (TextUtils.isEmpty(binding.signUpUserSecondaryPhoneEditTxt.getText())){
            binding.signUpUserSecondaryPhoneEditTxt.setError("ادخل رقم الهاتف الثانى من فضلك !");
            binding.signUpUserSecondaryPhoneEditTxt.requestFocus();
            return;
        }
        if (Objects.requireNonNull(binding.signUpUserSecondaryPhoneEditTxt.getText()).length() != 11){
            binding.signUpUserSecondaryPhoneEditTxt.setError("رقم الهاتف يجب ان يتكون من 11 رقم فقط !");
            binding.signUpUserSecondaryPhoneEditTxt.requestFocus();
            return;
        }
        if (!binding.signUpUserSecondaryPhoneEditTxt.getText().toString().startsWith("01")){
            binding.signUpUserSecondaryPhoneEditTxt.setError("رقم الهاتف يجب ان يبدأ بـ 01 !");
            binding.signUpUserSecondaryPhoneEditTxt.requestFocus();
            return;
        }
        String primaryPhone = binding.signUpUserPrimaryPhoneEditTxt.getText().toString();
        String secondaryPhone = binding.signUpUserSecondaryPhoneEditTxt.getText().toString();
        if (primaryPhone.equals(secondaryPhone)) {
            Toast.makeText(this, "من فضلك ادخل رقمين مختلفين !", Toast.LENGTH_SHORT).show();
            return;
        }
        //User Address Validation
        if (TextUtils.isEmpty(binding.signUpUserAddressEditTxt.getText())){
            binding.signUpUserAddressEditTxt.setError("ادخل العنوان بالتفصيل من فضلك !");
            binding.signUpUserAddressEditTxt.requestFocus();
            return;
        }
        //User Region Validation
        if (binding.signUpRegionsSpinner.getSelectedItem() == null){
            Toast.makeText(this,"من فضلك قم باختيار المنطقه !",Toast.LENGTH_SHORT).show();
            return;
        }
        createNewUser();
    }
    private void createNewUser(){
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
        usersMap.put("UserToken",userToken);
        usersMap.put("UserId",Objects.requireNonNull(binding.signUpUserPrimaryPhoneEditTxt.getText()).toString());

        usersRef.child(binding.signUpUserPrimaryPhoneEditTxt.getText().toString())
                .setValue(usersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    //clear progress bar
                    binding.signUpProgressBarHolder.setAnimation(outAnimation);
                    binding.signUpProgressBarHolder.setVisibility(View.GONE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                    Toast.makeText(SignUp.this.getApplicationContext(), "Sign Up Successfully", Toast.LENGTH_SHORT).show();
                    SignUp.this.goToLogin();
                } else {
                    //clear progress bar
                    binding.signUpProgressBarHolder.setAnimation(outAnimation);
                    binding.signUpProgressBarHolder.setVisibility(View.GONE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                    Toast.makeText(SignUp.this.getApplicationContext(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        switch (position) {
            case 0:
                usersMap.put("UserRegion","القاهرة");
                usersMap.put("UserRegionIndex",0);
                // Whatever you want to happen when the first item gets selected
                break;
            case 1:
                usersMap.put("UserRegion","الاسكندرية");
                usersMap.put("UserRegionIndex",1);
                // Whatever you want to happen when the second item gets selected
                break;
            case 2:
                usersMap.put("UserRegion","الجيزة");
                usersMap.put("UserRegionIndex",2);
                // Whatever you want to happen when the thrid item gets selected
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    private void goToLogin(){
        Intent intent = new Intent(SignUp.this, Login.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        this.finish();
    }
}