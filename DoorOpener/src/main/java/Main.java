import lejos.utility.Delay;
import smart_things.door.schema.CloseDoorEvent;
import smart_things.door.schema.CloseGarageEvent;
import smart_things.door.schema.DoorEventClassFamily;
import smart_things.door.schema.InfoRequestEvent;
import smart_things.door.schema.OpenDoorEvent;
import smart_things.door.schema.OpenGarageEvent;

/**
 * Created by Jan on 17.11.2016.
 */
public class Main {

    static KaaManager kaa = null;
    private static boolean test = false;
    // Kaa manager to handle the kaa connection
    
    // MotorControl to control the motors of the mindstorm
    private static GarageSensor garageSensor = new GarageSensor();
 // Setup MotorControl
    // Door Opener main functions
    private static HouseDoorOpener doorOpener = new HouseDoorOpener();

    public static void main(String args[]) {

        // Create new KAA manager and implement the different event listeners
        DoorEventClassFamily.Listener doorSystem = new DoorEventClassFamily.Listener() {

            @Override
            public void onEvent(CloseDoorEvent event, String source) {
                kaa.infoRequest(kaa.isDoorOpened(), kaa.getMoveDoor(), 
                        kaa.isGarageOpened(), kaa.getMoveGarage());
                doorOpener.closeDoor();
                kaa.setDoorOpened(false);
                kaa.doorClosed();
                kaa.infoRequest(kaa.isDoorOpened(), kaa.getMoveDoor(), 
                        kaa.isGarageOpened(), kaa.getMoveGarage());
            }

            @Override
            public void onEvent(OpenDoorEvent event, String source) {
                kaa.infoRequest(kaa.isDoorOpened(), kaa.getMoveDoor(), 
                        kaa.isGarageOpened(), kaa.getMoveGarage());
                doorOpener.openDoor();
                kaa.setDoorOpened(true);
                kaa.doorOpened();
                kaa.infoRequest(kaa.isDoorOpened(), kaa.getMoveDoor(), 
                        kaa.isGarageOpened(), kaa.getMoveGarage());

            }

            @Override
            public void onEvent(InfoRequestEvent event, String source) {
                //TODO MoveStates
                kaa.infoRequest(kaa.isDoorOpened(), kaa.getMoveDoor(), 
                        kaa.isGarageOpened(), kaa.getMoveGarage());

            }

            @Override
            public void onEvent(OpenGarageEvent event, String source) {
                System.out.println("Open Event erhalten");
                kaa.infoRequest(kaa.isDoorOpened(), kaa.getMoveDoor(), 
                        kaa.isGarageOpened(), kaa.getMoveGarage());
                garageSensor.garageOpen();
                kaa.setGarageOpened(true);
                kaa.garageOpened();
                //TODO MoveStates
                kaa.infoRequest(kaa.isDoorOpened(), kaa.getMoveDoor(), 
                        kaa.isGarageOpened(), kaa.getMoveGarage());
            }

            @Override
            public void onEvent(CloseGarageEvent event, String source) {
                System.out.println("Close Event erhalten");
                kaa.infoRequest(kaa.isDoorOpened(), kaa.getMoveDoor(), 
                        kaa.isGarageOpened(), kaa.getMoveGarage());
                garageSensor.garageClose();
                kaa.setGarageOpened(false);
                kaa.garageClosed();
                //TODO MoveStates
                kaa.infoRequest(kaa.isDoorOpened(), kaa.getMoveDoor(), 
                        kaa.isGarageOpened(), kaa.getMoveGarage());
            }
        };

        kaa = new KaaManager(doorSystem);
        kaa.connect(new Runnable() {
            
            @Override
            public void run() {
                //TODO MoveStates
                kaa.infoRequest(kaa.isDoorOpened(), kaa.getMoveDoor(), 
                        kaa.isGarageOpened(), kaa.getMoveGarage());             
                while (test == false) {
                    //garageSensor.preventCrash();
                    doorOpener.manually();
                    garageSensor.openManually();
                    garageSensor.stop();
                    Delay.msDelay(200);
//                    System.out.println("Bin bereit.");
                }
            }
        });
    }
    
    public KaaManager getKaa() {
        return kaa;
    }
    private static Main instance;
    // Verhindere die Erzeugung des Objektes über andere Methoden
    private Main() {
    }

    /**
     * Eine Zugriffsmethode auf Klassenebene, welches dir '''einmal''' ein
     * konkretes Objekt erzeugt und dieses zurückliefert.
     * 
     * @return instance
     */
    public static Main getInstance() {
        if (Main.instance == null) {
            Main.instance = new Main();
        }
        return Main.instance;
    }

}