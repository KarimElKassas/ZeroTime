package com.zerotime.zerotime.Moderator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import android.os.Bundle;
import android.view.View;

import com.zerotime.zerotime.Moderator.Adapters.ComplaintAdapter;
import com.zerotime.zerotime.Moderator.Pojos.Complaint_Pojo;
import com.zerotime.zerotime.Room.Data.UserDao;
import com.zerotime.zerotime.Room.Model.Complaint;
import com.zerotime.zerotime.Room.UserDataBase;
import com.zerotime.zerotime.databinding.ActivityModeratorComplaintsBinding;

import java.util.ArrayList;
import java.util.List;

public class ModeratorComplaints extends AppCompatActivity {
    private ActivityModeratorComplaintsBinding binding;

    // Room DB
    UserDao db;
    int ctr = 0;
    UserDataBase dataBase;
    ComplaintAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityModeratorComplaintsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        adapter = new ComplaintAdapter();
        binding.recyclerComplaints.setAdapter(adapter);


        //Room DB
        dataBase = Room.databaseBuilder(this, UserDataBase.class, "Complaint")
                .allowMainThreadQueries().build();
        db = dataBase.getUserDao();

        binding.recyclerComplaints.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        binding.recyclerComplaints.setLayoutManager(mLayoutManager);

        db.getComplaints().subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<Complaint>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(List<Complaint> complaints) {
                        adapter.setList(complaints);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });




        binding.deleteAllComplaints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.nukeTable();
                notifyAll();
            }
        });

    }

}

