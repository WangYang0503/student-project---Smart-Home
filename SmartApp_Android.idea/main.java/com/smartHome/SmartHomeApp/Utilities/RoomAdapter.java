package com.smartHome.SmartHomeApp.Utilities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;

import com.smartHome.SmartHomeApp.R;

/**
 * Created by Lia on 07.02.2017.
 */

public class RoomAdapter extends ArrayAdapter<String> {


    public RoomAdapter(Context context,int in) {
        super(context, in);
    }
    public View getView(final int position, View convertView, ViewGroup parent){
        final String room = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.lights_list, parent, false);
        }
        final CheckedTextView name=(CheckedTextView) convertView.findViewById(R.id.checkedTextView1);
        name.setText(room);
        name.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!name.isChecked()) {
                    name.setChecked(true);

                }
                else {
                    name.setChecked(false);

                }

            }
        });
        return convertView;
    }

}
