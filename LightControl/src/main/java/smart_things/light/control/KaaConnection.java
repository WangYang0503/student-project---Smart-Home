package smart_things.light.control;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.kaaproject.kaa.client.DesktopKaaPlatformContext;
import org.kaaproject.kaa.client.Kaa;
import org.kaaproject.kaa.client.KaaClient;
import org.kaaproject.kaa.client.SimpleKaaClientStateListener;
import org.kaaproject.kaa.client.event.EventFamilyFactory;
import org.kaaproject.kaa.client.event.FindEventListenersCallback;
import org.kaaproject.kaa.client.event.registration.UserAttachCallback;
import org.kaaproject.kaa.common.endpoint.gen.SyncResponseResultType;
import org.kaaproject.kaa.common.endpoint.gen.UserAttachResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import smart_things.light.schema.*;


public class KaaConnection implements Observer {

	private static final String USER_EXTERNAL_ID = "coffee_user";
	private static final String USER_ACCESS_TOKEN = "coffee_user_token";
	private static KaaClient kaaClient = null;
	private static LightEventClassFamily eventFamily = null;
	private static EventFamilyFactory eventFamilyFactory = null;
	//TODO DEPRECATED:
	//private static SmokeDetectorEventFamily smokeDetector = null;
	private static final Logger LOG = LoggerFactory.getLogger(KaaConnection.class);
	// private static final int ON = 255;
	// private static final int OFF = 0;

	private Home home;

	public KaaConnection(Home home) {
		this.home = home;
		kaaClient = Kaa.newClient(new DesktopKaaPlatformContext(), new SimpleKaaClientStateListener() {
			@Override
			public void onStarted() {
				// System.out.println("*****Server started*****");
				LOG.info("Server started...");
			}

			@Override
			public void onStopped() {
				// System.out.println("*****Server stopped*****");
				LOG.info("Server stopped");
			}
		});

		kaaClient.start();

		kaaClient.attachUser(USER_EXTERNAL_ID, USER_ACCESS_TOKEN, new UserAttachCallback() {
			@Override
			public void onAttachResult(UserAttachResponse response) {
				if (response.getResult() == SyncResponseResultType.SUCCESS) {
					// System.out.println("User succesfully attached!");
					LOG.info("User succesfully attached!");
					onUserAttached();
				} else {
					// System.err.println("Could not attach user!");
					LOG.error("Could not attach user!");
				}
			}
		});

		home.setObserver(this);
	}

	/**
	 * Send system information to all participants
	 */
	private void sendLEDInfoResponse() {
		java.util.List<LEDInfoObject> ledInfoList = home.getLEDInfoObjects();
		eventFamily.sendEventToAll(new LEDInfoResponseEvent(ledInfoList));
	}

	/**
	 * Creates a List-Object and adds all room-id's to send a RoomInfoResponse
	 * Event
	 */
	private void sendRoomInfoResponse() {
		List<String> roomInfoList = new ArrayList<>();
		for (Room r : home.getRooms()) {
			try {
				roomInfoList.add(r.getId());
			} catch (NumberFormatException e) {
				System.err.println("No valid room id!");
			}
		}
		
		eventFamily.sendEventToAll(new RoomInfoResponseEvent(roomInfoList));
	}

	private void onUserAttached() {
		// call the functions of the Home instance
		eventFamilyFactory = kaaClient.getEventFamilyFactory();
		eventFamily = eventFamilyFactory.getLightEventClassFamily();
		//TODO DEPRECATED:
		//smokeDetector = eventFamilyFactory.getSmokeDetectorEventFamily();
		eventFamily.addListener(new LightEventClassFamily.Listener() {

			@Override
			public void onEvent(LEDInfoRequestEvent event, String source) {
				System.out.println("Info request recieved...");
				sendLEDInfoResponse();
			}

			@Override
			public void onEvent(RoomInfoRequestEvent event, String source) {
				sendRoomInfoResponse();
			}

			@Override
			public void onEvent(AddRoomEvent event, String source) {
				System.out.println("Adding room...");
				home.addRoom(event.getRoomID());

			}

			@Override
			public void onEvent(RemoveRoomEvent event, String source) {
				System.out.println("Removing room...");
				home.removeRoom(event.getRoomID());

			}

			@Override
			public void onEvent(AddLightEvent event, String source) {
				System.out.println("Adding light...");
				Light l = new StandardLight(event.getLightID(), event.getSlot());
				home.addLight(l, event.getRoomID());

			}

			@Override
			public void onEvent(AddRGBLightEvent event, String source) {
				System.out.println("Adding RGB-Light...");
				Light l = new RGBLight(event.getLightID(), new LED('r', event.getSlotRed()),
						new LED('g', event.getSlotGreen()), new LED('b', event.getSlotBlue()));
				home.addLight(l, event.getRoomID());

			}

			@Override
			public void onEvent(RemoveLightEvent event, String source) {
				System.out.println("Removing light...");
				home.removeLight(event.getLightID());

			}

			@Override
			public void onEvent(SetBrightnessEvent event, String source) {
				System.out.println("Setting brightness...");
				home.setBrightness(event.getLightID(), event.getBrightness());

			}

			@Override
			public void onEvent(SetColorEvent event, String source) {
				System.out.println("Setting color...");
				home.setColor(event.getLightID(), event.getRed(), event.getGreen(), event.getBlue());

			}

			@Override
			public void onEvent(SetRoomBrightnessEvent event, String source) {
				System.out.println("Setting room brightness...");
				home.setRoomBrightness(event.getRoomID(), event.getBrightness());

			}

			@Override
			public void onEvent(SetRoomColorEvent event, String source) {
				System.out.println("Setting room color...");
				home.setRoomColor(event.getRoomID(), event.getRed(), event.getGreen(), event.getBlue());

			}

			@Override
			public void onEvent(SetEmergencyMode event, String source) {
				home.setEmergency(event.getIsEmergency());
				
			}
		});

		//TODO DEPRECATED (following block):
		/*
		smokeDetector.addListener(new SmokeDetectorEventFamily.Listener() {

			@Override
			public void onEvent(backToNormalEvent event, String source) {
				home.setEmergency(false);

			}

			@Override
			public void onEvent(smokeDetectionEvent event, String source) {
				home.setEmergency(true);

			}

			@Override
			public void onEvent(InfoResponseEvent event, String source) {
				// Not used in light control system

			}
		});*/
		
		findListeners();

	}

	@Override
	public void update(Observable o, Object arg) {
		sendLEDInfoResponse();
	}

	/**
	 * Finds all active event listeners of other running KAA endpoints so that
	 * events can be sent to these endpoints afterwards.
	 */
	private void findListeners() {
		// Check whether the KAA client is already instanciated
		if (kaaClient == null) {
			throw new IllegalStateException("KAA client connection is not initialized!");
		}

		// Filter: Only the listeners that receive events which are specified in
		// this list will be found
		List<String> listenerFqns = new LinkedList<>();
		listenerFqns.add(KaaConnection.class.getName());

		// Find all the listeners listening to events which are element of the
		// FQNs list
		kaaClient.findEventListeners(listenerFqns, new FindEventListenersCallback() {

			/**
			 * Called when the event listeners are received. Gives to
			 * possibility to perform any necessary actions with the obtained
			 * event listeners
			 * 
			 * @param eventListeners
			 *            The received event listeners
			 */
			@Override
			public void onEventListenersReceived(List<String> eventListeners) {
				System.out.println(eventListeners.size() + " event listeners received");
			}

			/**
			 * Called when the search request failed. Gives the possibility to
			 * perform any necessary actions in case of failure.
			 */
			@Override
			public void onRequestFailed() {
				System.err.println("Request failed");
			}
		});
	}
	

}
