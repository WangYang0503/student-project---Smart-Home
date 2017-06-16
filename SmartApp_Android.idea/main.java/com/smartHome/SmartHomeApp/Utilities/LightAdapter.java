package com.smartHome.SmartHomeApp.Utilities;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.BoolRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;

import com.smartHome.SmartHomeApp.R;
import com.smartHome.SmartHomeApp.gui.LightsActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lia on 24.01.2017.
 */

public class LightAdapter extends ArrayAdapter<RGBLight> {


    private Boolean itemChecked;


    public LightAdapter(Context context, ArrayList<RGBLight> lightList) {
        super(context,0, lightList);
    }



    public View getView(final int position, View convertView, ViewGroup parent){
        final RGBLight light = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.lights_list, parent, false);
        }
        final CheckedTextView name=(CheckedTextView) convertView.findViewById(R.id.checkedTextView1);
        name.setText(light.getId());
        name.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!light.isSelected()) {
                    name.setChecked(true);
                    light.setSelected(true);
                }
            else {
                    name.setChecked(false);
                    light.setSelected(false);

                }

            }
        });
        return convertView;
    }



    public Boolean getItemChecked() {
        return itemChecked;
    }

    public void setItemChecked(Boolean itemChecked) {
        this.itemChecked = itemChecked;
    }


}