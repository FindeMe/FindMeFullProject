package com.nyx.fineme.helper;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.nyx.fineme.MyDevices;
import com.nyx.fineme.R;
import com.nyx.fineme.SplashScreen;


public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FBE3LANI";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());
        Intent resultIntent = new Intent(getApplicationContext(), MyDevices.class);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_ONE_SHOT);
        String channelId = "testChannelId";
        NotificationCompat.Builder builder = new  NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody()).setAutoCancel(true).setContentIntent(pendingIntent);;
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }
        manager.notify(0, builder.build());



    }

    private void sendRegistrationToServer(final String token) {
        // sending gcm token to server
        Log.e(TAG, "sendRegistrationToServer: " + token);
              if(SharedPrefManager.getInstance(this).isLoggedIn()) {
                  FirebaseMessaging.getInstance().subscribeToTopic("user_" +
                          SharedPrefManager.getInstance(this).getUserID());
                  Log.d("FBE3LANI", "firebase subscribed to  topic");
              }
    }

    private void storeRegIdInPref(String token) {
        Log.d("FBE3LANI","firebase token generated");
        SharedPrefManager.getInstance(this).saveData(token ,SharedPrefManager.KEY_FB);



    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        String refreshedToken = s;
        // Saving reg id to shared preferences
        storeRegIdInPref(refreshedToken);
        // sending reg id to your server
        sendRegistrationToServer(refreshedToken);
        // Notify UI that registration has completed, so the progress indicator can be hidden.

    }

}