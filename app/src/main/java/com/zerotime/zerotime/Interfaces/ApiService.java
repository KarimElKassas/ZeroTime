package com.zerotime.zerotime.Interfaces;

import com.zerotime.zerotime.Notifications.MyResponse;
import com.zerotime.zerotime.Notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiService {
    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAxnCjVis:APA91bEXUg2khTEgatZdzhtIlftOEwowkLdhv5Fqfj7nXN-BhohNZxFSC0cij8nA2A-O_VW6m9jmyMVlff90Jn_SUVT6ObMHqoHhmqCsT9c7hkk3efEF5LIu5_tTSPz19WE-QPXcORl3"
    })
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
