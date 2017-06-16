package com.smartHome.SmartHomeApp.notifications;

/**
 * Created by Lukas on 16.12.2016.
 */

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.smartHome.SmartHomeApp.R;
import com.smartHome.SmartHomeApp.gui.MainActivity;
import com.smartHome.SmartHomeApp.kaa.KaaManager;


public class SmokeDetectorNotifications {

    /**
     * Coffee cooking finished
     * Notification Message
     */

    public void smokeDetectedNotification() {

        final Activity activity = KaaManager.getActivity();
        PendingIntent resultPendingIntent = PendingIntent.getActivity(activity, 0, new Intent(activity, MainActivity.class), 0);
        NotificationManager manager = null;
        Notification builder = new NotificationCompat.Builder(KaaManager.getActivity())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText(activity.getString(R.string.dialogSmokeAlarm))
                .setContentTitle(activity.getString(R.string.dialogWarning))
                .setContentIntent(resultPendingIntent)
                .setTicker(activity.getString(R.string.dialogNewMessage))
                .setDefaults(NotificationCompat.DEFAULT_SOUND)
                .setLights(0xffff00ff,1000,0)
//              .setFullScreenIntent()
                .build();


        manager = (NotificationManager) KaaManager.getActivity()
                .getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(5, builder);

        //CoffeeActivity.getInstance().updateWaterLvlDisplay();
       // MainActivity.getInstance().setSmokeButton(true);
    }



    /**
     * show dialog of the actual coffee
     */
    public void backToNormalNotification() {

        final Activity activity = KaaManager.getActivity();
        PendingIntent resultPendingIntent = PendingIntent.getActivity(activity, 0, new Intent(activity, MainActivity.class), 0);
        NotificationManager manager = null;

        NotificationCompat.Builder builder = new NotificationCompat.Builder(KaaManager.getActivity());
        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentText("Rauchmelder wurde zurückgesetzt")//R.string.dialogCoffeeCookingM1)+ )//activity.getString(R.string.dialogCoffeeCookingM2))
                .setContentTitle(activity.getString(R.string.dialogWarning))
                .setContentIntent(resultPendingIntent)
                .setTicker(activity.getString(R.string.dialogNewMessage))
                .setDefaults(NotificationCompat.DEFAULT_SOUND)
                .setAutoCancel(true)
                .build();
        manager = (NotificationManager) KaaManager.getActivity()
                .getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(6, builder.build());

        //to receive the alarm and start ringtone
       // AlarmReceiver alarmReceiver = new AlarmReceiver();

        //MainActivity.getInstance().setSmokeButton(false);

    }


}