package com.hust.bloddpressure.model;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.hust.bloddpressure.R;

import java.util.Date;

public class DemoService extends Service {
    Handler mHandler = new Handler();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("LogService", "run: " + new Date().toString());
                mHandler.postDelayed(this, 2000);

            }
        }, 2000);

        // the NotificationChannel class is new and not in the support library
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "chanel1")
                .setSmallIcon(R.drawable.btn_custom)
                .setContentTitle("My notification")
                .setContentText("Much longer text that cannot fit one line...")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Much longer text that cannot fit one line..."))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

// notificationId is a unique int for each notification that you must define

        notificationManager.notify(001, mBuilder.build());

        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
