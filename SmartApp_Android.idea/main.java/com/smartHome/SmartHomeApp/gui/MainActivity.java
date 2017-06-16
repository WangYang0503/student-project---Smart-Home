package com.smartHome.SmartHomeApp.gui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.smartHome.SmartHomeApp.R;
import com.smartHome.SmartHomeApp.kaa.KaaManager;
import com.smartHome.SmartHomeApp.notifications.IntroductionNotification;

/**
 * This is the main Window of the app. It shows 4 buttons which open new windows
 */
public class MainActivity extends AppCompatActivity {

    // Instance of mainActivity, only for backup purpose,
    // eg. instances from other activities are unavailable
    private static MainActivity ourInstance;

    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferences;

    private Button buttonLight;
    private Button buttonCar;
    private Button buttonCoffee;
    private Button buttonGarage;
    private Button buSmokeDetector;

    TextView onOffCar;
    TextView onOffDoor;
    TextView onOffGarage;
    TextView onOffCoffee;

    private boolean smoke;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ourInstance = this;

        buttonCar();
        buttonCoffee();
        buttonLight();
        buttonGarage();
        toolbarActivity();
        navigationBar();

        buSmokeDetector = (Button) findViewById(R.id.buttonSmoke);
        onOffCar = (TextView) findViewById(R.id.onOffCar);
        onOffDoor = (TextView) findViewById(R.id.onOffDoor);
        onOffGarage = (TextView) findViewById(R.id.onOffGarage);
        onOffCoffee = (TextView) findViewById(R.id.onOffCoffee);


        //show dialog at first time
        doFirstRun();

        KaaManager.connectToKaaServer();
    }


    @Override
    protected void onResume() {
        super.onResume();
        // update the enable state of the main buttons, if the manActivity is active again

        updateButtonEnableState();

    }

    /**
     * checks , if it's the first time the app runs
     * and show the user the introduction dialog
     */
    private void doFirstRun() {
        IntroductionNotification introductionNotification = new IntroductionNotification();
        sharedPreferences = getSharedPreferences("MainActivity", MODE_PRIVATE);
        if (sharedPreferences.getBoolean("isFirstRun", true)) {
            IntroductionNotification.introductionMainDialog(MainActivity.this);

            editor = sharedPreferences.edit();
            editor.putBoolean("isFirstRun", false);
            editor.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * menu on top right
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.help:
                buttonHelp();
                return true;
            case R.id.connection:
                Intent gpsOptionsIntent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                startActivityForResult(gpsOptionsIntent, 0);
                return true;
            case R.id.alarm:
                Intent alarmIntent = new Intent(getApplicationContext(), AlarmPreference.class);
                startActivity(alarmIntent);
                return true;
            case R.id.door:
                Intent doorIntent = new Intent(getApplicationContext(), DoorActivity.class);
                startActivity(doorIntent);
                return true;
            case R.id.adminPage:
                kaaService();
                return true;
            case R.id.aboutPage:
                aboutActivityStart();
                return true;
            case R.id.smokeDetector:
                Intent intent = new Intent(getApplicationContext(), SmokeDetectorActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void aboutActivityStart() {
        Intent i = new Intent(getApplicationContext(), AboutPrefernces.class);
        startActivity(i);
    }

    //Button Garage
    private void buttonGarage() {
        buttonGarage = (Button) findViewById(R.id.garageClassButton);
        buttonGarage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), GarageActivity.class);
                startActivity(i);
                slideTransition(v);
            }
        });
    }

    //Button Licht
    private void buttonLight() {
        buttonLight = (Button) findViewById(R.id.lightClassButton);
        buttonLight.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), LightsActivity.class);
                startActivity(i);
                slideTransition(v);
            }
        });
    }

    //Button Car
    private void buttonCar() {
        buttonCar = (Button) findViewById(R.id.carClassButton);
        buttonCar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), CarActivity.class);
                startActivity(i);
                slideTransition(v);
            }
        });
    }

    /**
     * Button Kaffee
     */
    private void buttonCoffee() {

        buttonCoffee = (Button) findViewById(R.id.coffeeClassButton);
        buttonCoffee.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), CoffeeActivity.class);
                startActivity(i);
                slideTransition(v);
            }
        });

    }

    /**
     * Button Hilfe
     */

    private void buttonHelp() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            Uri uri = Uri.parse("http://docs.kaaproject.org/display/KAA/Administration+UI+guide"); // missing 'http://' will cause crashed
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        } else {
            Toast.makeText(MainActivity.this, "Keine Internetverbindung", Toast.LENGTH_LONG).show();
        }
    }


    /**
     * Button Kaa for the Kaa setting page
     */

    private void kaaService() {
        if (KaaManager.isConnected()) {
            Uri uri = Uri.parse("http://192.168.0.20:8080/"); // missing 'http://' will cause crashed
            Intent i = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(i);
        } else {
            Toast.makeText(MainActivity.this, "Keine Internetverbindung", Toast.LENGTH_LONG).show();
        }

    }

    private void toolbarActivity() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarMainClass);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    public void slideTransition(View view) {
        overridePendingTransition(R.anim.activity_translate, R.anim.activity_scale);
    }


    /**
     * enables and disable the buttons in the mainActivity
     * according to the availability state of their services
     *
     * @return true if ALL buttons are set to enabled, false otherwise
     */
    public void updateButtonEnableState() {


        if (KaaManager.isConnected()) {
            if (KaaManager.getCoffeeMachineEventHandler() == null) {
                // kaa user not yet attached
                disableButtons();
                return;
            }
            // app is connected to kaaServer
            buttonLight.setEnabled(true);
            buttonCar.setEnabled(true);

            buttonLight.setAlpha(1f);
            buttonCar.setAlpha(1f);


            //Toast.makeText(MainActivity.this, "seltsam", Toast.LENGTH_LONG).show();
            changeOnOffSignal();
            //setSmokeButton(false);

            if (KaaManager.getCoffeeMachineEventHandler().getCoffeeStatus() != null) {
                // coffee server is (or at leas was oce) available
                buttonCoffee.setEnabled(true);
                buttonCoffee.setAlpha(1f);
            } else {
                // kaa is connected but coffee server isn't
                buttonCoffee.setEnabled(false);
                buttonCoffee.setAlpha(0.5f);
            }

            if (KaaManager.getGarageEventHandler() != null) {
                buttonGarage.setEnabled(true);
                buttonGarage.setAlpha(1f);
            } else {
                buttonGarage.setEnabled(false);
                buttonGarage.setAlpha(0.5f);
            }


        } else {
            // not connected to kaa
            disableButtons();
        }


    }

    /**
     * disables the main buttons (the one in the center of the screen) of the MainActivity
     */
    private void disableButtons() {
        buttonCar.setEnabled(false);
        buttonCoffee.setEnabled(false);
        buttonLight.setEnabled(true);
        buttonGarage.setEnabled(false);

        buttonCar.setAlpha(0.5f);
        buttonCoffee.setAlpha(0.5f);
        buttonLight.setAlpha(0.5f);
        buttonGarage.setAlpha(0.5f);
    }

    public static MainActivity getInstance() {
        return ourInstance;
    }

    /**
     * changes the status of the different things. so the user can see  in the main screen if the things
     * are working or not
     */
    public void changeOnOffSignal() {
        if (KaaManager.isConnected()) {
            if (KaaManager.getGarageEventHandler() != null) {
                if (KaaManager.getGarageEventHandler().getDoorAndGarageStatus() != null) {
                    if (KaaManager.getGarageEventHandler().garageIsOpen()) {
                        onOffGarage.setText("an");
                        onOffGarage.setTextColor(0xFF00FF00);
                    } else {
                        onOffGarage.setText("aus");
                        onOffGarage.setTextColor(0xffcc0000);
                    }
                }
            }

            if (KaaManager.getCoffeeMachineEventHandler() != null) {
                if (KaaManager.getCoffeeMachineEventHandler().getCoffeeStatus() != null) {
                    onOffCoffee.setText("an");
                    onOffCoffee.setTextColor(0xFF00FF00);
                } else {
                    //onOffCoffee.setText("aus");
                    //onOffCoffee.setTextColor(0xffcc0000);
                }

            }

            if (KaaManager.getMindstormsEventHandler() != null) {
                if (KaaManager.getMindstormsEventHandler().isDriving()) {
                    onOffCar.setText("an");
                    onOffCar.setTextColor(0xFF00FF00);
                } else {
                    onOffCar.setText("aus");
                    onOffCar.setTextColor(0xffcc0000);
                }
            }

            if (KaaManager.getSmokeDetectorEventHandler() != null) {
                setSmokeButton();
            }
        }
    }

    private void navigationBar() {
        ListView mDrawerList = (ListView) findViewById(R.id.navList);

        String[] lightsAttay = {"Wohnzimmer", "Schlafzimmer", "Küche", "Bad", "Flur"};
        ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lightsAttay);
        mDrawerList.setAdapter(mAdapter);
    }

    public void setSmokeButton() {
        if (KaaManager.getSmokeDetectorEventHandler().getSmokeDetectorStatus()) {
            buSmokeDetector.setBackgroundResource(R.color.Red);
            //Toast.makeText(MainActivity.this, "Rotes Licht", Toast.LENGTH_LONG).show();
        } else {
            buSmokeDetector.setBackgroundResource(R.color.Green);
            //Toast.makeText(MainActivity.this, "Grünes Licht", Toast.LENGTH_LONG).show();
        }
    }
}