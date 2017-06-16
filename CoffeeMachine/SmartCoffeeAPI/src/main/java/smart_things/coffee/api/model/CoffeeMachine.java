package smart_things.coffee.api.model;

import smart_things.coffee.api.messages.StatusActivityTypes;
import smart_things.coffee.api.messages.StrengthCodes;
import smart_things.coffee.api.messages.WaterLevelCodes;

import java.util.Observable;

public class CoffeeMachine extends Observable {
    // Coffee machine attributes
    // Activity status
    private StatusActivityTypes status = StatusActivityTypes.UNKOWN;
    // Use of filter (true) or grinder (true)
    private boolean filter = false;
    // Availability of a carafe in the coffee machine
    private boolean carafe = false;
    private WaterLevelCodes waterLevel = WaterLevelCodes.UNKNOWN;
    private StrengthCodes brewingStrength = StrengthCodes.UNKNOWN;
    private int numberOfCups = 0;
    private String wifiStrength = "";

    /**
     * Notifies the observers about changes at the coffee machine model object.
     * A reference to the object will be sent with the notification.
     */
    public void changed() {
        setChanged();
        notifyObservers(this);
    }

    /**
     * Returns the activity status type of the coffee machine model.
     *
     * @return The activity status
     */
    public StatusActivityTypes getStatus() {
        return status;
    }

    /**
     * Sets the activity status type of the coffee machine model.
     *
     * @param status The activity status type to set
     */
    public void setStatus(StatusActivityTypes status) {
        this.status = status;
    }

    /**
     * Checks whether the coffee machine is configured to use the filter as
     * brewing type.
     *
     * @return True, when the use of the filter is set; false, when grinder is set.
     */
    public boolean isFilter() {
        return filter;
    }

    /**
     * Sets the brewing type of the coffee machine model.
     *
     * @param filter True, when the filter is in use; false, when the grinder is in use
     */
    public void setFilter(boolean filter) {
        this.filter = filter;
    }

    /**
     * Checks whether the carafe of the coffee machine model is available.
     *
     * @return True, when there is a carafe; false otherwise
     */
    public boolean isCarafe() {
        return carafe;
    }

    /**
     * Sets the carafe availability of the coffee machine model.
     *
     * @param carafe True, when the carafe is available; false otherwise
     */
    public void setCarafe(boolean carafe) {
        this.carafe = carafe;
    }

    /**
     * Returns the latest received water level of the coffee machine.
     *
     * @return The water level
     */
    public WaterLevelCodes getWaterLevel() {
        return waterLevel;
    }

    /**
     * Sets the water level of the coffee machine model.
     *
     * @param waterLevel The water level
     */
    public void setWaterLevel(WaterLevelCodes waterLevel) {
        if (waterLevel == null) {
            waterLevel = WaterLevelCodes.UNKNOWN;
        }
        this.waterLevel = waterLevel;
    }

    /**
     * Returns the latest received brewing strength of the coffee machine.
     *
     * @return The brewing strength
     */
    public StrengthCodes getBrewingStrength() {
        return brewingStrength;
    }

    /**
     * Sets the brewing strength of the coffee machine model.
     *
     * @param brewingStrength The brewing strength
     */
    public void setBrewingStrength(StrengthCodes brewingStrength) {
        if (brewingStrength == null) {
            brewingStrength = StrengthCodes.UNKNOWN;
        }
        this.brewingStrength = brewingStrength;
    }

    /**
     * Returns the latest received number of cups of the coffee machine.
     *
     * @return The number of cups
     */
    public int getNumberOfCups() {
        return numberOfCups;
    }

    /**
     * Sets the number of cups of the coffee machine model.
     *
     * @param numberOfCups The number of cups
     */
    public void setNumberOfCups(int numberOfCups) {
        this.numberOfCups = numberOfCups;
    }

    /**
     * Returns the latest received Wifi strength of the coffee machine.
     *
     * @return The Wifi strength
     */
    public String getWifiStrength() {
        return wifiStrength;
    }

    /**
     * Sets the Wifi strength of the coffee machine model.
     *
     * @param wifiStrength The Wifi strength
     */
    public void setWifiStrength(String wifiStrength) {
        if ((wifiStrength == null) || (wifiStrength.equals(""))) {
            wifiStrength = "";
        }
        this.wifiStrength = wifiStrength;
    }
}