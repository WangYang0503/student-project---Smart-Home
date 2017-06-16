package smart_things.backend.server.action.implementation;

import smart_things.backend.server.action.ActionImplementation;
import smart_things.backend.server.kaa.ECFCollection;
import smart_things.backend.server.main.MultiLogger;
import smart_things.car.schema.ParkingPossibleEvent;
import smart_things.car.schema.StartDrivingEvent;
import smart_things.car.schema.StopDrivingEvent;
import smart_things.coffee.schema.StartBrewingEvent;
import smart_things.door.schema.CloseDoorEvent;
import smart_things.door.schema.CloseGarageEvent;
import smart_things.door.schema.OpenDoorEvent;
import smart_things.door.schema.OpenGarageEvent;
import smart_things.light.schema.SetEmergencyMode;


/**
 * Concrete implementation of the ActionImplementation class. This class is defined by the user who can specify
 * how the different actions as "then"-part of the rules shall be executed. In detail, each method of this class
 * (except the constructors) represents one action that can be chosen to be the "then"-part of a defined rule. The
 * names of the methods (together with the package names using the Canoncial Name syntax) are saved as "action method"
 * in the rules and the database to be called using reflection afterwards.
 * <p>
 * Created by Jan on 16.01.2017.
 * <p>
 * TODO: Implementation for our purposes and rules
 * TODO: Documentation
 */
public class Implementation1 extends ActionImplementation {

    /**
     * Creates a new object of this implementation that can be used to be passed to the RuleWorker. Using this
     * constructor it is necessary to pass Kaa's ECFCollection later by the regarding set method.
     */
    public Implementation1() {
        super();
    }

    /**
     * Creates a new object of this implementation that can be used to be passed to the RuleWorker. It needs Kaa's
     * ECFCollection so that KAA event class families can be used in the different implementations.
     *
     * @param ecfCollection
     */
    public Implementation1(ECFCollection ecfCollection) {
        super(ecfCollection);
    }

    @Override
    public boolean CarStart() {
        StartDrivingEvent event = new StartDrivingEvent();
        MultiLogger.eventOutNotify(event);
        ecfCollection.getTecf_car().sendEventToAll(event);
        return true;
    }

    @Override
    public boolean CarStop() {
        StopDrivingEvent event = new StopDrivingEvent();
        MultiLogger.eventOutNotify(event);
        ecfCollection.getTecf_car().sendEventToAll(event);
        return true;
    }

    //TODO Only for test purposes at the moment
    @Override
    public boolean CoffeemachineBrew() {
        StartBrewingEvent event = new StartBrewingEvent();
        MultiLogger.eventOutNotify(event);
        ecfCollection.getTecf_coffee().sendEventToAll(event);
        return true;
    }

    @Override
    public boolean DoorOpen() {
        OpenDoorEvent event = new OpenDoorEvent();
        MultiLogger.eventOutNotify(event);
        ecfCollection.getTecf_door().sendEventToAll(event);
        return true;
    }

    @Override
    public boolean DoorClose() {
        CloseDoorEvent event = new CloseDoorEvent();
        MultiLogger.eventOutNotify(event);
        ecfCollection.getTecf_door().sendEventToAll(event);
        return true;
    }

    @Override
    public boolean GarageOpen() {
        OpenGarageEvent event = new OpenGarageEvent();
        MultiLogger.eventOutNotify(event);
        ecfCollection.getTecf_door().sendEventToAll(event);
        return true;
    }

    @Override
    public boolean GarageClose() {
        CloseGarageEvent event = new CloseGarageEvent();
        MultiLogger.eventOutNotify(event);
        ecfCollection.getTecf_door().sendEventToAll(event);
        return true;
    }

    @Override
    public boolean ParkingPossible() {
        ParkingPossibleEvent event = new ParkingPossibleEvent();
        MultiLogger.eventOutNotify(event);
        ecfCollection.getTecf_car().sendEventToAll(event);
        return true;
    }

    @Override
    public boolean LightsOff() {
        return false;
    }

    @Override
    public boolean LightsOn() {
        return false;
    }

    @Override
    public boolean LightsEmergency() {
        SetEmergencyMode event = new SetEmergencyMode();
        MultiLogger.eventOutNotify(event);
        event.setIsEmergency(true);
        ecfCollection.getTecf_light().sendEventToAll(event);
        return true;
    }

    @Override
    public boolean LightsRed() {
        return false;
    }

    @Override
    public boolean LightsGreen() {
        return false;
    }

    @Override
    public boolean LightsBlue() {
        return false;
    }
}