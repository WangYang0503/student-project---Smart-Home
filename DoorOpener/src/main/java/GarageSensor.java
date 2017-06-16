import smart_things.door.schema.MoveStates;

/**
 * This Class controls the Garage and his open and close functions.
 *
 * @author david ziegler, iheb harchi, benedikt schulz
 */
public class GarageSensor {

    // Variablen
    private boolean open = true;
    private boolean close = false;
    float[] AngleSamples = new float[2];

    // MotorCoontrol Object
    // MotorControl control = new MotorControl();
    // private HouseDoorOpener doorOpener = new HouseDoorOpener();
    KaaManager kaaManager = new KaaManager(null);

    /**
     * Method to open the Garage, when the distance from the nxtUltrasonic
     * Sensor is less than 20 mm.
     */
    public void garageOpen() {
        kaaManager.setMoveGarage(MoveStates.OPENING);
        kaaManager.infoRequest(kaaManager.isDoorOpened(), kaaManager.getMoveDoor(), 
                kaaManager.isGarageOpened(), kaaManager.getMoveGarage());
        while (open) {
            close = false;
            if (Sensoren.getInstance().touchSensor1.isPressed()) {
                //System.out.println("open false");
                open = false;
            }
            
            Sensoren.getInstance().garagenMotor.forward();
        }
        Sensoren.getInstance().garagenMotor.stop();
        kaaManager.setGarageOpened(true);
        kaaManager.setMoveGarage(MoveStates.NONE);
        kaaManager.infoRequest(kaaManager.isDoorOpened(), kaaManager.getMoveDoor(), 
                kaaManager.isGarageOpened(), kaaManager.getMoveGarage());
        
        
    }

    /**
     * Method to close the Garage, when the distance from the EV3Ultrasonic
     * Sensor is less than 20 mm.
     */
    public void garageClose() {
        
        // Until the value of the nxt ultrasonic sensor is bigger than 0.08 the
        // garage will close
        kaaManager.setMoveGarage(MoveStates.CLOSING);
        kaaManager.infoRequest(kaaManager.isDoorOpened(), kaaManager.getMoveDoor(), 
                kaaManager.isGarageOpened(), kaaManager.getMoveGarage());
        while (!close) {
            open = true;
            if(Sensoren.getInstance().touchSensor3.isPressed()){
                //System.out.println("close false");
                close = true;
            } 
            Sensoren.getInstance().garagenMotor.backward();          
        }
        Sensoren.getInstance().garagenMotor.stop();
        kaaManager.setGarageOpened(false);
        kaaManager.setMoveGarage(MoveStates.NONE);
        kaaManager.infoRequest(kaaManager.isDoorOpened(), kaaManager.getMoveDoor(), 
                kaaManager.isGarageOpened(), kaaManager.getMoveGarage());
        
    }

    /**
     * this method opens the garage manually from the inside.
     */
    public void openManually() {
        //System.out.println("nicht gedrückt");
        if (Sensoren.getInstance().touchSensor2.isPressed() && !kaaManager.isGarageOpened()) {
            System.out.println("öffnen");
            garageOpen();
            kaaManager.setGarageOpened(true);
            kaaManager.garageOpened();
            // TODO MoveStates
            kaaManager.infoRequest(kaaManager.isDoorOpened(), kaaManager.getMoveDoor(),
                    kaaManager.isGarageOpened(), kaaManager.getMoveGarage());
        } else if (Sensoren.getInstance().touchSensor2.isPressed() && kaaManager.isGarageOpened()) {
            System.out.println("schließen");
            garageClose();
            kaaManager.setGarageOpened(false);
            kaaManager.garageClosed();
            // TODO MoveStates
            kaaManager.infoRequest(kaaManager.isDoorOpened(), kaaManager.getMoveDoor(),
                    kaaManager.isGarageOpened(), kaaManager.getMoveGarage());
        }

    }

    /**
     * When touch sensor is pressed the garage will stop.
     */
    public void preventCrash() {
        if (!Sensoren.getInstance().touchSensor1.isPressed() 
                && Sensoren.getInstance().touchSensor3.isPressed()) {
            kaaManager.setGarageOpened(false);
            // TODO MoveStates
            kaaManager.infoRequest(kaaManager.isDoorOpened(), kaaManager.getMoveDoor(),
                    kaaManager.isGarageOpened(), kaaManager.getMoveGarage());
        } else if (!Sensoren.getInstance().touchSensor1.isPressed() 
                && !Sensoren.getInstance().touchSensor3.isPressed()) {
            garageClose();
            kaaManager.setGarageOpened(false);
            // TODO MoveStates
            kaaManager.infoRequest(kaaManager.isDoorOpened(), kaaManager.getMoveDoor(),
                    kaaManager.isGarageOpened(), kaaManager.getMoveGarage());
        } else if (Sensoren.getInstance().touchSensor1.isPressed() 
                && !Sensoren.getInstance().touchSensor3.isPressed()) {
            open = true;
            garageClose();
            kaaManager.setGarageOpened(false);
            // TODO MoveStates
            kaaManager.infoRequest(kaaManager.isDoorOpened(), kaaManager.getMoveDoor(),
                    kaaManager.isGarageOpened(), kaaManager.getMoveGarage());
        }
    }

    /**
     * The rotation will stop immediately when this method is called
     */
    public void stop() {
        Sensoren.getInstance().garagenMotor.stop();
    }

}
