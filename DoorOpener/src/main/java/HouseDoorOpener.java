import smart_things.door.schema.MoveStates;

public class HouseDoorOpener {

    // Kaa manager to handle the kaa connection
    private static KaaManager kaaManager = new KaaManager(null);

    /**
     * Method to open door.
     */
    public void openDoor() {
        kaaManager.setMoveDoor(MoveStates.OPENING);
        kaaManager.infoRequest(kaaManager.isDoorOpened(), kaaManager.getMoveDoor(),
                kaaManager.isGarageOpened(), kaaManager.getMoveGarage());
        Sensoren.getInstance().doorMotor.rotateTo(-180);
        Sensoren.getInstance().doorMotor.stop();
        kaaManager.setDoorOpened(true);
        kaaManager.doorOpened();
        kaaManager.setMoveDoor(MoveStates.NONE);
        kaaManager.infoRequest(kaaManager.isDoorOpened(), kaaManager.getMoveDoor(),
                kaaManager.isGarageOpened(), kaaManager.getMoveGarage());
    }

    /**
     * Method to close the door.
     */
    public void closeDoor() {
        kaaManager.setMoveDoor(MoveStates.CLOSING);
        kaaManager.infoRequest(kaaManager.isDoorOpened(), kaaManager.getMoveDoor(),
                kaaManager.isGarageOpened(), kaaManager.getMoveGarage());
        Sensoren.getInstance().doorMotor.rotateTo(180);
        Sensoren.getInstance().doorMotor.stop();
        kaaManager.setDoorOpened(false);
        kaaManager.doorClosed();
        kaaManager.setMoveDoor(MoveStates.NONE);
        kaaManager.infoRequest(kaaManager.isDoorOpened(), kaaManager.getMoveDoor(),
                kaaManager.isGarageOpened(), kaaManager.getMoveGarage());      
    }

    public void manually() {
        if (Sensoren.getInstance().touchSensor4.isPressed() && !kaaManager.isDoorOpened()) {
            openDoor();
        } else if (Sensoren.getInstance().touchSensor4.isPressed() && kaaManager.isDoorOpened()) {
            closeDoor();
        }
    }

}