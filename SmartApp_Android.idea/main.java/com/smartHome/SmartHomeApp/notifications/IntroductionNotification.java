package com.smartHome.SmartHomeApp.notifications;

import android.app.AlertDialog;
import android.content.Context;

import com.smartHome.SmartHomeApp.gui.AlarmPreference;
import com.smartHome.SmartHomeApp.gui.MainActivity;

/**
 * Created by 4nja on 24.01.2017.
 * introduction when the user visits first time the app
 */

public class IntroductionNotification {

    /**
     * introduction when the user first visits the app
     */

    public static void introductionMainDialog(Context context) {
        android.support.v7.app.AlertDialog.Builder builder2 = new android.support.v7.app.AlertDialog.Builder(context)
                .setTitle("Einleitung")
                .setMessage("Um sich in den KaaProject Server einzuloggen, gehen Sie auf " +
                        " \"Verbindung\" und verbinden Sie sich mit KaaProject Server." +
                        " Geben Sie als Usernamen: KaaProject und Passwort: KaaProject ein." +
                        " Um sich im KaaServer als Adminstrator einzuloggen: Geben Sie als " +
                        "Usernamen: masermo und Passwort: toomanysecrets ein." +
                        " Auf Settings->Change Password kÃ¶nnen Sie das Passwort Ã¤ndern." +
                        " In der Readme Datei finden Sie weitere Hinweise.");

        android.support.v7.app.AlertDialog al = builder2.create();
        al.show();

    }



    public static void introductionAlarmDialog(Context context) {
        android.support.v7.app.AlertDialog.Builder builder2 = new android.support.v7.app.AlertDialog.Builder(context)
                .setTitle("Einleitung")
                .setMessage("Um ein Alarm zu erstellen, müssen sie ein Kaffee eingeben, denn sie" +
                        "erstellen möchten. Ihr Handy muss mindenst die Androidversion KITKAT" +
                        "haben, sonst ist der Alarm nicht lauffähig.");

        android.support.v7.app.AlertDialog al = builder2.create();
        al.show();

    }
}
