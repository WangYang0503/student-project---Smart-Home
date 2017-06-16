package com.smartHome.SmartHomeApp.gui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.smartHome.SmartHomeApp.R;
import com.smartHome.SmartHomeApp.kaa.KaaManager;
import com.smartHome.SmartHomeApp.notifications.CarTrackingNotification.CarTrackingActivity;

/**
 * This Class allows to  start and stop the mindstorm. It also has a Toolbar which contains
 * the CarTracking Activity, which is a Log file.
 * The CarActivity  has the following methods: OnCreate, toolbarActivity, onCreateOptionsMenu
 * and onOptionsItemSelected
 * Created by Lia on 19.10.2016.
 */

public class CarActivity extends AppCompatActivity {
    /**
     * This method has two ClickListeneners one to start the mindstorm, one to stop the mindstorm
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.car_layout);

        toolbarActivity();
        startDriving();
        stopDriving();
    }

    /**
     * this method allows to stop the driving car when the button is clicked.
     * Makes the start Button clickable and the stop Button not clickable
     */
    private void stopDriving() {
        final Button startButton = (Button) findViewById(R.id.startCarButton);
        final Button stopButton = (Button) findViewById(R.id.stopCarButton);
        stopButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (KaaManager.getMindstormsEventHandler().isDriving()) {
                    KaaManager.getMindstormsEventHandler().sendStopDrivingEvent();
                    stopButton.setClickable(false);
                    startButton.setClickable(true);
                    stopButton.setAlpha((float) 0.5);
                    startButton.setAlpha((float) 1.0);
                }
            }
        });

    }

    /**
     * this method allows to start the car to drive when the button is clicked.
     * Makes the stop Button clickable and the start Button not clickable
     */
    private void startDriving() {
        final Button startButton = (Button) findViewById(R.id.startCarButton);
        final Button stopButton = (Button) findViewById(R.id.stopCarButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!KaaManager.getMindstormsEventHandler().isDriving()) {
                    KaaManager.getMindstormsEventHandler().sendStartDrivingEvent();
                    startButton.setClickable(false);
                    stopButton.setClickable(true);
                    startButton.setAlpha((float) 0.5);
                    stopButton.setAlpha((float) 1.0);

                }
            }
        });
    }

    /**
     * creates a toolbar
     */
    private void toolbarActivity() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarCar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    /**
     * creates the menu of the toolbar
     *
     * @param menu
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_car, menu);

        return true;
    }

    /**
     * Opens the new Window when the Item of the menu is clicked
     *
     * @param item
     * @return super.onOptionsItemSelected(item)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.carTracking:
                Intent i = new Intent(getApplicationContext(), CarTrackingActivity.class);
                startActivity(i);

            default:
                return super.onOptionsItemSelected(item);

        }

    }
}
