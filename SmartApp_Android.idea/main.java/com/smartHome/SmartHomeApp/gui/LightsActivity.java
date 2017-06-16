package com.smartHome.SmartHomeApp.gui;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;

import com.smartHome.SmartHomeApp.R;
import com.smartHome.SmartHomeApp.Utilities.Light;
import com.smartHome.SmartHomeApp.Utilities.RGBLight;
import com.smartHome.SmartHomeApp.Utilities.Room;
import com.smartHome.SmartHomeApp.Utilities.LightAdapter;
import com.smartHome.SmartHomeApp.kaa.KaaManager;


import org.kaaproject.kaa.client.Kaa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Observer;



/**
 * This class has got the basic functionalities for the light system
 * It allows to  turn the light on and off, to change the color and the brightness of the light
 * and to add a new light in the system
 * Created by Lia on 29.08.2016.
 */
public class LightsActivity extends AppCompatActivity implements Observer{

    private int brightness=0;
    private int brightnessRed=0;
    private int brightnessGreen = 0;
    private  int brightnessBlue =0;

    private HashMap<String, Integer> brightnessStates = new HashMap<>();
    private HashMap<String, Color> colorStates = new HashMap<>();
    private Button addRoomBtn;
    private Button removeRoomBtn;
    private Button addLightBtn;
    private Button removeLightBtn;
    private Button lightOn;
    private Button lightOff;
    private Button changeColorBtn;
    private SeekBar seekbar;
    private ListView roomList;
    private ListView lightList;
    private String inputString = "";
    private Context context = this;
    private EditText addText;
    private ArrayList<String> roomItem = new ArrayList<>();
   // private ArrayAdapter<String> arrayAdapter;

    private Button resBtn;

    private ArrayList<RGBLight> lightsList = new ArrayList<>();
    private LightAdapter adapter;
    private ArrayAdapter<String> roomAdapter;



    public List<RGBLight> getLightItem(){
        return lightsList;
    }

    public List<String> getListItems() {
        return roomItem;
    }

    public void setListItems(ArrayList<String> listItems) {
        this.roomItem = listItems;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
       KaaManager.getLightEventHandler().addObserver(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.light_layout);
        initGuiObjects();
        btnSendRequest();
    }




    /**
     * @param roomID distinct ID of the room
     */
    private void removeRoom(String roomID){
        KaaManager.getLightEventHandler().sendRemoveRoomEvent(roomID);
    }

    /**
     * @param lightID distinct ID of the light
     */
    private void addLight(String lightID, String roomID, int slot1, int slot2, int slot3){
        KaaManager.getLightEventHandler().sendAddRGBLightEvent(lightID,roomID,slot1,slot2,slot3);
        RGBLight l = new RGBLight(lightID, slot1,slot2,slot3);
        lightsList.add(l);
        adapter.notifyDataSetChanged();

    }

    /**
     *@param lightID ID of the selected light
     * @param brightness Value between 0-255
     */
    private void setBrightness( String lightID,int brightness){
        KaaManager.getLightEventHandler().sendBrightnessEvent(lightID, brightness);
    }

    /**
     * @param lightID distinct ID of the light
     */
    public void setColor(String lightID, int red, int green, int blue){
       KaaManager.getLightEventHandler().sendColorEvent(lightID, red, green, blue);
    }

    /**
     * @param lightID distinct ID of the light
     */
    private void getColor(String lightID){


    }

    /**
     *
     * @param roomID distinct ID of the room in the smart home
     */
    private void setRoomColor(String roomID, int red, int green, int blue){
       // KaaManager.getLightEventHandler().sendSetRoomColor(roomID, red, green, blue);
    }


    /**
     * Inits all gui objects from the xml file
     */
    private void initGuiObjects(){
        // Buttons
        addRoomBtn = (Button) findViewById(R.id.addRoom);
        removeRoomBtn = (Button) findViewById(R.id.removeRoom);
        addLightBtn = (Button) findViewById(R.id.addLight);
        removeLightBtn = (Button) findViewById(R.id.removeLight);
        lightOn = (Button) findViewById(R.id.lightOnButton);
        lightOff= (Button) findViewById(R.id.lightOffButton);

        seekbar =(SeekBar) findViewById(R.id.seekBar2);

        //ListView for room
        roomList = (ListView) findViewById(R.id.roomList);
        roomAdapter = new ArrayAdapter<String>
                (this,android.R.layout.simple_list_item_multiple_choice, roomItem);
        roomList.setAdapter(roomAdapter);
        roomList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        //ListView for Light
        lightList = (ListView) findViewById(R.id.lightList);
        adapter = new LightAdapter(this, lightsList);
        lightList.setAdapter(adapter);
        lightList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        //OnClickListener

        addRoomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addRoomWindow();
            }
        });
        removeRoomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                removeRoomWindow();
            }
        });
        addLightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addLightWindow();
            }
        });

        removeLightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                removeLight();
            }
        });

        lightOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                turnlightOn();
            }
        });
        lightOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                turnlightOff();
            }
        });
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                brightness = progress;
                   changeBrightness();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        changeColorBtn = (Button) findViewById(R.id.changeColor);
        changeColorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeColor();
            }
        });

    }
    private void btnSendRequest(){
        resBtn = (Button) findViewById(R.id.reqBtn);

        resBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KaaManager.getLightEventHandler().initialize();
                List<String> list = KaaManager.getLightEventHandler().getRoomItems();

                for(String s : list){
                    Log.i("LISTITEM", s);
                    roomItem.add(s);
                }
                //listItems.addAll(list);

                roomAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * @param roomID distinct ID of the room
     */
    private void addRoom(String roomID){
        KaaManager.getLightEventHandler().sendAddRoomEvent(roomID);
        Room r = new Room(roomID);
        roomItem.add(r.toString());
        roomAdapter.notifyDataSetChanged();
    }

    /**
     * Opens a popup window to input the id of the room. If the user presses "OK", the room will be added
     */
    private void addRoomWindow() {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.addroom_layout);
        dialog.setTitle("Neues Zimmer hinzufügen");
        addText = (EditText) dialog.findViewById(R.id.RoomEditText);
        Button dialogOkButton = (Button) dialog.findViewById(R.id.DialogOkButton);


        dialogOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputString = (addText.getText().toString());
                addRoom(inputString);
                dialog.dismiss();

            }
        });
        dialog.show();
    }


    private void addLightWindow(){
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.addlight_layout);
        dialog.setTitle("Neues Licht hinzufügen");

        addText = (EditText) dialog.findViewById(R.id.LightEditText);
        final EditText addRommET = (EditText)dialog.findViewById(R.id.RommEditText);
        final EditText addPort1 = (EditText) dialog.findViewById(R.id.slotEditText);
        final EditText addPort2 = (EditText) dialog.findViewById(R.id.slotEditText2);
        final EditText addPort3 = (EditText) dialog.findViewById(R.id.slotEditText3);
        Button dialogOkButton = (Button) dialog.findViewById(R.id.DialogOkButton);


        dialogOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addLight(addText.getText().toString(),addRommET.getText().toString(),
                        Integer.parseInt(addPort3.getText().toString()),
                        Integer.parseInt(addPort2.getText().toString()),
                        Integer.parseInt(addPort1.getText().toString()));
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void removeRoomWindow(){
        int counter = roomList.getCount();
        for (int i = counter; i >= 0; i--) {

            if (roomList.isItemChecked(i)) {
                KaaManager.getLightEventHandler().sendRemoveRoomEvent(roomItem.get(i));
                roomItem.remove(i);
                roomList.setItemChecked(i, false);
                roomAdapter.notifyDataSetChanged();

            }
        }
    }

    private void removeLight(){
        ArrayList<RGBLight> removedLights = new ArrayList<>();

        for (RGBLight light:lightsList) {
            if (light.isSelected()) {
                removedLights.add(light);
                KaaManager.getLightEventHandler().sendRemoveLightEvent(light.getId());
            }
        }
        lightsList.removeAll(removedLights);
        adapter.notifyDataSetChanged();

    }

    private void turnlightOn(){
        int counter = roomList.getCount();
        for (int i = counter; i >= 0; i--) {

            if (roomList.isItemChecked(i)) {
            setColor(roomItem.get(i),255,255,225);
            }
        }

    }

    private void turnlightOff(){
        int counter = roomList.getCount();
        for (int i = counter; i >= 0; i--) {

            if (roomList.isItemChecked(i)) {
                setColor(roomItem.get(i),0,0,0);
            }
        }

    }
    private void changeBrightness(){
        for(RGBLight light:lightsList){
            if(light.isSelected()){
                setColor(light.getId(),brightness,brightness,brightness);
            }
        }
    }

    private void changeColor(){
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.change_brightness_layout);
        dialog.setTitle("Neues Licht hinzufügen");


        SeekBar seekbar =(SeekBar) findViewById(R.id.seekBar);
        SeekBar seekbar2 =(SeekBar) findViewById(R.id.seekBar3);
        SeekBar seekbar3 =(SeekBar) findViewById(R.id.seekBar4);

        Button dialogOkButton = (Button) dialog.findViewById(R.id.button2);

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                brightnessRed =progress;

                for(RGBLight light:lightsList){
                    if(light.isSelected()){
                        setColor(light.getId(),brightnessRed,brightnessGreen,brightnessBlue);
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekbar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                brightnessGreen=progress;

                for(RGBLight light:lightsList){
                    if(light.isSelected()){
                        setColor(light.getId(),brightnessRed,brightnessGreen,brightnessBlue);
                    }
                }


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekbar3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                brightnessBlue = progress;
                for (RGBLight light : lightsList) {
                    if (light.isSelected()) {
                        setColor(light.getId(), brightnessRed, brightnessGreen, brightnessBlue);
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        dialogOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });
        dialog.show();

    }


    @Override
    public void update(Observable observable, Object o) {



    }

}