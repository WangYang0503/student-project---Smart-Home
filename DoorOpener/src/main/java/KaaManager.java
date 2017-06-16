import java.util.LinkedList;
import java.util.List;

import org.kaaproject.kaa.client.DesktopKaaPlatformContext;
import org.kaaproject.kaa.client.Kaa;
import org.kaaproject.kaa.client.KaaClient;
import org.kaaproject.kaa.client.SimpleKaaClientStateListener;
import org.kaaproject.kaa.client.event.EventFamilyFactory;
import org.kaaproject.kaa.client.event.FindEventListenersCallback;
import org.kaaproject.kaa.client.event.registration.UserAttachCallback;
import org.kaaproject.kaa.common.endpoint.gen.SyncResponseResultType;
import org.kaaproject.kaa.common.endpoint.gen.UserAttachResponse;

import smart_things.door.schema.CloseDoorEvent;
import smart_things.door.schema.CloseGarageEvent;
import smart_things.door.schema.DoorClosedEvent;
import smart_things.door.schema.DoorEventClassFamily;
import smart_things.door.schema.DoorOpenedEvent;
import smart_things.door.schema.GarageClosedEvent;
import smart_things.door.schema.GarageOpenedEvent;
import smart_things.door.schema.InfoObject;
import smart_things.door.schema.InfoRequestEvent;
import smart_things.door.schema.InfoResponseEvent;
import smart_things.door.schema.MoveStates;
import smart_things.door.schema.OpenDoorEvent;
import smart_things.door.schema.OpenGarageEvent;

/**
 * Objects from this class are able to establish a connection to the KAA server
 * as an Garage door endpoint. In its constructor a listener can be passed that
 * implements the reactions on the events that are received from KAA.
 *
 * @author Jan
 */
public class KaaManager {
    // KAA user settings
    private static final String USER_EXTERNAL_ID = "coffee_user";
    private static final String USER_ACCESS_TOKEN = "coffee_user_token";
    private boolean isGarageOpened;
    private boolean isDoorOpened;
    private String moveStatesGarage;
    private String moveStatesDoor;
    // KAA attributes
    private static KaaClient kaaClient;
    private static DoorEventClassFamily tecf = null;
    private DoorEventClassFamily.Listener doorListener = null;
    private static EventFamilyFactory eventFamilyFactory = null;

    public KaaManager(DoorEventClassFamily.Listener listener) {
        this.doorListener = listener;
    }

    /**
     * Establishes the connection to KAA and calls a callback method after that
     *
     * @param callback The callback method that is called after the connection
     *                 establishments
     * @return callback
     */
    public KaaClient connect(Runnable callback) {
        // Create a new KAA client with default start and stop methods
        kaaClient = Kaa.newClient(new DesktopKaaPlatformContext(),
                new SimpleKaaClientStateListener() {
                    /**
                     * Called after the KAA client is started.
                     */
                    @Override
                    public void onStarted() {
                        System.out.println("*****Server started*****");
                    }

                    /**
                     * Called after the KAA client stopped.
                     */
                    @Override
                    public void onStopped() {
                        System.out.println("*****Server stopped*****");
                    }
                });
        // Start the KAA client
        kaaClient.start();

        // Attaches the user with the former specified user data to KAA
        kaaClient.attachUser(USER_EXTERNAL_ID, USER_ACCESS_TOKEN, new UserAttachCallback() {
            @Override
            public void onAttachResult(UserAttachResponse response) {
                // Check for success
                if (response.getResult() == SyncResponseResultType.SUCCESS) {
                    // Attachment was successful, call method to perform further
                    // KAA configurations
                    attachListeners();
                } else {
                    // Attachment was not successful, stop the KAA client
                    kaaClient.stop();
                    System.err.println("Could not attach user!");
                }
            }
        });
        
        // Run callback Runnable
        callback.run();
        return kaaClient;
    }

    /**
     * Finds all active event listeners of other running KAA endpoints so that
     * events can be sent to these endpoints afterwards.
     */
    private void attachListeners() {
        
        eventFamilyFactory = kaaClient.getEventFamilyFactory();
        tecf = eventFamilyFactory.getDoorEventClassFamily();

        // Check whether the KAA client is already instanciated
        if (kaaClient == null) {
            throw new IllegalStateException("KAA client connection is not initialized!");
        }

        // Filter: Only the listeners that receive events which are specified in
        // this list will be found
        List<String> listenerFqns = new LinkedList<>();
        listenerFqns.add(KaaManager.class.getName());

        tecf.addListener(doorListener);
        tecf.addListener(new DoorEventClassFamily.Listener() {
            
            @Override
            public void onEvent(CloseGarageEvent event, String source) {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void onEvent(OpenGarageEvent event, String source) {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void onEvent(CloseDoorEvent event, String source) {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void onEvent(OpenDoorEvent event, String source) {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void onEvent(InfoRequestEvent event, String source) {
                // TODO Auto-generated method stub
                
            }
        });

        // Find all the listeners listening to events which are element of the
        // FQNs list
        kaaClient.findEventListeners(listenerFqns, new FindEventListenersCallback() {

            /**
             * Called when the event listeners are received. Gives to
             * possibility to perform any necessary actions with the obtained
             * event listeners
             *
             * @param eventListeners
             *            The received event listerns
             */
            @Override
            public void onEventListenersReceived(List<String> eventListeners) {
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
    
    

    /**
     * door opened Event.
     */
    public void doorOpened() {
        if (tecf == null) {
            return;
        }
        tecf.sendEventToAll(new DoorOpenedEvent());
        setDoorOpened(true);
    }

    /**
     * door closed Event.
     */
    public void doorClosed() {
        if (tecf == null) {
            return;
        }
        tecf.sendEventToAll(new DoorClosedEvent());
        setDoorOpened(false);
    }

    /**
     * Garage closed Event.
     */
    public void garageClosed() {
        if (tecf == null) {
            return;
        }
        tecf.sendEventToAll(new GarageClosedEvent());
        setGarageOpened(false);
    }

    /**
     * garage opened Event.
     */
    public void garageOpened() {
        if (tecf == null) {
            return;
        }
        tecf.sendEventToAll(new GarageOpenedEvent());
        setGarageOpened(true);
    }

    /**
     * Inforequest event with information about the door and the garage.
     *
     * @param doorOpened
     * @param garageOpened
     */
    public void infoRequest(boolean doorOpened, MoveStates movementDoor, boolean garageOpened,
                            MoveStates movementGarage) {
        if (tecf == null) {
            return;
        }
        tecf.sendEventToAll(new InfoResponseEvent(
                new InfoObject(doorOpened, movementDoor, garageOpened, movementGarage)));
    }

    /**
     * returns a boolean if the door garage is closed or not.
     */
    public boolean isGarageOpened() {
        return isGarageOpened;
    }

    /**
     * sets a boolean if the door garage is closed or not.
     *
     * @param closed
     */
    public void setGarageOpened(boolean closed) {
        this.isGarageOpened = closed;
    }

    public boolean isDoorOpened() {
        return isDoorOpened;
    }

    public void setDoorOpened(boolean isDoorClosed) {
        this.isDoorOpened = isDoorClosed;
    }
    
    public void setMoveGarage(MoveStates type){
        moveStatesGarage = type.toString();
    }
    
    public MoveStates getMoveGarage(){
        if(moveStatesGarage == "NONE"){
            return MoveStates.NONE;
        }else if(moveStatesGarage == "OPENING"){
            return MoveStates.OPENING;
        }else if(moveStatesGarage == "CLOSING"){
            return MoveStates.CLOSING;
        }
        return MoveStates.NONE;
    }
    
    public void setMoveDoor(MoveStates type){
        moveStatesDoor = type.toString();
    }
    
    
    
    public MoveStates getMoveDoor(){
        if(moveStatesDoor == "NONE"){
            return MoveStates.NONE;
        }else if(moveStatesDoor == "OPENING"){
            return MoveStates.OPENING;
        }else if(moveStatesDoor == "CLOSING"){
            return MoveStates.CLOSING;
        }
        return MoveStates.NONE;
    }
}