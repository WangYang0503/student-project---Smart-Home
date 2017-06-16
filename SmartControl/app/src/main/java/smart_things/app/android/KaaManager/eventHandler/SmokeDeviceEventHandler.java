package smart_things.app.android.KaaManager.eventHandler;

import android.util.Log;
import android.widget.TextView;

import org.kaaproject.kaa.client.KaaClient;
import org.kaaproject.kaa.client.event.EventFamilyFactory;
import org.kaaproject.kaa.client.event.FindEventListenersCallback;

import java.util.LinkedList;
import java.util.List;

import smart_things.smoke.schema.InfoRequestEvent;
import smart_things.smoke.schema.InfoResponseEvent;
import smart_things.smoke.schema.SmokeEventClassFamily;
import smart_things.smoke.schema.backToNormalEvent;
import smart_things.smoke.schema.smokeDetectionEvent;

/**
 * Created by Lia on 13.02.2017.
 */

public class SmokeDeviceEventHandler {
    private KaaClient kaaClient;
    private SmokeEventClassFamily tecf;
    private TextView tvSmokeDetector;

    /**
     * status of the coffeeMachine, contains information of
     * the current state, the water level, the current selected brewing config, etc
     */

    private static volatile boolean smokeDetected = false;

    /**
     * constructor
     *
     * @param kaaClient
     */
    public SmokeDeviceEventHandler(KaaClient kaaClient) {
        this.kaaClient = kaaClient;

        EventFamilyFactory eventFamilyFactory = kaaClient.getEventFamilyFactory();
        tecf = eventFamilyFactory.getSmokeEventClassFamily();

        List<String> listenerFQNs = new LinkedList<>();


        tecf.addListener(new SmokeEventClassFamily.Listener() {

            //Message Low Water
            @Override
            public void onEvent(smokeDetectionEvent event, String source) {
                Log.d("SmokeDetectorEvHand", "*****smokeDetectionEvent RECEIVED*****");
//                new SmokeDetectorNotifications().smokeDetectedNotification();
//                updateButtonStatusSmoke();
//                smokeDetected = true;
            }

            public void onEvent(backToNormalEvent event, String source) {
                Log.d("CoffeeMachineEvHand", "*****backToNormalEvent RECEIVED*****");
                //TODO
//                new SmokeDetectorNotifications().backToNormalNotification();
//                updateButtonStatusNormal();
//                smokeDetected = false;
            }

            // Message Info Response
            @Override
            public void onEvent(InfoResponseEvent event, String source) {
                Log.d("CoffeeMachineEvHand", "*****InfoResponseEvent RECEIVED*****");


            }
        });

        // Find all the listeners listening to the events from the FQNs list.
        kaaClient.findEventListeners(listenerFQNs, new FindEventListenersCallback() {
            // Perform any necessary actions with the obtained event listeners.
            @Override
            public void onEventListenersReceived(List<String> eventListeners) {
                Log.d("SmokeDetectorEvHand", eventListeners.size() + " event listeners received");
            }

            // Perform any necessary actions in case of failure.
            @Override
            public void onRequestFailed() {
                Log.d("SmokeDetectorEvHand", "Request failed");
            }
        });
    }

    //
    public void sendRequestEvent() {
        tecf.sendEventToAll(new InfoRequestEvent());
    }

}
