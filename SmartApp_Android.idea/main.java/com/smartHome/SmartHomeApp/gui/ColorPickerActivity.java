package com.smartHome.SmartHomeApp.gui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.OpacityBar;
import com.larswerkman.holocolorpicker.SVBar;
import com.smartHome.SmartHomeApp.R;
import com.smartHome.SmartHomeApp.Utilities.Light;
import com.smartHome.SmartHomeApp.kaa.KaaManager;

import org.kaaproject.kaa.client.Kaa;

/**
 * Created by Lia on 30.01.2017.
 */

public class ColorPickerActivity extends AppCompatActivity {
    Light light;
    LightsActivity lightsActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
       
        super.onCreate(savedInstanceState);
        setContentView(R.layout.color_picker);


        ColorPicker picker = (ColorPicker) findViewById(R.id.picker);
        SVBar svBar = (SVBar) findViewById(R.id.svbar);
        picker.addSVBar(svBar);


//To get the color
        picker.getColor();

//To set the old selected color u can do it like this
        picker.setOldCenterColor(picker.getColor());
// adds listener to the colorpicker which is implemented
//in the activity

        picker.setOnColorChangedListener(new ColorPicker.OnColorChangedListener() {
            @Override
            public void onColorChanged(int color) {

               // lightsActivity.setColor(light.getId(),color,color,color);

            }
        });

//to turn of showing the old color
        picker.setShowOldCenterColor(false);
        
    }
}
