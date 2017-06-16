package smart_things.light.control;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * This class is the interface between the user and the LightControl system
 * based on terminal commands.
 * 
 * @author Studienprojekt, Mario Maser
 *
 */
public class Terminal extends Thread {

	private Home home;
	private InputStreamReader isr;
	private BufferedReader br;

	public Terminal(Home home) {
		this.home = home;
		isr = new InputStreamReader(System.in);
		br = new BufferedReader(isr);
	}

	@Override
	public void run() {
		ui();
	}

	/**
	 * Controls all IO from the user in the terminal
	 */
	private void ui() {
		String input = "";
		System.out.println("Smart home LightControl -- v1.0 started");
		System.out.println("Type --help for more information");
		while (true) {
			try {
				input = br.readLine();
			} catch (IOException e) {
				System.err.println("No valid input");
			}
			String[] args = input.split(" ");
			try {

				switch (args[0]) {
				case "add-room":
					home.addRoom(args[1]);
					listRooms();
					break;
				case "remove-room":
					home.removeRoom(args[1]);
					listRooms();
					break;
				case "add-light":
					addStandardLight(args);
					listLights();
					break;
				case "add-RGBLight":
					addLight(args);
					listLights();
					break;
				case "remove-light":
					home.removeLight(args[1]);
					listLights();
					break;
				case "set-brightness":
					setBrightness(args);
					break;
				case "set-color":
					setColor(args);
					break;
				case "list-rooms":
					listRooms();
					break;
				case "list-lights":
					listLights();
					break;
				case "--help":
					printHelp();
					break;
				case "-h":
					printHelp();
					break;
				case "addEmRoom":
					addEMRoom(args);
					break;
				case "removeEmRoom":
					removeEMRoom(args);
					break;
				case "setEmergency":
					setEmergency(args);
					break;
				case "listEmRooms":
					listEmRooms();
					break;
				case "exit":
					System.exit(0);
					break;
				default:
					printHelp();
					break;

				}
			} catch (ArrayIndexOutOfBoundsException e) {
				System.err.println("Check the parameters!");
			}
		}
	}
	
	/**
	 * prints a list of all rooms in the emergency path
	 */
	private void listEmRooms(){
		for(Room r : home.getEmergencyPath()){
			System.out.println("EmRoom: " + r.getId());
		}
	}
	
	/**
	 * Adds a room to the emergency path
	 * @param args
	 */
	private void addEMRoom(String[] args){
		String id = args[1];
		home.addRoomToEmergencyPath(home.getRoom(id));
	}
	
	/**
	 * Removes the room with the specified id from the emergency path list
	 * @param args
	 */
	private void removeEMRoom(String[] args){
		String id = args[1];
		home.removeRoomFromEmergencyPath(home.getRoom(id));
	}
	
	/**
	 * Activate emergency light settings
	 * @param args
	 */
	private void setEmergency(String[] args){
		String bool = args[1];
		home.setEmergency(bool.equals("true") ? true : false);
	}
	
	/**
	 * Sets the color to the given rgb light
	 * @param args
	 */
	private void setColor(String[] args){
		String id = args[1];
		int red = Integer.parseInt(args[2]);
		int green = Integer.parseInt(args[3]);
		int blue = Integer.parseInt(args[4]);
		home.setColor(id, red, green, blue);
	}

	/**
	 * prints the registered lights
	 */
	private void listLights() {
		for (Light l : home.getLights()) {
			System.out.println("Light ID: " + l.getId());
		}
	}

	/**
	 * prints the registered rooms
	 */
	private void listRooms() {
		for (Room r : home.getRooms()) {
			System.out.println("Room ID: " + r.getId());
		}
	}

	/**
	 * adds ad standard (white) light
	 * @param args
	 */
	private void addStandardLight(String[] args) {
		try {
			home.addLight(new StandardLight(args[1], Integer.parseInt(args[2])), args[3]);
		} catch (NumberFormatException e) {
			System.err.println("Could not parse input!");
		}
	}

	/**
	 * sets the brightness of the light with the specified id
	 * @param args
	 */
	private void setBrightness(String[] args) {
		try {
			home.setBrightness(args[1], Integer.parseInt(args[2]));
		} catch (NumberFormatException e) {
			System.err.println("Could not parse input!");
		}
	}

	/**
	 * Adds a light with input parameters
	 * 
	 * @param args
	 *            user input as String[] splitted by " " (SPACE)
	 */
	private void addLight(String[] args) {
		try {

			home.addLight(new RGBLight(args[1], new LED('r', Integer.parseInt(args[2])), new LED('g', Integer.parseInt(args[3])),
					new LED('b', Integer.parseInt(args[4]))), args[5]);
		} catch (NumberFormatException e) {
			System.err.println("Could not parse input!");
		}
	}

	/**
	 * prints help information screen
	 */
	private void printHelp() {
		System.out.println("Arguments are seperated with SPACE");
		System.out.println();
		System.out.println("add-room \t[ID] where ID is a distinct id for the room");
		System.out.println("remove-room \t[ID] where ID is a distinct id for the room.");
		System.out.println("add-light \t[ID] [GpioPort] 	[RoomID]");
		System.out.println("add-RGBLight \t[ID] [GpioPortRed] 	[GpioPortGreen] [GpioPortBlue] [RoomID]");
		System.out.println("remove-light \t[ID]");
		System.out.println("set-brightness \t[ID] [0-255]");
		System.out.println("set-color \t[ID] [red] [green] [blue]");
		System.out.println("list-lights \tShows a list of all registered lights");
		System.out.println("list-rooms \tShows a list of all registered rooms");
		System.out.println("addEmRoom \t[ID]");
		System.out.println("removeEmRoom \t[ID]");
		System.out.println("setEmergency \t[true, false]");
		System.out.println("--help or -h 	Prints this informatione screen");
		System.out.println();
	}

}
