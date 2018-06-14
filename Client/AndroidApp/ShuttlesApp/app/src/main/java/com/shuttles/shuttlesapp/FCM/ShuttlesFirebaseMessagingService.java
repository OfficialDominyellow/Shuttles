package com.shuttles.shuttlesapp.FCM;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;


import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.shuttles.shuttlesapp.LoginActivity;
import com.shuttles.shuttlesapp.NoticeActivity;
import com.shuttles.shuttlesapp.R;
import com.shuttles.shuttlesapp.Utils.Constants;

public class ShuttlesFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if(remoteMessage.getNotification() != null)
            sendNotification(remoteMessage.getNotification().getBody());
    }

    private void sendNotification(String messageBody) {
        //TODO:모든 메세지는 수신된지 10초 이내에 처리되어야 함
        Log.i(Constants.LOG_TAG,"noti messagebody : "+messageBody);
/*
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , intent,
                PendingIntent.FLAG_ONE_SHOT);
*/
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,"notify_001")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Shuttles Push Test")
                .setTicker("Notify")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri);
                //.setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("notify_001",
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 , notificationBuilder.build());
        Log.i(Constants.LOG_TAG,"notify");
    }

}
