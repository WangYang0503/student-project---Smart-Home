package com.smartHome.SmartHomeApp.notifications.CarTrackingNotification;

/**
 * Created by Lia on 08.11.2016.
 * Messages  for the car tracking system only getter and setter
 */

import com.smartHome.SmartHomeApp.kaa.KaaManager;
import com.smartHome.SmartHomeApp.notifications.CarNotifications;

public class CarTrackingMessages {
    private static String startedMessage = "Starting Message: no Information yet";
    private static String parkedMessage = "Parked Message: no Information yet";
    private static String stoppedMessage = "Stopped Message: no Information yet";
    private static String avoidedDeathMessage = "Avoided Death Message: no Information yet";
    private static String dogingMessage = "Doging Message: no Information yet";



    public static String getParkedMessage() {
        return parkedMessage;
    }

    public static void setParkedMessage(String parkedMessage) {
        CarTrackingMessages.parkedMessage= parkedMessage;


    }

    public static String getStoppedMessage() {
        return stoppedMessage;
    }

    public static void setStoppedMessage(String stoppedMessage) {
        CarTrackingMessages.stoppedMessage= stoppedMessage;
    }


    public static String getAvoidedDeathMessage() {
        return avoidedDeathMessage;
    }

    public static void setAvoidedDeathMessage(String avoidedDeathMessage) {
        CarTrackingMessages.avoidedDeathMessage= avoidedDeathMessage;
    }

    public static String getStartedMessage() {
        return startedMessage;
    }

    public static void setStartedMessage(String startedMessage) {
        CarTrackingMessages.startedMessage = startedMessage;

    }

    public static String getDogingMessage() {
        return dogingMessage;
    }

    public static void setDogingMessage(String dogingMessage) {
        CarTrackingMessages.dogingMessage = dogingMessage;
    }
}
