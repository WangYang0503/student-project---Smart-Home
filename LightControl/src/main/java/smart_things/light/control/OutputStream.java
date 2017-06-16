package smart_things.light.control;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;

/**
 * 
 * @author Studienprojekt, Mario Maser This class can be used by all kind of
 *         objects, that need to control a GPIO Port of the Raspbarry Pi with
 *         pwm modulation
 */
public class OutputStream {

	private static Charset UTF8 = Charset.forName("UTF-8");
	public static final String PATH = "/dev/pi-blaster";
	private static OutputStream outputStream;

	/**
	 * Constructor
	 */
	public OutputStream() {
	}

	/**
	 * IMPORTANT: all GPIO Ports used in this method, MUST be set as output!
	 * Else a damage of the Raspberry Pi is possible.
	 * 
	 * @param gpio
	 *            GPIO Port of the light.
	 * @param brightness
	 *            value between (including) 0 and 1
	 */
	public void setValue(int gpio, double brightness) {
		Writer writer = null;
		try {
			writer = new OutputStreamWriter(new FileOutputStream(PATH), UTF8);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		if (writer != null) {
			try {
				writer.write(gpio + "=" + brightness + "\n");
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Singleton
	 * 
	 * @return One and only one instance of this class
	 */
	public static OutputStream getInstance() {
		if (outputStream != null) {
			return outputStream;
		} else {
			outputStream = new OutputStream();
		}
		return outputStream;
	}
}
