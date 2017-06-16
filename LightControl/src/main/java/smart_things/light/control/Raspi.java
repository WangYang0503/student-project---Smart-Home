package smart_things.light.control;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;

/**
 * This class is used as singleton only for mapping a given int gpioPort to the
 * Pin given as constant in RaspiPin
 * 
 * @author Studienprojekt, Mario Maser
 *
 */

public class Raspi {
	private Pin[] pins = new Pin[30];

	// Sinlgeton
	private static Raspi instance;

	/**
	 * Fills the pins-array with RaspiPin.GPIO_XX constants so you can use just
	 * a int (0-29) for the GPIO constant
	 */
	private void initGpioPorts() {
		pins[0] = RaspiPin.GPIO_00;
		pins[1] = RaspiPin.GPIO_01;
		pins[2] = RaspiPin.GPIO_02;
		pins[3] = RaspiPin.GPIO_03;
		pins[4] = RaspiPin.GPIO_04;
		pins[5] = RaspiPin.GPIO_05;
		pins[6] = RaspiPin.GPIO_06;
		pins[7] = RaspiPin.GPIO_07;
		pins[8] = RaspiPin.GPIO_08;
		pins[9] = RaspiPin.GPIO_09;
		pins[10] = RaspiPin.GPIO_10;
		pins[11] = RaspiPin.GPIO_11;
		pins[12] = RaspiPin.GPIO_12;
		pins[13] = RaspiPin.GPIO_13;
		pins[14] = RaspiPin.GPIO_14;
		pins[15] = RaspiPin.GPIO_15;
		pins[16] = RaspiPin.GPIO_16;
		pins[17] = RaspiPin.GPIO_17;
		pins[18] = RaspiPin.GPIO_18;
		pins[19] = RaspiPin.GPIO_19;
		pins[20] = RaspiPin.GPIO_20;
		pins[21] = RaspiPin.GPIO_21;
		pins[22] = RaspiPin.GPIO_22;
		pins[23] = RaspiPin.GPIO_23;
		pins[24] = RaspiPin.GPIO_24;
		pins[25] = RaspiPin.GPIO_25;
		pins[26] = RaspiPin.GPIO_26;
		pins[27] = RaspiPin.GPIO_27;
		pins[28] = RaspiPin.GPIO_28;
		pins[29] = RaspiPin.GPIO_29;
	}

	/**
	 * @param gpioPort
	 *            Must be between 0 and 29. Look at Raspi pinout for more
	 *            details of GPIO ports
	 * @return Pin object of the given gpio port, or GPIO_02 as default
	 */
	public Pin getPinByInt(int gpioPort) {
		if (gpioPort > 0 || gpioPort < 30) {
			return pins[gpioPort];
		} else {
			//Default Pin
			return pins[2];
		}
	}

	/**
	 * 
	 * @return array of all existing pins
	 */
	public Pin[] getPins() {
		return pins;
	}
	
	/**
	 * @param gpioPin
	 *            Pin of the GPIO
	 * @return String of form: "GPIO_XX" where XX is a two digit number
	 */
	public String gpioToName(int gpioPort) {
		String s = "GPIO_";
		if (gpioPort < 10) {
			s += "0" + gpioPort;
		} else if (gpioPort > 99) {
			s += "00";
		} else {
			s += gpioPort;
		}
		return s;
	}

	/**
	 * Creates a new instance when this method is called the first time and
	 * initializes the array of the GPIO ports. All further calls return the
	 * only existing instance
	 * 
	 * @return The only instace of this class (Singleton)
	 */
	public static Raspi getInstance() {
		if (instance != null) {
			return instance;
		} else {
			instance = new Raspi();
			instance.initGpioPorts();
		}
		return instance;
	}
}
