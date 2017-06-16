package smart_things.backend.server.kaa;

import org.apache.avro.specific.SpecificRecordBase;
import smart_things.backend.server.rules.RuleWorker;

/**
 * Makes the basic outline for each event handler class; the concrete event handler classes have to inherit from this
 * class. By using this class it is able to propagate received events to the rule worker.
 * <p>
 * Created by Jan on 13.01.2017.
 */
public abstract class EventHandler {
    //The worker to that the events should be propagated
    protected RuleWorker worker;

    /**
     * Creates a new event handler that is able to handle events and to pass them to the rule worker that looks up
     * matching rules for the received events and executes the corresponding actions.
     *
     * @param worker The rule worker to which the events have to be propagated
     */
    protected EventHandler(RuleWorker worker) {
        this.worker = worker;
    }

    /**
     * Propagates received events and the regarding sender string (source) to the rule worker which looks up matching
     * rules for the received event and executes the corresponding actions.
     *
     * @param event  The event that has to be propagated to the rule worker
     * @param source The source string of the component that has sent the event
     */
    protected void propagateEvent(SpecificRecordBase event, String source) {
        worker.handleEventCall(event, source);
    }
}
