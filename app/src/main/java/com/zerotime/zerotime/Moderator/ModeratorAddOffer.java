package com.zerotime.zerotime.Moderator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zerotime.zerotime.No_Internet_Connection;
import com.zerotime.zerotime.R;
import com.zerotime.zerotime.databinding.ModeratorActivityAddOfferBinding;
import com.zerotime.zerotime.MyBroadCast;

import java.util.HashMap;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class ModeratorAddOffer extends AppCompatActivity {
    private ModeratorActivityAddOfferBinding binding;
    private DatabaseReference offersRef;
    private HashMap<String,String> offersMap = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ModeratorActivityAddOfferBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // Check Internet State
        if (!haveNetworkConnection()) {
            Intent i = new Intent(ModeratorAddOffer.this, No_Internet_Connection.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
        }
        checkInternetConnection();
        //-----------------------------------
        offersRef = FirebaseDatabase.getInstance().getReference("Offers");

        offersRef.child("Offers").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    if (snapshot.hasChildren()){
                        String firstOffer = snapshot.child("FirstOffer").getValue(String.class);
                        String secondOffer = snapshot.child("SecondOffer").getValue(String.class);
                        String thirdOffer = snapshot.child("ThirdOffer").getValue(String.class);
                        String fourthOffer = snapshot.child("FourthOffer").getValue(String.class);
                        String fifthOffer = snapshot.child("FifthOffer").getValue(String.class);
                        String sixthOffer = snapshot.child("SixthOffer").getValue(String.class);
                        binding.moderatorAddOfferFirstFixedOfferEditText.setText(firstOffer);
                        binding.moderatorAddOfferSecondFixedOfferEditText.setText(secondOffer);
                        binding.moderatorAddOfferThirdFixedOfferEditText.setText(thirdOffer);
                        binding.moderatorAddOfferFourthFixedOfferEditText.setText(fourthOffer);
                        binding.moderatorAddOfferFifthFixedOfferEditText.setText(fifthOffer);
                        binding.moderatorAddOfferSixthFixedOfferEditText.setText(sixthOffer);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.moderatorAddOfferSixthUpdateBtn.setOnClickListener(view1 -> {
            addOffer();
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(ModeratorAddOffer.this, ModeratorHome.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }
    private void addOffer(){
        offersRef.child("Offers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists() || !snapshot.hasChildren()){
                    //First Offer Edit Text
                    if (TextUtils.isEmpty(binding.moderatorAddOfferFirstFixedOfferEditText.getText())){
                        offersMap.put("FirstOffer","لا يوجد");
                    }else {
                        offersMap.put("FirstOffer", Objects.requireNonNull(binding.moderatorAddOfferFirstFixedOfferEditText.getText()).toString());
                    }
                    //Second Offer Edit Text
                    if (TextUtils.isEmpty(binding.moderatorAddOfferSecondFixedOfferEditText.getText())){
                        offersMap.put("SecondOffer","لا يوجد");
                    }else {
                        offersMap.put("SecondOffer", Objects.requireNonNull(binding.moderatorAddOfferSecondFixedOfferEditText.getText()).toString());
                    }
                    //Third Offer Edit Text
                    if (TextUtils.isEmpty(binding.moderatorAddOfferThirdFixedOfferEditText.getText())){
                        offersMap.put("ThirdOffer","لا يوجد");
                    }else {
                        offersMap.put("ThirdOffer", Objects.requireNonNull(binding.moderatorAddOfferThirdFixedOfferEditText.getText()).toString());
                    }
                    //Fourth Offer Edit Text
                    if (TextUtils.isEmpty(binding.moderatorAddOfferFourthFixedOfferEditText.getText())){
                        offersMap.put("FourthOffer","لا يوجد");
                    }else {
                        offersMap.put("FourthOffer", Objects.requireNonNull(binding.moderatorAddOfferFourthFixedOfferEditText.getText()).toString());
                    }
                    //Fifth Offer Edit Text
                    if (TextUtils.isEmpty(binding.moderatorAddOfferFifthFixedOfferEditText.getText())){
                        offersMap.put("FifthOffer","لا يوجد");
                    }else {
                        offersMap.put("FifthOffer", Objects.requireNonNull(binding.moderatorAddOfferFifthFixedOfferEditText.getText()).toString());
                    }
                    //Sixth Offer Edit Text
                    if (TextUtils.isEmpty(binding.moderatorAddOfferSixthFixedOfferEditText.getText())){
                        offersMap.put("SixthOffer","لا يوجد");
                    }else {
                        offersMap.put("SixthOffer", Objects.requireNonNull(binding.moderatorAddOfferSixthFixedOfferEditText.getText()).toString());
                    }
                    offersRef.child("Offers").setValue(offersMap).addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            //clearTools();
                            Toasty.success(getApplicationContext(),"تم بنجاح",Toasty.LENGTH_SHORT,true).show();
                        }
                    });
                }else {
                    String firstOffer = snapshot.child("FirstOffer").getValue(String.class);
                    String secondOffer = snapshot.child("SecondOffer").getValue(String.class);
                    String thirdOffer = snapshot.child("ThirdOffer").getValue(String.class);
                    String fourthOffer = snapshot.child("FourthOffer").getValue(String.class);
                    String fifthOffer = snapshot.child("FifthOffer").getValue(String.class);
                    String sixthOffer = snapshot.child("SixthOffer").getValue(String.class);

                    //First Offer
                    if (TextUtils.isEmpty(binding.moderatorAddOfferFirstFixedOfferEditText.getText())){
                        offersRef.child("Offers").child("FirstOffer").setValue(firstOffer);
                    }else {
                        offersRef.child("Offers").child("FirstOffer")
                                .setValue(Objects.requireNonNull(binding.moderatorAddOfferFirstFixedOfferEditText.getText()).toString());
                    }
                    //Second Offer
                    if (TextUtils.isEmpty(binding.moderatorAddOfferSecondFixedOfferEditText.getText())){
                        offersRef.child("Offers").child("SecondOffer").setValue(secondOffer);
                    }else {
                        offersRef.child("Offers").child("SecondOffer")
                                .setValue(Objects.requireNonNull(binding.moderatorAddOfferSecondFixedOfferEditText.getText()).toString());
                        //binding.moderatorAddOfferSecondFixedOfferEditText.getText().clear();
                    }
                    //Third Offer
                    if (TextUtils.isEmpty(binding.moderatorAddOfferThirdFixedOfferEditText.getText())){
                        offersRef.child("Offers").child("ThirdOffer").setValue(thirdOffer);
                    }else {
                        offersRef.child("Offers").child("ThirdOffer")
                                .setValue(Objects.requireNonNull(binding.moderatorAddOfferThirdFixedOfferEditText.getText()).toString());
                    }
                    //Fourth Offer
                    if (TextUtils.isEmpty(binding.moderatorAddOfferFourthFixedOfferEditText.getText())){
                        offersRef.child("Offers").child("FourthOffer").setValue(fourthOffer);
                    }else {
                        offersRef.child("Offers").child("FourthOffer")
                                .setValue(Objects.requireNonNull(binding.moderatorAddOfferFourthFixedOfferEditText.getText()).toString());
                    }
                    //Fifth Offer
                    if (TextUtils.isEmpty(binding.moderatorAddOfferFifthFixedOfferEditText.getText())){
                        offersRef.child("Offers").child("FifthOffer").setValue(fifthOffer);
                    }else {
                        offersRef.child("Offers").child("FifthOffer")
                                .setValue(Objects.requireNonNull(binding.moderatorAddOfferFifthFixedOfferEditText.getText()).toString());
                    }
                    //Sixth Offer
                    if (TextUtils.isEmpty(binding.moderatorAddOfferSixthFixedOfferEditText.getText())){
                        offersRef.child("Offers").child("SixthOffer").setValue(sixthOffer);
                    }else {
                        offersRef.child("Offers").child("SixthOffer")
                                .setValue(Objects.requireNonNull(binding.moderatorAddOfferSixthFixedOfferEditText.getText()).toString());
                    }
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
    private void checkInternetConnection(){
        MyBroadCast broadCast=new MyBroadCast();
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(broadCast,intentFilter);

    }
}