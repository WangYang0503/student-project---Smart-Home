package smart_things.backend.server.rules;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.Timestamp;

/**
 * Using this class it is possible to create Rule objects that specify a rule name, the name of the trigger class that
 * triggers the rule when the event class is fired, the action method and its class name, that should be performed when
 * the rule gets triggered, the current enabled of the rule (Enabled/Disabled) and the timestamp when the rule was
 * created.
 * <p>
 * This class is layered with Annotations from an ORMLite Data Access Object so that it is possible to map objects
 * from this class to SQL entities and to store them in an SQLite database table named 'rules'.
 * <p>
 * Created by Jan on 13.01.2017.
 */
@DatabaseTable(tableName = "rules")
public class Rule {
    //Unique name of the rule (Primary Key)
    @DatabaseField(canBeNull = false, id = true)
    private String name = null;
    //Name of the class that triggers the execution of the rule
    @DatabaseField(canBeNull = false, columnName = "trigger")
    private String triggerClass = null;
    //Name of the method in the action class that performs the action as result of the rule
    @DatabaseField(canBeNull = false, columnName = "action")
    private String actionMethod = null;
    //State of the rule (Enabled/true or Disabled/false)
    @DatabaseField(canBeNull = false)
    private boolean enabled = false;
    //Timestamp at which the rule was created
    @DatabaseField(canBeNull = false)
    private Timestamp time = null;

    /**
     * Creates a new rule without specifying any field except the timestamp (will be set to now).
     * Warning: This constructor is mandatory required by the DAO, do not remove!
     */
    public Rule() {

    }

    /**
     * Returns the unique name (ID) of the rule.
     *
     * @return The name of the rule
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the unique name (ID) of the rule.
     *
     * @param name The name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the name of the trigger class of the rule that will be triggered when a event of this class name is
     * called.
     *
     * @return The name of the trigger class
     */
    public String getTriggerClass() {
        return triggerClass;
    }

    /**
     * Sets the name of the trigger class of the rule that will be triggerd when a event of this class name is called.
     *
     * @param triggerClass The name of the trigger class to set
     */
    public void setTriggerClass(String triggerClass) {
        this.triggerClass = triggerClass;
    }

    /**
     * Returns the name of the action method that is defined in the action class and should be called as action of the
     * rule in the case that the rule is triggered.
     *
     * @return The name of the action method
     */
    public String getActionMethod() {
        return actionMethod;
    }

    /**
     * Sets the name of the action method that is defined in the action class and should be called as action of the
     * rule in the case that the rule is triggered.
     *
     * @param actionMethod The name of the action method to set
     */
    public void setActionMethod(String actionMethod) {
        this.actionMethod = actionMethod;
    }

    /**
     * Returns the state of the rule: True, when the rule is enabled and false when it is disabled.
     *
     * @return The state of the rule
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Sets the state of the rule: True, when the rule is enabled and false when it is disabled.
     *
     * @param enabled The state of the rule to set
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Returns the timestamp at which the rule was created.
     *
     * @return The creation timestamp of the rule
     */
    public Timestamp getTime() {
        return time;
    }

    /**
     * Sets the timestamp at which the rule was created.
     *
     * @param time The creation timestamp of the rule to set
     */
    public void setTime(Timestamp time) {
        this.time = time;
    }

    /**
     * Updates the timestamp that remembers when the rule was created and sets it to the current timestamp.
     */
    public void refreshTime() {
        this.time = new Timestamp(System.currentTimeMillis());
    }

    /**
     * Returns a string that represents the rule in a graphical way using the following syntax:
     * Name: [State] Trigger_class --> Action_Method (creation timestamp)
     *
     * @return The representing string
     */
    public String toString() {
        //Build string that represents the rule using all fields
        StringBuilder representation = new StringBuilder();
        representation.append("[");
        representation.append(this.enabled ? "Enabled" : "Disabled");
        representation.append("] ");
        representation.append(this.name);
        representation.append(": ");
        representation.append(this.triggerClass);
        representation.append(" --> ");
        representation.append(this.actionMethod);
        representation.append(" (");
        representation.append(this.time.toLocalDateTime().toString());
        representation.append(")");

        //Return the created string
        return representation.toString();
    }
}