package org.kaaproject.kaa.demo.device;

import org.kaaproject.kaa.client.DesktopKaaPlatformContext;
import org.kaaproject.kaa.client.Kaa;
import org.kaaproject.kaa.client.KaaClient;
import org.kaaproject.kaa.client.SimpleKaaClientStateListener;
import org.kaaproject.kaa.client.event.EventFamilyFactory;
import org.kaaproject.kaa.client.event.FindEventListenersCallback;
import org.kaaproject.kaa.client.event.registration.UserAttachCallback;
import org.kaaproject.kaa.common.endpoint.gen.SyncResponseResultType;
import org.kaaproject.kaa.common.endpoint.gen.UserAttachResponse;
import org.kaaproject.kaa.schema.coffeemachine.*;
import org.kaaproject.kaa.schema.coffeemachine.coffeemachineeventfamily.Listener;

import javax.swing.*;
import java.util.LinkedList;
import java.util.List;

public class Device {

    private static final String USER_EXTERNAL_ID = "coffee_user";
    private static final String USER_ACCESS_TOKEN = "coffee_user_token";

    private static KaaClient kaaClient;
    private static Gui gui;
    private static coffeemachineeventfamily tecf = null;
    private static EventFamilyFactory eventFamilyFactory = null;

    private static String coffeeStatus;
    private static String coffeeStrength;
    private static String coffeeWhaterLevel;
    private static int coffeeNumberOfCups;

    public static void main(String args[]) {
        gui = new Gui();
        kaaClient = Kaa.newClient(new DesktopKaaPlatformContext(), new SimpleKaaClientStateListener() {
            @Override
            public void onStarted() {
                System.out.println("*****Server started*****");
            }

            @Override
            public void onStopped() {
                System.out.println("*****Server stopped*****");
            }
        });
        kaaClient.start();

        kaaClient.attachUser(USER_EXTERNAL_ID, USER_ACCESS_TOKEN, new UserAttachCallback() {
            @Override
            public void onAttachResult(UserAttachResponse response) {
                if (response.getResult() == SyncResponseResultType.SUCCESS) {
                    System.out.println("User succesfully attached!");
                    onUserAttached();
                } else {
                    System.err.println("Could not attach user!");
                }
            }
        });
    }

    protected static void onUserAttached() {

        eventFamilyFactory = kaaClient.getEventFamilyFactory();
        tecf = eventFamilyFactory.getcoffeemachineeventfamily();

        List<String> listenerFQNs = new LinkedList<>();

        tecf.addListener(new Listener() {

            @Override
            public void onEvent(StopBrewingEvent event, String source) {
                // TODO Auto-generated method stub
                System.out.println("*****EVENT RECEIVED*****");
            }

            @Override
            public void onEvent(StartBrewingEvent event, String source) {
                // TODO Auto-generated method stub
                System.out.println("*****EVENT RECEIVED*****");
            }

            @Override
            public void onEvent(LowWaterEvent event, String source) {
                // TODO Auto-generated method stub
                System.out.println("*****EVENT RECEIVED*****");
                JOptionPane.showMessageDialog(gui.getFrame(), "Der Wasserstand ist niedrig", "Warnung", JOptionPane.WARNING_MESSAGE);
            }

            @Override
            public void onEvent(BrewingFinishedEvent event, String source) {
                // TODO Auto-generated method stub
                System.out.println("*****EVENT RECEIVED*****");
                JOptionPane.showMessageDialog(gui.getFrame(), "Kaffee ist fertig!", " Event received", JOptionPane.INFORMATION_MESSAGE);
            }

            @Override
            public void onEvent(NoWaterEvent event, String source) {
                // TODO Auto-generated method stub
                System.out.println("*****EVENT RECEIVED*****");
                JOptionPane.showMessageDialog(gui.getFrame(), "Kein Wasser vorhanden", "Fehler", JOptionPane.ERROR_MESSAGE);
            }

            @Override
            public void onEvent(NoCarafeEvent event, String source) {
                // TODO Auto-generated method stub
                System.out.println("*****EVENT RECEIVED*****");
                JOptionPane.showMessageDialog(gui.getFrame(), "Keine Karaffe erkannt", "Fehler", JOptionPane.ERROR_MESSAGE);
            }

            @Override
            public void onEvent(TechnicalErrorEvent event, String source) {
                // TODO Auto-generated method stub
                System.out.println("*****EVENT RECEIVED*****");
                JOptionPane.showMessageDialog(gui.getFrame(), "Es ist ein technischer Fehler aufgetreten:\n" + event.getErrorType(), "Fehler", JOptionPane.ERROR_MESSAGE);
            }

            @Override
            public void onEvent(AlreadyBrewingEvent event, String source) {
                // TODO Auto-generated method stub
                System.out.println("*****EVENT RECEIVED*****");
                JOptionPane.showMessageDialog(gui.getFrame(), "Es wird bereits Kaffee gekocht", "Fehler", JOptionPane.ERROR_MESSAGE);
            }

            @Override
            public void onEvent(InfoResponseEvent event, String source) {
                // TODO Auto-generated method stub
                System.out.println("*****EVENT RECEIVED*****");

                InfoObject infoObject = event.getInfoObject();
                coffeeStatus = infoObject.getStatus();
                coffeeNumberOfCups = infoObject.getNumberofCups();
                coffeeWhaterLevel = infoObject.getWaterLevel();
                coffeeStrength = infoObject.getStrength();

                gui.setTfCupCountText(String.valueOf(coffeeNumberOfCups));
                gui.setTfCoffeeStrengthText(coffeeStrength);
                gui.setInfoText(coffeeStatus);
            }
        });

        // Find all the listeners listening to the events from the FQNs list.
        kaaClient.findEventListeners(listenerFQNs, new FindEventListenersCallback() {

            // Perform any necessary actions with the obtained event listeners.
            @Override
            public void onEventListenersReceived(List<String> eventListeners) {
                System.out.println(eventListeners.size() + " event listeners received");
            }

            // Perform any necessary actions in case of failure.
            @Override
            public void onRequestFailed() {
                System.out.println("Request failed");
            }
        });

        // request Status
        sendRequestStateEvent();
    }

    static void sendStartBrewingEvent() {
        tecf.sendEventToAll(new StartBrewingEvent());
    }

    static void sendSetCupCountEvent(int cupCount) {
        tecf.sendEventToAll(new SetCupsEvent(cupCount));
    }

    static void sendSetStrengthEvent(int strength) {
        tecf.sendEventToAll(new SetStrengthEvent(strength));
    }

    static void sendStopBrewingEvent() {
        tecf.sendEventToAll(new StopBrewingEvent());
    }

    static void sendRequestStateEvent() {
        tecf.sendEventToAll(new InfoRequestEvent());
    }

    static void sendToggleBrewingTypeEvent() {
        tecf.sendEventToAll(new ToggleBrewingTypeEvent());
    }
}
