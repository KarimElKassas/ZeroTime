package com.zerotime.zerotime.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.zerotime.zerotime.databinding.UserFragmentUpdateUserDataBinding;

import java.util.HashMap;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;


public class UpdateUserDataFragment extends Fragment {

    UserFragmentUpdateUserDataBinding binding;
    DatabaseReference usersRef;
    HashMap<String,Object> usersMap;

    String userPhone;
    String userToken;

    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    View view;
    Context context;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = UserFragmentUpdateUserDataBinding.inflate(inflater, container, false);
        view = binding.getRoot();
        context = container.getContext();

        usersMap = new HashMap<>();
        binding.updateDataFragmentPrimaryPhoneEditTxt.setEnabled(false);
        //animation
        inAnimation = new AlphaAnimation(0f,2f);
        outAnimation = new AlphaAnimation(2f,0f);


        usersRef = FirebaseDatabase.getInstance().getReference("Users");
        SharedPreferences prefs = Objects.requireNonNull(getContext()).getSharedPreferences("UserState", MODE_PRIVATE);
        userPhone = prefs.getString("isLogged", "No phone defined");//"No name defined" is the default value.

        //get Token ID
        //get user token id
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (task.isSuccessful()){
                    userToken = Objects.requireNonNull(task.getResult()).getToken();
                }
            }
        });



        return view;
    }


    private void checkData() {
        //User Name Validation
        if (TextUtils.isEmpty(binding.updateDataFragmentNameEditTxt.getText())) {
            binding.updateDataFragmentNameEditTxt.setError("ادخل الاسم من فضلك !");
            binding.updateDataFragmentNameEditTxt.requestFocus();
            return;
        }
        //User Password Validation
        if (TextUtils.isEmpty(binding.updateDataFragmentPasswordEditTxt.getText())) {
            binding.updateDataFragmentPasswordEditTxt.setError("ادخل كلمة السر من فضلك !");
            binding.updateDataFragmentPasswordEditTxt.requestFocus();
            return;
        }
        if (Objects.requireNonNull(binding.updateDataFragmentPasswordEditTxt.getText()).length() < 8) {
            binding.updateDataFragmentPasswordEditTxt.setError("كلمة السر يجب ان تكون اكثر من 8 حروف او ارقام !");
            binding.updateDataFragmentPasswordEditTxt.requestFocus();
            return;
        }
        //Primary Phone Validation
        if (TextUtils.isEmpty(binding.updateDataFragmentPrimaryPhoneEditTxt.getText())) {
            binding.updateDataFragmentPrimaryPhoneEditTxt.setError("ادخل رقم الهاتف الاول من فضلك !");
            binding.updateDataFragmentPrimaryPhoneEditTxt.requestFocus();
            return;
        }
        if (Objects.requireNonNull(binding.updateDataFragmentPrimaryPhoneEditTxt.getText()).length() != 11) {
            binding.updateDataFragmentPrimaryPhoneEditTxt.setError("رقم الهاتف يجب ان يتكون من 11 رقم فقط !");
            binding.updateDataFragmentPrimaryPhoneEditTxt.requestFocus();
            return;
        }
        if (!binding.updateDataFragmentPrimaryPhoneEditTxt.getText().toString().startsWith("01")) {
            binding.updateDataFragmentPrimaryPhoneEditTxt.setError("رقم الهاتف يجب ان يبدأ بـ 01 !");
            binding.updateDataFragmentPrimaryPhoneEditTxt.requestFocus();
            return;
        }
        //Secondary Phone Validation
        if (TextUtils.isEmpty(binding.updateDataFragmentSecondaryPhoneEditTxt.getText())) {
            binding.updateDataFragmentSecondaryPhoneEditTxt.setError("ادخل رقم الهاتف الثانى من فضلك !");
            binding.updateDataFragmentSecondaryPhoneEditTxt.requestFocus();
            return;
        }
        if (Objects.requireNonNull(binding.updateDataFragmentSecondaryPhoneEditTxt.getText()).length() != 11) {
            binding.updateDataFragmentSecondaryPhoneEditTxt.setError("رقم الهاتف يجب ان يتكون من 11 رقم فقط !");
            binding.updateDataFragmentSecondaryPhoneEditTxt.requestFocus();
            return;
        }
        if (!binding.updateDataFragmentSecondaryPhoneEditTxt.getText().toString().startsWith("01")) {
            binding.updateDataFragmentSecondaryPhoneEditTxt.setError("رقم الهاتف يجب ان يبدأ بـ 01 !");
            binding.updateDataFragmentSecondaryPhoneEditTxt.requestFocus();
            return;
        }
        String primaryPhone = binding.updateDataFragmentPrimaryPhoneEditTxt.getText().toString();
        String secondaryPhone = binding.updateDataFragmentSecondaryPhoneEditTxt.getText().toString();
        if (primaryPhone.equals(secondaryPhone)) {
            Toast.makeText(getContext(), "من فضلك ادخل رقمين مختلفين !", Toast.LENGTH_SHORT).show();
            return;
        }
        //User Address Validation
        if (TextUtils.isEmpty(binding.updateDataFragmentAddressEditTxt.getText())) {
            binding.updateDataFragmentAddressEditTxt.setError("ادخل العنوان بالتفصيل من فضلك !");
            binding.updateDataFragmentAddressEditTxt.requestFocus();
            return;
        }

        updateData();
    }

    private void updateData() {
        //Progress Bar
        binding.updateDataFragmentProgressBarHolder.setAnimation(inAnimation);
        binding.updateDataFragmentProgressBarHolder.setVisibility(View.VISIBLE);
        Objects.requireNonNull(getActivity()).getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        usersRef.child(userPhone).child("UserName").setValue(Objects.requireNonNull(binding.updateDataFragmentNameEditTxt.getText()).toString());
        usersRef.child(userPhone).child("UserPassword").setValue(Objects.requireNonNull(binding.updateDataFragmentPasswordEditTxt.getText()).toString());
        usersRef.child(userPhone).child("UserSecondaryPhone").setValue(Objects.requireNonNull(binding.updateDataFragmentSecondaryPhoneEditTxt.getText()).toString());
        usersRef.child(userPhone).child("UserAddress").setValue(Objects.requireNonNull(binding.updateDataFragmentAddressEditTxt.getText()).toString())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            //clear progress bar
                            binding.updateDataFragmentProgressBarHolder.setAnimation(outAnimation);
                            binding.updateDataFragmentProgressBarHolder.setVisibility(View.GONE);
                            Objects.requireNonNull(getActivity()).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                            Toast.makeText(getContext(), "تم تعديل البيانات بنجاح", Toast.LENGTH_SHORT).show();
                        }else {
                            //clear progress bar
                            binding.updateDataFragmentProgressBarHolder.setAnimation(outAnimation);
                            binding.updateDataFragmentProgressBarHolder.setVisibility(View.GONE);
                            Objects.requireNonNull(getActivity()).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                            Toast.makeText(getContext(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        usersRef.child(Objects.requireNonNull(userPhone)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    if (snapshot.hasChildren()) {
                        binding.updateDataFragmentNameEditTxt
                                .setText(snapshot.child("UserName").getValue(String.class));
                        binding.updateDataFragmentPasswordEditTxt
                                .setText(snapshot.child("UserPassword").getValue(String.class));
                        binding.updateDataFragmentPrimaryPhoneEditTxt
                                .setText(snapshot.child("UserPrimaryPhone").getValue(String.class));
                        binding.updateDataFragmentSecondaryPhoneEditTxt
                                .setText(snapshot.child("UserSecondaryPhone").getValue(String.class));
                        binding.updateDataFragmentAddressEditTxt
                                .setText(snapshot.child("UserAddress").getValue(String.class));

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.updateDataFragmentUpdateBtn.setOnClickListener(view1 -> {
            checkData();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener((v, keyCode, event) -> {

            if (keyCode == KeyEvent.KEYCODE_BACK) {
                assert getFragmentManager() != null;
                getFragmentManager().popBackStackImmediate();
                return true;
            }

            return false;
        });
    }
}