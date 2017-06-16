import lejos.hardware.Battery;
import lejos.robotics.Color;
import lejos.utility.Delay;

public class StandartFunction {

    // instance from Object kaaVerbindung
    private static KaaVerbindung kaa = LinieFolgen.getInstance().getKaa();

    int blue = Color.BLUE;
    int green = Color.GREEN;
    int black = Color.BLACK;
    int red = Color.RED;

    private float batteryStatus;
    private float batteryProcent;
    private int battery;

    /*
     * method to set speed.
     */
    public void setSpeed1(int speed) {
        Sensoren.getInstance().motor_links.setSpeed(speed);
        Sensoren.getInstance().motor_rechts.setSpeed(speed);
    }

    /**
     * Method to detect and prevent falling from the Table. When the Mindstorms
     * sees a redline he knows, that he is on the edge of the Table. So he can
     * move Backwards and turn 120 degrees to a safe Location.
     * @param color input
     */
    public void avoidFalling(int color){

        // The Mindstorms will stop when he recognize the color Red
        if (Sensoren.getInstance().sensor2.getColorID() == red
                || Sensoren.getInstance().sensor3.getColorID() == red) {
            Sensoren.getInstance().motor_links.stop();
            Sensoren.getInstance().motor_rechts.stop();
            kaa.avoidFallingEvent();
            Delay.msDelay(3000);

            // Sends KAA Event

            // Mindstorm drives Backward to a safe Location until he sees no Red
            // Line
            while (Sensoren.getInstance().sensor2.getColorID() != color
                    && Sensoren.getInstance().sensor3.getColorID() != color) {

                // When the app sends a stopp event, the Mindstorm will stop
                if (!LinieFolgen.getInstance().getStart()) {
                    break;
                }

                setSpeed1(30);
                // drives Backward
                Sensoren.getInstance().motor_links.backward();
                Sensoren.getInstance().motor_rechts.backward();

            }
            Sensoren.getInstance().motor_links.stop();
            Sensoren.getInstance().motor_rechts.stop();

            Sensoren.getInstance().motor_links.backward();
            Sensoren.getInstance().motor_rechts.backward();
            Delay.msDelay(200);

            Sensoren.getInstance().motor_links.stop();
            Sensoren.getInstance().motor_rechts.stop();

            Sensoren.getInstance().motor_links.backward();
            Sensoren.getInstance().motor_rechts.forward();
            Delay.msDelay(200);

            Sensoren.getInstance().motor_links.stop();
            Sensoren.getInstance().motor_rechts.stop();

            // Mindstorm rotates 120 degree
            // Sensoren.getInstance().motor_links.backward();
            // Sensoren.getInstance().motor_rechts.backward();
            // Delay.msDelay(400);
        }
    }

    /**
     * Calculates the battery.
     * 
     * @return battery
     */
    public int getBattery() {
        batteryStatus = Battery.getVoltage() / 9;
        batteryProcent = batteryStatus * 100;
        batteryProcent = batteryProcent - 50;
        batteryProcent = batteryProcent * 2;
        if (batteryProcent < 0) {
            batteryProcent = 1;
        }
        battery = (int) batteryProcent;
        System.out.println(battery);
        return battery;
    }

    private static StandartFunction instance;

    // Verhindere die Erzeugung des Objektes über andere Methoden
    private StandartFunction() {
    }

    /**
     * Eine Zugriffsmethode auf Klassenebene, welches dir '''einmal''' ein
     * konkretes Objekt erzeugt und dieses zurückliefert.
     * 
     * @return instance
     */
    public static StandartFunction getInstance() {
        if (StandartFunction.instance == null) {
            StandartFunction.instance = new StandartFunction();
        }
        return StandartFunction.instance;
    }
}
