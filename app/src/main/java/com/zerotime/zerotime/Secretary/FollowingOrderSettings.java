package com.zerotime.zerotime.Secretary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zerotime.zerotime.databinding.SecretaryActivityFollowingOrderSettingsBinding;
import com.zerotime.zerotime.myBroadCast;

import java.util.ArrayList;
import java.util.HashMap;

public class FollowingOrderSettings extends AppCompatActivity {
    private SecretaryActivityFollowingOrderSettingsBinding binding;

    private HashMap<String, String> ordersMap;
    String userName;
    private DatabaseReference orderRef, clerkRef, deliveredOrdersCountRef, deliveredOrdersRef, usersRef;
    ArrayList<String> clerksList;
    HashMap<String, Object> orderCountMap;
    String currentOrderState, currentOrderUnique, currentOrderNewState, clerkPhone, userPrimaryPhone;
    long deliveredOrdersCount;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(FollowingOrderSettings.this, FollowingTheOrderState.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SecretaryActivityFollowingOrderSettingsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        checkInternetConnection();
        deliveredOrdersCount = 0;

        ordersMap = new HashMap<>();
        orderCountMap = new HashMap<>();
        clerksList = new ArrayList<>();

        currentOrderUnique = getIntent().getStringExtra("OrderDate");
        usersRef = FirebaseDatabase.getInstance().getReference("Users");
        deliveredOrdersCountRef = FirebaseDatabase.getInstance().getReference("OrdersCount");
        deliveredOrdersRef = FirebaseDatabase.getInstance().getReference("DeliveredOrders");
        orderRef = FirebaseDatabase.getInstance().getReference("PendingOrders");
        orderRef.child(currentOrderUnique).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    if (dataSnapshot.hasChildren()) {
                        currentOrderState = dataSnapshot.child("OrderState").getValue(String.class);
                        String orderNotes = dataSnapshot.child("ArrivalNotes").getValue(String.class);
                        String orderDescription = dataSnapshot.child("OrderDescription").getValue(String.class);
                        String orderDate = dataSnapshot.child("OrderDate").getValue(String.class);
                        String orderPrice = dataSnapshot.child("OrderPrice").getValue(String.class);
                        String orderSize = dataSnapshot.child("OrderSize").getValue(String.class);
                        String userPhone = dataSnapshot.child("UserPrimaryPhone").getValue(String.class);
                        String receiverName = dataSnapshot.child("ReceiverName").getValue(String.class);
                        String receiverAddress = dataSnapshot.child("ReceiverAddress").getValue(String.class);
                        String receiverPrimaryPhone = dataSnapshot.child("ReceiverPrimaryPhone").getValue(String.class);
                        String receiverSecondaryPhone = dataSnapshot.child("ReceiverSecondaryPhone").getValue(String.class);

                        userPrimaryPhone = userPhone;

                        //Getting Delivered Orders Count
                        deliveredOrdersRef.orderByChild("UserPrimaryPhone").equalTo(userPrimaryPhone).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    if (snapshot.hasChildren()) {

                                        deliveredOrdersCount = snapshot.getChildrenCount();

                                    } else {
                                        deliveredOrdersCount = 0;
                                    }
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        ordersMap.put("ArrivalNotes", orderNotes);
                        ordersMap.put("OrderDescription", orderDescription);
                        ordersMap.put("OrderDate", orderDate);
                        ordersMap.put("OrderPrice", orderPrice);
                        ordersMap.put("OrderSize", orderSize);
                        ordersMap.put("UserPrimaryPhone", userPhone);
                        ordersMap.put("ReceiverName", receiverName);
                        ordersMap.put("ReceiverAddress", receiverAddress);
                        ordersMap.put("ReceiverPrimaryPhone", receiverPrimaryPhone);
                        ordersMap.put("ReceiverSecondaryPhone", receiverSecondaryPhone);

                        if (currentOrderState != null) {
                            switch (currentOrderState) {
                                case "لم يتم الاستلام":
                                    binding.FollowingOrderSettingSelectClerk.setEnabled(false);
                                    String[] states = {"تم الإستلام", "جارى التوصيل", "تم التوصيل"};
                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(FollowingOrderSettings.this,
                                            android.R.layout.simple_spinner_item, states);
                                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    binding.FollowingOrderSettingSelectState.setAdapter(adapter);
                                    binding.FollowingOrderSettingSelectState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                                            switch (position) {
                                                case 0:
                                                    currentOrderNewState = "تم الاستلام";
                                                    break;
                                                case 1:
                                                    currentOrderNewState = "جارى التوصيل";
                                                    break;
                                                case 2:
                                                    currentOrderNewState = "تم التوصيل";
                                                    break;

                                            }
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> adapterView) {

                                        }
                                    });
                                    break;
                                case "تم الاستلام":
                                    binding.FollowingOrderSettingSelectClerk.setEnabled(true);
                                    String[] states2 = {"جارى التوصيل", "تم التوصيل"};
                                    ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(FollowingOrderSettings.this,
                                            android.R.layout.simple_spinner_item, states2);
                                    adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    binding.FollowingOrderSettingSelectState.setAdapter(adapter2);
                                    binding.FollowingOrderSettingSelectState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                                            switch (position) {
                                                case 0:
                                                    currentOrderNewState = "جارى التوصيل";
                                                    break;
                                                case 1:
                                                    currentOrderNewState = "تم التوصيل";
                                                    break;

                                            }
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> adapterView) {

                                        }
                                    });
                                    break;
                                case "جارى التوصيل":
                                    String[] states3 = {"تم التوصيل"};
                                    ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(FollowingOrderSettings.this,
                                            android.R.layout.simple_spinner_item, states3);
                                    adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    binding.FollowingOrderSettingSelectState.setAdapter(adapter3);
                                    binding.FollowingOrderSettingSelectState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                                            if (position == 0) {
                                                binding.FollowingOrderSettingSelectClerk.setEnabled(true);

                                                currentOrderNewState = "تم التوصيل";
                                            }
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> adapterView) {

                                        }
                                    });
                                    break;
                                case "تم التوصيل":

                                    break;

                            }

                        }
                        Toast.makeText(FollowingOrderSettings.this, currentOrderState, Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(getApplicationContext(), "No Children Found", Toast.LENGTH_SHORT).show();

                } else
                    Toast.makeText(getApplicationContext(), "Snapshot not Found", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        clerkRef = FirebaseDatabase.getInstance().getReference("Clerks");
        clerkRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String clerkName = dataSnapshot.child("ClerkName").getValue(String.class);
                    clerkPhone = dataSnapshot.child("ClerkPhone1").getValue(String.class);
                    if (clerkName != null) {
                        clerksList.add(clerkName);
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(FollowingOrderSettings.this,
                                android.R.layout.simple_spinner_item, clerksList);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        binding.FollowingOrderSettingSelectClerk.setAdapter(adapter);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        binding.FollowingOrderSettingsUpdateBtn.setOnClickListener(view1 -> {
            if (currentOrderNewState != null && clerkPhone != null) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setTitle("تأكيد ...");
                builder1.setMessage("هل تريد تعديل بيانات هذا الطلب ؟");
                builder1.setCancelable(false);

                builder1.setPositiveButton(
                        "نعم",
                        (dialog, id) -> {
                            if (currentOrderNewState.equals("تم الاستلام")) {
                                orderRef.child(currentOrderUnique).child("OrderState").setValue(currentOrderNewState)
                                        .addOnCompleteListener(task -> {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getApplicationContext(), "تم تحديث حالة الاوردر", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(FollowingOrderSettings.this, FollowingTheOrderState.class);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                Toast.makeText(getApplicationContext(), "فشل تحديث الاوردر", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            } else if (currentOrderNewState.equals("جارى التوصيل")) {
                                ordersMap.put("OrderState", currentOrderNewState);

                                orderRef.child(currentOrderUnique).setValue(ordersMap).addOnSuccessListener(aVoid -> {
                                    Intent intent = new Intent(FollowingOrderSettings.this, FollowingTheOrderState.class);
                                    startActivity(intent);
                                    finish();
                                });

                            } else if (currentOrderNewState.equals("تم التوصيل")) {
                                ordersMap.put("ClerkName", binding.FollowingOrderSettingSelectClerk.getSelectedItem().toString());
                                clerkRef.orderByChild("ClerkName")
                                        .equalTo(binding.FollowingOrderSettingSelectClerk.getSelectedItem().toString())
                                        .addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (snapshot.exists()) {
                                                    if (snapshot.hasChildren()) {
                                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                                            String clerkPhone = dataSnapshot.child("ClerkPhone1").getValue(String.class);
                                                            ordersMap.put("ClerkPhone1", clerkPhone);
                                                        }


                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });

                                orderRef.child(currentOrderUnique).removeValue().addOnSuccessListener(aVoid ->
                                        deliveredOrdersRef.child(currentOrderUnique).setValue(ordersMap).addOnCompleteListener(task -> {
                                            if (task.isSuccessful()) {


                                                usersRef.orderByChild("UserPrimaryPhone").equalTo(userPrimaryPhone).addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        if (snapshot.exists()) {
                                                            if (snapshot.hasChildren()) {
                                                                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                                                                    userName = dataSnapshot.child("UserName").getValue(String.class);
                                                                    orderCountMap.put("UserName", userName);
                                                                    Toast.makeText(FollowingOrderSettings.this, ""+userName, Toast.LENGTH_SHORT).show();
                                                                    orderCountMap.put("OrdersCount", deliveredOrdersCount);
                                                                    deliveredOrdersCountRef.child(userPrimaryPhone).setValue(orderCountMap);
                                                                    Intent intent = new Intent(FollowingOrderSettings.this, FollowingTheOrderState.class);
                                                                    startActivity(intent);
                                                                    finish();
                                                                    Toast.makeText(getApplicationContext(), "Delivered Order Ref Done", Toast.LENGTH_SHORT).show();

                                                                }

                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });



                                            } else
                                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }));
                            }

                        });

                builder1.setNegativeButton(
                        "لا",
                        (dialog, id) -> dialog.cancel());

                AlertDialog alert11 = builder1.create();
                alert11.show();


            } else {
                Toast.makeText(this, "new state null", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void checkInternetConnection(){
        myBroadCast broadCast=new myBroadCast();
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(broadCast,intentFilter);

    }
}
