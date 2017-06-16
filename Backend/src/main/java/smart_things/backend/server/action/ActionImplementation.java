package smart_things.backend.server.action;

import smart_things.backend.server.kaa.ECFCollection;

/**
 * Allows to create concrete action method implementations. The subclasses of this class are defined by the user who
 * can specify how the different actions as "then"-part of the rules shall be executed. In detail, each method of this
 * its subclasses represents one action that can be chosen to be the "then"-part of a defined rule. The names of the
 * methods (together with the package names using the Canoncial Name syntax) are saved as "action method" in the rules
 * and database to be called using reflection afterwards.
 * Created by Jan on 16.01.2017.
 */
public abstract class ActionImplementation implements Actions {
    protected ECFCollection ecfCollection;

    /**
     * Creates a new object of this implementation that can be used to be passed to the RuleWorker. Using this
     * constructor it is necessary to pass Kaa's ECFCollection later by the regarding set method.
     */
    public ActionImplementation() {

    }

    /**
     * Creates a new object of this implementation that can be used to be passed to the RuleWorker. It needs Kaa's
     * ECFCollection so that KAA event class families can be used in the different implementations.
     *
     * @param ecfCollection The collection of the event class families (tecf) of the smart things
     */
    public ActionImplementation(ECFCollection ecfCollection) {
        this.ecfCollection = ecfCollection;
    }

    /**
     * Returns the current event class family collection that contains all relevant event class families
     * of the different smart things so that they can be used in the concrete action methods implementations.
     *
     * @return The event class family collection
     */
    public ECFCollection getEcfCollection() {
        return ecfCollection;
    }

    /**
     * Sets the current event class family collection that contains all relevant event class families of the different
     * smart things so that they can be used in the concrete action methods implementations.
     *
     * @param ecfCollection The event class family collection to set
     */
    public void setEcfCollection(ECFCollection ecfCollection) {
        this.ecfCollection = ecfCollection;
    }
}