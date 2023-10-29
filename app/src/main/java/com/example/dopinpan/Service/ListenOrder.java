package com.example.dopinpan.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.dopinpan.Common.Common;
import com.example.dopinpan.Model.Request;
import com.example.dopinpan.R;
import com.example.dopinpan.ui.gallery.GalleryFragment;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ListenOrder extends Service implements ChildEventListener {

    private FirebaseDatabase database;
    private DatabaseReference requests;

    public ListenOrder() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        database=FirebaseDatabase.getInstance();
        requests= database.getReference("Requests");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        requests.addChildEventListener(this);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
        Request request=snapshot.getValue(Request.class);
        showNotification(snapshot.getKey(),request);
    }

    private void showNotification(String key, Request request) {
        Intent intent=new Intent(getBaseContext(), GalleryFragment.class);
        intent.putExtra("userPhone",request.getPhone());
        PendingIntent contentIntent=PendingIntent.getActivity(getBaseContext(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder=new NotificationCompat.Builder(getBaseContext());
        builder.setAutoCancel(true).setDefaults(Notification.DEFAULT_ALL).setWhen(System.currentTimeMillis()).setTicker("DoPinPan").setSmallIcon(R.drawable.logo).setContentText("Your Oder Was Updated").setContentText("Order #"+key+" was update status to "+ Common.convertCodeToStatus(request.getStatus())).setContentIntent(contentIntent);

        NotificationManager notificationManager=(NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1,builder.build());
    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
}
