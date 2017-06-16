package smart_things.app.android.gui.message;

import android.content.Context;

/**
 * Created by Anja on 24.01.2017.
 * introduction when the user visits first time the app
 */

public class IntroductionNotification {

    /**
     * introduction when the user first visits the app
     */

    public static void introductionMainDialog(Context context) {
        android.support.v7.app.AlertDialog.Builder builder2 = new android.support.v7.app.AlertDialog.Builder(context)
                .setTitle("Einleitung")
                .setMessage("Um sich in den SmartHome Server einzuloggen, müssen Sie sich mit dem" +
                        " WLAN SmartHome verbinden." +
                        " Geben Sie als Usernamen: SmartHome und Passwort: SmartHome ein." +
                        "Auf der Seitenleiste der App unter Kaa-Adminstration können sie sich im SmartHome Server " +
                        "als Adminstrator einloggen."+
                        " Unter Settings->Change Password können Sie ihr Passwort ändern." +
                        " In der Readme Datei finden Sie weitere Hinweise.");

        android.support.v7.app.AlertDialog al = builder2.create();
        al.show();

    }
    
}
