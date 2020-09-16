package com.zerotime.zerotime.Moderator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

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

public class Moderator_complaints extends AppCompatActivity {
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

        //Room DB
        dataBase = Room.databaseBuilder(this, UserDataBase.class, "Complaint")
                .allowMainThreadQueries().build();
        db = dataBase.getUserDao();

//        db.nukeTable();

        List<Complaint> complaint = db.getComplaints();


        binding.recyclerComplaints.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        binding.recyclerComplaints.setLayoutManager(mLayoutManager);

        ctr = db.getCount();

        for (int i = 0; i < ctr; i++) {

            if (complaint != null) {
                Complaint_Pojo complaint_pojo = new Complaint_Pojo();
                complaint_pojo.setName(complaint.get(i).getName());
                complaint_pojo.setComplaint(complaint.get(i).getUserComplaint());
                complaint_pojo.setPhone(complaint.get(i).getUserPhone());
                complaint_pojo.setDate(complaint.get(i).getDate());

                complaint_pojos.add(complaint_pojo);

            }


        }
        adapter = new ComplaintAdapter(complaint_pojos, Moderator_complaints.this);
        binding.recyclerComplaints.setAdapter(adapter);
        binding.deleteAllComplaints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.nukeTable();
                notifyAll();
            }
        });

    }

}

