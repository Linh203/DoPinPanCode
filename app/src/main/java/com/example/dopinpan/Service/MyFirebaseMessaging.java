package com.example.dopinpan.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.dopinpan.Common.Common;
import com.example.dopinpan.Helper.NotificationHelper;
import com.example.dopinpan.Home2Activity;
import com.example.dopinpan.R;
import com.example.dopinpan.ui.gallery.GalleryFragment;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

public class MyFirebaseMessaging extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
            sendNotificationAPI26(message);
        else
            sendNotification(message);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sendNotificationAPI26(RemoteMessage message) {
        RemoteMessage.Notification notification=message.getNotification();
        String title=notification.getTitle();
        String content= notification.getBody();

        Intent intent=new Intent(this, GalleryFragment.class);
        intent.putExtra(Common.PHONE_TEXT, Common.currentUser.getPhone());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationHelper helper=new NotificationHelper(this);
        Notification.Builder builder=helper.getDoPinPanChannelNotification(title,content,pendingIntent,defaultSoundUri);

        helper.getManager().notify(new Random().nextInt(),builder.build());



    }

    private void sendNotification(RemoteMessage message) {

        RemoteMessage.Notification notification=message.getNotification();
        Intent intent=new Intent(this, Home2Activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.logo)
                .setContentText(notification.getBody())
                .setContentTitle(notification.getTitle())
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager noti= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        noti.notify(0,builder.build());

    }
}
