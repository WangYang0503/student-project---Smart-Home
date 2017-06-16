package smart_things.backend.server.kaa;

import smart_things.backend.server.rules.RuleWorker;
import smart_things.door.schema.*;

/**
 * Objects from this class are able to handle all Kaa events that are received from the door control. After a event is
 * received, it will be propagated (as far it is a relevant event) to the superclass of this handler that passes
 * the event to the RuleWorker. The RuleWorker then checks if there any rules which get triggered by this event and
 * executes the corresponding actions of the rules in this case.
 * <p>
 * For a description of the different events that can be received have a look at the implementation of the smart
 * thing's Kaa software implementation itself.
 * <p>
 * Created by Jan on 13.01.2017.
 */
public class DoorEventHandler extends EventHandler implements DoorEventClassFamily.Listener {
    /**
     * Creates a new object that is able to handle events regarding the door control and to pass them to the rule
     * worker.
     *
     * @param ruleWorker The rule worker to which the received events shall be passed
     */
    public DoorEventHandler(RuleWorker ruleWorker) {
        super(ruleWorker);
    }

    @Override
    public void onEvent(InfoResponseEvent infoResponseEvent, String s) {
        propagateEvent(infoResponseEvent, s);
    }

    @Override
    public void onEvent(DoorClosedEvent doorClosedEvent, String s) {
        propagateEvent(doorClosedEvent, s);
    }

    @Override
    public void onEvent(DoorOpenedEvent doorOpenedEvent, String s) {
        propagateEvent(doorOpenedEvent, s);
    }

    @Override
    public void onEvent(GarageClosedEvent garageClosedEvent, String s) {
        propagateEvent(garageClosedEvent, s);
    }

    @Override
    public void onEvent(GarageOpenedEvent garageOpenedEvent, String s) {
        propagateEvent(garageOpenedEvent, s);
    }

    @Override
    public void onEvent(InfoRequestEvent infoRequestEvent, String s) {
        propagateEvent(infoRequestEvent, s);
    }

    @Override
    public void onEvent(OpenDoorEvent openDoorEvent, String s) {
        propagateEvent(openDoorEvent, s);

    }

    @Override
    public void onEvent(CloseDoorEvent closeDoorEvent, String s) {
        propagateEvent(closeDoorEvent, s);

    }

    @Override
    public void onEvent(OpenGarageEvent openGarageEvent, String s) {
        propagateEvent(openGarageEvent, s);

    }

    @Override
    public void onEvent(CloseGarageEvent closeGarageEvent, String s) {
        propagateEvent(closeGarageEvent, s);

    }
}
