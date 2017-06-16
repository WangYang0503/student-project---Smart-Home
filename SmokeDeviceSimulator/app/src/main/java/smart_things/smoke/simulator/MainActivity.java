package smart_things.smoke.simulator;

import android.app.Activity;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.kaaproject.kaa.client.Kaa;

import smart_things.smoke.simulator.R;
import smart_things.smoke.simulator.kaaManager.KaaManager;
import smart_things.smoke.simulator.kaaManager.eventHandler.SmokeDeviceEventHandler;

public class MainActivity extends AppCompatActivity {

    private static MainActivity ourInstance;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ourInstance = this;

        final Button buttonStart = (Button)findViewById(R.id.button);
        final MediaPlayer warningMusic = MediaPlayer.create(this
                ,R.raw.smokersound);
        KaaManager.connectToKaa();

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(warningMusic.isPlaying()){
                    warningMusic.pause();
                    warningMusic.seekTo(0);
                    buttonStart.setBackgroundColor(Color.GRAY);
                    KaaManager.getSmokeDeviceEventHandler().sendBackToNormalEvent();
                }
                else {
                    buttonStart.setBackgroundColor(Color.RED);
                    warningMusic.start();
                    warningMusic.setLooping(true);
                    KaaManager.getSmokeDeviceEventHandler().sendSmokeDetectionEvent();
                }

            }
        });
    }

    public static MainActivity getInstance() {
        return ourInstance;
    }

}
