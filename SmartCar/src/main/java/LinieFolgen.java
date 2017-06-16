import lejos.robotics.Color;
import lejos.utility.Delay;
import smart_things.car.schema.CarEventClassFamily.Listener;
import smart_things.car.schema.InfoRequestEvent;
import smart_things.car.schema.ParkingPossibleEvent;
import smart_things.car.schema.SetSpeedEvent;
import smart_things.car.schema.StartDrivingEvent;
import smart_things.car.schema.StopDrivingEvent;
import smart_things.car.schema.ToogleStandbyEvent;

/**
 * Also critical features like evading objects and not falling from the Demo
 * Board.
 * 
 * @author sujanthan the master
 *
 */
public class LinieFolgen {
    // Variablen
    Integer wert = 0;
    private Integer counter = 0;

    // private boolean gerade = true;
    private int ruckwaertsZaehler = 0;

    private static boolean isTouched = false;
    private static double currentValue = 0;
    public static boolean parkingPossibleflag = false;

    private static boolean start = false;
    boolean test = true;

    // private int uSensorCounter = 0;

    int blue = Color.BLUE;
    int green = Color.GREEN;
    int black = Color.BLACK;
    int red = Color.RED;

    // instance from Object kaaVerbindung
    private static KaaVerbindung kaa = null;

    /*
     * The Mindstorm follows the black line with the two Colorsensors. The
     * Mindstorm tries to hold the black line between the two color Sensors.
     * When two color sensors detect a black line, than the Mindstorm will turn
     * left until its only one Sensor.
     */
    private void followBlackLine() throws InterruptedException {

        EvadesObject.getInstance().ultraSchalSensor();
        System.out.println("Folge der Linie......");
        // The Mindstorm will stop when he can varify the Green Line
        while (!(Sensoren.getInstance().sensor2.getColorID() == green)) {
            if (start) {
                System.out.println("Start ist auf true");
            } else if (!start) {
                System.out.println("Start ist auf false");
            }
            // When the StartDrivingEvent was sent, the boolean start is set on
            // true
            // and the Mindstorm will start Driving.
            // When the StoppDrivingEvent was sent,the boolean start is set on
            // False
            // and the Mindstorm will stop.

            // motor_links.rotate(-100);

            StandartFunction.getInstance().getBattery();
            while (start) {
                
                StandartFunction.getInstance().avoidFalling(black);

                StandartFunction.getInstance().getBattery();
                // Calls Method avoid Falling for checking dangerous Envoirement
                StandartFunction.getInstance().avoidFalling(black);

                // uSensorCounter++;

                // drive forward when sensor2 and 3 recognize the color Black
                if (Sensoren.getInstance().sensor2.getColorID() == black
                        && Sensoren.getInstance().sensor3.getColorID() == black) {
                    // set speed too 100,when the speed is over 100, The
                    // mindstorm
                    // has problems too recognize colors
                    StandartFunction.getInstance().setSpeed1(100);
                    Sensoren.getInstance().motor_links.forward();
                    Sensoren.getInstance().motor_rechts.forward();
                    Delay.msDelay(60);
                    checkerRechts();
                    counter = 0;
                    // wenn der linke sensor auf der linie ist, drehe nach links
                } else if (Sensoren.getInstance().sensor2.getColorID() == black) {

                    StandartFunction.getInstance().setSpeed1(100);
                    Sensoren.getInstance().motor_links.backward();
                    Sensoren.getInstance().motor_rechts.forward();
                    System.out.println("Linke Sensor auf der Linie");
                    counter = 0;
                    // wenn der rechte sensor auf der linie ist, drehe nach
                    // rechts
                } else if (Sensoren.getInstance().sensor3.getColorID() == black) {
                    StandartFunction.getInstance().setSpeed1(100);
                    Sensoren.getInstance().motor_links.forward();
                    Sensoren.getInstance().motor_rechts.backward();
                    System.out.println("Rechte Sensor auf der Linie");
                    counter = 0;
                } else {
                    counter++;
                    if (counter % 800 == 0) {
                        // We need to verify if the mindstorm is still driving
                        // on
                        // the Blackline. We make this verifiaction every few
                        // seconds
                        checkBlackline();
                    }
                    // drive forward
                    StandartFunction.getInstance().setSpeed1(100);
                    Sensoren.getInstance().motor_links.forward();
                    Sensoren.getInstance().motor_rechts.forward();
                    // if (!Sensoren.getInstance().sensor4.isEnabled() &&
                    // uSensorCounter == 400) {
                    // Sensoren.getInstance().sensor4.enable();
                    // }

                }
                EvadesObject.getInstance().preventCollision();
                // Needs to check if the Mindstorm needs to Start Parking
                Parking.getInstance().blueLineParking();
                // prevent program crash
                Thread.sleep(10);
            }

            Sensoren.getInstance().motor_links.stop();
            Sensoren.getInstance().motor_rechts.stop();
            Delay.msDelay(3000);
        }

    }

    /**
     * Method to turn Mindstorm left, when the two color sensors are on the
     * blackline.
     */
    private void checkerRechts() {
        boolean result = true;
        while (result) {
            StandartFunction.getInstance().avoidFalling(black);
            Sensoren.getInstance().pilot.setRotateSpeed(100);
            Sensoren.getInstance().pilot.rotate(10);
            System.out.println("turn left");
            if(!start){
                break;
            }
            if (Sensoren.getInstance().sensor2.getColorID() == black
                    || Sensoren.getInstance().sensor3.getColorID() == black) {
                result = false;
            }
        }
    }

    /**
     * The mindstorm will check every 4 seconds the blackline and when he
     * deviates the line he will drive back 3 times and Stops when he doesn't
     * find anything.
     */
    private void checkBlackline() {

        System.out.println("BlackLine");
        Sensoren.getInstance().pilot.setRotateSpeed(100);
        int counterBlack = 0;
        StandartFunction.getInstance().avoidFalling(black);
        boolean black = false;
        while (Sensoren.getInstance().sensor3.getColorID() != Color.BLACK
                || Sensoren.getInstance().sensor2.getColorID() != Color.BLACK
                || counterBlack == 5) {
            Sensoren.getInstance().pilot.rotate(15);
            counterBlack++;
            if (counterBlack == 5) {
                break;
            } else if (Sensoren.getInstance().sensor3.getColorID() == Color.BLACK
                    || Sensoren.getInstance().sensor2.getColorID() == Color.BLACK) {
                black = true;
                ruckwaertsZaehler = 0;
            }
        }
        int zaehler = counterBlack + 5;
        counterBlack = 0;
        while (Sensoren.getInstance().sensor2.getColorID() != Color.BLACK
                || Sensoren.getInstance().sensor3.getColorID() != Color.BLACK
                || counterBlack == zaehler) {
            Sensoren.getInstance().pilot.rotate(-15);
            counterBlack++;
            System.out.println("bla");
            if (counterBlack == 5) {
                break;
            } else if (Sensoren.getInstance().sensor2.getColorID() == Color.BLACK
                    || Sensoren.getInstance().sensor3.getColorID() == Color.BLACK) {
                black = true;
                ruckwaertsZaehler = 0;
            }
        }
        if (!black) {
            ruckwaertsZaehler++;
            if (ruckwaertsZaehler % 3 == 0) {
                System.exit(0);
                // Event schicken, bla das Auto steht
            }
            System.out.println("rueckwaerts");
            StandartFunction.getInstance().setSpeed1(100);
            Sensoren.getInstance().motor_rechts.backward();
            Sensoren.getInstance().motor_links.backward();
            Delay.msDelay(3000);
            checkBlackline();
        }
    }

    /**
     * main.
     * 
     * @param args
     *            bla
     */
    public static void main(String[] args) {

        // startet die Kaa Verbindung
        Listener kaaMindstormListener = new Listener() {
            @Override
            public void onEvent(SetSpeedEvent event, String source) {
                System.out.println("SetSpeedEvent received");
            }

            @Override
            public void onEvent(StopDrivingEvent event, String source) {
                System.out.println("StopDrivingEvent received");
                start = false;
                kaa.infoResponseEvent(Sensoren.getInstance().motor_links.getSpeed(), start,
                        Sensoren.getInstance().sensor2.toString(),
                        Sensoren.getInstance().sensor3.toString(),
                        Sensoren.getInstance().distance.getDistance(), isTouched,
                        StandartFunction.getInstance().getBattery());
            }

            @Override
            public void onEvent(StartDrivingEvent event, String source) {
                System.out.println("StartDrivingEvent received");
                start = true;
                kaa.infoResponseEvent(Sensoren.getInstance().motor_links.getSpeed(), start,
                        Sensoren.getInstance().sensor2.toString(),
                        Sensoren.getInstance().sensor3.toString(),
                        Sensoren.getInstance().distance.getDistance(), isTouched,
                        StandartFunction.getInstance().getBattery());
            }

            @Override
            public void onEvent(InfoRequestEvent event, String source) {
                System.out.println("InfoRequestEvent received");
                kaa.infoResponseEvent(Sensoren.getInstance().motor_links.getSpeed(), start,
                        Sensoren.getInstance().sensor2.toString(),
                        Sensoren.getInstance().sensor3.toString(),
                        Sensoren.getInstance().distance.getDistance(), isTouched,
                        StandartFunction.getInstance().getBattery());
            }

            @Override
            public void onEvent(ToogleStandbyEvent event, String source) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onEvent(ParkingPossibleEvent event, String source) {
                parkingPossibleflag = true;

            }
        };

        kaa = new KaaVerbindung(kaaMindstormListener);
        kaa.connect(new Runnable() {
            @Override
            public void run() {

                try {
                    kaa.infoResponseEvent(Sensoren.getInstance().motor_links.getSpeed(), start,
                            Sensoren.getInstance().sensor2.toString(),
                            Sensoren.getInstance().sensor3.toString(),
                            Sensoren.getInstance().distance.getDistance(), isTouched,
                            StandartFunction.getInstance().getBattery());
                    LinieFolgen folgen = new LinieFolgen();
                    folgen.followBlackLine();
                } catch (InterruptedException e) {
                }
                System.out.println("Ich warte");
            }
        });
    }

    public boolean isParkingPossibleflag() {
        return parkingPossibleflag;
    }

    public KaaVerbindung getKaa() {
        return kaa;
    }

    private static LinieFolgen instance;

    // Verhindere die Erzeugung des Objektes über andere Methoden
    private LinieFolgen() {
    }

    /**
     * Eine Zugriffsmethode auf Klassenebene, welches dir '''einmal''' ein
     * konkretes Objekt erzeugt und dieses zurückliefert.
     * 
     * @return instance
     */
    public static LinieFolgen getInstance() {
        if (LinieFolgen.instance == null) {
            LinieFolgen.instance = new LinieFolgen();
        }
        return LinieFolgen.instance;
    }

    public boolean getStart() {
        return start;
    }
}
