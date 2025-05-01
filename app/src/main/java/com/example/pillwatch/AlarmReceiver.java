package com.example.pillwatch;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "alarmChannel")
                .setSmallIcon(R.drawable.ic_alarm)
                .setContentTitle("Pirula emlékeztető")
                .setContentText("Gyógyszerbeszedési idő eljött")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        // Get NotificationManager and display the notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(1001, builder.build());
    }
}