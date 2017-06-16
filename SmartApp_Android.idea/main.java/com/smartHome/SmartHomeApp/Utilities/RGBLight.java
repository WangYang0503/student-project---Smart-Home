package com.smartHome.SmartHomeApp.Utilities;

/**
 * Created by Lia on 29.01.2017.
 */

public class RGBLight {
    private int red;
    private int green;
    private int blue;
    private String id;
    private Boolean selected = false;

    public RGBLight(String lightID, int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.id = lightID;
    }

    public int getRed() {
        return red;
    }

    public int getGreen() {
        return green;
    }

    public int getBlue() {
        return blue;
    }

    public String getId() {
        return id;
    }
    public Boolean isSelected() {
        return selected;

    }
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public String toString() {
        return "Light: " + id;
    }



}
