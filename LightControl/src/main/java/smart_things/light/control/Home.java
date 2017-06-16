package smart_things.light.control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;
import javax.swing.Timer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.pi4j.io.gpio.exception.GpioPinExistsException;

import smart_things.light.schema.LEDInfoObject;

/**
 * The Instance of this class contains all rooms and light of the house.
 * 
 * @author Studienprojekt, Mario Maser
 *
 */
public class Home extends Observable {
	/*
	 * XML Constants: Tag names of XML elements to read in.
	 */
	public static final String ROOM = "room";
	public static final String ROOM_ID = "id";
	public static final String LIGHT = "light";
	public static final String LIGHT_ID = "id";
	public static final String RGB_LIGHT = "rgbLight";
	public static final String RGB_ID = "id";
	public static final String BRIGHTNESS = "brightness";
	public static final String BRIGHTNESS_RED = "red";
	public static final String BRIGHTNESS_GREEN = "green";
	public static final String BRIGHTNESS_BLUE = "blue";
	public static final String GPIO = "gpio";
	public static final String GPIO_RED = "gpio_red";
	public static final String GPIO_GREEN = "gpio_green";
	public static final String GPIO_BLUE = "gpio_blue";
	public static final String NOXML = "No XML file found!";
	public static final String XML_SCHEMA = "smarthome.xsd";
	public static final String ROOTELEMENT = "smarthome";
	public static final String EMERGENCY_PATH = "emergencyPath";
	public static final String EMERGENCY_PATH_ROOM = "emergencyPathRoom";

	/*
	 * 6 standard rooms of the house model
	 */
	public static final String CORRIDOR = "Flur";
	public static final String OFFICE = "Buero";
	public static final String SLEEPINGROOM = "Schlafzimmer";
	public static final String LIVINGROOM = "Wohnzimmer";
	public static final String CITCHTEN = "Kueche";
	public static final String GARDEN = "Garten";

	private String path = "";
	private LinkedList<Room> rooms;
	private LinkedList<Light> lights;
	private boolean readingXML = false;
	private boolean isEmergency = false;
	private LinkedList<Room> emergencyPath;
	private Timer timer;

	/**
	 * Standard Constructor
	 */
	public Home() {
		rooms = new LinkedList<>();
		lights = new LinkedList<>();
		emergencyPath = new LinkedList<>();

	}

	/**
	 * Constructor
	 * 
	 * @param rooms
	 *            List of rooms, the smart home provides
	 * @param lights
	 *            list of lights, the smart home provides
	 */
	public Home(LinkedList<Room> rooms, LinkedList<Light> lights) {
		this.rooms = rooms;
		this.lights = lights;
		emergencyPath = new LinkedList<>();

	}

	/**
	 * Registers a observer
	 * 
	 * @param o
	 *            Observer
	 */
	public void setObserver(Observer o) {
		this.addObserver(o);
	}

	/**
	 * 
	 * @param oldID
	 *            Actual ID of the room
	 * @param newID
	 *            New ID of the room
	 */
	public void changeRoomID(String oldID, String newID) {
		for (Room r : rooms) {
			if (r.getId().equals(oldID)) {
				r.setId(newID);
				createXML();
				this.setChanged();
				notifyObservers();
			}
		}
	}

	/**
	 * 
	 * @param id
	 *            ID of the room
	 * @return true, if room already exists, else false
	 */
	private boolean roomExists(String id) {
		for (Room r : rooms) {
			if (r.getId().equals(id)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Adds new instance to the list of rooms
	 * 
	 * @param id
	 *            Distinct id of the room
	 */
	public synchronized void addRoom(String id) {
		if (!roomExists(id)) {
			rooms.add(new Room(id));
			if (!readingXML) {
				createXML();
				this.setChanged();
				notifyObservers();
			}
		}
	}

	/**
	 * 
	 * @param roomId
	 *            Distinct id of the room
	 * @return first occurrence of the room
	 */
	public synchronized Room getRoom(String roomId) {
		Room r = null;
		for (int i = 0; i < rooms.size(); i++) {
			if (rooms.get(i).getId().equals(roomId)) {
				r = rooms.get(i);
			}
		}
		return r;
	}

	/**
	 * Removes the first occurrence of a room with the specified id
	 * 
	 * @param id
	 *            Distinct id of the room
	 */
	public synchronized void removeRoom(String id) {
		for (int i = 0; i < rooms.size(); i++) {
			if (rooms.get(i).getId().equals(id)) {
				rooms.get(i).removeAllLights();
				rooms.remove(i);
			}
		}
		if (!readingXML) {
			createXML();
			this.setChanged();
			notifyObservers();
		}

	}

	/**
	 * Adds a instance of Light to the list of lights in the room if the room
	 * exists.
	 * 
	 * @param light
	 *            instance of type Light
	 * @param roomId
	 *            Distinct id of the room
	 */
	public synchronized void addLight(Light light, String roomId) {
		boolean roomExists = false;
		try {
			for (int i = 0; i < rooms.size(); i++) {
				if (rooms.get(i).getId().equals(roomId)) {
					// Check, if port is already used
					if (!portUsed(light)) {
						// if not, add light to the list of lights
						rooms.get(i).addLight(light);
						lights.add(light);
					}
					roomExists = true;
				}
			}
			// Creates a Room, if it does not exist and adds the light to the
			// Room
			if (!roomExists) {
				addRoom(roomId);
				addLight(light, roomId);
			}
			if (!readingXML) {
				createXML();
				this.setChanged();
				notifyObservers();
			}

		} catch (GpioPinExistsException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param light
	 *            Instance of the light that shell be checked.
	 * @return true if gpio port is used, else false
	 */
	private boolean portUsed(Light light) {
		for (Light l : lights) {
			if (l.getGpioPort() == light.getGpioPort()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Removes each time the first occurrence of the light in rooms and the main
	 * list "lights". If the id is distinct, all occurrences of the light will
	 * be removed.
	 * 
	 * @param lightId
	 *            Distinct id of the light to be removed
	 */
	public synchronized void removeLight(String lightId) {
		Light light = null;
		/*
		 * At first get instance of the light with specified id. Remove this
		 * instance from each room Remove this instance from the list lights
		 */
		for (int i = 0; i < lights.size(); i++) {
			if (lights.get(i).getId().equals(lightId)) {
				light = lights.get(i);
			}
		}
		for (int j = 0; j < rooms.size(); j++) {
			rooms.get(j).removeLight(lightId);
		}
		light.releasePin();
		lights.remove(light);
		if (!readingXML) {
			createXML();
			this.setChanged();
			notifyObservers();
		}
	}

	/**
	 * Sets the brightness of a light to a value between 0 and 255. A RGB-Light
	 * is set to the color "white" to the given brightness.
	 * 
	 * @param lightId
	 *            Distinct ID of the light
	 * @param brightness
	 */
	public synchronized void setBrightness(String lightId, int brightness) {
		for (int i = 0; i < lights.size(); i++) {
			if (lights.get(i).getId().equals(lightId)) {
				lights.get(i).setBrightness(brightness);
				try {
					writeBrightness(lights.get(i));
					this.setChanged();
					notifyObservers();
				} catch (ParserConfigurationException | SAXException | IOException | TransformerException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Sets the color of all lights in this room to white and the brightness of
	 * all lights to the given value.
	 * 
	 * @param roomID
	 *            Distinct ID of the room
	 * @param brightness
	 *            Value between 0 and 255
	 */
	public void setRoomBrightness(String roomID, int brightness) {
		Room temp = getRoom(roomID);
		if (temp != null) {
			for (Light l : temp.getLights()) {
				l.setBrightness(brightness);
			}
			this.setChanged();
			notifyObservers();
		}

	}

	/**
	 * The Brightness is a value between 0 (light is off) and 255 (light is at
	 * max. brightness)
	 * 
	 * @param lightId
	 *            Distinct ID of the light
	 * @return Value between 0 (low) and 255 (high)
	 */
	public synchronized int getBrightness(String lightId) {
		int brightness = -1;
		for (int i = 0; i < lights.size(); i++) {
			if (lights.get(i).getId().equals(lightId)) {
				brightness = lights.get(i).getBrightness();
			}
		}
		return brightness;
	}

	/**
	 * 
	 * @param id
	 *            ID of the light
	 * @param red
	 *            0-255
	 * @param green
	 *            0-255
	 * @param blue
	 *            0-255
	 */
	public synchronized void setColor(String id, int red, int green, int blue) {
		for (int i = 0; i < lights.size(); i++) {
			if (lights.get(i).getId().equals(id)) {
				if (lights.get(i) instanceof RGBLight) {
					RGBLight rgb = (RGBLight) lights.get(i);
					rgb.setRed(red);
					rgb.setGreen(green);
					rgb.setBlue(blue);
				}
				try {
					writeBrightness(lights.get(i));
					this.setChanged();
					notifyObservers();
				} catch (ParserConfigurationException | SAXException | IOException | TransformerException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Sets the color of all lights in the given room to the given value.
	 * 
	 * @param roomID
	 *            Distinct ID of the room
	 * @param red
	 *            Value between 0-255
	 * @param green
	 *            Value between 0-255
	 * @param blue
	 *            Value between 0-255
	 */
	public void setRoomColor(String roomID, int red, int green, int blue) {
		Room temp = getRoom(roomID);
		if (temp != null) {
			for (Light l : temp.getLights()) {
				if (l instanceof RGBLight) {
					((RGBLight) l).setRed(red);
					((RGBLight) l).setGreen(green);
					((RGBLight) l).setBlue(blue);
				} else {
					l.setBrightness((red + green + blue) / 3);
				}
				if (!isEmergency) {
					try {
						writeBrightness(l);
						this.setChanged();
						notifyObservers();
					} catch (ParserConfigurationException | SAXException | IOException | TransformerException e) {
						e.printStackTrace();
					}
				}

			}
		}
	}

	public LinkedList<Room> getRooms() {
		return rooms;
	}

	public LinkedList<Light> getLights() {
		return lights;
	}

	/**
	 * 
	 * @return list of LEDInfoObjects of all registered instances of Light
	 */
	public java.util.List<LEDInfoObject> getLEDInfoObjects() {
		java.util.List<LEDInfoObject> list = new ArrayList<>();
		for (Room r : rooms) {
			for (Light l : r.getLights()) {
				if (l instanceof RGBLight) {
					RGBLight rgb = (RGBLight) l;
					LEDInfoObject ledObject = new LEDInfoObject(l.getId(), r.getId(), l.isRGB(), rgb.getRed(),
							rgb.getGreen(), rgb.getBlue(), rgb.getBrightness());
					list.add(ledObject);

				} else {
					StandardLight light = (StandardLight) l;
					LEDInfoObject lightObject = new LEDInfoObject(l.getId(), r.getId(), l.isRGB(), -1, -1, -1,
							light.getBrightness());
					list.add(lightObject);
				}
			}
		}

		return list;
	}

	public String getPath() {
		return path;
	}

	/**
	 * The XML-File smarthome.xml must be in this folder
	 * 
	 * @param path
	 *            Execution directory
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * The emergency path contains all rooms which must be crossed by the user
	 * to get to the exit
	 * 
	 * @param emergencyPath
	 *            List of rooms
	 */
	public void setEmergencyPath(LinkedList<Room> emergencyPath) {
		this.emergencyPath = emergencyPath;
	}

	/**
	 * Add room to the emergency Path
	 * 
	 * @param room
	 *            Room
	 */
	public void addRoomToEmergencyPath(Room room) {
		emergencyPath.add(room);
		if (!readingXML) {
			createXML();
		}
	}

	/**
	 * Removes a room from the emergency path
	 * 
	 * @param room
	 *            Instance of Room
	 */
	public void removeRoomFromEmergencyPath(Room room) {
		emergencyPath.remove(room);
		if (!readingXML) {
			createXML();
		}
	}

	/**
	 * Emergency path is set to green, while all other rooms are set to red
	 * light
	 * 
	 * @param emergency
	 */
	public void setEmergency(Boolean emergency) {
		this.isEmergency = emergency;
		LinkedList<Room> redRooms = new LinkedList<>();
		redRooms.addAll(rooms);
		redRooms.removeAll(emergencyPath);
		if (emergency) {
			for (Room r : redRooms) {
				setRoomColor(r.getId(), 255, 0, 0);
			}
			for (Room r : emergencyPath) {
				setRoomColor(r.getId(), 0, 255, 0);
			}
			
			initTimer(500, redRooms);
            timer.start();

		} else {
			timer.stop();
			timer = null;
			for (Room r : rooms) {
				setRoomColor(r.getId(), 255, 255, 255);
			}
		}
		
	}

	public LinkedList<Room> getEmergencyPath() {
		return emergencyPath;
	}
	
	/**
	 * @param delayTime in ms
	 * @param rooms rooms where the LEDs shell blink
	 */
	private void initTimer(int delayTime, LinkedList<Room> rooms){
		if(timer != null){
			timer.stop();
			timer = null;
		}
		timer = new Timer(delayTime, new ActionListener() {
			boolean on = true;
			@Override
			public void actionPerformed(ActionEvent e) {
				on = !on;
				if(on){
					for(Room r : rooms){
						setRoomColor(r.getId(), 255, 0, 0);
					}
				}else{
					for(Room r : rooms){
						setRoomColor(r.getId(), 35, 0, 0);
					}
				}
			}
		});
		timer.setRepeats(true);
        timer.setCoalesce(true);
	}

	/**
	 * Call this method to read all instances of rooms, lights and the
	 * brightness level of each light from a XML file.
	 *
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public void readXML() {
		readingXML = true;
		/*
		 * Read in all instances of StandardLight and add them to the rooms
		 */
		try {
			File inputFile = new File(path + Main.XMLFILE);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();
			NodeList lightList = doc.getElementsByTagName(LIGHT);
			NodeList rgbList = doc.getElementsByTagName(RGB_LIGHT);
			NodeList roomList = doc.getElementsByTagName(ROOM);
			NodeList emRoomList = doc.getElementsByTagName(EMERGENCY_PATH_ROOM);
			/*
			 * Read in all instances of StandardLight and add them to their
			 * rooms
			 */
			readStandardLights(lightList);

			/*
			 * Read in all instances of RGBLight and add them to their rooms
			 */
			readRGBLights(rgbList);
			/*
			 * Adding room instances to the list of rooms
			 */
			for (int temp = 0; temp < roomList.getLength(); temp++) {
				Node roomNode = roomList.item(temp);
				Node id = (roomNode.getAttributes().getNamedItem(ROOM_ID));
				this.addRoom(id.getTextContent());
			}

			/*
			 * Reading all rooms of the emergency path
			 */
			readEmergencyPath(emRoomList);

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		} finally {
			readingXML = false;
		}
		readingXML = false;

	}

	/**
	 * Read in all instances of StandardLight and add them to their rooms
	 * 
	 * @param lightList
	 *            NodeList with instances of StandardLight
	 */
	private void readStandardLights(NodeList lightList) {
		for (int temp = 0; temp < lightList.getLength(); temp++) {
			Node lightNode = lightList.item(temp);
			if (lightNode.getNodeType() == Node.ELEMENT_NODE) {
				/*
				 * Creating instance of StandardLight with ID, GPIO port, Room
				 * ID and brightness, given from the XML document
				 */
				Element child = (Element) lightNode.getChildNodes();
				Element parent = (Element) child.getParentNode();
				Light l = new StandardLight(child.getAttribute(LIGHT_ID),
						Integer.parseInt(child.getElementsByTagName(GPIO).item(0).getTextContent()));
				this.addLight(l, parent.getAttribute(ROOM_ID));
				this.setBrightness(child.getAttribute(LIGHT_ID),
						Integer.parseInt(child.getElementsByTagName(BRIGHTNESS).item(0).getTextContent()));
			}
		}
	}

	/**
	 * Read in all instances of RGBLight and add them to their rooms
	 * 
	 * @param rgbList
	 *            NodeList with instances of RGBLight
	 */
	private void readRGBLights(NodeList rgbList) {
		for (int temp = 0; temp < rgbList.getLength(); temp++) {
			Node rgbLightNode = rgbList.item(temp);
			if (rgbLightNode.getNodeType() == Node.ELEMENT_NODE) {
				Element child = (Element) rgbLightNode.getChildNodes();
				Element parent = (Element) child.getParentNode();
				/*
				 * Creating instance of RGBLight with ID, GPIO ports of each
				 * LED, Room ID and brightness of each LED, given from the XML
				 * document
				 */
				String id = child.getAttribute(LIGHT_ID);
				LED red = new LED('r', Integer.parseInt(child.getElementsByTagName(GPIO_RED).item(0).getTextContent()));
				LED green = new LED('g',
						Integer.parseInt(child.getElementsByTagName(GPIO_GREEN).item(0).getTextContent()));
				LED blue = new LED('b',
						Integer.parseInt(child.getElementsByTagName(GPIO_BLUE).item(0).getTextContent()));
				RGBLight l = new RGBLight(id, red, green, blue);
				l.setRed(Integer.parseInt(child.getElementsByTagName(BRIGHTNESS_RED).item(0).getTextContent()));
				l.setGreen(Integer.parseInt(child.getElementsByTagName(BRIGHTNESS_GREEN).item(0).getTextContent()));
				l.setBlue(Integer.parseInt(child.getElementsByTagName(BRIGHTNESS_BLUE).item(0).getTextContent()));
				this.addLight(l, parent.getAttribute(ROOM_ID));
			}
		}
	}

	/**
	 * Reads all instances of Room in the xml file
	 * 
	 * @param emRoomList
	 *            Nodelist with all room instances of the em path
	 */
	private void readEmergencyPath(NodeList emRoomList) {
		for (int temp = 0; temp < emRoomList.getLength(); temp++) {
			Node emRoomNode = emRoomList.item(temp);
			Node emRoomId = (emRoomNode.getAttributes().getNamedItem(ROOM_ID));
			this.addRoomToEmergencyPath(this.getRoom(emRoomId.getTextContent()));
		}
	}

	/**
	 * Modifies a XML File with current brightness of the given light (id)
	 * 
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws SAXException
	 * @throws TransformerException
	 */
	public void writeBrightness(Light l)
			throws ParserConfigurationException, SAXException, IOException, TransformerException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(path + Main.XMLFILE);
		doc.getDocumentElement().normalize();
		if (l instanceof StandardLight) {
			NodeList lightList = doc.getElementsByTagName(LIGHT);
			for (int i = 0; i < lightList.getLength(); i++) {
				Element child = (Element) lightList.item(i);
				NamedNodeMap attr = lightList.item(i).getAttributes();
				Node nodeAttr = attr.getNamedItem(LIGHT_ID);
				if (nodeAttr.getTextContent().equals(l.getId())) {
					child.getElementsByTagName(BRIGHTNESS).item(0).setTextContent(l.getBrightness() + "");
				}
			}
		} else if (l instanceof RGBLight) {
			RGBLight rgb = (RGBLight) l;
			NodeList rgbList = doc.getElementsByTagName(RGB_LIGHT);
			for (int i = 0; i < rgbList.getLength(); i++) {
				Element child = (Element) rgbList.item(i);
				NamedNodeMap attr = rgbList.item(i).getAttributes();
				Node nodeAttr = attr.getNamedItem(RGB_ID);
				if (nodeAttr.getTextContent().equals(l.getId())) {
					child.getElementsByTagName(BRIGHTNESS_RED).item(0).setTextContent("" + rgb.getRed());
					child.getElementsByTagName(BRIGHTNESS_GREEN).item(0).setTextContent("" + rgb.getGreen());
					child.getElementsByTagName(BRIGHTNESS_BLUE).item(0).setTextContent("" + rgb.getBlue());
				}
			}
		}

		// write the content into xml file
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(path + Main.XMLFILE));
		transformer.transform(source, result);
	}

	/**
	 * Creates a new XML File if it does not exist
	 */
	public void createXML() {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;
		try {
			docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			// root element
			Element rootElement = doc.createElement(ROOTELEMENT);
			doc.appendChild(rootElement);
			Element emergencyPathElement = doc.createElement(EMERGENCY_PATH);

			for (Room r : rooms) {
				Element roomElement = doc.createElement(ROOM);
				roomElement.setAttribute(ROOM_ID, r.getId());
				/*
				 * Adding lights of the room to the XML Document
				 */
				for (Light l : r.getLights()) {
					if (l instanceof StandardLight) {
						roomElement.appendChild(createLightElement((StandardLight) l, doc));
					} else if (l instanceof RGBLight) {
						roomElement.appendChild(createRGBElement((RGBLight) l, doc));
					}
				}
				rootElement.appendChild(roomElement);
			}

			/*
			 * Adding all rooms of emergency path
			 */
			for (Room r : emergencyPath) {
				Element emPathRoom = doc.createElement(EMERGENCY_PATH_ROOM);
				emPathRoom.setAttribute(ROOM_ID, r.getId());
				emergencyPathElement.appendChild(emPathRoom);
			}
			rootElement.appendChild(emergencyPathElement);

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);

			/*
			 * Pretty print
			 */
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

			/*
			 * Write XML File
			 */
			StreamResult result = new StreamResult(new File(path + Main.XMLFILE));
			transformer.transform(source, result);

		} catch (ParserConfigurationException | TransformerException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates XML Element of the given light
	 * 
	 * @param l
	 *            instance of StandardLight
	 * @param doc
	 *            Document, the instance is going to be appended at
	 * @return Element to append to the root node of the Document
	 */
	private Element createLightElement(StandardLight l, Document doc) {
		StandardLight light = (StandardLight) l;
		Element lightElement = doc.createElement(LIGHT);
		lightElement.setAttribute(LIGHT_ID, l.getId());
		// Append gpio
		Element gpio = doc.createElement(GPIO);
		gpio.setTextContent(light.getGpioPort() + "");
		lightElement.appendChild(gpio);
		// Append brightness
		Element brightness = doc.createElement(BRIGHTNESS);
		brightness.setTextContent(light.getBrightness() + "");
		lightElement.appendChild(brightness);

		return lightElement;
	}

	/**
	 * Creates XML Element of the given light
	 * 
	 * @param l
	 *            instance of RGBLight
	 * @param doc
	 *            Document, the instance is going to be appended at
	 * @return Element to append to the root node of the Document
	 */
	private Element createRGBElement(RGBLight l, Document doc) {
		RGBLight light = (RGBLight) l;
		Element lightElement = doc.createElement(RGB_LIGHT);
		lightElement.setAttribute(RGB_ID, l.getId());
		// Append gpio_red
		Element gpioRed = doc.createElement(GPIO_RED);
		gpioRed.setTextContent(light.getGPIORed() + "");
		lightElement.appendChild(gpioRed);
		// Append gpio_green
		Element gpioGreen = doc.createElement(GPIO_GREEN);
		gpioGreen.setTextContent(light.getGPIOGreen() + "");
		lightElement.appendChild(gpioGreen);
		// Append gpio_blue
		Element gpioBlue = doc.createElement(GPIO_BLUE);
		gpioBlue.setTextContent(light.getGPIOBlue() + "");
		lightElement.appendChild(gpioBlue);
		// Append brightness_red
		Element red = doc.createElement(BRIGHTNESS_RED);
		red.setTextContent(light.getRed() + "");
		lightElement.appendChild(red);
		// Append brightness_green
		Element green = doc.createElement(BRIGHTNESS_GREEN);
		green.setTextContent(light.getGreen() + "");
		lightElement.appendChild(green);
		// Append brightness_blue
		Element blue = doc.createElement(BRIGHTNESS_BLUE);
		blue.setTextContent(light.getBlue() + "");
		lightElement.appendChild(blue);
		return lightElement;
	}

	/**
	 * Returns the path where the currently running JAR-file is located. Example
	 * value: C:\MyProject\build\jar\
	 * 
	 * @return Path of the JAR-file
	 */
	private static String getJarExecutionDirectory() {
		String jarFile = null;
		String jarDirectory = null;
		int cutFileSeperator = 0;
		// int cutSemicolon = -1;

		try {
			jarFile = System.getProperty("java.class.path");
			// Cut seperators
			cutFileSeperator = jarFile.lastIndexOf(System.getProperty("file.separator"));
			if (cutFileSeperator > 0) {
				jarDirectory = jarFile.substring(0, cutFileSeperator);
			} else {
				jarDirectory = "";
			}
			// Cut semicolons
			// cutSemicolon = jarDirectory.lastIndexOf(';');
			// jarDirectory = jarDirectory.substring(cutSemicolon + 1,
			// jarDirectory.length());
		} catch (StringIndexOutOfBoundsException e) {
			e.printStackTrace();
		}
		if (jarDirectory.length() > 0) {
			return jarDirectory + System.getProperty("file.separator");
		} else {
			return jarDirectory;
		}
	}

	/**
	 * Sets the execution path of this application. This Method can be extended
	 * with all necessary parameters, the application needs at startup.
	 */
	public void initPiBlaster() {
		String executionDirectory = getJarExecutionDirectory();
		System.out.println("Execution directory: " + executionDirectory);
		this.setPath(executionDirectory);
		String scriptDir = executionDirectory;
		try {
			if (executionDirectory.equals("")) {
				executionDirectory += "./";
			}
			String[] arg = new String[] { executionDirectory + Main.PIBLASTER, scriptDir };
			ProcessBuilder pb = new ProcessBuilder(arg);
			Process p = pb.start(); // Start the process.
			p.waitFor(); // Wait for the process to finish.
			System.out.println("Script executed successfully");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
