package com.smartHome.SmartHomeApp.notifications;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import com.smartHome.SmartHomeApp.R;
import com.smartHome.SmartHomeApp.kaa.KaaManager;

/**
 * Created by Anja on 11.01.2017.
 * Licht notifications werden angezeigt
 */

public class LightNotifications {

    /**
     * popup Message when Light Button is clicked when no Connection to Kaa is established
     */
    public static void responseDialog() {
        //TODO Alex please fix
        final Activity activity = KaaManager.getActivity();
        if (activity == null) {
            return;
        }
        activity.runOnUiThread(new Runnable() {
            public void run() {
                new AlertDialog.Builder(activity)
                        .setTitle("Response")
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
}
