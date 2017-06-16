package smart_things.app.android.gui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import smart_things.app.android.KaaManager.KaaManager;
import smart_things.app.android.R;

/**
 * Created by Lukas on 09.03.2017.
 */

public class ChangeLight extends Activity {

    private EditText inputRoomName;
    private Button btnConfirm;
    private SeekBar redcolor;
    private SeekBar greencolor;
    private SeekBar bluecolor;
    private TextView redcounter, greencounter, bluecounter;

    public int getRedProgress() {
        return redProgress;
    }

    public int getBlueProgress() {
        return blueProgress;
    }

    public int getGreenProgress() {
        return greenProgress;
    }

    private int redProgress;
    private int blueProgress;
    private int greenProgress;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changelight);

        inputRoomName = (EditText) findViewById(R.id.light_room_name);
        redcolor = (SeekBar) findViewById(R.id.light_red_bar);
        greencolor = (SeekBar) findViewById(R.id.light_green_bar);
        bluecolor = (SeekBar) findViewById(R.id.light_blue_bar);
        btnConfirm = (Button) findViewById(R.id.btn_confirm);
        redcounter = (TextView) findViewById(R.id.light_red_counter);
        greencounter = (TextView) findViewById(R.id.light_green_counter);
        bluecounter = (TextView) findViewById(R.id.light_blue_counter);


        //change color red
        redcolor.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                redcounter.setText(" " + progress);
                redProgress = progress;


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        //change color green
        greencolor.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                greencounter.setText(" " + progress);
                greenProgress = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        //change color blue
        bluecolor.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                bluecounter.setText(" " + progress);
                blueProgress = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KaaManager.getLightEventHandler().sendColorEvent(inputRoomName.getText().toString(), getRedProgress(),
                        getGreenProgress(), getBlueProgress());

            }
        });
    }


}
