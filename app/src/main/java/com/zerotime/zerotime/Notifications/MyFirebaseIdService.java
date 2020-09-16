package com.zerotime.zerotime.Notifications;

import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;

import java.util.Objects;

public class MyFirebaseIdService extends FirebaseMessagingService {
    String userToken;

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        //get user token id
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                userToken = Objects.requireNonNull(task.getResult()).getToken();
                refreshToken(userToken);
            }
        });
    }

    private void refreshToken(String refreshToken) {
        SharedPreferences prefs = getSharedPreferences("UserState", MODE_PRIVATE);
        String userPhone = prefs.getString("isLogged", "");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token = new Token(refreshToken);
        reference.child(userPhone).setValue(token);
    }
}
