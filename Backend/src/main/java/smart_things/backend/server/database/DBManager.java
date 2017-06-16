package smart_things.backend.server.database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import smart_things.backend.server.rules.Rule;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

/**
 * Database Manager class that uses a DataAccessObject (from ORMLite) to manage a SQLite database that persists the
 * different rules. These rules are created by the user to specify which action the Smart Home should perform in the
 * case that a certain trigger event gets activated. These kind of rules can be stored, checked  and queried using
 * this manager and the matching Rule (rules.Rule) class.
 * <p>
 * Created by Jan on 13.01.2017.
 */
public class DBManager {
    //Specifies the path and name of the sqlite database
    private static final String DBNAME = "database.sqlite";
    //Specifies the driver and kind of database
    private static final String DBSOURCE = "jdbc:sqlite:" + DBNAME;

    //The name of the trigger column in the database, needed to create queries on the trigger
    private static final String TRIGGER_COLUMN_NAME = "trigger";

    //Connection source that keeps up the connection to the database
    private ConnectionSource connection = null;
    //Data Access Object that makes it possible to work with the database using Rule objects and Strings as object ID
    private Dao<Rule, String> ruleDao;

    /**
     * Creates a new DBManager that establishes the connection to the database using the JDBC driver and creates a DAO
     * from it. If there has no rules table been created so far, a new table will be automatically created.
     *
     * @throws SQLException In case of an internal SQL error
     */
    public DBManager() throws SQLException {
        connection = new JdbcConnectionSource(DBSOURCE);
        ruleDao = DaoManager.createDao(connection, Rule.class);
        //Check whether the rule table does not exist
        if (!ruleDao.isTableExists()) {
            //Create table
            TableUtils.createTable(connection, Rule.class);
        }
    }

    /**
     * Persists a given rule to the database. Important: The name of the rule has to unique, otherwise a SQLException
     * will be thrown.
     *
     * @param rule The rule to persist
     * @throws SQLException In case of an internal SQL error
     */
    public void addRule(Rule rule) throws SQLException {
        ruleDao.createIfNotExists(rule);
    }

    /**
     * Deletes a given rule from the database. If there is no matching rule to delete, a SQLException will be thrown.
     *
     * @param rule The rule to delete
     * @throws SQLException In case of an internal SQL error
     */
    public void deleteRule(Rule rule) throws SQLException {
        ruleDao.delete(rule);
    }

    /**
     * Deletes a rule, given by its ID (name), from the database. If there is no matching rule to delete, a
     * SQLException will be thrown.
     *
     * @param ruleName The unique name of the rule to delete
     * @throws SQLException In case of an internal SQL error
     */
    public void deleteRule(String ruleName) throws SQLException {
        ruleDao.deleteById(ruleName);
    }

    /**
     * Updates a given rule (recognized by its ID) to new fields. Therefore it is important the the given rule has
     * the same ID like the rule that should be changed.
     *
     * @param rule The rule with changed fields
     * @throws SQLException In case of an internal SQL error
     */
    public void updateRule(Rule rule) throws SQLException {
        ruleDao.update(rule);
    }

    /**
     * Changes the ID of a given rule to a new ID that is passed in the method call.
     *
     * @param rule  The rule whose ID has to be changed
     * @param newID The new ID of the rule
     * @throws SQLException In case of an internal SQL error
     */
    public void updateRuleID(Rule rule, String newID) throws SQLException {
        ruleDao.updateId(rule, newID);
    }

    /**
     * Checks if a rule, given by its ID (name), exists in the database already.
     *
     * @param ruleName The unique name of the rule to check
     * @return True, if a rule with this name already exists in the database, false otherwise
     * @throws SQLException In case of an internal SQL error
     */
    public boolean ruleExists(String ruleName) throws SQLException {
        return ruleDao.idExists(ruleName);
    }

    /**
     * Fetches a rule, given by its ID (name), from the database, as far it exists. If the rule does not exist, a
     * SQLException will be thrown.
     *
     * @param ruleName The unique name of the rule to fetch
     * @return The rule, fetched from the database, as far it exists
     * @throws SQLException In case of an internal SQL error
     */
    public Rule getRule(String ruleName) throws SQLException {
        return ruleDao.queryForId(ruleName);
    }

    /**
     * Enables a rule given by its ID (name) in the database, as far as the rule exists. If the rule does not exist,
     * a SQLException will be thrown.
     *
     * @param ruleName The name of the rule to enable
     * @throws SQLException In case of an internal SQL error
     */
    public void enableRule(String ruleName) throws SQLException {
        //Fetch rule from database and set its state to enabled
        Rule ruleToChange = getRule(ruleName);
        ruleToChange.setEnabled(true);

        //Update the changed rule in the database
        updateRule(ruleToChange);
    }

    /**
     * Disables a rule given by its ID (name) in the database, as far as the rule exists. If the rule does not exist,
     * a SQLException will be thrown.
     *
     * @param ruleName The name of the rule to disable
     * @throws SQLException In case of an internal SQL error
     */
    public void disableRule(String ruleName) throws SQLException {
        //Fetch rule from database and set its state to disabled
        Rule ruleToChange = getRule(ruleName);
        ruleToChange.setEnabled(false);

        //Update the changed rule in the database
        updateRule(ruleToChange);
    }

    /**
     * Fetches a list of rules, given by their trigger class names, from the database, as far as one exists. If no rule
     * exists that matches the trigger class name, a SQLException will be thrown.
     *
     * @param triggerClass The name of the class that triggers the rule
     * @return A list of all rules that have the given trigger class name as trigger for the execution of the rule
     * @throws SQLException In case of an internal SQL error
     */
    public List<Rule> getRulesByTrigger(String triggerClass) throws SQLException {
        //Create a HashMap<field name, value>  to build up the search for the trigger class name and put in the pair of
        // trigger column name and given value
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(TRIGGER_COLUMN_NAME, triggerClass);

        //Execute the query and return the result
        return ruleDao.queryForFieldValues(map);
    }

    /**
     * Fetches all rules from the database and returns them in a list, as far as rules exist. If no rule exists,
     * a SQLException will be thrown.
     *
     * @return A list of all rules in the database
     * @throws SQLException In case of an internal SQL error
     */
    public List<Rule> getAllRules() throws SQLException {
        return ruleDao.queryForAll();
    }

    /**
     * Closes the connection to the database.
     *
     * @throws SQLException In case of an internal SQL error
     */
    public void close() throws SQLException {
        connection.close();
    }
}
