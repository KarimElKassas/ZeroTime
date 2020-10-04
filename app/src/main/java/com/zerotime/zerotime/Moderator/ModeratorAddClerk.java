package com.zerotime.zerotime.Moderator;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.zerotime.zerotime.R;
import com.zerotime.zerotime.databinding.ModeratorActivityAddClerckBinding;
import com.zerotime.zerotime.myBroadCast;

import java.util.HashMap;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class ModeratorAddClerk extends AppCompatActivity {
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;

    private ModeratorActivityAddClerckBinding binding;
    private DatabaseReference clerksRef;
    private HashMap<String, String> clerksMap;

    private String hasVehicle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //view binding
        binding = ModeratorActivityAddClerckBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //animation
        inAnimation = new AlphaAnimation(0f, 2f);
        outAnimation = new AlphaAnimation(2f, 0f);

        //Check Internet Connection State
        checkInternetConnection();

        //Firebase Database Reference initialization
        clerksRef = FirebaseDatabase.getInstance().getReference("Clerks");
        clerksMap = new HashMap<>();

        //Add Clerk Button
        binding.ModeratorAddClerkAddBtn.setOnClickListener(view1 -> addClerk());

    }


    public void addClerk() {
        try {
            //Clerk Name Validation
            if (TextUtils.isEmpty(binding.ModeratorAddClerkNameEdt.getText())) {
                binding.ModeratorAddClerkNameEdt.setError("من فضلك قم بادخال اسم المندوب !");
                binding.ModeratorAddClerkNameEdt.requestFocus();
                return;
            }
            //Primary Phone Validation
            if (TextUtils.isEmpty(binding.ModeratorAddClerkPhone1Edt.getText())) {
                binding.ModeratorAddClerkPhone1Edt.setError("ادخل رقم الهاتف الاول من فضلك !");
                binding.ModeratorAddClerkPhone1Edt.requestFocus();
                return;
            }
            if (Objects.requireNonNull(binding.ModeratorAddClerkPhone1Edt.getText()).length() != 11) {
                binding.ModeratorAddClerkPhone1Edt.setError("رقم الهاتف يجب ان يتكون من 11 رقم فقط !");
                binding.ModeratorAddClerkPhone1Edt.requestFocus();
                return;
            }
            if (!binding.ModeratorAddClerkPhone1Edt.getText().toString().startsWith("01")) {
                binding.ModeratorAddClerkPhone1Edt.setError("رقم الهاتف يجب ان يبدأ بـ 01 !");
                binding.ModeratorAddClerkPhone1Edt.requestFocus();
                return;
            }
            //Secondary Phone Validation
            if (TextUtils.isEmpty(binding.ModeratorAddClerkPhone2Edt.getText())) {
                binding.ModeratorAddClerkPhone2Edt.setError("ادخل رقم الهاتف الثانى من فضلك !");
                binding.ModeratorAddClerkPhone2Edt.requestFocus();
                return;
            }
            if (Objects.requireNonNull(binding.ModeratorAddClerkPhone2Edt.getText()).length() != 11) {
                binding.ModeratorAddClerkPhone2Edt.setError("رقم الهاتف يجب ان يتكون من 11 رقم فقط !");
                binding.ModeratorAddClerkPhone2Edt.requestFocus();
                return;
            }
            if (!binding.ModeratorAddClerkPhone2Edt.getText().toString().startsWith("01")) {
                binding.ModeratorAddClerkPhone2Edt.setError("رقم الهاتف يجب ان يبدأ بـ 01 !");
                binding.ModeratorAddClerkPhone2Edt.requestFocus();
                return;
            }
            //Primary Phone and Secondary Phone difference Validation
            String primaryPhone = binding.ModeratorAddClerkPhone1Edt.getText().toString();
            String secondaryPhone = binding.ModeratorAddClerkPhone2Edt.getText().toString();
            if (primaryPhone.equals(secondaryPhone)) {
                binding.ModeratorAddClerkPhone2Edt.setError("من فضلك قم باختيار رقمين مختلفين !");
                binding.ModeratorAddClerkPhone2Edt.requestFocus();
                return;
            }
            //Clerk Address Validation
            if (TextUtils.isEmpty(binding.ModeratorAddClerkAddressEdt.getText())) {
                binding.ModeratorAddClerkAddressEdt.setError("ادخل العنوان بالتفصيل من فضلك !");
                binding.ModeratorAddClerkAddressEdt.requestFocus();
                return;
            }
            //Clerk Age Validation
            if (TextUtils.isEmpty(binding.ModeratorAddClerkAgeEdt.getText())) {
                binding.ModeratorAddClerkAgeEdt.setError("من فضلك ادخل عمر المندوب !");
                binding.ModeratorAddClerkAgeEdt.requestFocus();
                return;
            }
            //Clerk Vehicle Validation
            if (!binding.radioHave.isChecked() && !binding.radioDonthave.isChecked()) {
                Toast.makeText(this, "من فضلك اخبرنا إن كنت تمتلك طياره ام لا ", Toast.LENGTH_SHORT).show();
                return;
            } else {
                if (binding.radioHave.isChecked()) hasVehicle = "يمتلك طياره";
                if (binding.radioDonthave.isChecked()) hasVehicle = "لا يمتلك طياره";
            }


            // getting data from user
            String name = Objects.requireNonNull(binding.ModeratorAddClerkNameEdt.getText()).toString();
            String phone1 = Objects.requireNonNull(binding.ModeratorAddClerkPhone1Edt.getText()).toString();
            String phone2 = Objects.requireNonNull(binding.ModeratorAddClerkPhone2Edt.getText()).toString();
            int age = Integer.parseInt(Objects.requireNonNull(binding.ModeratorAddClerkAgeEdt.getText()).toString());
            String address = Objects.requireNonNull(binding.ModeratorAddClerkAddressEdt.getText()).toString();

            //Progress Bar
            binding.addClerkProgressBarHolder.setAnimation(inAnimation);
            binding.addClerkProgressBarHolder.setVisibility(View.VISIBLE);
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            // full the map
            clerksMap.put("ClerkName", name);
            clerksMap.put("ClerkPhone1", phone1);
            clerksMap.put("ClerkPhone2", phone2);
            clerksMap.put("ClerkAge", String.valueOf(age));
            clerksMap.put("ClerkAddress", address);
            clerksMap.put("hasVehicle", hasVehicle);

            //send data to firebase
            clerksRef.child(phone1)
                    .setValue(clerksMap).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    //clear progress bar
                    binding.addClerkProgressBarHolder.setAnimation(outAnimation);
                    binding.addClerkProgressBarHolder.setVisibility(View.GONE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                    Toasty.success(getApplicationContext(),
                            "تمت الإضافه بنجاح ",
                            Toasty.LENGTH_SHORT,
                            true)
                            .show();

                    //Clear Texts from edit text
                    clearTools();
                } else {
                    //clear progress bar
                    binding.addClerkProgressBarHolder.setAnimation(outAnimation);
                    binding.addClerkProgressBarHolder.setVisibility(View.GONE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                    Toasty.error(getApplicationContext(),
                            Objects.requireNonNull(Objects.requireNonNull(task.getException()).getMessage()),
                            Toasty.LENGTH_SHORT,
                            true)
                            .show();
                }
            });
        } catch (Exception e) {
            Toasty.error(getApplicationContext(),
                    Objects.requireNonNull(e.getMessage()),
                    Toasty.LENGTH_SHORT,
                    true)
                    .show();
        }


    }

    private void clearTools() {
        Objects.requireNonNull(binding.ModeratorAddClerkNameEdt.getText()).clear();
        Objects.requireNonNull(binding.ModeratorAddClerkPhone1Edt.getText()).clear();
        Objects.requireNonNull(binding.ModeratorAddClerkPhone2Edt.getText()).clear();
        Objects.requireNonNull(binding.ModeratorAddClerkAddressEdt.getText()).clear();
        Objects.requireNonNull(binding.ModeratorAddClerkAgeEdt.getText()).clear();

        if (binding.radioHave.isChecked()) {
            binding.radioHave.setChecked(false);
        }
        if (binding.radioDonthave.isChecked()) {
            binding.radioDonthave.setChecked(false);
        }
    }


    private void checkInternetConnection() {
        myBroadCast broadCast = new myBroadCast();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(broadCast, intentFilter);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(ModeratorAddClerk.this, ModeratorHome.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

}