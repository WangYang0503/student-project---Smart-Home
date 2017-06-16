package com.smartHome.SmartHomeApp.notifications;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.NotificationCompat;

import com.smartHome.SmartHomeApp.R;
import com.smartHome.SmartHomeApp.gui.CoffeeActivity;
import com.smartHome.SmartHomeApp.kaa.KaaManager;

// import com.smartHome.SmartHomeApp.gui.AlarmActivity;

/**
 * Created by Alex on 08.09.16.
 * <p/>
 * All notifications, warnings and errors which needed to be shown in association with the coffee machin
 * <p/>
 * All methods are static
 */
public class CoffeeNotifications {

    /**
     * popup Message when Coffee Button is clicked when no Connection to Kaa is established
     */
    public static void notConnectedToKaaDialog() {
        final Activity activity = KaaManager.getActivity();
        if (activity == null) {
            return;
        }
        activity.runOnUiThread(new Runnable() {
            public void run() {
                new AlertDialog.Builder(activity)
                        .setTitle(activity.getString(R.string.dialogWarning))
                        .setMessage(activity.getString(R.string.dialogConnectionError))
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
     * Coffee cooking finished
     * Notification Message
     */

    public static void cookingFinishedNotification() {
        final Activity activity = KaaManager.getActivity();
        NotificationManager manager = null;
        Notification builder = new NotificationCompat.Builder(KaaManager.getActivity())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText(activity.getString(R.string.dialogCoffeeFinishedMessage))
                .setContentTitle(activity.getString(R.string.dialogCoffeeInfo))
                .setTicker(activity.getString(R.string.dialogNewMessage))
                .setDefaults(NotificationCompat.DEFAULT_SOUND)
                .setAutoCancel(true)
                .build();

        manager = (NotificationManager) KaaManager.getActivity()
                .getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(5, builder);

        CoffeeActivity.getInstance().updateWaterLvlDisplay();
        CoffeeActivity.getInstance().findViewById(R.id.cookCoffeeButton).setEnabled(true);
        CoffeeActivity.getInstance().findViewById(R.id.buttonKAbbrechen).setEnabled(true);
        CoffeeActivity.getInstance().findViewById(R.id.cookCoffeeButton).setAlpha(1f);
        CoffeeActivity.getInstance().findViewById(R.id.buttonKAbbrechen).setAlpha(1f);

    }

    /**
     * Message if Amount of Cups is under 1
     */
    public static void LowCupNumber(Context context) {
        android.support.v7.app.AlertDialog.Builder builder2 = new android.support.v7.app.AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.dialogWarning))
                .setMessage(context.getString(R.string.dialogLowCupNumber));

        android.support.v7.app.AlertDialog al = builder2.create();
        al.show();
    }

    /**
     * Message if amount of cups is over 12
     */
    public static void HighCupNumber(Context context) {

        android.support.v7.app.AlertDialog.Builder builder1 = new android.support.v7.app.AlertDialog.Builder(context)

                .setTitle(context.getString(R.string.dialogWarning))
                .setMessage(context.getString(R.string.dialogHighCupNumber))
                .setPositiveButton(R.string.alert_dialog_ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        });
        android.support.v7.app.AlertDialog al1 = builder1.create();
        al1.show();

    }


    /**
     * no water in the coffee machine
     */
    public static void noWaterDialog() {
        final Activity activity = KaaManager.getActivity();
        if (activity == null) {
            return;
        }
        activity.runOnUiThread(new Runnable() {
            public void run() {
                new AlertDialog.Builder(activity)
                        .setTitle(activity.getString(R.string.dialogWarning))
                        .setMessage(activity.getString(R.string.dialiogNoWater))
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
     * popup Message when  stop Button is clicked
     */
    public static void stopBrewingDialog() {
        final Activity activity = KaaManager.getActivity();
        if (activity == null) {
            return;
        }
        activity.runOnUiThread(new Runnable() {
            public void run() {
                new AlertDialog.Builder(activity)
                        .setTitle(activity.getString(R.string.dialogMessage))
                        .setMessage(activity.getString(R.string.dialogStoppedBrewing))
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
     * almost no water in the coffeemachine
     */
    public static void LowWaterNotification() {
        final Activity activity = KaaManager.getActivity();
        if (activity == null) {
            return;
        }
        activity.runOnUiThread(new Runnable() {
            public void run() {
                new AlertDialog.Builder(activity)
                        .setTitle(activity.getString(R.string.dialogMessage))
                        .setMessage(activity.getString(R.string.dialogLowWater))
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
     * no carafe
     */
    public static void noCarafeDialog() {

        final Activity activity = KaaManager.getActivity();
        if (activity == null) {
            return;
        }
        activity.runOnUiThread(new Runnable() {
            public void run() {
                new AlertDialog.Builder(activity)
                        .setTitle(activity.getString(R.string.dialogWarning))
                        .setMessage(activity.getString(R.string.dialogNoCarafe))
                        .setPositiveButton(R.string.alert_dialog_ok,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                    }
                                })
                        .show();
            }
        });
    }

    public static void alreadyBrewing() {

        final Activity activity = KaaManager.getActivity();
        if (activity == null) {
            return;
        }
        activity.runOnUiThread(new Runnable() {
            public void run() {
                new AlertDialog.Builder(activity)
                        .setTitle(activity.getString(R.string.dialogWarning))
                        .setMessage(activity.getString(R.string.dialogNoCarafe))
                        .setPositiveButton(R.string.alert_dialog_ok,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                    }
                                })
                        .show();

            }});
    }

    /**
     * show dialog of the actual coffee
     */
    public static void showActualCoffeeNotification() {
        NotificationManager manager = null;
        final Activity activity = KaaManager.getActivity();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(KaaManager.getActivity());
        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentText("Es wird gleich Kaffee gekocht.")
                .setContentTitle(activity.getString(R.string.dialogCoffeeInfo))
                .setTicker(activity.getString(R.string.dialogNewMessage))
                .setDefaults(NotificationCompat.DEFAULT_SOUND)
                .setAutoCancel(true)
                .build();
        manager = (NotificationManager) KaaManager.getActivity()
                .getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(6, builder.build());


    }


}