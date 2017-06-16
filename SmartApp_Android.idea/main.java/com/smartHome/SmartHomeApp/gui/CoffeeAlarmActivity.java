package com.smartHome.SmartHomeApp.gui;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.smartHome.SmartHomeApp.R;
import com.smartHome.SmartHomeApp.controller.ReadJson;
import com.smartHome.SmartHomeApp.controller.WriteJson;

import java.util.ArrayList;
import java.util.Arrays;

import smart_things.coffee.schema.StrengthTypes;


/**
 * Created by Anja on 17.01.2017.
 * changed CoffeeActivity class for alarm
 * to save the coffeesettings in a json file
 */

public class CoffeeAlarmActivity extends AppCompatActivity {

    private static EditText number;
    private RadioButton filter;
    private RadioButton medium;
    private RadioButton strong;
    private RadioButton weak;
    private Button buttonSaveCoffee;
    private int i = 0;
    private static CoffeeAlarmActivity ourInstance = null;
    private ArrayList<String> coffeeSettings;
    private ReadJson readJson;
    private String emptyArray = "";
    private WriteJson writeJson;

    public boolean isSetSave() {
        return setSave;
    }

    public void setSetSave(boolean setSave) {
        this.setSave = setSave;
    }

    private boolean setSave = false;


    public static CoffeeAlarmActivity getInstance(){
        if(ourInstance == null)
        {
            ourInstance = new CoffeeAlarmActivity();
        }
        return ourInstance;
    }

    public ArrayList<String> getCoffeeSettings() {
        if(coffeeSettings==null){
            coffeeSettings = new ArrayList<>();
        }else{
            getCoffeeSettings();
        }
        return coffeeSettings;
    }

    public void setCoffeeSettings(ArrayList<String> coffeeSettings) {
       this.coffeeSettings = coffeeSettings;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coffeealarm_layout);
        saveInformationOfCoffeeSettings();
        readJson = ReadJson.getInstance();
        writeJson = WriteJson.getInstance();

        if (writeJson.getInstance() != null) {
            readFile();
        }

        if (coffeeSettings != null){

            i = coffeeSettings.size()
            ;
        }
    }



    /**
     * save Information of CoffeeSettings in a json file
     */
    private void saveInformationOfCoffeeSettings() {

        buttonSaveCoffee = (Button) findViewById(R.id.buttonSaveCoffee);
        buttonSaveCoffee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (coffeeSettings == null) {
                    coffeeSettings = new ArrayList<String>();
                }
                //get data from activity
                String saveInfos = "";
                StrengthTypes strengthTypes = StrengthTypes.UNKNOWN;

                filter = (RadioButton) findViewById(R.id.filterRadioButton);
                weak = (RadioButton) findViewById(R.id.strengthWeakRadioButton);
                medium = (RadioButton) findViewById(R.id.strengthMediumRadioButton);
                strong = (RadioButton) findViewById(R.id.strengthStrongRadioButton);
                number = (EditText) findViewById(R.id.numberCupText);
                String cupNumber = number.getText().toString();
                if (filter.isChecked()) {
                    saveInfos = "Filter";
                } else {
                    saveInfos = "Bohnen";
                }
                if (weak.isChecked()) {
                    strengthTypes = StrengthTypes.WEAK;
                }
                if (medium.isChecked()) {
                    strengthTypes = StrengthTypes.MEDIUM;
                }
                if (strong.isChecked()) {
                    strengthTypes = StrengthTypes.STRONG;
                }
                //save Information in an list
                //alarmlist and coffeesettings list has the same index
                String item = saveInfos + ". " + cupNumber + ". " + strengthTypes.toString();
                coffeeSettings.add(i, item);
                writeJson.writeCoffeeSettings(coffeeSettings,getApplicationContext());
                i++;
                setSave = true;
                Intent intent = new Intent(getBaseContext(), AlarmPreference.class);
                intent.putExtra("SetAlarmOn", true);
                startActivity(intent);
            }
        });
    }


    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        readFile();
    }

    @Override
    protected void onRestoreInstanceState(Bundle outState) {
        super.onRestoreInstanceState(outState);
        readFile();
    }

    /**
     * save the list if the user returns from the a activity
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        readFile();

        //saveDAta
    }

    public void readFile(){
        if(readJson.ObjectCoffeeSettingsToString()!=null) {
            //if string is not empty split the string
            if (!readJson.ObjectCoffeeSettingsToString().matches("")) {
                String correctAlarmV = readJson.ObjectCoffeeSettingsToString();
                String correctAlarmV1 = correctAlarmV.replace("[", "");
                String correctAlarmV2 = correctAlarmV1.replace("\"", "");
                String correctAlarmV3 = correctAlarmV2.replace("]", "");

                coffeeSettings = new ArrayList<String>(Arrays.asList(correctAlarmV3.split(", ")));
            }
        }else {
            coffeeSettings = new ArrayList<String>(Arrays.asList(emptyArray));
        }
        if (!coffeeSettings.isEmpty()) {
            i = coffeeSettings.size();

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getIntent().getExtras() != null) {
            setSave = getIntent().getExtras().getBoolean("SetAlarmOn");
        }
    }

}
