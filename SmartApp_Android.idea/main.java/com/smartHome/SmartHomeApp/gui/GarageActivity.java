package com.smartHome.SmartHomeApp.gui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.smartHome.SmartHomeApp.R;
import com.smartHome.SmartHomeApp.kaa.KaaManager;
import com.smartHome.SmartHomeApp.kaa.eventHandler.GarageEventHandler;

/**
 * This class has got the basic functionalities for the door open system it contains the functions
 * for the 2 buttons, the open and close button
 * Created by Lia on 10.11.2016.
 */

public class GarageActivity extends AppCompatActivity {
    private Button garageButton;
    private ProgressBar garageProgressBar;
    private boolean garageIsMoving = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.garage_layout);

        final GarageEventHandler garageEventHandler = KaaManager.getGarageEventHandler();

        if (garageEventHandler == null || garageEventHandler.getDoorAndGarageStatus() == null) {
            Toast.makeText(KaaManager.getActivity(), "Keine Verbindung zu Kaa", Toast.LENGTH_LONG).show();
            finish();
        }


        garageButton = (Button) findViewById(R.id.garageActionButton);
        garageProgressBar = (ProgressBar) findViewById(R.id.garage_progressbar);
        garageProgressBar.setVisibility(View.INVISIBLE);

        garageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                garageButton.setVisibility(View.INVISIBLE);
                garageProgressBar.setVisibility(View.VISIBLE);
                garageIsMoving = true;

                try {
                    if (KaaManager.getGarageEventHandler().garageIsOpen()) {
                        garageEventHandler.sendCloseGarage();
                    } else {
                        garageEventHandler.sendOpenGarage();
                    }
                } catch (NullPointerException e) {
                    Toast.makeText(KaaManager.getActivity(), "Keine Verbindung zur Garage", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        updateButtons();
    }

    public void updateButtons() {
        // TODO: Implement method
        GarageEventHandler eventHandler = KaaManager.getGarageEventHandler();
        if (eventHandler != null) {
            if (eventHandler.getDoorAndGarageStatus() != null) {
                if (!garageIsMoving) {
                    if (KaaManager.getGarageEventHandler().garageIsOpen()) {
                        garageButton.setText(R.string.door_close);
                    } else {
                        garageButton.setText(R.string.door_open);
                    }
                }
            } else {
                Toast.makeText(KaaManager.getActivity(), "Keine Verbindung zur Garage", Toast.LENGTH_LONG).show();
                // exit the activity
                finish();
            }
        } else {
            Toast.makeText(KaaManager.getActivity(), "Keine Verbindung zu Kaa", Toast.LENGTH_LONG).show();
            // exit the activity
            finish();
        }
    }

    public void garageStopped(boolean isOpen) {
        garageIsMoving = false;
        garageProgressBar.setVisibility(View.INVISIBLE);
        garageButton.setVisibility(View.VISIBLE);
        if (isOpen) {
            garageButton.setText("Schließen");
        } else {
            garageButton.setText("Öffnen");
        }
    }
}
