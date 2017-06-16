package com.smartHome.SmartHomeApp.Utilities;

import java.util.LinkedList;

/**
 * Created by Lia on 29.01.2017.
 */

public class Room{
    private LinkedList<Light> lights = new LinkedList<>();
    private String roomID;
    private boolean selected = false;
    public Room(String roomID){
        this.roomID = roomID;
    }

    public void addLight(Light l){
        lights.add(l);
    }

    public void removeLight(Light l){
        lights.remove(l);
    }

    public String getRoomID() {
        return roomID;
    }

    @Override
    public String toString(){
        return roomID;
    }

    public Boolean isSelected() {
        return selected;

    }
}
