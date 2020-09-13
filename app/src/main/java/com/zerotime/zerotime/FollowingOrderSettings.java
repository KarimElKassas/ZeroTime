package com.zerotime.zerotime;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.zerotime.zerotime.databinding.ActivityFollowingOrderSettingsBinding;
import com.zerotime.zerotime.databinding.ActivitySignUpBinding;

import java.util.HashMap;

public class FollowingOrderSettings extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
      private static final String[] states = {"تم الإستلام", "جارى التوصيل", "تم التوصيل"};
      private ActivityFollowingOrderSettingsBinding binding;
    private HashMap<String,String> statesMap ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFollowingOrderSettingsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(FollowingOrderSettings.this,
                android.R.layout.simple_spinner_item,states);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.FollowinOrderSettingSelectState.setAdapter(adapter);
        binding.FollowinOrderSettingSelectState.setOnItemSelectedListener(this);







    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (i) {
            case 0:
                statesMap.put("UserRegion","القاهرة");
                // Whatever you want to happen when the first item gets selected
                break;
            case 1:
                statesMap.put("UserRegion","الاسكندرية");
                // Whatever you want to happen when the second item gets selected
                break;
            case 2:
                statesMap.put("UserRegion","الجيزة");
                // Whatever you want to happen when the thrid item gets selected
                break;

        }














    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}