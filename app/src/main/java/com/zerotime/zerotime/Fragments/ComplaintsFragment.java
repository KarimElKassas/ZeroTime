package com.zerotime.zerotime.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import es.dmoral.toasty.Toasty;
import io.reactivex.CompletableObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zerotime.zerotime.Room.Data.UserDao;
import com.zerotime.zerotime.Room.Model.Complaint;
import com.zerotime.zerotime.Room.UserDataBase;
import com.zerotime.zerotime.databinding.UserFragmentComplaintsBinding;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;
import static com.zerotime.zerotime.Room.Data.UserDao.MIGRATION_1_2;


public class ComplaintsFragment extends Fragment {

    private UserFragmentComplaintsBinding binding;
    private DatabaseReference usersRef,complaintsRef;
    HashMap<String,String> complaintMap;
    Context context;
    View view;
    // Room DB
    UserDao userDao;

    String name;
    SharedPreferences preferences;

    String userPhone, userComplaint;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = UserFragmentComplaintsBinding.inflate(getLayoutInflater());
        view = binding.getRoot();
        context = container.getContext();

        //Room DB
        userDao = Room.databaseBuilder(context, UserDataBase.class, "Complaint").allowMainThreadQueries().addMigrations(MIGRATION_1_2)
                .build().getUserDao();


        preferences = context.getSharedPreferences("UserState", MODE_PRIVATE);
        userPhone = preferences.getString("isLogged", "");

        usersRef = FirebaseDatabase.getInstance().getReference("Users");
        complaintsRef = FirebaseDatabase.getInstance().getReference("Complaints");
        complaintMap = new HashMap<>();


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding.complaintsFragmentSendComplaintBtn.setOnClickListener(view1 -> {

            if (TextUtils.isEmpty(binding.complaintsFragmentComplaintEditText.getText())) {
                binding.complaintsFragmentComplaintEditText.setError("من فضلك قم بادخال شكوتك");
                binding.complaintsFragmentComplaintEditText.requestFocus();

            } else {
                @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm:ss");
                String currentTime = df.format(Calendar.getInstance().getTime());

                usersRef.child(userPhone).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        name = snapshot.child("UserName").getValue(String.class);

                        complaintMap.put("UserName",name);
                        complaintMap.put("UserPhone",userPhone);
                        complaintMap.put("ComplaintDate",currentTime);
                        complaintMap.put("Complaint", Objects.requireNonNull(binding.complaintsFragmentComplaintEditText.getText()).toString());

                        complaintsRef.child(currentTime).setValue(complaintMap)
                                .addOnSuccessListener(aVoid -> {
                                    Toasty.success(context,
                                            "تم ارسال الشكوى بنجاح سوف نرد عليك قريباً",
                                            Toasty.LENGTH_LONG,
                                            true).show();
                                    binding.complaintsFragmentComplaintEditText.setText("");

                                }).addOnFailureListener(e -> Toasty.error(context,
                                        "لقد حدث خطأ ما برجاء المحاولة لاحقاً",
                                        Toasty.LENGTH_SHORT,
                                        true).show());

                        userComplaint = Objects.requireNonNull(binding.complaintsFragmentComplaintEditText.getText()).toString();
                        Complaint complaint = new Complaint(userPhone, userComplaint, currentTime, name);
                        userDao.insertComplaint(complaint).subscribeOn(Schedulers.computation())
                                .subscribe(new CompletableObserver() {
                                    @Override
                                    public void onSubscribe(Disposable d) {

                                    }

                                    @Override
                                    public void onComplete() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }
                                });



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
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