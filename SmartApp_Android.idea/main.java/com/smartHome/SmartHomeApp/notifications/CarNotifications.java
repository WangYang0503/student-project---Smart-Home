package com.smartHome.SmartHomeApp.notifications;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.widget.Button;

import com.smartHome.SmartHomeApp.R;
import com.smartHome.SmartHomeApp.kaa.KaaManager;
import com.smartHome.SmartHomeApp.notifications.CarTrackingNotification.CarTrackingMessages;

import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * Created by Alext on 08.09.16.
 * <p>
 * All notifications, warnings and errors which needed to be shown in association with the car
 */
public class CarNotifications {

    /**
     * car begins to drive
     */

    public static void StartDrivingNotification() {

        final Activity activity = KaaManager.getActivity();
        if (activity == null) {
            return;
        }
        activity.runOnUiThread(new Runnable() {
            public void run() {
                new AlertDialog.Builder(activity)
                        .setTitle(activity.getString(R.string.dialogMessage))
                        .setMessage(activity.getString(R.string.dialogCarStarts))
                        .setPositiveButton(R.string.alert_dialog_ok,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                    }
                                })
                        .show();
            }
        });
    }

    /**
     * Mindstorm stops to drive
     */
    public static void StopDrivingNotification() {
        final Activity activity = KaaManager.getActivity();
        if (activity == null) {
            return;
        }
        activity.runOnUiThread(new Runnable() {
            public void run() {
                new AlertDialog.Builder(activity)
                        .setTitle(activity.getString(R.string.dialogMessage))
                        .setMessage(activity.getString(R.string.dialogCarStop))
                        .setPositiveButton(R.string.alert_dialog_ok,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                    }
                                })
                        .show();
            }
        });
    }

    /**
     * Mindstorm starts with Parking
     */
    public static void StartParkNotification() {
        final Activity activity = KaaManager.getActivity();
        if (activity == null) {
            return;
        }
        activity.runOnUiThread(new Runnable() {
            public void run() {
                new AlertDialog.Builder(activity)
                        .setTitle(activity.getString(R.string.dialogMessage))
                        .setMessage(activity.getString(R.string.dialogCarParking))
                        .setPositiveButton(R.string.alert_dialog_ok,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                    }
                                })
                        .show();
            }
        });
}
    public static void lostWayNotification() {
        final Activity activity = KaaManager.getActivity();
        if (activity == null) {
            return;
        }
        activity.runOnUiThread(new Runnable() {
            public void run() {
                new AlertDialog.Builder(activity)
                        .setTitle(activity.getString(R.string.dialogMessage))
                        .setMessage(("Das Auto ist von der spur abgekommen.Spur wird gesucht"))
                        .setPositiveButton(R.string.alert_dialog_ok,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                    }
                                })
                        .show();
            }
        });
    }
    /**
     * car is doging
     */

    public static void dogingNotification() {
        final Activity activity = KaaManager.getActivity();
        if (activity == null) {
            return;
        }
        activity.runOnUiThread(new Runnable() {
            public void run() {
                new AlertDialog.Builder(activity)
                        .setTitle(activity.getString(R.string.dialogMessage))
                        .setMessage(("Das Auto wendet"))
                        .setPositiveButton(R.string.alert_dialog_ok,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                    }
                                })
                        .show();
            }
        });
    }

    /**
     * car avoids walls and holes
     */
    public static void avoidedDeathNotification() {
        final Activity activity = KaaManager.getActivity();
        if (activity == null) {
            return;
        }
        activity.runOnUiThread(new Runnable() {
            public void run() {
                new AlertDialog.Builder(activity)
                        .setTitle(activity.getString(R.string.dialogMessage))
                        .setMessage(("Das Auto hat einen Unfall vermeidet"))
                        .setPositiveButton(R.string.alert_dialog_ok,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                    }
                                })
                        .show();
            }
        });
    }

    /**
     *  The following Methods are for the car tracking messages
     */
    public static SimpleDateFormat carStartedMessage(){
        Calendar date = Calendar.getInstance();
        SimpleDateFormat startedTime = new SimpleDateFormat("HH:mm");
        CarTrackingMessages.setStartedMessage( "Auto ist gestartet" +" "+ startedTime.format(date.getTime()));
        return startedTime;
    }

    public static SimpleDateFormat carToppedMessage(){
        Calendar date = Calendar.getInstance();
        SimpleDateFormat stoppedTime = new SimpleDateFormat("HH:mm");
        CarTrackingMessages.setStoppedMessage( "Auto hat gestoppt" +" "+ stoppedTime.format(date.getTime()));
        return stoppedTime;
    }

    public  static SimpleDateFormat carParkedMessage(){
        Calendar date = Calendar.getInstance();
        SimpleDateFormat parkedTime = new SimpleDateFormat("HH:mm");
        CarTrackingMessages.setParkedMessage( "Auto hat geparkt" +" "+ parkedTime.format(date.getTime()));
        return parkedTime;
    }

    public static SimpleDateFormat carDoggedMessage(){
        Calendar date = Calendar.getInstance();
        SimpleDateFormat dogedTime = new SimpleDateFormat("HH:mm");
        CarTrackingMessages.setDogingMessage( "Auto ist einem Objekt ausgewichen" +" "+ dogedTime.format(date.getTime()));
        return dogedTime;
    }
    public static SimpleDateFormat carAvoidedDeatMessage(){
        Calendar date = Calendar.getInstance();
        SimpleDateFormat avoidedDeathTime = new SimpleDateFormat("HH:mm");
        CarTrackingMessages.setAvoidedDeathMessage( "Auto ist nicht runtergefallen" +" "+ avoidedDeathTime.format(date.getTime()));
        return avoidedDeathTime;
    }

}
