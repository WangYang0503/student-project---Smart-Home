package com.smartHome.SmartHomeApp.gui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.smartHome.SmartHomeApp.R;

/**
 * The imprint class opens the imprint layout
 * Created by Lia on 29.08.2016.
 */
public class ImpActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.impressum_layout);
    }
}
