package smart_things.light.control;
import com.pi4j.io.gpio.Pin;

public class RGBLight implements Light {

	private LED red;
	private LED green;
	private LED blue;
	private String id = "";
	private boolean isRGB;

	/**
	 * Constructor A RGB light needs a LED for each basic color (red, green,
	 * blue). Further more each light needs a distinct id
	 */
	public RGBLight(String id, LED red, LED green, LED blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.id = id;
		
		/*
		 * A RGB light is only given, if all basic colors exist
		 */
		if(red == null || blue == null || green == null){
			isRGB = false;
		}else{
			isRGB = true;
		}
	}

	/**
	 * Sets the brightness of the red LED
	 * 
	 * @param r
	 *            value: 0(low) - 255(high)
	 */
	public void setRed(int r) {
		red.setBrightness(r);
	}

	/**
	 * Sets the brightness of the green LED
	 * 
	 * @param g
	 *            value: 0(low) - 255(high)
	 */
	public void setGreen(int g) {
		green.setBrightness(g);
	}

	/**
	 * Sets the brightness of the blue LED
	 * 
	 * @param b
	 *            value: 0(low) - 255(high)
	 */
	public void setBlue(int b) {
		blue.setBrightness(b);
	}

	/**
	 * 
	 * @return value between 0(low) and 255(high)
	 */
	public int getRed() {
		return red.getBrightness();
	}

	/**
	 * 
	 * @return value between 0(low) and 255(high)
	 */
	public int getGreen() {
		return green.getBrightness();
	}

	/**
	 * 
	 * @return value between 0(low) and 255(high)
	 */
	public int getBlue() {
		return blue.getBrightness();
	}
	
	/**
	 * 
	 * @return gpio port of the red LED
	 */
	public int getGPIORed(){
		return red.getGpioPort();
	}
	
	/**
	 * 
	 * @return gpio port of the green LED
	 */
	public int getGPIOGreen(){
		return green.getGpioPort();
	}
	
	
	/**
	 * 
	 * @return gpio port of the blue LED
	 */
	public int getGPIOBlue(){
		return blue.getGpioPort();
	}

	@Override
	public int getBrightness() {
		int brightness = (int) ((red.getBrightness() + green.getBrightness() + blue.getBrightness()) / 3);
		return brightness;
	}

	@Override
	public void setBrightness(int brightness) {
		/*
		 * Special case for RGB lights. If only one brightness level is given,
		 * the color of the light will be white in the given brightness. That
		 * means, all colors where set to the same brightness
		 */
		red.setBrightness(brightness);
		green.setBrightness(brightness);
		blue.setBrightness(brightness);
	}
	
	

	@Override
	public boolean isRGB() {
		return isRGB;
	}

	@Override
	public int getGpioPort() {
		return red.getGpioPort();
	}

	@Override
	public Pin getGpioPin() {
		return red.getGpioPin();
	}

	@Override
	public void setGpioPort(int gpioPort) {
		red.setGpioPort(gpioPort);
	}

	@Override
	public void setGpioPin(Pin pin) {
		red.setGpioPin(pin);

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
		red.releasePin();
		green.releasePin();
		blue.releasePin();
		
	}

}
