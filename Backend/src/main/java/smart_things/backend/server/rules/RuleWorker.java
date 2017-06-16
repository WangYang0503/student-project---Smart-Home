package smart_things.backend.server.rules;

import org.apache.avro.specific.SpecificRecordBase;
import smart_things.backend.server.action.Actions;
import smart_things.backend.server.database.DBManager;
import smart_things.backend.server.main.MultiLogger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.List;
import java.util.Observable;

/**
 * Manages the execution of the rules. Takes the event that was originally called, looks up concerning rules that match
 * to this event, retrieves the action part of the rule (name of the method that should be called) andmexecutes it by
 * calling this action method that was specified in the rule.
 * <p>
 * Created by Michael on 12.01.2017.
 * Updated and implemented by Jan on 15.01.2017
 */
public class RuleWorker extends Observable {

    //Database Manager to communicate with the database
    private DBManager manager = null;
    //Implementation of the available actions the backend can perform
    private Actions actionImplementation = null;

    /**
     * Creates a new rule worker that is able to parse rules after a database manager and a implementation of the
     * actions that the backend can perform are added
     */
    public RuleWorker() {
    }

    /**
     * Creates a new rule worker that is able to parse rules using a given database manager and a implementation of
     * the actions the backend can perform
     *
     * @param manager              The database manager that is able to connect with the database
     * @param actionImplementation A concrete implementation of the actions the backend can perform
     */
    public RuleWorker(DBManager manager, Actions actionImplementation) {
        //Initialize fields with parameters
        this.manager = manager;
        this.actionImplementation = actionImplementation;
    }

    /**
     * Handles a happened event call based on a given event object that was delivered during this call. In detail,
     * the class name of the event is looked up in database for a matching rule. If such a rule exists, die action part
     * of it will be performed though the call of a action method. Represents the "when"-part of the rules.
     *
     * @param event  The event object of the event that was called.
     * @param source String of the source that has sent the event originally
     * @return True, when an action method of a matching rule was called during the handling of the event;
     * false otherwise
     */
    public boolean handleEventCall(SpecificRecordBase event, String source) {
        //Check if current database manager and action implementation are available
        if ((this.manager == null) || (this.actionImplementation == null)) {
            throw new IllegalStateException("Database manager or action implementing are missing!");
        }

        //Check event parameter
        if (event == null) {
            throw new IllegalArgumentException("Illegal event parameter");
        }

        MultiLogger.eventInNotify(event);

        //List for storing the matching rules for this event call
        List<Rule> ruleList = null;

        try {
            //Get possibly existing rules for this call
            ruleList = manager.getRulesByTrigger(event.getClass().getCanonicalName());
        } catch (SQLException e) {
            MultiLogger.severe("Error while handling " + event.getClass().getCanonicalName() + " event: " +
                    e.getMessage());
            return false;
        }
        //Are there no rules?
        if ((ruleList == null) || (ruleList.size() < 1)) {
            return false;
        }

        MultiLogger.info("Found " + ruleList.size() + " matching rule(s)");

        //Iterate over all rules and invoke reactions
        for (Rule rule : ruleList) {
            executeRule(rule);
        }

        //Everything okay, action(s) was/were performed
        return true;
    }

    /**
     * Takes a rule and executes its action as far as the rule is enabled. Assumes, that the rule was triggered before.
     *
     * @param rule The rule to execute
     */
    public void executeRule(Rule rule) {
        //Log the execution
        MultiLogger.infoNotify("Executing rule: " + rule.getName());

        //Check if rule is enabled
        if (rule.isEnabled()) {
            invokeResponseMethodCall(rule.getActionMethod());
        }
    }

    /**
     * Invokes the call of a method, given by its name. Represents the "then"/action part of the rules and takes care
     * that the action is executed.
     *
     * @param methodName The method which should be called as action
     * @return True, if the concerning action was performed successfully; false otherwise
     */
    private boolean invokeResponseMethodCall(String methodName) {
        //Check if current database manager and action implementation are available
        if ((this.manager == null) || (this.actionImplementation == null)) {
            throw new IllegalStateException("Database manager or action implementing are missing!");
        }

        //Class and method objects that correspond to the action that should be performed
        Class actionClass = null;
        Method actionMethod = null;

        //Retrieve the class in which the action method should be called
        actionClass = actionImplementation.getClass();

        //Get the action method with name methodName in the actionClass
        try {
            actionMethod = actionClass.getDeclaredMethod(methodName);
        } catch (NoSuchMethodException e) {
            MultiLogger.severe("Action method " + methodName + " was not found in class " +
                    actionClass.getCanonicalName() + ": " + e.getMessage());
            throw new IllegalArgumentException("Illegal method name");
        }

        MultiLogger.info("Invoking method call: " + actionClass.getCanonicalName() + "::" + actionMethod);

        //Call the method methodName in the action class
        try {
            //Parameter contains the action implementation object on which the action should be performed
            actionMethod.invoke(actionImplementation);
        } catch (IllegalAccessException e) {
            MultiLogger.severe("IllegalAccessException while calling method " + methodName + "in class " +
                    actionClass.getCanonicalName() + ": " + e.getMessage());
            return false;
        } catch (InvocationTargetException e) {
            MultiLogger.severe("InvocationTargetException while calling method " + methodName + " in class " +
                    actionClass.getCanonicalName() + ": " + e.getMessage());
            //Print stacktrace for debug purposes
            e.printStackTrace();

            return false;
        }

        //Everything okay, action was performed
        return true;
    }

    /**
     * Returns the database manager that the rule worker is using to connect to the database
     *
     * @return The database manager
     */
    public DBManager getManager() {
        return manager;
    }

    /**
     * Sets the database manager of the rule worker that is able to connect to the database
     *
     * @param manager The database manager to set
     */
    public void setDBManager(DBManager manager) {
        this.manager = manager;
    }

    /**
     * Returns the action implementation of the rule worker that is used to perform the action ("then") parts of the
     * rules
     *
     * @return The concrete action implementation
     */
    public Actions getActionImplementation() {
        return actionImplementation;
    }

    /**
     * Sets the action implementation of the rule worker that can be used to perform the action ("then") parts of the
     * rules
     *
     * @param actionImplementation The concrete action implementation to set
     */
    public void setActionImplementation(Actions actionImplementation) {
        this.actionImplementation = actionImplementation;
    }
}
