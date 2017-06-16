package com.smartHome.SmartHomeApp.controller;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.smartHome.SmartHomeApp.gui.CoffeeAlarmActivity;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 * Created by Anja on 13.12.2016.
 * reads the json File
 * Attention to the path. Each smartphone has another path to store the json-file
 */

public class ReadJson {
    private static ReadJson ourInstance = null ;
    public static ReadJson getInstance(){
        if(ourInstance == null)
        {
            ourInstance = new ReadJson();
        }
        return ourInstance;
    }
    String stringFromFile;

    Context context;
    /**
     * read the data from File for AlarmList
     * @return data as a String
     */

    public String read(Context context) {
        this.context = context;
        //loads the files
        try {
            //path of file

            String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
            String pathDir = baseDir + File.separator + "Android" + File.separator + "data" + File.separator +
                    "com.smartHome.SmartHomeApp" + File.separator + "files";
            //Settings
            String fileName = "coffeesettings.json";
            File f = new File(pathDir + File.separator + fileName);
            Log.d("fielpa",f.toString() );
            //read data from file
            FileInputStream fileInput = new FileInputStream(f);
            BufferedReader inputReader = new BufferedReader(new InputStreamReader(fileInput, "UTF-8"), 8);
            StringBuilder strBuilder = new StringBuilder();
            //make the data to string
            String lineOfAlarmList = null;
            while ((lineOfAlarmList = inputReader.readLine()) != null) {
                strBuilder.append(lineOfAlarmList + "\n");
            }
            fileInput.close();
            stringFromFile = strBuilder.toString();

        } catch (IOException e) {
            Log.e("log_tag", "Error building string " + e.toString());
        }
        return stringFromFile;
    }

    /**
     * Object to String for AlarmList
     * @return
     */
    public String StringToObject() {
        String fileToJson = read(context);
        String jsonToString = null;
        try {
            JSONObject obj = new JSONObject(fileToJson);
            JSONArray jsonArray = obj.getJSONArray("alarmList");
            jsonToString = jsonArray.toString();
            Log.d("File", obj.toString());
        } catch (Throwable t) {
            Log.e("File", "Could not parse malformed JSON: \"" + fileToJson + "\"");
        }
        return jsonToString;
    }

    public String readCoffeeSettings(Context context) {
        this.context = context;
        //loads the files
        try {
                String fileName = "coffeesettings.json";
                //Settings
            String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
            String pathDir = baseDir + File.separator + "Android" + File.separator + "data" + File.separator +
                    "com.smartHome.SmartHomeApp" + File.separator + "files";
            File f = new File(pathDir + File.separator + fileName);
            //read data from file
            FileInputStream fileInput = new FileInputStream(f);
            BufferedReader inputReader = new BufferedReader(new InputStreamReader(fileInput, "UTF-8"), 8);
            StringBuilder strBuilder = new StringBuilder();
            //make the data to string
            String lineOfAlarmList = null;
            while ((lineOfAlarmList = inputReader.readLine()) != null) {
                strBuilder.append(lineOfAlarmList + "\n");
            }
            fileInput.close();
            stringFromFile = strBuilder.toString();

        } catch (IOException e) {
            Log.e("File", "Could not read the file" + e.toString());
        }
        return stringFromFile;
    }

    public String ObjectCoffeeSettingsToString() {
        String fileToJson = readCoffeeSettings(context);
        String jsonToString = null;
        try {
            JSONObject obj = new JSONObject(fileToJson);
            JSONArray jsonArray = obj.getJSONArray("CoffeeSetting");
            jsonToString = jsonArray.toString();
            Log.d("CoffeeSettings","Read " + obj.toString());
        } catch (Throwable t) {
            Log.e("File", "Could not parse malformed JSON: \"" + fileToJson + "\"");
        }
        return jsonToString;
    }


}
