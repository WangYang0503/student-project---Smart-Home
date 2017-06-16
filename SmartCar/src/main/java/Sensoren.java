import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.SensorModes;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.navigation.DifferentialPilot;

/**
 * Declaration from the Motor and sensors.
 * 
 * @author david ziegler
 *
 */
public class Sensoren {

    // private static Sensoren singleton;
    //
    // /**
    // * private Constructor prevents any other Class from instantint
    // */
    // private Sensoren() {
    // }
    //
    // /**
    // * Allows one Object
    // *
    // * @return singleton
    // */
    // public static synchronized Sensoren getInstance() {
    // if(Sensoren.singleton == null){
    // Sensoren.singleton = new Sensoren();
    // }
    // return Sensoren.singleton;
    //
    // }

    // initialization Sensor ports
    protected EV3ColorSensor sensor3 = new EV3ColorSensor(SensorPort.S3);
    protected EV3ColorSensor sensor2 = new EV3ColorSensor(SensorPort.S2);
    SensorModes sensor_1 = new EV3TouchSensor(SensorPort.S1);
    protected SimpleTouch touch = new SimpleTouch(sensor_1);
    protected EV3UltrasonicSensor sensor4 = new EV3UltrasonicSensor(SensorPort.S4);

    // Values from Ultra Sensor
    protected SimpleTouchDistance distance = new SimpleTouchDistance(sensor4);

    // initialization Motor ports
    protected RegulatedMotor motor_links = new EV3LargeRegulatedMotor(MotorPort.B);
    protected RegulatedMotor motor_rechts = new EV3LargeRegulatedMotor(MotorPort.C);
    protected RegulatedMotor drehMotor = new EV3LargeRegulatedMotor(MotorPort.D);
    protected DifferentialPilot pilot = new DifferentialPilot(1, 1, motor_links, motor_rechts);

    private static Sensoren instance;

    // Verhindere die Erzeugung des Objektes über andere Methoden
    private Sensoren() {
    }

    /**
     * Eine Zugriffsmethode auf Klassenebene, welches dir '''einmal''' ein
     * konkretes Objekt erzeugt und dieses zurückliefert.
     * 
     * @return instance
     */
    public static Sensoren getInstance() {
        if (Sensoren.instance == null) {
            Sensoren.instance = new Sensoren();
        }
        return Sensoren.instance;
    }

}