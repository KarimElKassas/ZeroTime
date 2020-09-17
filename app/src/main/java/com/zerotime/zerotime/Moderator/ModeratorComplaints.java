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
    ArrayList<Complaint_Pojo> complaint_pojos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityModeratorComplaintsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        complaint_pojos = new ArrayList<>();
        adapter = new ComplaintAdapter();
        binding.recyclerComplaints.setAdapter(adapter);


        //Room DB
        dataBase = Room.databaseBuilder(this, UserDataBase.class, "Complaint")
                .allowMainThreadQueries().build();
        db = dataBase.getUserDao();

        //Single<List<Complaint>> complaint = db.getComplaints();


        binding.recyclerComplaints.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        binding.recyclerComplaints.setLayoutManager(mLayoutManager);

        ctr = db.getCount();


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


       /* for (int i = 0; i < ctr; i++) {

            if (complaint != null) {
                Complaint_Pojo complaint_pojo = new Complaint_Pojo();
                complaint_pojo.setName(complaint.get(i).getName());
                complaint_pojo.setComplaint(complaint.get(i).getUserComplaint());
                complaint_pojo.setPhone(complaint.get(i).getUserPhone());
                complaint_pojo.setDate(complaint.get(i).getDate());

                complaint_pojos.add(complaint_pojo);

            }


        }*/


        binding.deleteAllComplaints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.nukeTable();
                notifyAll();
            }
        });

    }

}

