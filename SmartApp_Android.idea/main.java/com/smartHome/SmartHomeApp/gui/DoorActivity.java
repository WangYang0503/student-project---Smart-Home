package com.smartHome.SmartHomeApp.gui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.smartHome.SmartHomeApp.R;
import com.smartHome.SmartHomeApp.kaa.KaaManager;
import com.smartHome.SmartHomeApp.kaa.eventHandler.GarageEventHandler;

import smart_things.door.schema.InfoObject;

/**
 * Created by Kari on 11.01.2017.
 * TODO call Event for DoorClose oder DoorOpen
 * the user can close and open the door
 */

public class DoorActivity extends Activity {
    private Button openDoorButton;
    private Button closeDoorButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.door_layout);
        openDoorButton = (Button) findViewById(R.id.openDoorButton);
        closeDoorButton = (Button) findViewById(R.id.closeDoorButton);


        openDoorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              KaaManager.getGarageEventHandler().sendOpenDoor();
            }
        });

        closeDoorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KaaManager.getGarageEventHandler().sendCloseDoor();
            }
        });



    }


}
