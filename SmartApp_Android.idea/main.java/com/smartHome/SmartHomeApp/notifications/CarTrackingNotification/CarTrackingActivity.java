package com.smartHome.SmartHomeApp.notifications.CarTrackingNotification;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.smartHome.SmartHomeApp.R;

import java.util.ArrayList;

/**
 * Created by Lia on 08.11.2016.
 * Activity for the  saved car tracking informations one can read when the car does something
 */

public class CarTrackingActivity extends Activity {


    private ArrayList<String> carList = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private ListView carListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.car_tracking_list);


        carListView = (ListView) findViewById(R.id.carTrackingListView);
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, carList);

        carListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        carList.add(CarTrackingMessages.getStartedMessage());
        carList.add(CarTrackingMessages.getStoppedMessage());
        carList.add(CarTrackingMessages.getParkedMessage());
        carList.add(CarTrackingMessages.getAvoidedDeathMessage());
        carList.add(CarTrackingMessages.getDogingMessage());
    }

}
