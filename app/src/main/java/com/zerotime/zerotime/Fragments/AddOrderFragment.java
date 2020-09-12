package com.zerotime.zerotime.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.zerotime.zerotime.R;
import com.zerotime.zerotime.databinding.FragmentAddOrderBinding;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;


public class AddOrderFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private boolean doubleBackToExitPressedOnce = false;
    FragmentAddOrderBinding binding;
    DatabaseReference ordersRef;

    String userPhone;
    HashMap<String,String> ordersMap = new HashMap<>();
    private static final String[] sizes = {"صغير", "متوسط", "كبير"};
    Context context;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentAddOrderBinding.inflate(inflater,container,false);
        View view =  binding.getRoot();
        // Getting User Phone
        SharedPreferences prefs = Objects.requireNonNull(getContext()).getSharedPreferences("UserState", MODE_PRIVATE);
        userPhone = prefs.getString("isLogged", "No phone defined");//"No name defined" is the default value.

        ordersRef = FirebaseDatabase.getInstance().getReference("PendingOrders");
        //Sizes Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, sizes);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.addOrderOrderSizeSpinner.setAdapter(adapter);
        binding.addOrderOrderSizeSpinner.setOnItemSelectedListener(this);

        binding.addOrderRequestBtn.setOnClickListener(view1 -> {
            checkData();
        });
        return view;
    }
    private void checkData(){
        //Order Description Validation
        if (TextUtils.isEmpty(binding.addOrderOrderDescriptionEditText.getText())){
            binding.addOrderOrderDescriptionEditText.setError("من فضلك ادخل وصف الطلب !");
            binding.addOrderOrderDescriptionEditText.requestFocus();
            return;
        }
        //Receiver Name Validation
        if (TextUtils.isEmpty(binding.addOrderReceiverNameEditText.getText())){
            binding.addOrderReceiverNameEditText.setError("من فضلك ادخل اسم المستلم");
            binding.addOrderReceiverNameEditText.requestFocus();
            return;
        }
        //Primary Phone Validation
        if (TextUtils.isEmpty(binding.addOrderReceiverPrimaryPhoneEditText.getText())){
            binding.addOrderReceiverPrimaryPhoneEditText.setError("ادخل رقم الهاتف الاول للمستلم من فضلك !");
            binding.addOrderReceiverPrimaryPhoneEditText.requestFocus();
            return;
        }
        if (Objects.requireNonNull(binding.addOrderReceiverPrimaryPhoneEditText.getText()).length() != 11){
            binding.addOrderReceiverPrimaryPhoneEditText.setError("رقم الهاتف يجب ان يتكون من 11 رقم فقط !");
            binding.addOrderReceiverPrimaryPhoneEditText.requestFocus();
            return;
        }
        if (!binding.addOrderReceiverPrimaryPhoneEditText.getText().toString().startsWith("01")){
            binding.addOrderReceiverPrimaryPhoneEditText.setError("رقم الهاتف يجب ان يبدأ بـ 01 !");
            binding.addOrderReceiverPrimaryPhoneEditText.requestFocus();
            return;
        }
        //Secondary Phone Validation
        if (TextUtils.isEmpty(binding.addOrderReceiverSecondaryPhoneEditText.getText())){
            binding.addOrderReceiverSecondaryPhoneEditText.setError("ادخل رقم الهاتف الثانى للمستلم من فضلك !");
            binding.addOrderReceiverSecondaryPhoneEditText.requestFocus();
            return;
        }
        if (Objects.requireNonNull(binding.addOrderReceiverSecondaryPhoneEditText.getText()).length() != 11){
            binding.addOrderReceiverSecondaryPhoneEditText.setError("رقم الهاتف يجب ان يتكون من 11 رقم فقط !");
            binding.addOrderReceiverSecondaryPhoneEditText.requestFocus();
            return;
        }
        if (!binding.addOrderReceiverSecondaryPhoneEditText.getText().toString().startsWith("01")){
            binding.addOrderReceiverSecondaryPhoneEditText.setError("رقم الهاتف يجب ان يبدأ بـ 01 !");
            binding.addOrderReceiverSecondaryPhoneEditText.requestFocus();
            return;
        }
        String primaryPhone = binding.addOrderReceiverPrimaryPhoneEditText.getText().toString();
        String secondaryPhone = binding.addOrderReceiverSecondaryPhoneEditText.getText().toString();
        if (primaryPhone.equals(secondaryPhone)){
            Toast.makeText(getContext(),"من فضلك ادخل رقمين مختلفين !",Toast.LENGTH_SHORT).show();
            return;
        }
        //User Address Validation
        if (TextUtils.isEmpty(binding.addOrderReceiverAddressEditText.getText())){
            binding.addOrderReceiverAddressEditText.setError("ادخل عنوان المستلم بالتفصيل من فضلك !");
            binding.addOrderReceiverAddressEditText.requestFocus();
            return;
        }
        //Order Price Validation
        if (TextUtils.isEmpty(binding.addOrderOrderPriceEditText.getText())){
            binding.addOrderOrderPriceEditText.setError("من فضلك ادخل مبلغ الطلب !");
            binding.addOrderOrderPriceEditText.requestFocus();
            return;
        }
        requestOrder();
    }
    private void requestOrder(){
        @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm:ss");
        String currentTime = df.format(Calendar.getInstance().getTime());

        ordersMap.put("OrderDescription", Objects.requireNonNull(binding.addOrderOrderDescriptionEditText.getText()).toString());
        ordersMap.put("ReceiverName", Objects.requireNonNull(binding.addOrderReceiverNameEditText.getText()).toString());
        ordersMap.put("ReceiverPrimaryPhone", Objects.requireNonNull(binding.addOrderReceiverPrimaryPhoneEditText.getText()).toString());
        ordersMap.put("ReceiverSecondaryPhone", Objects.requireNonNull(binding.addOrderReceiverSecondaryPhoneEditText.getText()).toString());
        ordersMap.put("ReceiverAddress", Objects.requireNonNull(binding.addOrderReceiverAddressEditText.getText()).toString());
        ordersMap.put("OrderPrice", Objects.requireNonNull(binding.addOrderOrderPriceEditText.getText()).toString());
        ordersMap.put("OrderDate",currentTime);
        ordersMap.put("OrderState","لم يتم الاستلام");
        ordersMap.put("UserPrimaryPhone",userPhone);

        if (binding.addOrderArrivalDateNotesEditText.getText() == null || TextUtils.isEmpty(binding.addOrderArrivalDateNotesEditText.getText()) ){
            ordersMap.put("ArrivalNotes","لا توجد");

        }else {
            ordersMap.put("ArrivalNotes",Objects.requireNonNull(binding.addOrderArrivalDateNotesEditText.getText()).toString());
        }
        ordersRef.child(currentTime.toString()).setValue(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getContext(),"تم ارسال الطلب بنجاح",Toast.LENGTH_SHORT).show();
                    clearTools();
                }
            }
        });

    }
    private void clearTools(){
        Objects.requireNonNull(binding.addOrderOrderDescriptionEditText.getText()).clear();
        Objects.requireNonNull(binding.addOrderReceiverNameEditText.getText()).clear();
        Objects.requireNonNull(binding.addOrderReceiverPrimaryPhoneEditText.getText()).clear();
        Objects.requireNonNull(binding.addOrderReceiverSecondaryPhoneEditText.getText()).clear();
        Objects.requireNonNull(binding.addOrderReceiverAddressEditText.getText()).clear();
        Objects.requireNonNull(binding.addOrderOrderPriceEditText.getText()).clear();
        if (!TextUtils.isEmpty(binding.addOrderArrivalDateNotesEditText.getText())){
            Objects.requireNonNull(binding.addOrderArrivalDateNotesEditText.getText()).clear();
        }
        binding.addOrderOrderSizeSpinner.setSelection(0);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        switch (position) {
            case 0:
                ordersMap.put("OrderSize","صغير");
                // Whatever you want to happen when the first item gets selected
                break;
            case 1:
                ordersMap.put("OrderSize","متوسط");
                // Whatever you want to happen when the second item gets selected
                break;
            case 2:
                ordersMap.put("OrderSize","كبير");
                // Whatever you want to happen when the thrid item gets selected
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    @Override
    public void onResume() {

        super.onResume();

        Objects.requireNonNull(getView()).setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener((v, keyCode, event) -> {

            if( keyCode == KeyEvent.KEYCODE_BACK )
            {
                Fragment newFragment = new HomeFragment();
                assert getFragmentManager() != null;
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                transaction.replace(R.id.Frame_Content, newFragment);
                transaction.addToBackStack(null);

                transaction.commit();
                return true;
            }

            return false;
        });
    }
}