package com.smartHome.SmartHomeApp.kaa.eventHandler;

import android.app.Activity;
import android.util.Log;
import android.widget.TextView;

import com.smartHome.SmartHomeApp.R;
import com.smartHome.SmartHomeApp.gui.CoffeeActivity;
import com.smartHome.SmartHomeApp.gui.MainActivity;
import com.smartHome.SmartHomeApp.gui.SmokeDetectorActivity;
import com.smartHome.SmartHomeApp.kaa.KaaManager;
import com.smartHome.SmartHomeApp.notifications.CoffeeNotifications;
import com.smartHome.SmartHomeApp.notifications.SmokeDetectorNotifications;

import org.kaaproject.kaa.client.KaaClient;
import org.kaaproject.kaa.client.event.EventFamilyFactory;
import org.kaaproject.kaa.client.event.FindEventListenersCallback;

//import org.kaaproject.kaa.schema.coffeemachine.AlreadyBrewingEvent;
//import org.kaaproject.kaa.schema.coffeemachine.BrewingFinishedEvent;
//import org.kaaproject.kaa.schema.coffeemachine.ConnectEvent;
//import org.kaaproject.kaa.schema.coffeemachine.DisconnectEvent;
//import org.kaaproject.kaa.schema.coffeemachine.InfoObject;
//import org.kaaproject.kaa.schema.coffeemachine.InfoRequest;
//import org.kaaproject.kaa.schema.coffeemachine.InfoResponseEvent;
//import org.kaaproject.kaa.schema.coffeemachine.LowWaterEvent;
//import org.kaaproject.kaa.schema.coffeemachine.NoCarafeEvent;
//import org.kaaproject.kaa.schema.coffeemachine.NoWaterEvent;
//import org.kaaproject.kaa.schema.coffeemachine.SetCupsEvent;
//import org.kaaproject.kaa.schema.coffeemachine.SetHotplateTimeEvent;
//import org.kaaproject.kaa.schema.coffeemachine.SetStrengthEvent;
//import org.kaaproject.kaa.schema.coffeemachine.StartBrewingEvent;
//import org.kaaproject.kaa.schema.coffeemachine.StatusTypes;
//import org.kaaproject.kaa.schema.coffeemachine.StopBrewingEvent;
//import org.kaaproject.kaa.schema.coffeemachine.StrengthTypes;
//import org.kaaproject.kaa.schema.coffeemachine.TechnicalErrorEvent;
//import org.kaaproject.kaa.schema.coffeemachine.ToggleBrewingTypeEvent;
//import org.kaaproject.kaa.schema.coffeemachine.coffeemachineeventfamily;


import smart_things.smoke.schema.SmokeEventClassFamily;
import smart_things.smoke.schema.backToNormalEvent;
import smart_things.smoke.schema.InfoRequestEvent;
import smart_things.smoke.schema.InfoResponseEvent;
import smart_things.smoke.schema.smokeDetectionEvent;

import java.util.LinkedList;
import java.util.List;



/**
 * Created by Lukas on 27.12.16.
 * Smokedetector events
 */
public class SmokeDetectorEventHandler {

    private KaaClient kaaClient;
    private SmokeEventClassFamily tecf;
    private TextView tvSmokeDetector;

    /**
     * status of the coffeeMachine, contains information of
     * the current state, the water level, the current selected brewing config, etc
     */

    private static volatile boolean smokeDetected = false;

    /**
     * constructor
     *
     * @param kaaClient
     */
    public SmokeDetectorEventHandler(KaaClient kaaClient) {
        this.kaaClient = kaaClient;

        EventFamilyFactory eventFamilyFactory = kaaClient.getEventFamilyFactory();
        tecf = eventFamilyFactory.getSmokeEventClassFamily();

        List<String> listenerFQNs = new LinkedList<>();


        tecf.addListener(new SmokeEventClassFamily.Listener() {

            //Message Low Water
            @Override
            public void onEvent(smokeDetectionEvent event, String source) {
                Log.d("SmokeDetectorEvHand", "*****smokeDetectionEvent RECEIVED*****");
                new SmokeDetectorNotifications().smokeDetectedNotification();
                updateButtonStatusSmoke();
                smokeDetected = true;
            }

            public void onEvent(backToNormalEvent event, String source) {
                Log.d("CoffeeMachineEvHand", "*****backToNormalEvent RECEIVED*****");
                new SmokeDetectorNotifications().backToNormalNotification();
                updateButtonStatusNormal();
                smokeDetected = false;
            }

            // Message Info Response
            @Override
            public void onEvent(InfoResponseEvent event, String source) {
                Log.d("CoffeeMachineEvHand", "*****InfoResponseEvent RECEIVED*****");


            }
        });

        // Find all the listeners listening to the events from the FQNs list.
        kaaClient.findEventListeners(listenerFQNs, new FindEventListenersCallback() {
            // Perform any necessary actions with the obtained event listeners.
            @Override
            public void onEventListenersReceived(List<String> eventListeners) {
                Log.d("SmokeDetectorEvHand", eventListeners.size() + " event listeners received");
            }

            // Perform any necessary actions in case of failure.
            @Override
            public void onRequestFailed() {
                Log.d("SmokeDetectorEvHand", "Request failed");
            }
        });
    }

//
      public void sendRequestEvent() {
        tecf.sendEventToAll(new InfoRequestEvent());
      }
//
//

    /**
     * getter of the SmokeDetector status
     *
     * @return Status of the Smoke Detector, true for smokeDetected
     */
    public boolean getSmokeDetectorStatus() {
        return smokeDetected;
    }
//
//    /**
//     * updates the water level display if coffee activity is currently active
//     */
//    private void updateCoffeeMachineStatus() {
//        final Activity activity = KaaManager.getActivity();
//        if (activity instanceof CoffeeActivity) {
//            try {
//                activity.runOnUiThread(new Runnable() {
//                    public void run() {
//                        ((CoffeeActivity) activity).updateWaterLvlDisplay();
//                        ((CoffeeActivity) activity).updateRadioButtons();
//                    }
//                });
//            } catch (NullPointerException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    /*
     * enables and disable the [radio]buttons in the coffeeActivity
     * according to the brewing state of the coffeeMachine
     *
     * @param isBrewing true, if the coffeeMachine is currently brewing, false otherwise
     */
    private void updateButtonStatusSmoke() {
        final Activity activity = KaaManager.getActivity();
        if (activity instanceof MainActivity) {
            try {
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        ((MainActivity) activity).setSmokeButton();
                    }
                });
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateButtonStatusNormal() {
        final Activity activity = KaaManager.getActivity();
        if (activity instanceof MainActivity) {
            try {
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        ((MainActivity) activity).setSmokeButton();
                    }
                });
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

//    /**
//     * enable coffee button in mainActivity is mainActivity is currently active
//     */
//    private void enableCoffeeButtonIfIsActive() {
//        final Activity activity = KaaManager.getActivity();
//        if (activity instanceof MainActivity) {
//            try {
//                activity.runOnUiThread(new Runnable() {
//                    public void run() {
//                        ((MainActivity) activity).updateButtonEnableState();
//                    }
//                });
//            } catch (NullPointerException e) {
//                e.printStackTrace();
//            }
//        }
//    }
}