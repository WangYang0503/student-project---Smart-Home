package com.smartHome.SmartHomeApp.controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.RequiresPermission;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;
import android.widget.ListView;

import com.smartHome.SmartHomeApp.R;
import com.smartHome.SmartHomeApp.gui.AlarmListActivity;
import com.smartHome.SmartHomeApp.gui.AlarmPreference;
import com.smartHome.SmartHomeApp.gui.LightsActivity;
import com.smartHome.SmartHomeApp.kaa.KaaManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by Anja on 08.11.2016.
 * the alarm is received here
 * alarm calls the Ringtone and sends signals to coffeemachine and light
 */

public class AlarmReceiver extends WakefulBroadcastReceiver {
    /**
     * receive the alarm
     */
    Intent ringIntent;

    @Override
    public void onReceive(final Context context, Intent intent) {
        //TODO write code for turning light on here

        if (KaaManager.getLightEventHandler() != null) {
                KaaManager.getLightEventHandler().initialize();
                List<String> list = KaaManager.getLightEventHandler().getRoomItems();
                LightsActivity lightsActivity1 = new LightsActivity();
                for(int i=0; i<list.size();i++){
                    lightsActivity1.setColor(list.get(i),0,0,0);
                }
                Log.d("Alarm", "received and Light on");
        }

        ReadJson readJson = ReadJson.getInstance();
        if (KaaManager.getCoffeeMachineEventHandler() != null) {
            // readJson.readCoffeeSettings(getA);
            Log.d("Alarm", "received and make Coffee");

              KaaManager.getCoffeeMachineEventHandler().sendStartBrewingEvent();
            //TODO prÃ¼fe ob genug Wasser vorhanden ist, um den Kaffee zu kochen
            /**    if (readJson.StringToObject() != null) {
             if (!readJson.StringToObject().matches("")) {
             String correctAlarm = readJson.StringToObject();
             String correctAlarm1 = correctAlarm.replace("[", "");
             String correctAlarm2 = correctAlarm1.replace("\"", "");
             String correctAlarm3 = correctAlarm2.replace("]", "");

             listAlarm = new ArrayList<String>(Arrays.asList(correctAlarm3.split(", ")));
             alarmListView.setEnabled(true);
             }
             }**/
        }


        //calls the Ringtone
        Log.e("Alarm", "received");
        ringIntent = new Intent(context, PlayMusicService.class);
        context.startService(ringIntent);

    }
}

