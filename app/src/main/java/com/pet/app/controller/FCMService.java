package com.pet.app.controller;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.api.Api;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.pet.app.MainActivity;
import com.pet.app.R;
import com.pet.app.resources.Apis;
import com.pet.app.resources.UserSession;

import java.util.HashMap;
import java.util.Map;

/**
 * This is the notification listener class
 * FCMService is triggered whenever a notification or a payload is send to
 * fcm token of your android device
 * */
public class FCMService extends FirebaseMessagingService {
    public static final String CHANNEL_1_ID = "channel1";
    public static final String CHANNEL_2_ID = "channel2";
    private static final String TAG = FCMService.class.getName();

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        String token = UserSession.getSession(this).getToken();
        //You can notify all app users from FCM console by sending notification to all channel
        FirebaseMessaging.getInstance().subscribeToTopic("all");
        if (token != null) {
            StringRequest request = new StringRequest(Request.Method.POST, Apis.fcmUpdate,
                    response -> {
                        Log.d(TAG, "onNewToken: " + response);
                    },
                    error -> {
                        Log.d(TAG, "onNewToken: " + error.getLocalizedMessage());
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> m = new HashMap<>();
                    m.put(Apis.kAuth, "Bearer " + token);
                    return m;
                }

                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<>();
                    map.put("fcm", s);
                    return map;
                }

            };
            Reque.getInstance(this).addToRequestQueue(request);
        }
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        createNotificationChannels();
        //Data payload notification for background
        Map<String, String> data = remoteMessage.getData();
        notification(data.get("title"), data.get("body"));

        /*
         * If notification is send this works only when app is foreground
         * */
        if(remoteMessage.getNotification()!=null)
        notification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
    }

    void notification(String title, String data) {
        NotificationManagerCompat notificationManager1 = NotificationManagerCompat.from(this);
        Intent activityIntent = new Intent(this, MainActivity.class);
        @SuppressLint("UnspecifiedImmutableFlag") PendingIntent contentIntent = PendingIntent.getActivity(this,
                0, activityIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_baseline_pets_24)
                .setColor(Color.BLUE)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentText(data)
                .setOnlyAlertOnce(true)
                .build();

        notificationManager1.notify((int) Math.random(), notification);


    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(
                    CHANNEL_1_ID,
                    "Channel 1",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel1.setDescription("Notification");


            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
        }
    }
}
