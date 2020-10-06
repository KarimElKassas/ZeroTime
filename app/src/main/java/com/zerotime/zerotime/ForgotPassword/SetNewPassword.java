package com.zerotime.zerotime.ForgotPassword;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.zerotime.zerotime.MyBroadCast;
import com.zerotime.zerotime.databinding.ActivitySetNewPasswordBinding;

import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;

public class SetNewPassword extends AppCompatActivity {
    private ActivitySetNewPasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySetNewPasswordBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        binding.updatePassword.setOnClickListener(view1 -> {
            // check Internet Connection
            checkInternetConnection();

            // data validation
            validateData();




        });


    }

    private void validateData() {
        //password Validation
        if (TextUtils.isEmpty(Objects.requireNonNull(binding.newpassword1.getText()).toString().trim())) {
            binding.newpassword1.setError(" من فضلك ادخل كلمه المرور الجديده !");
            binding.newpassword1.requestFocus();
            return;
        }
        if (Objects.requireNonNull(binding.newpassword1.getText()).length() < 8) {
            binding.newpassword1.setError("كلمة المرور يجب ان تكون اكثر من 7 حروف او ارقام !");
            binding.newpassword1.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(binding.newpassword2.getText().toString().trim())) {
            binding.newpassword2.setError(" من فضلك اعد كتابه كلمه المرور الجديده !");
            binding.newpassword2.requestFocus();
            return;
        }
        if (Objects.requireNonNull(binding.newpassword2.getText()).length() < 8) {
            binding.newpassword2.setError("كلمة المرور يجب ان تكون اكثر من 7 حروف او ارقام !");
            binding.newpassword2.requestFocus();
            return;
        }
        if (!binding.newpassword1.getText().toString().trim().equals(binding.newpassword2.getText().toString().trim())) {
            Toast.makeText(this, "يجب ان تتطابق الكلمتان !", Toast.LENGTH_SHORT).show();
            return;
        }

        binding.setNewPasswordProgress.setVisibility(View.VISIBLE);
        String newPassword = binding.newpassword1.getText().toString().trim();
        String phone = getIntent().getStringExtra("phoneNo");
        // update data in firebase
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        assert phone != null;
        reference.child(phone).child("UserPassword").setValue(newPassword);
        startActivity(new Intent(getApplicationContext(), forgotPasswordSuccess.class));
        finish();

    }

    private void checkInternetConnection() {
        MyBroadCast broadCast = new MyBroadCast();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(broadCast, intentFilter);

    }
}