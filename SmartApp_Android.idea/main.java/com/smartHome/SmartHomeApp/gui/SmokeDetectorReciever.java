package com.smartHome.SmartHomeApp.gui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


import com.smartHome.SmartHomeApp.kaa.KaaManager;


/**
 * Created by Anja on 08.11.2016.
 * the alarm is received here
 * alarm calls the Ringtone and sends signas to coffeemachine and light
 */

public class SmokeDetectorReciever extends BroadcastReceiver {
    /**
     * receive the alarm
     */
    Intent ringIntent;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Intent i = new Intent(context, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }


}
