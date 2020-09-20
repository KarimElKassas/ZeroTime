package com.zerotime.zerotime.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zerotime.zerotime.R;
import com.zerotime.zerotime.databinding.FragmentDisplayOffersBinding;

import java.util.Objects;


public class DisplayOffersFragment extends Fragment {
    private FragmentDisplayOffersBinding binding;

    private DatabaseReference offersRef;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDisplayOffersBinding.inflate(getLayoutInflater());
        view = binding.getRoot();

        offersRef = FirebaseDatabase.getInstance().getReference("Offers");


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gettingOffers();

    }

    private void gettingOffers(){
        offersRef.child("Offers").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    if (snapshot.hasChildren()){
                        binding.displayOffersFragmentProgress.setVisibility(View.GONE);
                        binding.scrollLayout.setVisibility(View.VISIBLE);
                        String firstOffer = snapshot.child("FirstOffer").getValue(String.class);
                        String secondOffer = snapshot.child("SecondOffer").getValue(String.class);
                        String thirdOffer = snapshot.child("ThirdOffer").getValue(String.class);
                        String fourthOffer = snapshot.child("FourthOffer").getValue(String.class);
                        String fifthOffer = snapshot.child("FifthOffer").getValue(String.class);
                        String sixthOffer = snapshot.child("SixthOffer").getValue(String.class);

                        if (Objects.requireNonNull(firstOffer).equals("لا يوجد")){
                            binding.displayOffersFragmentFirstCard.setVisibility(View.GONE);
                        }else {
                            binding.displayOffersFragmentFirstOfferTextView.setText(firstOffer);

                        }
                        if (Objects.requireNonNull(secondOffer).equals("لا يوجد")){
                            binding.displayOffersFragmentSecondCard.setVisibility(View.GONE);
                        }else {
                            binding.displayOffersFragmentSecondOfferTextView.setText(secondOffer);

                        }
                        if (Objects.requireNonNull(thirdOffer).equals("لا يوجد")){
                            binding.displayOffersFragmentThirdCard.setVisibility(View.GONE);
                        }else {
                            binding.displayOffersFragmentThirdOfferTextView.setText(thirdOffer);

                        }
                        if (Objects.requireNonNull(fourthOffer).equals("لا يوجد")){
                            binding.displayOffersFragmentFourthCard.setVisibility(View.GONE);
                        }else {
                            binding.displayOffersFragmentFourthOfferTextView.setText(fourthOffer);

                        }
                        if (Objects.requireNonNull(fifthOffer).equals("لا يوجد")){
                            binding.displayOffersFragmentFifthCard.setVisibility(View.GONE);
                        }else {
                            binding.displayOffersFragmentFifthOfferTextView.setText(fifthOffer);

                        }
                        if (Objects.requireNonNull(sixthOffer).equals("لا يوجد")){
                            binding.displayOffersFragmentSixthCard.setVisibility(View.GONE);
                        }else {
                            binding.displayOffersFragmentSixthOfferTextView.setText(sixthOffer);

                        }
                       
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    public void onResume() {

        super.onResume();
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener((v, keyCode, event) -> {

            if( keyCode == KeyEvent.KEYCODE_BACK )
            {

                assert getFragmentManager() != null;
                getFragmentManager().popBackStackImmediate();

                return true;
            }

            return false;
        });
    }
}