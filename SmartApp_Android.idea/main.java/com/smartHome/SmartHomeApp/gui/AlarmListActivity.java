package com.smartHome.SmartHomeApp.gui;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.smartHome.SmartHomeApp.R;
import com.smartHome.SmartHomeApp.controller.ReadJson;
import com.smartHome.SmartHomeApp.controller.WriteJson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Anja on 08.11.2016.
 * shows all alarm and coffeesettings that the user has set 
 * TODO dont show all alarms and coffeesettings from the file yet; attention to path of the file
 */

public class AlarmListActivity extends Activity {

    private ListView alarmListView;
    private ListView coffeeListView;
    private List<String> listCoffee;
    private String emptyArray = "";
    private List<String> listAlarm;
    private WriteJson writeJson;
    private ReadJson readJson;
    private ArrayList<String> writeArray = new ArrayList<String>();
    private ArrayList<String> writeArrayCoffee = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarmlist_layout);
        readJson = ReadJson.getInstance();
        writeJson = WriteJson.getInstance();
        alarmListView = (ListView) findViewById(R.id.exampleListView);
        coffeeListView = (ListView) findViewById(R.id.ListViewCoffee);

        //read coffeesetting and alarmlist
        readFile();

        //add listAlarm to listView
        final ArrayAdapter<String> arrayAdapterJson = new ArrayAdapter<String>(this, R.layout.list_item, listAlarm);
        alarmListView.setAdapter(arrayAdapterJson);

        final ArrayAdapter<String> arrayAdapterCoffee = new ArrayAdapter<String>(this, R.layout.list_item, listCoffee);
        coffeeListView.setAdapter(arrayAdapterCoffee);

        if (listAlarm != null || !listAlarm.isEmpty()) {
            /**
             *  delete the item in alarmJson and listView
             */
            alarmListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    final int pos = position;
                    //dialog for deleting an item
                    Log.d("Alarm delete", pos + "");
                    //deleted linked alarm with coffeesettings
                    listAlarm.remove(pos);

                    //for the write method
                    arrayAdapterJson.notifyDataSetChanged();
                }
            });
        }

        if (listCoffee != null || !listCoffee.isEmpty()) {
            /**
             *  delete the item in ListCoffee and CoffeelistView
             */
            coffeeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    final int pos = position;
                    //dialog for deleting an item
                    Log.d("Alarm delete", pos + "");
                    //deleted linked alarm with coffeesettings

                    listCoffee.remove(pos);
                    //actualies the listView
                    arrayAdapterCoffee.notifyDataSetChanged();
                }
            });
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (listAlarm.isEmpty()) {
            writeArray.clear();
            writeArray.add(0, emptyArray);
        } else {
            if (listAlarm != null) {
                for (int i = 0; i < listAlarm.size(); i++) {
                    writeArray.add(i, listAlarm.get(i));
                }
                //actualies the listView
                Log.d("AlarmList", "" + writeArray);
            }
        }
        if (listCoffee.isEmpty()) {
            writeArrayCoffee.clear();
            writeArrayCoffee.add(0, emptyArray);

        } else {
            if (listCoffee != null) {
                for (int i = 0; i < listCoffee.size(); i++) {
                    writeArrayCoffee.add(i, listCoffee.get(i));
                }
                //actualies the listView
                Log.d("CoffeeList", "" + writeArrayCoffee);
            }

        }
        writeJson.writeCoffeeSettings(writeArrayCoffee, getApplicationContext());
        writeJson.write(writeArray, getApplicationContext());
        finish();
    }

    public void readFile() {
        //delete the json [," from the string
        //if string is not empty split the string
        if (readJson.StringToObject() != null) {
            if (!readJson.StringToObject().matches("")) {
                String correctAlarm = readJson.StringToObject();
                String correctAlarm1 = correctAlarm.replace("[", "");
                String correctAlarm2 = correctAlarm1.replace("\"", "");
                String correctAlarm3 = correctAlarm2.replace("]", "");

                listAlarm = new ArrayList<String>(Arrays.asList(correctAlarm3.split(", ")));
                alarmListView.setEnabled(true);
            }
        } else {
            listAlarm = new ArrayList<String>(Arrays.asList(emptyArray));
            alarmListView.setEnabled(false);
        }

        //add listAlarm to listView
        final ArrayAdapter<String> arrayAdapterJson = new ArrayAdapter<String>(this, R.layout.list_item, listAlarm);
        alarmListView.setAdapter(arrayAdapterJson);


        /**
         *read the coffeeSettings.json
         */
        if (readJson.ObjectCoffeeSettingsToString() != null) {
            //if string is not empty split the string
            if (!readJson.ObjectCoffeeSettingsToString().matches("")) {
                String correctAlarmV = readJson.ObjectCoffeeSettingsToString();
                String correctAlarmV1 = correctAlarmV.replace("[", "");
                String correctAlarmV2 = correctAlarmV1.replace("\"", "");
                String correctAlarmV3 = correctAlarmV2.replace("]", "");

                listCoffee = new ArrayList<String>(Arrays.asList(correctAlarmV3.split(", ")));
                coffeeListView.setEnabled(true);

            }
        } else {
            listCoffee = new ArrayList<String>(Arrays.asList(emptyArray));
            coffeeListView.setEnabled(false);
        }
        //add listAlarm to listView

        final ArrayAdapter<String> arrayAdapterCoffee = new ArrayAdapter<String>(this, R.layout.list_item, listCoffee);
        coffeeListView.setAdapter(arrayAdapterCoffee);

    }
}
