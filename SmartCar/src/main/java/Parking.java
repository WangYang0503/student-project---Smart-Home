import lejos.robotics.Color;
import lejos.utility.Delay;

/**
 * Class for mindstorms parking.
 * 
 * @author Sujanthan
 *
 */
public class Parking {

    // variable
    private int virtuellerZaehler = 0;
    private int speed = 100;
    private static boolean isDrive = true;
    private boolean waited = false;

    // Color declaration
    int blue = Color.BLUE;
    int green = Color.GREEN;
    int black = Color.BLACK;
    int red = Color.RED;

    // instance from Object kaaVerbindung
    private static KaaVerbindung kaa = LinieFolgen.getInstance().getKaa();

    /*
     * When the Mindstorms recognizes a Blue Line he slows down for Parking.
     * When he recognize a green Line he stops and send a signal to Kaa a
     * brewing request
     * 
     */
    public void blueLineParking() {
        // if sensor2 and 3 both have recognized the color Blue the Car will
        // stop and send a Event to KAA
        if (Sensoren.getInstance().sensor2.getColorID() == blue
                || Sensoren.getInstance().sensor3.getColorID() == blue) {
            int newSpeed = speed;

            // Stops the Vehicle and brew coffee
            Sensoren.getInstance().pilot.stop();
            // Sends Parking Event to everyone
            kaa.startParkingEvent();
            // waiting 10 sec before starting again
            Delay.msDelay(10000);
            // move Mindstorm a little bit forward
            Sensoren.getInstance().motor_links.forward();
            Sensoren.getInstance().motor_rechts.forward();
            Delay.msDelay(1000);
            // The Mindstorm will stop,when he sees the Color Green
            while (!(Sensoren.getInstance().sensor2.getColorID() == green
                    || Sensoren.getInstance().sensor3.getColorID() == green)) {

//                StandartFunction.getInstance().avoidFalling(blue);
                while (!LinieFolgen.getInstance().getStart()) {
                    Sensoren.getInstance().pilot.stop();
                    waited = true;
                }

                if (waited) {
                    blueLineChecker();
                }

                // checks if the Garage is open and the Mindstorm can start
                // Parking
                while (!LinieFolgen.getInstance().isParkingPossibleflag()) {
                    Sensoren.getInstance().motor_links.stop();
                    Sensoren.getInstance().motor_rechts.stop();
                    System.out.println("Garage nicht offen warten");

                }
                newSpeed = numberChecker(newSpeed);

                // when both sensors recognize Blue then go straight forward
                if (Sensoren.getInstance().sensor2.getColorID() == blue
                        && Sensoren.getInstance().sensor3.getColorID() == blue) {
                    StandartFunction.getInstance().setSpeed1(newSpeed);
                    Sensoren.getInstance().motor_links.forward();
                    Sensoren.getInstance().motor_rechts.backward();
                    Delay.msDelay(50);
                    // checkerRechts();
                    // wenn der linke sensor auf der linie ist, drehe nach links
                } else if (Sensoren.getInstance().sensor2.getColorID() == blue) {
                    // wenn der linke sensor auf der linie ist, drehe nach links
                    StandartFunction.getInstance().setSpeed1(newSpeed);
                    Sensoren.getInstance().motor_links.backward();
                    Sensoren.getInstance().motor_rechts.forward();
                    System.out.println("Linke Sensor auf der Linie");
                    // wenn der rechte sensor auf der linie ist, drehe nach
                    // rechts
                } else if (Sensoren.getInstance().sensor3.getColorID() == blue) {
                    // wenn der rechte sensor auf der linie ist, drehe nach
                    // rechts
                    StandartFunction.getInstance().setSpeed1(newSpeed);
                    Sensoren.getInstance().motor_links.forward();
                    Sensoren.getInstance().motor_rechts.backward();
                    System.out.println("Rechte Sensor auf der Linie");
                } else {

                    // sonst vorwaerts fahren
                    StandartFunction.getInstance().setSpeed1(newSpeed);
                    Sensoren.getInstance().motor_links.forward();
                    Sensoren.getInstance().motor_rechts.forward();
                    System.out.println(50);
                    // Delay.msDelay(1);
                    // System.out.println(Battery.getVoltage());
                    // NumberFormat numberFormat = new DecimalFormat("0.00");
                    // numberFormat.setRoundingMode(RoundingMode.DOWN);
                    // System.out.println(numberFormat.format(route) + "cm");
                }
            }
            Sensoren.getInstance().pilot.stop();
            isDrive = false;

            // Sends Event to KAA
            kaa.stoppedDrivingEvent();

            // waits before Closing the Programm 60s,because KAA needs time to
            // Send Events
            Delay.msDelay(60000);
            System.exit(0);
        }
    }

    /**
     * Method to control the integer.
     * 
     * @param num
     *            parking speed
     * 
     * @return current parking speed
     */
    public int numberChecker(int num) {
        virtuellerZaehler++;
        if (virtuellerZaehler % 10000 == 0) {
            num = num - 10;
            System.out.println(num);
        }
        if (num <= 60) {
            num = 60;
        }
        return num;
    }

    /**
     * checks Blueline.
     */
    public void blueLineChecker() {
        int counter = 0;

        StandartFunction.getInstance().setSpeed1(50);
        boolean blueReco = false;
        while (!blueReco) {
            
            if (!LinieFolgen.getInstance().getStart()) {
                break;
            }
            if (Sensoren.getInstance().sensor2.getColorID() == blue
                    && Sensoren.getInstance().sensor3.getColorID() == blue) {
                blueReco = true;
                break;
            }
            System.out.println("suche blaue Linie");
            Sensoren.getInstance().motor_links.backward();
            Sensoren.getInstance().motor_rechts.backward();
            

            while (counter != 5) {
                
                if (!LinieFolgen.getInstance().getStart()) {
                    break;
                }
                if (Sensoren.getInstance().sensor2.getColorID() == blue
                        && Sensoren.getInstance().sensor3.getColorID() == blue) {
                    blueReco = true;
                    break;
                }
                Sensoren.getInstance().motor_links.forward();
                Sensoren.getInstance().motor_rechts.backward();
                counter++;
            }

            while (counter != -5) {
                
                if (!LinieFolgen.getInstance().getStart()) {
                    break;
                }
                if (Sensoren.getInstance().sensor2.getColorID() == blue
                        && Sensoren.getInstance().sensor3.getColorID() == blue) {
                    blueReco = true;
                    break;
                }
                Sensoren.getInstance().motor_links.backward();
                Sensoren.getInstance().motor_rechts.forward();
                counter--;
            }
        }
        waited = false;

    }

    private static Parking instance;

    // Verhindere die Erzeugung des Objektes über andere Methoden
    private Parking() {
    }

    /**
     * Eine Zugriffsmethode auf Klassenebene, welches dir '''einmal''' ein
     * konkretes Objekt erzeugt und dieses zurückliefert.
     * 
     * @return instance
     */
    public static Parking getInstance() {
        if (Parking.instance == null) {
            Parking.instance = new Parking();
        }
        return Parking.instance;
    }
}
