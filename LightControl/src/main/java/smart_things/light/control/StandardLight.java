package smart_things.light.control;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.exception.GpioPinExistsException;

/**
 * A standard light has the color white, only one GPIO port and can only be
 * regulated in brightness
 * 
 * @author Studienprojekt, Mario Maser
 *
 */
public class StandardLight implements Light {

	private int brightness;
	private int gpioPort;
	private Pin gpioPin;
	private GpioPinDigitalOutput pin;
	private String id = "";
	private GpioController gpio;

	/**
	 * Constructor
	 * 
	 * @param gpioPort
	 *            GPIO port, the light is connected to. Take a look at Raspi
	 *            Layout for more information
	 */
	public StandardLight(String id, int gpioPort) {
		if (gpioPort != 6 && gpioPort != 0) {
			this.gpioPort = gpioPort;
			this.id = id;
			gpioPin = Raspi.getInstance().getPinByInt(gpioPort);
			gpio = GpioFactory.getInstance();
			try {
				pin = gpio.provisionDigitalOutputPin(Raspi.getInstance().getPinByInt(gpioPort), "LED", PinState.LOW);
			} catch (GpioPinExistsException e) {
				pin = (GpioPinDigitalOutput) gpio.getProvisionedPin(Raspi.getInstance().getPinByInt(gpioPort));
			}
		}else{
			System.out.println("Port 0 and 6 are reserved!");
		}

	}

	@Override
	public int getBrightness() {
		return brightness;
	}

	@Override
	public void setBrightness(int b) {
		this.brightness = b;

		if (brightness > 255) {
			brightness = 255;
		} else if (brightness < 0) {
			brightness = 0;
		} else {
			this.brightness = b;
		}
		if (brightness == 255) {
			OutputStream.getInstance().setValue(gpioPort, 1);
		} else {
			// Mapping of the value to a percentage value
			OutputStream.getInstance().setValue(gpioPort, brightness * 0.0039);
		}

	}

	@Override
	public boolean isRGB() {
		return false;
	}

	@Override
	public int getGpioPort() {
		return gpioPort;
	}

	@Override
	public Pin getGpioPin() {
		return gpioPin;
	}

	@Override
	public void setGpioPort(int gpioPort) {
		this.gpioPort = gpioPort;
		gpioPin = Raspi.getInstance().getPinByInt(gpioPort);
	}

	@Override
	public void setGpioPin(Pin pin) {
		this.gpioPin = pin;
		gpioPort = pin.getAddress();

	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public void releasePin() {
		gpio.shutdown();
		gpio.unprovisionPin(pin);
	}

}
