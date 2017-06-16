package smart_things.backend.server.kaa;

import smart_things.backend.server.rules.RuleWorker;
import smart_things.light.schema.*;

/**
 * Objects from this class are able to handle all Kaa events that are received from the light control. After a event is
 * received, it will be propagated (as far it is a relevant event) to the superclass of this handler that passes
 * the event to the RuleWorker. The RuleWorker then checks if there any rules which get triggered by this event and
 * executes the corresponding actions of the rules in this case.
 * <p>
 * For a description of the different events that can be received have a look at the implementation of the smart
 * thing's Kaa software implementation itself.
 * <p>
 * Created by Jan on 13.01.2017.
 */
public class LightEventHandler extends EventHandler implements LightEventClassFamily.Listener {
    /**
     * Creates a new object that is able to handle events regarding the light control and to pass them to the worker.
     *
     * @param ruleWorker The rule worker to which the received events shall be passed
     */
    public LightEventHandler(RuleWorker ruleWorker) {
        super(ruleWorker);
    }

    @Override
    public void onEvent(LEDInfoResponseEvent ledInfoResponse, String s) {
        propagateEvent(ledInfoResponse, s);
    }

    @Override
    public void onEvent(RoomInfoResponseEvent roomInfoResponse, String s) {
        propagateEvent(roomInfoResponse, s);
    }

    @Override
    public void onEvent(LEDInfoRequestEvent ledInfoRequestEvent, String s) {
        propagateEvent(ledInfoRequestEvent, s);

    }

    @Override
    public void onEvent(RoomInfoRequestEvent roomInfoRequestEvent, String s) {
        propagateEvent(roomInfoRequestEvent, s);

    }

    @Override
    public void onEvent(AddRoomEvent addRoomEvent, String s) {
        propagateEvent(addRoomEvent, s);

    }

    @Override
    public void onEvent(RemoveRoomEvent removeRoomEvent, String s) {
        propagateEvent(removeRoomEvent, s);

    }

    @Override
    public void onEvent(AddLightEvent addLightEvent, String s) {
        propagateEvent(addLightEvent, s);

    }

    @Override
    public void onEvent(AddRGBLightEvent addRGBLightEvent, String s) {
        propagateEvent(addRGBLightEvent, s);

    }

    @Override
    public void onEvent(RemoveLightEvent removeLightEvent, String s) {
        propagateEvent(removeLightEvent, s);

    }

    @Override
    public void onEvent(SetBrightnessEvent setBrightnessEvent, String s) {
        propagateEvent(setBrightnessEvent, s);

    }

    @Override
    public void onEvent(SetColorEvent setColorEvent, String s) {
        propagateEvent(setColorEvent, s);

    }

    @Override
    public void onEvent(SetRoomBrightnessEvent setRoomBrightnessEvent, String s) {
        propagateEvent(setRoomBrightnessEvent, s);

    }

    @Override
    public void onEvent(SetRoomColorEvent setRoomColorEvent, String s) {
        propagateEvent(setRoomColorEvent, s);

    }

    @Override
    public void onEvent(SetEmergencyMode setEmergencyMode, String s) {
        propagateEvent(setEmergencyMode, s);

    }
}
