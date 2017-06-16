package com.smartHome.SmartHomeApp.gui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.smartHome.SmartHomeApp.R;

import com.smartHome.SmartHomeApp.kaa.KaaManager;

import com.smartHome.SmartHomeApp.notifications.CoffeeNotifications;
import com.smartHome.SmartHomeApp.notifications.SmokeDetectorNotifications;
import com.smartHome.SmartHomeApp.kaa.eventHandler.SmokeDetectorEventHandler;

/**
 * Created by Lukas on 16.12.2016.
 */



public class SmokeDetectorActivity extends AppCompatActivity {

    private ImageButton buttonStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.smokedetector_layout);

        buttonStatus = (ImageButton) findViewById(R.id.buttonstatus);
        setButtonStatus(false);
        buttonStatus.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        KaaManager.getSmokeDetectorEventHandler().sendRequestEvent();
                    }
                });


    }

    /**
     * On click Method for the  Vorgang Abbrechen Button.
     * makes possible to use the Button so the coffee machine stops working if button was
     * clicked
     */


    public void setButtonStatus(boolean smokeDetected) {
        if(smokeDetected)
            buttonStatus.setBackgroundColor(Color.RED);
        else
            buttonStatus.setBackgroundColor(Color.GREEN);
    }
}
