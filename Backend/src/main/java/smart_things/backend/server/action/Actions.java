package smart_things.backend.server.action;

/**
 * This interface defines all the actions the user can choose to be executed by rules as "then"part. It is used by
 * the ActionImplementation class that is inherited by the subclasses which really define the concrete actions the
 * backend shall trigger in case of that a rule has to be executed.
 * <p>
 * Created by Michael on 12.01.2017.
 * TODO: Extension and completion as far as necessary
 * TODO: Documentation
 */
public interface Actions {
    /**
     * @return
     */
    boolean CarStart();

    /**
     * @return
     */
    boolean CarStop();

    /**
     * @return
     */
    boolean CoffeemachineBrew();

    /**
     * @return
     */
    boolean DoorOpen();

    /**
     * @return
     */
    boolean DoorClose();

    /**
     * @return
     */
    boolean GarageOpen();

    /**
     * @return
     */
    boolean GarageClose();

    /**
     * @return
     */
    boolean LightsOff();

    /**
     * @return
     */
    boolean LightsOn();

    /**
     * @return
     */
    boolean LightsEmergency();

    /**
     * @return
     */
    boolean LightsRed();

    /**
     * @return
     */
    boolean LightsGreen();

    /**
     * @return
     */
    boolean ParkingPossible();

    /**
     * @return
     */
    boolean LightsBlue();
}

