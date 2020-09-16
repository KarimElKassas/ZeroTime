package com.zerotime.zerotime.Fragments;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.room.Query;
import androidx.room.Room;
import androidx.room.migration.Migration;
import io.reactivex.CompletableObserver;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import android.text.TextUtils;
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
import com.zerotime.zerotime.Moderator.Pojos.Complaint_Pojo;
import com.zerotime.zerotime.R;
import com.zerotime.zerotime.Room.Data.UserDao;
import com.zerotime.zerotime.Room.Model.Complaint;
import com.zerotime.zerotime.Room.UserDataBase;
import com.zerotime.zerotime.databinding.FragmentComplaintsBinding;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;
import static com.zerotime.zerotime.Room.Data.UserDao.MIGRATION_1_2;


public class ComplaintsFragment extends Fragment {

    private FragmentComplaintsBinding binding;
    private DatabaseReference complaintsRef;
    private HashMap<String, String> complaintsMap = new HashMap<>();

    // Room DB
    UserDao userDao;

    String name;
    SharedPreferences preferences;

    String userPhone, userComplaint;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentComplaintsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        //Room DB
        userDao = Room.databaseBuilder(getContext(), UserDataBase.class, "Complaint").allowMainThreadQueries().addMigrations(MIGRATION_1_2)
                .build().getUserDao();


        preferences = Objects.requireNonNull(getContext()).getSharedPreferences("UserState", MODE_PRIVATE);
        complaintsRef = FirebaseDatabase.getInstance().getReference("Users");
        userPhone = preferences.getString("isLogged", "");

        binding.complaintsFragmentSendComplaintBtn.setOnClickListener(view1 -> {


            if (TextUtils.isEmpty(binding.complaintsFragmentComplaintEditText.getText())) {
                binding.complaintsFragmentComplaintEditText.setError("من فضلك قم بادخال شكوتك");
                binding.complaintsFragmentComplaintEditText.requestFocus();

            } else {

                complaintsRef.child(userPhone).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        name = snapshot.child("UserName").getValue(String.class);

                        @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm:ss");
                        String currentTime = df.format(Calendar.getInstance().getTime());


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
                        Toast.makeText(getContext(),
                                "تم ارسال الشكوى بنجاح سوف نرد عليك قريباً",
                                Toast.LENGTH_LONG).show();
                        binding.complaintsFragmentComplaintEditText.setText("");



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });

        return view;
    }
}