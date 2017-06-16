package com.smartHome.SmartHomeApp.kaa.eventHandler;

import android.util.Log;

import com.smartHome.SmartHomeApp.notifications.CarNotifications;
import com.smartHome.SmartHomeApp.notifications.CarTrackingNotification.CarTrackingMessages;

import org.kaaproject.kaa.client.KaaClient;
import org.kaaproject.kaa.client.event.EventFamilyFactory;
import org.kaaproject.kaa.client.event.FindEventListenersCallback;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import smart_things.car.schema.AvoidedDeathEvent;
import smart_things.car.schema.CarEventClassFamily;
import smart_things.car.schema.DodgeEvent;
import smart_things.car.schema.InfoResponseEvent;
import smart_things.car.schema.LostWayEvent;
import smart_things.car.schema.StartDrivingEvent;
import smart_things.car.schema.StartedDrivingEvent;
import smart_things.car.schema.StartedParkingEvent;
import smart_things.car.schema.StopDrivingEvent;
import smart_things.car.schema.StoppedDrivingEvent;

/**
 * Created by Jan on 12.09.16.
 * mindstorms (simulated car) events and handlers
 * all events imported from kaa
 * With the methods from this class you can send and receive the events from / to kaa
 */
public class MindstormsEventHandler {

    private KaaClient kaaClient;
    private CarEventClassFamily tecf;
    private boolean driving = false;

    public MindstormsEventHandler(KaaClient kaaClient) {
        this.kaaClient = kaaClient;

        EventFamilyFactory eventFamilyFactory = kaaClient.getEventFamilyFactory();
        tecf = eventFamilyFactory.getCarEventClassFamily();

        List<String> listenerFQNs = new LinkedList<>();

        tecf.addListener(new CarEventClassFamily.Listener() {

            @Override
            public void onEvent(InfoResponseEvent infoResponse, String s) {

            }

            @Override
            public void onEvent(StartedDrivingEvent startedDriving, String s) {
                Log.d("CarStart","***Started driving***");
                CarNotifications.StartDrivingNotification();
                CarNotifications.carStartedMessage();
            }

            @Override
            public void onEvent(StoppedDrivingEvent stoppedDriving, String s) {
                Log.d("CarStop","***Stopped Driving***");
                CarNotifications.StopDrivingNotification();
                CarNotifications.carToppedMessage();
            }

            @Override
            public void onEvent(StartedParkingEvent startedParking, String s) {
                Log.d("CarStart","***Started Pariking***");
               CarNotifications.StartParkNotification();
                CarNotifications.carParkedMessage();
            }

            @Override
            public void onEvent(DodgeEvent dodging, String s) {
               Log.d("CarDogged","***Car Dogged***");
                CarNotifications.dogingNotification();
                CarNotifications.carDoggedMessage();
            }

            @Override
            public void onEvent(AvoidedDeathEvent avoidedDeath, String s) {
               Log.d("AvoidDeath","*** Car Turned***");
                CarNotifications.avoidedDeathNotification();
                CarNotifications.carAvoidedDeatMessage();

            }

            @Override
            public void onEvent(LostWayEvent lostWay, String s) {
                Log.d("LostWay","*** Car lost its way***");
                CarNotifications.lostWayNotification();

            }
        });

        // Find all the listeners listening to the events from the FQNs list.
        kaaClient.findEventListeners(listenerFQNs, new FindEventListenersCallback() {
            // Perform any necessary actions with the obtained event listeners.
            @Override
            public void onEventListenersReceived(List<String> eventListeners) {
                Log.d("MindstormEvHand", eventListeners.size() + " event listeners received");
            }

            // Perform any necessary actions in case of failure.
            @Override
            public void onRequestFailed() {
                Log.d("MindstormEvHand", "Request failed");
            }
        });
    }

    public void sendStartDrivingEvent(){
        tecf.sendEventToAll(new StartDrivingEvent());
        setDriving(true);

    }
    public void sendStopDrivingEvent(){
        tecf.sendEventToAll(new StopDrivingEvent());
        setDriving(false);

    }


    public boolean isDriving() {
        return driving;
    }

    public void setDriving(boolean driving) {
        this.driving = driving;
    }
}