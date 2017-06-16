package smart_things.light.control;

/**
 * 
 * @author Studienprojekt, Mario Maser
 *
 */
public class Main {

	public static final String XMLFILE = "smarthome.xml";
	public static final String PIBLASTER = "initPiBlaster.sh";

	public static void main(String[] args) {
			Home home = new Home();
//			home.initPiBlaster();
			home.readXML();
			Terminal t = new Terminal(home);
			t.start();
			new KaaConnection(home);

	}

}
