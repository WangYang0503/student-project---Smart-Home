package smart_things.backend.server.kaa;

import smart_things.backend.server.rules.RuleWorker;
import smart_things.coffee.schema.*;

/**
 * Objects from this class are able to handle all Kaa events that are received from the smart coffee machine. After a
 * event is received, it will be propagated (as far it is a relevant event) to the superclass of this handler that
 * passes the event to the RuleWorker. The RuleWorker then checks if there any rules which get triggered by this event
 * and executes the corresponding actions of the rules in this case.
 * <p>
 * For a description of the different events that can be received have a look at the implementation of the smart
 * thing's Kaa software implementation itself.
 * <p>
 * Created by Jan on 13.01.2017.
 */
public class CoffeeEventHandler extends EventHandler implements CoffeeEventClassFamily.Listener {

    /**
     * Creates a new object that is able to handle events regarding the smart coffee machine and to pass them to the
     * rule worker.
     *
     * @param ruleWorker The rule worker to which the received events shall be passed
     */
    public CoffeeEventHandler(RuleWorker ruleWorker) {
        super(ruleWorker);
    }

    @Override
    public void onEvent(InfoResponseEvent infoResponseEvent, String s) {
        //propagateEvent(infoResponseEvent, s);
    }

    @Override
    public void onEvent(AlreadyBrewingEvent alreadyBrewingEvent, String s) {
        propagateEvent(alreadyBrewingEvent, s);
    }

    @Override
    public void onEvent(TechnicalErrorEvent technicalErrorEvent, String s) {
        propagateEvent(technicalErrorEvent, s);
    }

    @Override
    public void onEvent(NoCarafeEvent noCarafeEvent, String s) {
        propagateEvent(noCarafeEvent, s);
    }

    @Override
    public void onEvent(NoWaterEvent noWaterEvent, String s) {
        propagateEvent(noWaterEvent, s);
    }

    @Override
    public void onEvent(BrewingFinishedEvent brewingFinishedEvent, String s) {
        propagateEvent(brewingFinishedEvent, s);
    }

    @Override
    public void onEvent(LowWaterEvent lowWaterEvent, String s) {
        propagateEvent(lowWaterEvent, s);
    }

    @Override
    public void onEvent(ConnectEvent connectEvent, String s) {
        propagateEvent(connectEvent, s);
    }

    @Override
    public void onEvent(DisconnectEvent disconnectEvent, String s) {
        propagateEvent(disconnectEvent, s);
    }

    @Override
    public void onEvent(InfoRequestEvent infoRequestEvent, String s) {
        propagateEvent(infoRequestEvent, s);

    }

    @Override
    public void onEvent(SetCupsEvent setCupsEvent, String s) {
        propagateEvent(setCupsEvent, s);

    }

    @Override
    public void onEvent(SetStrengthEvent setStrengthEvent, String s) {
        propagateEvent(setStrengthEvent, s);

    }

    @Override
    public void onEvent(SetHotplateTimeEvent setHotplateTimeEvent, String s) {
        propagateEvent(setHotplateTimeEvent, s);

    }

    @Override
    public void onEvent(ToggleBrewingTypeEvent toggleBrewingTypeEvent, String s) {
        propagateEvent(toggleBrewingTypeEvent, s);

    }

    @Override
    public void onEvent(StartBrewingEvent startBrewingEvent, String s) {
        propagateEvent(startBrewingEvent, s);

    }

    @Override
    public void onEvent(StopBrewingEvent stopBrewingEvent, String s) {
        propagateEvent(stopBrewingEvent, s);

    }
}
