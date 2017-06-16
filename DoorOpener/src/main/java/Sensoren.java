import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.robotics.RegulatedMotor;

public class Sensoren {

    // Sensoren
    protected EV3TouchSensor sensor3 = new EV3TouchSensor(SensorPort.S3);
    protected EV3TouchSensor sensor4 = new EV3TouchSensor(SensorPort.S4);
    protected EV3TouchSensor sensor2 = new EV3TouchSensor(SensorPort.S2);
    protected EV3TouchSensor sensor1 = new EV3TouchSensor(SensorPort.S1);
    protected Touch touchSensor1 = new Touch(sensor1);
    protected Touch touchSensor2 = new Touch(sensor2);
    protected Touch touchSensor3 = new Touch(sensor3);
    protected Touch touchSensor4 = new Touch(sensor4);
    
    //Motoren
    protected RegulatedMotor doorMotor = new EV3LargeRegulatedMotor(MotorPort.B);
    protected RegulatedMotor garagenMotor = new EV3LargeRegulatedMotor(MotorPort.A);
    
    
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
