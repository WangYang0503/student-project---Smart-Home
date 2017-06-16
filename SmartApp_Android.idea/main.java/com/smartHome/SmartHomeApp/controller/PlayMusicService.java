package com.smartHome.SmartHomeApp.controller;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.database.ContentObserver;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.smartHome.SmartHomeApp.R;

/**
 * Created by Anja on 26.10.2016.
 * receive the signal to play the alarm tone
 */

public class PlayMusicService extends Service {


    MediaPlayer alarm_song;

    @Nullable
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        alarm_song = MediaPlayer.create(this, R.raw.tetris);
        alarm_song.start();

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }
}
