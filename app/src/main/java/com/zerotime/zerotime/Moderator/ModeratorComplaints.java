package com.zerotime.zerotime.Moderator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.zerotime.zerotime.Moderator.Adapters.ComplaintAdapter;
import com.zerotime.zerotime.R;
import com.zerotime.zerotime.Room.Data.UserDao;
import com.zerotime.zerotime.Room.Model.Complaint;
import com.zerotime.zerotime.Room.UserDataBase;
import com.zerotime.zerotime.databinding.ModeratorActivityComplaintsBinding;
import com.zerotime.zerotime.myBroadCast;

import java.util.List;

public class ModeratorComplaints extends AppCompatActivity {
    private ModeratorActivityComplaintsBinding binding;

    // Room DB
    UserDao db;
    int ctr = 0;
    UserDataBase dataBase;
    ComplaintAdapter adapter;

    private void layoutAnimation(RecyclerView recyclerView) {

        Context context = recyclerView.getContext();
        LayoutAnimationController animationController =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_slide_right);
        recyclerView.setLayoutAnimation(animationController);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ModeratorActivityComplaintsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        adapter = new ComplaintAdapter();
        binding.recyclerComplaints.setAdapter(adapter);
        binding.recyclerComplaints.setItemAnimator(new DefaultItemAnimator());

        checkInternetConnection();
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

    private void checkInternetConnection() {
        myBroadCast broadCast = new myBroadCast();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(broadCast, intentFilter);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(ModeratorComplaints.this, ModeratorHome.class);
        startActivity(i);
        finish();
    }

}

