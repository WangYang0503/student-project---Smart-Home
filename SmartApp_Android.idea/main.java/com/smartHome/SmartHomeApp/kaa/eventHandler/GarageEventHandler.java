package com.smartHome.SmartHomeApp.kaa.eventHandler;

import android.app.Activity;
import android.util.Log;

import com.smartHome.SmartHomeApp.gui.GarageActivity;
import com.smartHome.SmartHomeApp.gui.MainActivity;
import com.smartHome.SmartHomeApp.kaa.KaaManager;

import org.kaaproject.kaa.client.KaaClient;
import org.kaaproject.kaa.client.event.EventFamilyFactory;


import java.util.LinkedList;
import java.util.List;

import smart_things.door.schema.CloseDoorEvent;
import smart_things.door.schema.CloseGarageEvent;
import smart_things.door.schema.DoorClosedEvent;
import smart_things.door.schema.DoorEventClassFamily;
import smart_things.door.schema.DoorOpenedEvent;
import smart_things.door.schema.GarageClosedEvent;
import smart_things.door.schema.GarageOpenedEvent;
import smart_things.door.schema.InfoObject;
import smart_things.door.schema.InfoRequestEvent;
import smart_things.door.schema.InfoResponseEvent;
import smart_things.door.schema.OpenDoorEvent;
import smart_things.door.schema.OpenGarageEvent;

/**
 * Created by Lia on 13.11.2016.
 * door opener (garage) events and handlers
 * all events imported from kaa
 * With the methods from this class you can send and receive the events from / to kaa
 */

public class GarageEventHandler {

    private KaaClient kaaClient;
    private DoorEventClassFamily tecf;
    private InfoObject doorAndGarageStatus = null;


    public GarageEventHandler(KaaClient kaaClient) {
        this.kaaClient = kaaClient;

        EventFamilyFactory eventFamilyFactory = kaaClient.getEventFamilyFactory();
        tecf = eventFamilyFactory.getDoorEventClassFamily();
        List<String> listenerFQNs = new LinkedList<>();

        tecf.addListener(new DoorEventClassFamily.Listener() {
            @Override
            public void onEvent(InfoResponseEvent infoResponseEvent, String s) {
                Log.d("InfoResponseEvent", "***Info Response***");
                doorAndGarageStatus = infoResponseEvent.getInfoObject();
                //updateGarageStatus();
                //enableGarageButtonIfIsActive();
            }

            @Override
            public void onEvent(DoorClosedEvent doorClosedEvent, String s) {
                Log.d("DoorClosedEvent", "***Door Closed***");
            }

            @Override
            public void onEvent(DoorOpenedEvent doorOpenedEvent, String s) {
                Log.d("DoorOpenedEvent", "***Door Opened***");
            }

            @Override
            public void onEvent(GarageClosedEvent garageClosedEvent, String s) {
                Log.d("GarageClosedEvent", "***Garage Closed***");
                final Activity currentActivity = KaaManager.getActivity();
                if (currentActivity instanceof GarageActivity) {
                    currentActivity.runOnUiThread(new Runnable() {
                        public void run() {
                            ((GarageActivity) currentActivity).garageStopped(false);
                        }
                    });
                }
            }

            @Override
            public void onEvent(GarageOpenedEvent garageOpenedEvent, String s) {
                Log.d("GarageOpenedEvent", "***Garage Opened***");
                final Activity currentActivity = KaaManager.getActivity();
                if (currentActivity instanceof GarageActivity) {
                    currentActivity.runOnUiThread(new Runnable() {
                        public void run() {
                            ((GarageActivity) currentActivity).garageStopped(true);
                        }
                    });
                }
            }
        });
    }

    public void sendOpenDoor() {
        tecf.sendEventToAll(new OpenDoorEvent());

    }

    public void sendCloseDoor() {
        tecf.sendEventToAll(new CloseDoorEvent());
    }

    public void sendCloseGarage() {
        tecf.sendEventToAll(new CloseGarageEvent());
    }

    public void sendOpenGarage() {
        tecf.sendEventToAll(new OpenGarageEvent());
    }

    public boolean garageIsOpen() {
        return doorAndGarageStatus.getGarageOpened();
    }

    public boolean doorIsOpen() {
        return doorAndGarageStatus.getDoorOpened();
    }

    public InfoObject getDoorAndGarageStatus() {
        return doorAndGarageStatus;
    }

    public void sendInfoRequestEvent() {
        tecf.sendEventToAll(new InfoRequestEvent());
    }

    /**
     * enable garage button in mainActivity is mainActivity is currently active
     */
    private void enableGarageButtonIfIsActive() {
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

    private void updateGarageStatus() {
        final Activity activity = KaaManager.getActivity();
        if (activity instanceof GarageActivity)
            try {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((GarageActivity) activity).updateButtons();
                    }
                });
            } catch (NullPointerException e) {

            }
    }
}