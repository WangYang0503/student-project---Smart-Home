package smart_things.smoke.simulator.kaaManager.eventHandler;

import android.util.Log;
import android.widget.TextView;

import org.kaaproject.kaa.client.KaaClient;
import org.kaaproject.kaa.client.event.EventFamilyFactory;
import org.kaaproject.kaa.client.event.FindEventListenersCallback;

import smart_things.smoke.schema.InfoRequestEvent;
import smart_things.smoke.schema.InfoResponseEvent;
import smart_things.smoke.schema.SmokeEventClassFamily;
import smart_things.smoke.schema.backToNormalEvent;
import smart_things.smoke.schema.smokeDetectionEvent;
import smart_things.smoke.simulator.kaaManager.KaaManager;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Lukas on 29.12.16.
 * Modified by Jan on 12.03.17.
 */
public class SmokeDeviceEventHandler {
    private KaaClient kaaClient;
    private SmokeEventClassFamily tecf;
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
            @Override
            public void onEvent(smart_things.smoke.schema.InfoRequestEvent infoRequestEvent, String s) {
                tecf.sendEventToAll(new InfoResponseEvent(smokeDetected));

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


    public void sendSmokeDetectionEvent() {
        tecf.sendEventToAll(new smokeDetectionEvent());
        setSmokeDetected(true);
    }

    public void sendBackToNormalEvent() {
        tecf.sendEventToAll(new backToNormalEvent());
        setSmokeDetected(false);
    }

    public void setSmokeDetected(boolean smokeDetected) {
        this.smokeDetected = smokeDetected;
    }

    public boolean getSmokeDetected() {
        return smokeDetected;
    }
}