package smart_things.app.android.KaaManager.eventHandler;

import android.app.Fragment;
import android.support.design.widget.Snackbar;
import android.util.Log;

import org.kaaproject.kaa.client.KaaClient;
import org.kaaproject.kaa.client.event.EventFamilyFactory;
import org.kaaproject.kaa.client.event.FindEventListenersCallback;

import java.util.LinkedList;
import java.util.List;

import smart_things.app.android.gui.MainActivity;
import smart_things.car.schema.AvoidedDeathEvent;
import smart_things.car.schema.CarEventClassFamily;
import smart_things.car.schema.DodgeEvent;
import smart_things.car.schema.InfoObject;
import smart_things.car.schema.InfoRequestEvent;
import smart_things.car.schema.InfoResponseEvent;
import smart_things.car.schema.LostWayEvent;
import smart_things.car.schema.StartDrivingEvent;
import smart_things.car.schema.StartedDrivingEvent;
import smart_things.car.schema.StartedParkingEvent;
import smart_things.car.schema.StopDrivingEvent;
import smart_things.car.schema.StoppedDrivingEvent;

/**
 * Created by Lia on 13.02.2017.
 */

public class MindstormsEventHandler {
    private KaaClient kaaClient;
    private CarEventClassFamily tecf;
    private Fragment carFragment;
    private boolean driving = false;

    private InfoObject carInfo;

    public MindstormsEventHandler(KaaClient kaaClient) {
        this.kaaClient = kaaClient;

        EventFamilyFactory eventFamilyFactory = kaaClient.getEventFamilyFactory();
        tecf = eventFamilyFactory.getCarEventClassFamily();

        List<String> listenerFQNs = new LinkedList<>();

        tecf.addListener(new CarEventClassFamily.Listener() {

            @Override
            public void onEvent(InfoResponseEvent infoResponse, String s) {
                final InfoResponseEvent event = infoResponse;
                carInfo = infoResponse.getInfoObject();
                if (MainActivity.getInstance() != null && MainActivity.getInstance().getCarFragment() != null) {
                    MainActivity.getInstance().runOnUiThread(new Runnable() {
                        public void run() {
                            MainActivity.getInstance().getCarFragment().setAllViewsEnableState(true);
                            MainActivity.getInstance().getCarFragment().setButtonAction(!event.getInfoObject().getDriving());
                        }
                    });
                }
                if (MainActivity.getInstance() != null && MainActivity.getInstance().getStatusFragment() != null) {
                    MainActivity.getInstance().runOnUiThread(new Runnable() {
                        public void run() {
                            MainActivity.getInstance().getStatusFragment().updateStatus();
                        }
                    });
                }
            }

            @Override
            public void onEvent(StartedDrivingEvent startedDriving, String s) {
                Log.d("CarStart", "***Started driving***");
                driving = true;
                if (MainActivity.getInstance() != null
                        && MainActivity.getInstance().getCarFragment() != null) {
                }
            }

            @Override
            public void onEvent(StoppedDrivingEvent stoppedDriving, String s) {
                Log.d("CarStop", "***Stopped Driving***");
                driving = false;
                if (MainActivity.getInstance() != null
                        && MainActivity.getInstance().getCarFragment() != null) {
                }
            }

            @Override
            public void onEvent(StartedParkingEvent startedParking, String s) {
                Log.d("CarStart", "***Started Parking***");
                driving = true;
                if (MainActivity.getInstance() != null && MainActivity.getInstance().getCurrentFocus() != null) {
                    MainActivity.getInstance().runOnUiThread(new Runnable() {
                        public void run() {
                            Snackbar
                                    .make(MainActivity.getInstance().getCurrentFocus(), "The car has started parking", Snackbar.LENGTH_LONG)
                                    .show();
                        }
                    });
                }
            }

            @Override
            public void onEvent(DodgeEvent dodging, String s) {
                Log.d("CarDogged", "***Car Dogged***");
                driving = true;
                if (MainActivity.getInstance() != null && MainActivity.getInstance().getCurrentFocus() != null) {
                    MainActivity.getInstance().runOnUiThread(new Runnable() {
                        public void run() {
                            Snackbar
                                    .make(MainActivity.getInstance().getCurrentFocus(), "The car is dodeing an object", Snackbar.LENGTH_LONG)
                                    .show();
                        }
                    });
                }
            }

            @Override
            public void onEvent(AvoidedDeathEvent avoidedDeath, String s) {
                Log.d("AvoidDeath", "*** Car Turned***");
                if (MainActivity.getInstance() != null && MainActivity.getInstance().getCarFragment() != null) {
                    MainActivity.getInstance().runOnUiThread(new Runnable() {
                        public void run() {
                            MainActivity.getInstance().getCarFragment().setAllViewsEnableState(true);
                            Snackbar
                                    .make(MainActivity.getInstance().getCurrentFocus(), "The car didn't fall down", Snackbar.LENGTH_LONG)
                                    .show();
                        }
                    });
                }
            }

            @Override
            public void onEvent(LostWayEvent lostWay, String s) {
                Log.d("LostWay", "*** Car lost its way***");
                if (MainActivity.getInstance() != null && MainActivity.getInstance().getCarFragment() != null) {
                    MainActivity.getInstance().runOnUiThread(new Runnable() {
                        public void run() {
                            MainActivity.getInstance().getCarFragment().setAllViewsEnableState(false);
                            Snackbar
                                    .make(MainActivity.getInstance().getCurrentFocus(), "The car has lost the track", Snackbar.LENGTH_LONG)
                                    .show();
                        }
                    });
                }
            }
        });

        // Find all the listeners listening to the events from the FQNs list.
        kaaClient.findEventListeners(listenerFQNs, new

                FindEventListenersCallback() {
                    // Perform any necessary actions with the obtained event listeners.
                    @Override
                    public void onEventListenersReceived(List<String> eventListeners) {
                        Log.d("MindstormEvHand", eventListeners.size() + " event listeners received");
                    }

                    // Perform any necessary actions in case of failure.
                    @Override
                    public void onRequestFailed() {
                        Log.d("MindstormEvHand", "Request failed");
                    }
                });
    }

    public void sendStartDrivingEvent() {
        tecf.sendEventToAll(new StartDrivingEvent());
    }

    public void sendStopDrivingEvent() {
        tecf.sendEventToAll(new StopDrivingEvent());
    }

    public void sendInfoRequestEvent() {
        tecf.sendEventToAll(new InfoRequestEvent());
    }


    public boolean isDriving() {
        return driving;
    }

    public InfoObject getInfoObject() {
        return carInfo;
    }

}
