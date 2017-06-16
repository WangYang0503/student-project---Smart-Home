package com.smartHome.SmartHomeApp.controller;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Anja on 13.12.2016.
 * write in json File
 * Attention to the path. Each smartphone has another path to store the json-file
 */

public class WriteJson {
    private static WriteJson ourInstance;
    public static WriteJson getInstance(){
        if(ourInstance == null)
        {
            ourInstance = new WriteJson();
        }
        return ourInstance;
    }


    /**
     * transforms he list from AlarmActivity to a JsnObject
     * to save the AlarmList
     * @param list
     */

    public void write(ArrayList<String> list, Context context) {
        //Settings
        String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
        String pathDir = baseDir + File.separator + "Android" + File.separator + "data" + File.separator +
                "com.smartHome.SmartHomeApp" + File.separator + "files";
        String fileName = "alarmList.json";
        File f = new File(pathDir + File.separator + fileName);


        try {
            if (!f.exists()) {
                f.createNewFile();
                Log.e("WriteJsonFile", "Created");
            }
                if (!list.isEmpty()) {
                    JSONObject obj = new JSONObject();
                    JSONArray arrayJson = new JSONArray();
                    Log.e("WriteJsonFile", "Write");
                    arrayJson.put(list);
                    obj.put("alarmList", arrayJson);
                    f.getParentFile().mkdirs();
                    FileWriter file = new FileWriter(f);
                    file.write(obj.toString());
                    file.flush();
                    file.close();
                } else {
                    Log.e("WriteJsonFile", "List is empty");
                }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void writeCoffeeSettings(ArrayList<String> saveCoffeeSettings, Context context) {
        String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
        String pathDir = baseDir + File.separator + "Android" + File.separator + "data" + File.separator +
                "com.smartHome.SmartHomeApp" + File.separator + "files";
        String fileName = "coffeesettings.json";
        //Settings

        File f = new File(pathDir + File.separator + fileName);
        try {
            if (!f.exists()) {
                f.createNewFile();
                Log.d("CoffeeSettingFile", "Created");
            }
                if (!saveCoffeeSettings.isEmpty()) {
                    JSONObject obj = new JSONObject();
                    JSONArray arrayJson = new JSONArray();
                    Log.e("CoffeeSettingFile", "Write");
                    arrayJson.put(saveCoffeeSettings);

                    //need same index as alarmList for array
                    Log.d("CoffeeSettings", arrayJson.toString());
                    obj.put("CoffeeSetting", arrayJson);
                    f.getParentFile().mkdirs();
                    FileWriter file = new FileWriter(f);
                    file.write(obj.toString());
                    file.flush();
                    file.close();
                }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
