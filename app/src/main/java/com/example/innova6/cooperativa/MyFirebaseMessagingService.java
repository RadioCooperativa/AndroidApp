package com.example.innova6.cooperativa;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by innova6 on 05-07-2016.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService{
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e("FIREBASE", remoteMessage.getNotification().getBody());
        Log.i("info", "From: " + remoteMessage.getFrom());
        Log.i("info", "Notification Message Body: " + remoteMessage.getNotification().getBody());
    }
}
