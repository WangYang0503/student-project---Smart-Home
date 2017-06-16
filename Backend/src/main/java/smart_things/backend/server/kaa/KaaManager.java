package smart_things.backend.server.kaa;

import org.kaaproject.kaa.client.DesktopKaaPlatformContext;
import org.kaaproject.kaa.client.Kaa;
import org.kaaproject.kaa.client.KaaClient;
import org.kaaproject.kaa.client.SimpleKaaClientStateListener;
import org.kaaproject.kaa.client.event.EventFamilyFactory;
import org.kaaproject.kaa.client.event.FindEventListenersCallback;
import org.kaaproject.kaa.common.endpoint.gen.SyncResponseResultType;
import smart_things.backend.server.main.MultiLogger;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Jan on 10.01.2017.
 */
public class KaaManager {
    //KAA user settings
    private String userId = null;
    private String userToken = null;

    //Kaa client that handles the kaa communitcation through the network
    private KaaClient kaaClient = null;

    //KAA tecfs (summed up in a collection)
    private ECFCollection ecfCollection = null;

    //KAA event listeners (summed up in a collection)
    private ListenerCollection listenerCollection = null;

    /**
     * Creates a new KaaManager instance with a specific configuration
     *
     * @param userId             KAA user ID. Must be the same for each endpoint to make a connection possible!
     * @param userToken          KAA user token ID. Must be the same for each endpoint to make a connection possible!
     * @param listenerCollection Collection that contains listener implementation for incoming events on the
     *                           coffee machine, the smart car, the light control system, the door control system and
     *                           the smoke detector device
     */
    public KaaManager(String userId, String userToken, ListenerCollection listenerCollection) {
        this.userId = userId;
        this.userToken = userToken;
        this.listenerCollection = listenerCollection;
    }

    /**
     * Initializes the KAA connection including necessary operations like the
     * attachment of the user and the attachment of listeners. After the connection is established and
     * all operations are performed, a callback is called
     *
     * @param callback The Runnable to call after the connection to KAA is established
     */
    public void initKaa(Runnable callback) {
        //Create a new KAA client with default start and stop methods
        kaaClient = Kaa.newClient(new DesktopKaaPlatformContext(), new SimpleKaaClientStateListener() {
            /**
             * Called after the KAA client is started.
             */
            @Override
            public void onStarted() {
            }

            /**
             * Called after the KAA client stopped.
             */
            @Override
            public void onStopped() {
            }
        });
        //Start the KAA client
        kaaClient.start();

        //Attaches the user with the former specified user data to KAA
        kaaClient.attachUser(userId, userToken, response -> {

            //Check for success
            if (response.getResult() == SyncResponseResultType.SUCCESS) {
                //Attachment was successful, call method to perform further KAA configurations
                onUserAttached(callback);
            } else {
                //Attachment was not successful, stop the KAA client
                kaaClient.stop();
                MultiLogger.severeNotify("Could not attach given user to the KAA server");
            }
        });
    }

    /**
     * Attaches a set of listeners to KAA that define the behaviour of the server on incoming events.
     * After the listeners are attached, a callback is called
     *
     * @param callback The Runnable to call after the listeners are attached
     */
    private void onUserAttached(Runnable callback) {
        //Check whether the KAA client is already instantiated
        if (kaaClient == null) {
            throw new IllegalStateException("KAA client connection is not initialized!");
        }

        //Create tecf collection:
        ecfCollection = new ECFCollection();

        //Get the event class families from a family factory
        EventFamilyFactory eventFamilyFactory = kaaClient.getEventFamilyFactory();
        ecfCollection.setTecf_coffee(eventFamilyFactory.getCoffeeEventClassFamily());
        ecfCollection.setTecf_car(eventFamilyFactory.getCarEventClassFamily());
        ecfCollection.setTecf_light(eventFamilyFactory.getLightEventClassFamily());
        ecfCollection.setTecf_door(eventFamilyFactory.getDoorEventClassFamily());
        ecfCollection.setTecf_smoke(eventFamilyFactory.getSmokeEventClassFamily());

        //Add the given listeners to the event class families
        ecfCollection.getTecf_coffee().addListener(listenerCollection.getListeners_coffee());
        ecfCollection.getTecf_car().addListener(listenerCollection.getListeners_car());
        ecfCollection.getTecf_light().addListener(listenerCollection.getListeners_light());
        ecfCollection.getTecf_door().addListener(listenerCollection.getListeners_door());
        ecfCollection.getTecf_smoke().addListener(listenerCollection.getListeners_smoke());

        //Find all active event listeners of other endpoints
        findListeners();
        //Run callback Runnable
        callback.run();
    }

    /**
     * Finds all active event listeners of other running KAA endpoints so that events can be sent to these
     * endpoints afterwards.
     */
    private void findListeners() {
        //Check whether the KAA client is already instanciated
        if (kaaClient == null) {
            throw new IllegalStateException("KAA client connection is not initialized!");
        }

        //Filter: Only the listeners that receive events which are specified in this list will be found
        List<String> listenerFqns = new LinkedList<>();

        // Find all the listeners listening to events which are element of the FQNs list
        kaaClient.findEventListeners(listenerFqns, new FindEventListenersCallback() {

            /**
             * Called when the event listeners are received. Gives to possibility to perform any necessary actions
             * with the obtained event listeners
             * @param eventListeners The received event listerns
             */
            @Override
            public void onEventListenersReceived(List<String> eventListeners) {
                MultiLogger.info("Received " + eventListeners.size() + " KAA event listeners");
            }

            /**
             * Called when the search request failed. Gives the possibility to perform any necessary actions
             * in case of failure.
             */
            @Override
            public void onRequestFailed() {
                MultiLogger.severeNotify("KAA event listener request failed");
            }
        });
    }

    /**
     * Stops the KAA server and invalidates the local attributes.
     */
    public void stop() {
        //Is the KAA client instantiated?
        if (kaaClient != null) {
            //Stop KAA server
            kaaClient.stop();
        }

        //Invalidate local variables
        kaaClient = null;
        listenerCollection = null;
        ecfCollection = null;
    }

    /**
     * Returns the collection of all event class families (tecfs) that were created by the KaaManager. These event
     * class families can be used to send/broadcast Kaa-events to endpoints.
     *
     * @return The tecf collection
     */
    public ECFCollection getEcfCollection() {
        return ecfCollection;
    }
}