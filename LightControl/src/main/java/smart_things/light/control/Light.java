package smart_things.light.control;
import com.pi4j.io.gpio.Pin;

/**
 * This interface should be implemented by all kind of lights connected with the
 * Raspberry Pi
 * 
 * @author Studienprojekt, Mario Maser
 *
 */
public interface Light {
	/**
	 * The brightness is achieved by pwm (pulse width modulation) by pi-blaster
	 * (debian package for Raspberry Pi) If a light consists of multiple colors,
	 * the brightness is the average of the brightnesses of all colors
	 * 
	 * @return value between 0(low) and 255(high)
	 */
	public int getBrightness();
	
	/**
	 * Release the pin to use it later again. Else a PinAlreadyExists Exception is thrown.
	 */
	public void releasePin();
	

	/**
	 * The brightness is achieved by pwm (pulse width modulation) by pi-blaster
	 * (debian package for Raspberry Pi)
	 * 
	 * @param brightness
	 *            value between 0(low) and 255(high)
	 */
	public void setBrightness(int brightness);

	/**
	 * @return true, if the light is made out of three colores (red, green,
	 *         blue)
	 */
	public boolean isRGB();

	/**
	 * Port of the Raspberry Pi, the light is connected to. Returns the value of
	 * the first color, if the light consists of multiple colors.
	 * 
	 * @return value of port number (0-29)
	 */
	public int getGpioPort();

	/**
	 * Returns the Pin object of the first color, if the light consists of
	 * multiple colors.
	 * 
	 * @return Pin object of the Raspberry Pi, the light is to
	 */
	public Pin getGpioPin();

	/**
	 * Take a look at the Raspberry Pi pin layout. Port 0 and 6 are reserved!
	 * 
	 * @param gpioPort
	 *            value including 1-29
	 */
	public void setGpioPort(int gpioPort);

	/**
	 * Take a look a the Raspberry Pi pin layout.  Pin 0 and 6 are reserved!
	 * 
	 * @param pin
	 *            Pin object of the pi4j library
	 */
	public void setGpioPin(Pin pin);

	/**
	 * @return id (name) of the light
	 */
	public String getId();

	/**
	 * @param id
	 *            id/name of the light
	 */
	public void setId(String id);

}
