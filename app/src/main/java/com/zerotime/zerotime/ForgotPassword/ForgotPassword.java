package com.zerotime.zerotime.ForgotPassword;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zerotime.zerotime.MyBroadCast;
import com.zerotime.zerotime.databinding.ActivityForgotPasswordBinding;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class ForgotPassword extends AppCompatActivity {
    private ActivityForgotPasswordBinding binding;
    String phone_number, phone_number2;
    String phone2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        binding.forgotNextBtn.setOnClickListener(view1 -> {
            // check Internet Connection
            checkInternetConnection();

            // data validation
            validateData();

        });

    }

    private void validateData() {
        //Phone Validation
        if (TextUtils.isEmpty(binding.forgotPhoneEdt.getText())) {
            binding.forgotPhoneEdt.setError(" من فضلك ادخل رقم الهاتف !");
            binding.forgotPhoneEdt.requestFocus();
            return;
        }
        if (Objects.requireNonNull(binding.forgotPhoneEdt.getText()).length() != 11) {
            binding.forgotPhoneEdt.setError("رقم الهاتف يجب ان يتكون من 11 رقم فقط !");
            binding.forgotPhoneEdt.requestFocus();
            return;
        }
        if (!binding.forgotPhoneEdt.getText().toString().startsWith("01")) {
            binding.forgotPhoneEdt.setError("رقم الهاتف يجب ان يبدأ بـ 01 !");
            binding.forgotPhoneEdt.requestFocus();
            return;
        }
          if (TextUtils.isEmpty(binding.forgotPhone2Edt.getText())) {
            binding.forgotPhone2Edt.setError(" من فضلك ادخل رقم الهاتف الثاني !");
            binding.forgotPhone2Edt.requestFocus();
            return;
        }
        if (Objects.requireNonNull(binding.forgotPhone2Edt.getText()).length() != 11) {
            binding.forgotPhone2Edt.setError("رقم الهاتف يجب ان يتكون من 11 رقم فقط !");
            binding.forgotPhone2Edt.requestFocus();
            return;
        }
        if (!binding.forgotPhone2Edt.getText().toString().startsWith("01")) {
            binding.forgotPhone2Edt.setError("رقم الهاتف يجب ان يبدأ بـ 01 !");
            binding.forgotPhone2Edt.requestFocus();
            return;
        }
        String primaryPhone = binding.forgotPhoneEdt.getText().toString();
        String secondaryPhone = binding.forgotPhone2Edt.getText().toString();
        if (primaryPhone.equals(secondaryPhone)) {
            binding.forgotPhone2Edt.setError("من فضلك قم بادخال رقمين مختلفين !");
            binding.forgotPhone2Edt.requestFocus();
            return;
        }
        binding.forgetProgress.setVisibility(View.VISIBLE);
        phone_number = Objects.requireNonNull(binding.forgotPhoneEdt.getText()).toString();
        phone_number2 = Objects.requireNonNull(binding.forgotPhone2Edt.getText()).toString();

        //check weather user exist or not in DB
        DatabaseReference user = FirebaseDatabase.getInstance().getReference("Users");
        user.child(phone_number).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    if (snapshot.hasChildren()){
                        phone2 = snapshot.child("UserSecondaryPhone").getValue(String.class);

                        assert phone2 != null;
                        if (!phone2.equals(phone_number2)) {
                            binding.forgotPhone2Edt.setError("رقم هاتفك الثاني غير صحيح !");
                            binding.forgotPhone2Edt.requestFocus();
                            binding.forgetProgress.setVisibility(View.INVISIBLE);
                            return;

                        }
                        else{
                            binding.forgotPhoneEdt.setError(null);
                            Intent intent = new Intent(ForgotPassword.this, SetNewPassword.class);
                            intent.putExtra("phoneNo", phone_number);
                            //  intent.putExtra("whatToDo","updateData");
                            startActivity(intent);
                            finish();
                            binding.forgetProgress.setVisibility(View.GONE);

                        }
                    }
                    else {

                        binding.forgetProgress.setVisibility(View.GONE);
                        binding.forgotPhoneEdt.setError("لا يوجد مستخدم بهذا الرقم !");
                        binding.forgotPhoneEdt.requestFocus();

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void checkInternetConnection() {
        MyBroadCast broadCast = new MyBroadCast();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(broadCast, intentFilter);

    }
}