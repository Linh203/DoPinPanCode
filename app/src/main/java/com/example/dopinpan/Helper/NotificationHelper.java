package com.example.dopinpan.Helper;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.dopinpan.R;

public class NotificationHelper extends ContextWrapper {

    private static final String DoPinPan_ID = "com.example.dopinpan.DOPINPAN";
    private static final String DoPinPan_Name = "DoPinPan";

    private NotificationManager manager;

    public NotificationHelper(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChanel();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createChanel() {

        NotificationChannel channel = new NotificationChannel(DoPinPan_ID, DoPinPan_Name, NotificationManager.IMPORTANCE_DEFAULT);
        channel.enableLights(false);
        channel.enableVibration(true);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getManager().createNotificationChannel(channel);
    }

    public NotificationManager getManager() {
        if (manager == null) {
            manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return manager;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Notification.Builder getDoPinPanChannelNotification(String title, String body, PendingIntent contentIntent, Uri soundUri){
        return new Notification.Builder(getApplicationContext(),DoPinPan_ID)
                .setContentIntent(contentIntent)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.drawable.logo)
                .setSound(soundUri)
                .setAutoCancel(false);
    }
}
