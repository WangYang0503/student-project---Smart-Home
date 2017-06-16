import lejos.robotics.Color;
import lejos.utility.Delay;

public class EvadesObject {

    private boolean gerade = true;
    private double leftRotationPilot = 180;
    private double rightRotationPilot = -180;
    private static double currentValue = 0;

    int blue = Color.BLUE;
    int green = Color.GREEN;
    int black = Color.BLACK;
    int red = Color.RED;

    // instance from Object kaaVerbindung
    private static KaaVerbindung kaa = LinieFolgen.getInstance().getKaa();

    /**
     * The Mindstorms rotate 90 degrees that he can drive around the object.
     * 
     * @param speed
     *            speed mindstorm
     * @param rotationRobot
     *            rotation mindstorm
     * @param rotationHead
     *            rotation mindstorm head
     */
    public void evadesObject(double speed, double rotationRobot, int rotationHead) {
        Sensoren.getInstance().pilot.setRotateSpeed(speed);
        Sensoren.getInstance().pilot.rotate(rotationRobot);
        Sensoren.getInstance().drehMotor.rotate(rotationHead);
        gerade = false;
    }

    /**
     * turn Mindstorm Ultraschall sensor.
     */
    public void ultraSchalSensor() {
        if (!gerade) {
            Sensoren.getInstance().drehMotor.rotate(-90);
            gerade = true;
        }
    }

    /**
     * Method to prevent collision with a object on the road.
     **/
    public void preventCollision() {
        // gets the first Value of the Array
        float distanceValue = Sensoren.getInstance().distance.getDistance();
        currentValue = (double) distanceValue;

        // Aslong the the distance between the mindstorm and the object is
        // smaller than 10cm
        // When a object is blocks the way, the mindstorm will make a turn
        // rechts positiv und links negativ
        if (distanceValue <= 0.1) {
            StandartFunction.getInstance().avoidFalling(black);
            kaa.dodgingObjectEvent();
            System.out.println("distanceValue");
            // checks if the envoirement is capable of evading
            Sensoren.getInstance().pilot.stop();
            Sensoren.getInstance().drehMotor.rotate(90);
            float distanceRight = Sensoren.getInstance().distance.getDistance();
            Delay.msDelay(500);
            Sensoren.getInstance().drehMotor.rotate(-180);
            float distanceLeft = Sensoren.getInstance().distance.getDistance();
            Delay.msDelay(500);
            Sensoren.getInstance().drehMotor.rotate(90);
            if (distanceRight > 0.2 || distanceLeft > 0.2) {
                if (distanceRight > 0.2) {
                    System.out.println("Rechts ausweichen");
                    evadingRight();

                    Sensoren.getInstance().drehMotor.rotate(90);

                } else if (distanceLeft > 0.2) {
                    System.out.println("Links ausweichen");
                    evadingLeft();
                    Sensoren.getInstance().drehMotor.rotate(-90);
                }
            }

        }
    }

    private void evadingLeft() {
        evadesObject(rightRotationPilot, leftRotationPilot, 90);
        int degree = 0;
        while (Sensoren.getInstance().sensor2.getColorID() != black
                && Sensoren.getInstance().sensor3.getColorID() != black) {

            if (!LinieFolgen.getInstance().getStart()) {
                break;
            }

            StandartFunction.getInstance().avoidFalling(black);

            float distanceValue = Sensoren.getInstance().distance.getDistance();
            StandartFunction.getInstance().setSpeed1(50);

            if (distanceValue < 0.2) {
                System.out.println("Kleiner als " + distanceValue);
                Sensoren.getInstance().motor_links.rotate(-5);
                // pilot.rotate(-10);
                degree = degree - 5;
                System.out.println(degree);
            } else if (distanceValue > 0.3) {
                System.out.println("Größer als " + distanceValue);
                Sensoren.getInstance().motor_rechts.rotate(-5);
                // pilot.rotate(10);
                degree = degree + 5;
                System.out.println(degree);
            }
            Sensoren.getInstance().motor_links.forward();
            Sensoren.getInstance().motor_rechts.forward();
            Delay.msDelay(500);

        }
        Sensoren.getInstance().pilot.stop();
        Sensoren.getInstance().motor_links.forward();
        Sensoren.getInstance().motor_rechts.forward();
        Delay.msDelay(50);
    }

    /**
     * Mindstorm will evade the object right direcetion.
     */
    private void evadingRight() {

        evadesObject(rightRotationPilot, rightRotationPilot, -90);
        int degree = 0;
        while (Sensoren.getInstance().sensor2.getColorID() != black
                && Sensoren.getInstance().sensor3.getColorID() != black) {

            if (!LinieFolgen.getInstance().getStart()) {
                break;
            }

            StandartFunction.getInstance().avoidFalling(black);
            float distanceValue = Sensoren.getInstance().distance.getDistance();
            StandartFunction.getInstance().setSpeed1(50);

            if (distanceValue < 0.2) {
                System.out.println("Kleiner als " + distanceValue);
                Sensoren.getInstance().motor_rechts.rotate(-5);
                // pilot.rotate(-10);
                degree = degree - 5;
                System.out.println(degree);
            } else if (distanceValue > 0.3) {
                System.out.println("Größer als " + distanceValue);
                Sensoren.getInstance().motor_links.rotate(-5);
                // pilot.rotate(10);
                degree = degree + 5;
                System.out.println(degree);
            }
            Sensoren.getInstance().motor_links.forward();
            Sensoren.getInstance().motor_rechts.forward();
            Delay.msDelay(500);

        }
        // Sensoren.getInstance().sensor4.disable();
        // uSensorCounter = 0;
//        Sensoren.getInstance().motor_links.rotate(15);
//        Sensoren.getInstance().motor_rechts.rotate(-15);
//        Sensoren.getInstance().pilot.stop();
        Sensoren.getInstance().pilot.stop();
        Sensoren.getInstance().motor_links.forward();
        Sensoren.getInstance().motor_rechts.forward();
        Delay.msDelay(50);

    }

    private static EvadesObject instance;

    // Verhindere die Erzeugung des Objektes über andere Methoden
    private EvadesObject() {
    }

    // Eine Zugriffsmethode auf Klassenebene, welches dir '''einmal''' ein
    // konkretes
    // Objekt erzeugt und dieses zurückliefert.
    public static EvadesObject getInstance() {
        if (EvadesObject.instance == null) {
            EvadesObject.instance = new EvadesObject();
        }
        return EvadesObject.instance;
    }

}
