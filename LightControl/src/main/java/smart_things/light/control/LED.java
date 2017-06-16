package smart_things.light.control;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.exception.GpioPinExistsException;

/**
 * @author Studienprojekt, Mario Maser
 */
public class LED {

	private int brightness;
	private char color;
	private int gpioPort;
	private Pin gpioPin;
	private GpioPinDigitalOutput pin;
	private GpioController gpio;

	/**
	 * @param color
	 *            color of the LED
	 * @param gpioPort
	 *            Port of the light
	 */
	public LED(char color, int gpioPort) {
		if (gpioPort != 6 && gpioPort != 0) {
			this.color = color;
			this.gpioPort = gpioPort;
			gpio = GpioFactory.getInstance();
			try {
				pin = gpio.provisionDigitalOutputPin(Raspi.getInstance().getPinByInt(gpioPort), "LED", PinState.LOW);
			} catch (GpioPinExistsException e) {
				pin = (GpioPinDigitalOutput) gpio.getProvisionedPin(Raspi.getInstance().getPinByInt(gpioPort));
			}
			this.gpioPin = Raspi.getInstance().getPinByInt(gpioPort);
		} else {
			System.out.println("Port 0 and 6 are reserved!");
		}
	}

	/**
	 * Must be called, to reuse the pin later again.
	 */
	public void releasePin() {
		gpio.shutdown();
		gpio.unprovisionPin(pin);
	}

	/**
	 * Range between 0(Low) and 255(High) This range is mapped to a percentage
	 * value (0-1)
	 * 
	 * @param b
	 *            value of the brightness
	 */
	public void setBrightness(int b) {
		if (b > 255) {
			brightness = 255;
		} else if (b < 0) {
			brightness = 0;
		} else {
			brightness = b;
		}
		if (brightness == 255) {
			OutputStream.getInstance().setValue(gpioPort, 1);
		} else {
			// Mapping of the value to a percentage value
			OutputStream.getInstance().setValue(gpioPort, brightness * 0.0039);
		}
	}

	public int getBrightness() {
		return brightness;
	}

	public char getColor() {
		return color;
	}

	/**
	 * @param color
	 *            r (red), g (green), b (blue)
	 */
	public void setColor(char color) {
		this.color = color;
	}

	public int getGpioPort() {
		return gpioPort;
	}

	public void setGpioPort(int gpioPort) {
		this.gpioPort = gpioPort;
		this.gpioPin = Raspi.getInstance().getPinByInt(gpioPort);
	}

	public Pin getGpioPin() {
		return gpioPin;
	}

	public void setGpioPin(Pin gpioPin) {
		this.gpioPin = gpioPin;
		this.gpioPort = gpioPin.getAddress();
	}

}
