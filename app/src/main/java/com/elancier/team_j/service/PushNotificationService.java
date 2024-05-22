package com.elancier.team_j.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;

import androidx.core.app.NotificationManagerCompat;

import com.elancier.team_j.MainActivity;
import com.elancier.team_j.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class PushNotificationService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        String title = remoteMessage.getNotification().getTitle();
        String text = remoteMessage.getNotification().getBody();

        final String CHANNEL_ID = "HEADS_UP_NOTIFICATION";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Heads Up Notification",
                    NotificationManager.IMPORTANCE_HIGH
            );
        }

        Notification.Builder notification =
                null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("from","Employee");
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
            notification = new Notification.Builder(this, CHANNEL_ID)
                    .setContentTitle(title)
                    .setContentText(text)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setChannelId(CHANNEL_ID)
                    .setAutoCancel(true);
        }

        NotificationManagerCompat.from(this).notify(1, notification.build());

        super.onMessageReceived(remoteMessage);
    }
}