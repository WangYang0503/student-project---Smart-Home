package smart_things.backend.server.kaa;

import smart_things.backend.server.rules.RuleWorker;
import smart_things.smoke.schema.*;

/**
 * Objects from this class are able to handle all Kaa events that are received from the smoke detector device.
 * After a event is received, it will be propagated (as far it is a relevant event) to the superclass of this handler
 * that passes the event to the RuleWorker. The RuleWorker then checks if there any rules which get triggered by this
 * event and executes the corresponding actions of the rules in this case.
 * <p>
 * For a description of the different events that can be received have a look at the implementation of the smart
 * thing's Kaa software implementation itself.
 * <p>
 * Created by Jan on 13.01.2017.
 */
public class SmokeEventHandler extends EventHandler implements SmokeEventClassFamily.Listener {
    /**
     * Creates a new object that is able to handle events regarding the smart car and to pass them to the worker.
     *
     * @param ruleWorker The rule worker to which the received events shall be passed
     */
    public SmokeEventHandler(RuleWorker ruleWorker) {
        super(ruleWorker);
    }

    @Override
    public void onEvent(InfoResponseEvent infoResponseEvent, String s) {
        propagateEvent(infoResponseEvent, s);
    }

    @Override
    public void onEvent(smokeDetectionEvent smokeDetectionEvent, String s) {
        propagateEvent(smokeDetectionEvent, s);
    }

    @Override
    public void onEvent(backToNormalEvent backToNormalEvent, String s) {
        propagateEvent(backToNormalEvent, s);
    }

    @Override
    public void onEvent(InfoRequestEvent infoRequestEvent, String s) {
        propagateEvent(infoRequestEvent, s);

    }
}
