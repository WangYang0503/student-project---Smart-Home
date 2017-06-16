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

import smart_things.car.schema.AvoidedDeathEvent;
import smart_things.car.schema.CarEventClassFamily;
import smart_things.car.schema.CarEventClassFamily.Listener;
import smart_things.car.schema.DodgeEvent;
import smart_things.car.schema.InfoObject;
import smart_things.car.schema.InfoResponseEvent;
import smart_things.car.schema.LostWayEvent;
import smart_things.car.schema.StartedDrivingEvent;
import smart_things.car.schema.StartedParkingEvent;
import smart_things.car.schema.StoppedDrivingEvent;



/**
 * Establish connection between KAA and Mindstorm.
 * Events are declared in this class and send.
 * @author david ziegler
 *
 */
public class KaaVerbindung {
    
    private final String USER_EXTERNAL_ID = "coffee_user";
    private final String USER_ACCESS_TOKEN = "coffee_user_token";

    private KaaClient kaaClient;
    private CarEventClassFamily tecf = null;

    private smart_things.car.schema.CarEventClassFamily.Listener mindstormsEventListener = null;
    /*
     * Constructor
     */
    public KaaVerbindung(Listener mindstormsEventListener) {
        this.mindstormsEventListener = mindstormsEventListener;
       
    }

    /**
     * Kaa connection.
     * 
     * @param callback
     *            Events
     * @return callback
     */
    public KaaClient connect(Runnable callback) {

        kaaClient = Kaa.newClient(new DesktopKaaPlatformContext(),
                new SimpleKaaClientStateListener() {
                    @Override
                    public void onStarted() {
                        System.out.println("*****Server started*****");
                    }

                    @Override
                    public void onStopped() {
                        System.out.println("*****Server stopped*****");
                    }
                });
        kaaClient.start();

        kaaClient.attachUser(USER_EXTERNAL_ID, USER_ACCESS_TOKEN, new UserAttachCallback() {
            @Override
            public void onAttachResult(UserAttachResponse response) {

                if (response.getResult() == SyncResponseResultType.SUCCESS) {
                    System.out.println("User succesfully attached!");
                    attachListeners();
                } else {
                    kaaClient.stop();
                    System.err.println("Could not attach user!");
                }
            }
        });
        EventFamilyFactory eventFamilyFactory = kaaClient.getEventFamilyFactory();
        tecf = eventFamilyFactory.getCarEventClassFamily();
        
        callback.run();
        return kaaClient;
    }

    private void attachListeners() {
        List<String> listenerFqns = new LinkedList<>();
        listenerFqns.add(KaaVerbindung.class.getName());

        tecf.addListener(mindstormsEventListener);
        System.out.println("Listeners attached!");

        // Find all the listeners listening to the events from the FQNs list.
        kaaClient.findEventListeners(listenerFqns, new FindEventListenersCallback() {

            // Perform any necessary actions with the obtained event listeners.
            @Override
            public void onEventListenersReceived(List<String> eventListeners) {
                System.out.println(eventListeners.size() + " event listeners received");
            }

            // Perform any necessary actions in case of failure.
            @Override
            public void onRequestFailed() {
                System.err.println("Request failed");
            }
        });
    }

    /**
     * Send Event when the Mindstorm started driving.
     */
    public void sendStartDrivingEvent() {
        if (tecf == null) {
            return;
        }
        tecf.sendEventToAll(new StartedDrivingEvent());
    }


    

   

    
    
    /**
     * Send Event when the Mindstorm recognize the blue Line.
     */
    public void startParkingEvent() {
        if (tecf == null) {
            return;
        }
        tecf.sendEventToAll(new StartedParkingEvent());

    }
    
    

    /**
     * Send Event when the Mindstorm recognize the red line.
     */
    public void avoidFallingEvent() {
        if (tecf == null) {
            return;
        }
        tecf.sendEventToAll(new AvoidedDeathEvent());
    }

    /**
     * send Event when the Mindstorm is stopped.
     */
    public void stoppedDrivingEvent() {
        if (tecf == null) {
            return;
        }
        tecf.sendEventToAll(new StoppedDrivingEvent());
    }

    /**
     * send event when mindstorm avoiding a Object.
     */
    public void dodgingObjectEvent() {
        if (tecf == null) {
            return;
        }
        tecf.sendEventToAll(new DodgeEvent());
    }

    /**
     * send event when mindstorm is not on the black line and doesn't find the
     * way back.
     */
    public void lostWay() {
        if (tecf == null) {
            return;
        }
        tecf.sendEventToAll(new LostWayEvent());
    }

    /**
     * event toogles the standby modus of the mindstorm.
     */
    // public void toogleStandby() {
    // if (tecf == null) {
    // return;
    // }
    // tecf.sendEventToAll(new ToogleStandby());
    // }

    /**
     * Information status Mindstorm.
     * 
     * @param speed
     *            speed of mindstorm
     * @param isDriving
     *            if the mindstorm is driving
     * @param color1
     *            color sensor1 recognize
     * @param color2
     *            color sensor2 recognize
     * @param distance
     *            get Distance in float to object
     * @param isTouched
     *            is paused
     * @param battery
     *              battery status           
     */
    public void infoResponseEvent(int speed, boolean isDriving, String color1, String color2,
            double distance, boolean isTouched, int battery) {
        if (tecf == null) {
            return;
        }
        tecf.sendEventToAll(new InfoResponseEvent(
                new InfoObject(speed, isDriving, color1, color2, distance, isTouched,battery)));
    }

}
