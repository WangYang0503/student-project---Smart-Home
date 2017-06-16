package com.smartHome.SmartHomeApp.Utilities;

/**
 * Created by Lia on 24.01.2017.
 */

public class Light {

        private int slot;
        private String id;
    private Boolean selected = false;

        public Light(String lightID, int slot) {
            this.slot = slot;
            this.id = lightID;
        }
        public int getSlot() {
            return slot;
        }

        public String getId() {
            return id;
        }
    public Boolean isSelected(){
        return selected;

    }

        @Override
        public String toString(){
            return id;
        }

}
