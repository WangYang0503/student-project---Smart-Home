package smart_things.app.android.gui.message;

import android.support.design.widget.Snackbar;
import android.util.Log;

import smart_things.app.android.KaaManager.eventHandler.MindstormsEventHandler;
import smart_things.app.android.gui.CarFragment;

/**
 * Created by Alext on 23.02.17.
 */

public class CarMessage {

    private CarFragment carFragment;
    private MindstormsEventHandler mindstormsEventHandler;

    public CarMessage(CarFragment carFragment) {
        this.carFragment = carFragment;
    }

    public void showCarStartedMessage() {
        try {
            final Snackbar snackbar = Snackbar.make(carFragment.getView(), "Car started driving", Snackbar.LENGTH_LONG);
            snackbar.show();
        } catch (Exception e) {
            Log.d("CarMessage.carStarted", "Car started message could not be shown (carFragment.getView() is probably null).");
        }
    }
}
