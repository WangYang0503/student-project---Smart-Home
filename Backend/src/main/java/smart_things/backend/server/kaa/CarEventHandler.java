package smart_things.backend.server.kaa;

import smart_things.backend.server.rules.RuleWorker;
import smart_things.car.schema.*;

/**
 * Objects from this class are able to handle all Kaa events that are received from the smart car. After a event is
 * received, it will be propagated (as far it is a relevant event) to the superclass of this handler that passes
 * the event to the RuleWorker. The RuleWorker then checks if there any rules which get triggered by this event and
 * executes the corresponding actions of the rules in this case.
 * <p>
 * For a description of the different events that can be received have a look at the implementation of the smart
 * thing's Kaa software implementation itself.
 * <p>
 * Created by Jan on 13.01.2017.
 */
public class CarEventHandler extends EventHandler implements CarEventClassFamily.Listener {
    /**
     * Creates a new object that is able to handle events regarding the smart car and to pass them to the rule worker.
     *
     * @param ruleWorker The rule worker to which the received events shall be passed
     */
    public CarEventHandler(RuleWorker ruleWorker) {
        super(ruleWorker);
    }

    @Override
    public void onEvent(InfoResponseEvent infoResponse, String s) {
        propagateEvent(infoResponse, s);
    }

    @Override
    public void onEvent(StartedDrivingEvent startedDriving, String s) {
        propagateEvent(startedDriving, s);
    }

    @Override
    public void onEvent(StoppedDrivingEvent stoppedDriving, String s) {
        propagateEvent(stoppedDriving, s);
    }

    @Override
    public void onEvent(StartedParkingEvent startedParking, String s) {
        propagateEvent(startedParking, s);
    }

    @Override
    public void onEvent(DodgeEvent dodging, String s) {
        propagateEvent(dodging, s);
    }

    @Override
    public void onEvent(AvoidedDeathEvent avoidedDeath, String s) {
        propagateEvent(avoidedDeath, s);
    }

    @Override
    public void onEvent(LostWayEvent lostWay, String s) {
        propagateEvent(lostWay, s);
    }

    @Override
    public void onEvent(InfoRequestEvent infoRequestEvent, String s) {
        propagateEvent(infoRequestEvent, s);

    }

    @Override
    public void onEvent(StartDrivingEvent startDrivingEvent, String s) {
        propagateEvent(startDrivingEvent, s);

    }

    @Override
    public void onEvent(StopDrivingEvent stopDrivingEvent, String s) {
        propagateEvent(stopDrivingEvent, s);

    }

    @Override
    public void onEvent(ParkingPossibleEvent parkingPossibleEvent, String s) {
        propagateEvent(parkingPossibleEvent, s);

    }

    @Override
    public void onEvent(SetSpeedEvent setSpeedEvent, String s) {
        propagateEvent(setSpeedEvent, s);

    }

    @Override
    public void onEvent(ToogleStandbyEvent toogleStandbyEvent, String s) {
        propagateEvent(toogleStandbyEvent, s);

    }
}