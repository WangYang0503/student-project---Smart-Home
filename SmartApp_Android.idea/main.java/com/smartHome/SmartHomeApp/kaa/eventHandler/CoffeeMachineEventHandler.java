package com.smartHome.SmartHomeApp.kaa.eventHandler;

import android.app.Activity;
import android.util.Log;

import com.smartHome.SmartHomeApp.gui.CoffeeActivity;
import com.smartHome.SmartHomeApp.gui.MainActivity;
import com.smartHome.SmartHomeApp.kaa.KaaManager;
import com.smartHome.SmartHomeApp.notifications.CoffeeNotifications;

import org.kaaproject.kaa.client.KaaClient;
import org.kaaproject.kaa.client.event.EventFamilyFactory;
import org.kaaproject.kaa.client.event.FindEventListenersCallback;

import java.util.LinkedList;
import java.util.List;

import smart_things.coffee.schema.AlreadyBrewingEvent;
import smart_things.coffee.schema.BrewingFinishedEvent;
import smart_things.coffee.schema.CoffeeEventClassFamily;
import smart_things.coffee.schema.ConnectEvent;
import smart_things.coffee.schema.DisconnectEvent;
import smart_things.coffee.schema.InfoObject;
import smart_things.coffee.schema.InfoRequestEvent;
import smart_things.coffee.schema.InfoResponseEvent;
import smart_things.coffee.schema.LowWaterEvent;
import smart_things.coffee.schema.NoCarafeEvent;
import smart_things.coffee.schema.NoWaterEvent;
import smart_things.coffee.schema.SetCupsEvent;
import smart_things.coffee.schema.SetHotplateTimeEvent;
import smart_things.coffee.schema.SetStrengthEvent;
import smart_things.coffee.schema.StartBrewingEvent;
import smart_things.coffee.schema.StatusTypes;
import smart_things.coffee.schema.StopBrewingEvent;
import smart_things.coffee.schema.StrengthTypes;
import smart_things.coffee.schema.TechnicalErrorEvent;
import smart_things.coffee.schema.ToggleBrewingTypeEvent;

/**
 * Created by Alex on 31.08.16.
 * coffeeMachine events and handlers
 * all events imported from kaa
 * With the methods from this class you can send and receive the events from / to kaa
 */
public class CoffeeMachineEventHandler {

    private KaaClient kaaClient;
    private CoffeeEventClassFamily tecf;

    /**
     * status of the coffeeMachine, contains information of
     * the current state, the water level, the current selected brewing config, etc
     */
    private static volatile InfoObject coffeeStatus = null;

    /**
     * constructor
     *
     * @param kaaClient
     */
    public CoffeeMachineEventHandler(KaaClient kaaClient) {
        this.kaaClient = kaaClient;

        EventFamilyFactory eventFamilyFactory = kaaClient.getEventFamilyFactory();
        tecf = eventFamilyFactory.getCoffeeEventClassFamily();

        final List<String> FQNs = new LinkedList<>();
        FQNs.add(CoffeeMachineEventHandler.class.getName());

        kaaClient.findEventListeners(FQNs, new FindEventListenersCallback() {
            @Override
            public void onEventListenersReceived(List<String> eventListeners) {
                // Some code
                FQNs.addAll(eventListeners);
                System.out.println();
            }

            @Override
            public void onRequestFailed() {
                // Some code
            }
        });

        List<String> listenerFQNs = new LinkedList<>();

        tecf.addListener(new CoffeeEventClassFamily.Listener() {

            //Message Low Water
            @Override
            public void onEvent(LowWaterEvent event, String source) {
                Log.d("CoffeeMachineEvHand", "*****LowWaterEvent RECEIVED*****");
                CoffeeNotifications.LowWaterNotification();
            }

            @Override
            public void onEvent(ConnectEvent connectEvent, String s) {

            }

            @Override
            public void onEvent(DisconnectEvent disconnectEvent, String s) {

            }

            @Override
            public void onEvent(StartBrewingEvent startBrewingEvent, String s) {

            }

            @Override
            public void onEvent(StopBrewingEvent stopBrewingEvent, String s) {

            }

            //Message brewing finished
            @Override
            public void onEvent(BrewingFinishedEvent event, String source) {
                Log.d("CoffeeMachineEvHand", "*****BrewingFinishedEvent RECEIVED*****");
                CoffeeNotifications.cookingFinishedNotification();
            }

            //Message  no water
            @Override
            public void onEvent(NoWaterEvent event, String source) {
                Log.d("CoffeeMachineEvHand", "*****NoWaterEvent RECEIVED*****");
                CoffeeNotifications.noWaterDialog();
            }

            // Message no carafe
            @Override
            public void onEvent(NoCarafeEvent event, String source) {
                Log.d("CoffeeMachineEvHand", "*****NoCarafeEvent RECEIVED*****");
                CoffeeNotifications.noCarafeDialog();
            }

            // Message technical Error
            @Override
            public void onEvent(TechnicalErrorEvent event, String source) {
                Log.d("CoffeeMachineEvHand", "*****TechnicalErrorEvent RECEIVED*****");

            }

            //Message already Brewing
            @Override
            public void onEvent(AlreadyBrewingEvent event, String source) {
                Log.d("CoffeeMachineEvHand", "*****AlreadyBrewingEvent RECEIVED*****");
                CoffeeNotifications.alreadyBrewing();
            }

            // Message Info Response
            @Override
            public void onEvent(InfoResponseEvent event, String source) {
                Log.d("CoffeeMachineEvHand", "*****InfoResponseEvent RECEIVED*****");

                coffeeStatus = event.getInfoObject();

                // if coffee activity is currently active, set the enable state of the radioButtons
                // and the start/stop brewing Buttons corresponding to the current brewing state
                // (if there is currently a coffee brewing or not)
                boolean isBrewing = event.getInfoObject().getStatus() == StatusTypes.BREWING
                        || event.getInfoObject().getStatus() == StatusTypes.GRINDING;
                updateButtonEnableState(isBrewing);

                // if coffee activity is currently active, update the display if the water level
                // NOTE:  since this is executed every time the water level changes,
                // there is no need to call this if e.g. the brewing is finished.
                updateCoffeeMachineStatus();

                // if Main activity is currently active, enable the coffee button
                enableCoffeeButtonIfIsActive();

            }
        });

        // Find all the listeners listening to the events from the FQNs list.
        kaaClient.findEventListeners(listenerFQNs, new FindEventListenersCallback() {
            // Perform any necessary actions with the obtained event listeners.
            @Override
            public void onEventListenersReceived(List<String> eventListeners) {
                Log.d("CoffeeMachineEvHand", eventListeners.size() + " event listeners received");
            }

            // Perform any necessary actions in case of failure.
            @Override
            public void onRequestFailed() {
                Log.d("CoffeeMachineEvHand", "Request failed");
            }
        });
    }

    public void sendStartBrewingEvent() {
        tecf.sendEventToAll(new StartBrewingEvent());

    }

    public void sendSetCupCountEvent(int cupCount) {
        tecf.sendEventToAll(new SetCupsEvent(cupCount));
    }

    public void sendSetStrengthEvent(StrengthTypes strength) {
        tecf.sendEventToAll(new SetStrengthEvent(strength));
    }

    public void sendStopBrewingEvent() {
        tecf.sendEventToAll(new StopBrewingEvent());
        CoffeeNotifications.stopBrewingDialog();
    }

    public void sendRequestStateEvent() {
        tecf.sendEventToAll(new InfoRequestEvent());
    }

    public void sendToggleBrewingTypeEvent() {
        tecf.sendEventToAll(new ToggleBrewingTypeEvent());
    }

    public void sendHotPlateTypeEvent(int hotplateTime) {
        tecf.sendEventToAll(new SetHotplateTimeEvent(hotplateTime));
    }

    /**
     * getter of the coffeeMachine status
     *
     * @return the InfoObject of the coffeeMachine
     */
    public InfoObject getCoffeeStatus() {
        return coffeeStatus;
    }

    /**
     * updates the water level display if coffee activity is currently active
     */
    private void updateCoffeeMachineStatus() {
        final Activity activity = KaaManager.getActivity();
        if (activity instanceof CoffeeActivity) {
            try {
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        ((CoffeeActivity) activity).updateWaterLvlDisplay();
                        ((CoffeeActivity) activity).updateRadioButtons();
                    }
                });
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * enables and disable the [radio]buttons in the coffeeActivity
     * according to the brewing state of the coffeeMachine
     *
     * @param isBrewing true, if the coffeeMachine is currently brewing, false otherwise
     */
    private void updateButtonEnableState(final boolean isBrewing) {
        final Activity activity = KaaManager.getActivity();
        if (activity instanceof CoffeeActivity) {
            try {
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        ((CoffeeActivity) activity).setRadioButtonsEnableState(!isBrewing);
                        ((CoffeeActivity) activity).setCookingButtonsEnableState(isBrewing);
                    }
                });
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * enable coffee button in mainActivity is mainActivity is currently active
     */
    private void enableCoffeeButtonIfIsActive() {
        final Activity activity = KaaManager.getActivity();
        if (activity instanceof MainActivity) {
            try {
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        ((MainActivity) activity).updateButtonEnableState();
                    }
                });
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }
}