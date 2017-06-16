package smart_things.app.android.KaaManager.eventHandler;

import android.util.Log;

import org.kaaproject.kaa.client.KaaClient;
import org.kaaproject.kaa.client.event.EventFamilyFactory;

import java.util.LinkedList;
import java.util.List;

import smart_things.app.android.R;
import smart_things.app.android.gui.MainActivity;
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
import smart_things.door.schema.OpenDoorEvent;
import smart_things.door.schema.OpenGarageEvent;

/**
 * Created by Lia on 13.02.2017.
 */

public class DoorEventHandler {
    private KaaClient kaaClient;
    private DoorEventClassFamily tecf;
    private InfoObject doorAndGarageStatus = null;


    public DoorEventHandler(KaaClient kaaClient) {
        this.kaaClient = kaaClient;

        EventFamilyFactory eventFamilyFactory = kaaClient.getEventFamilyFactory();
        tecf = eventFamilyFactory.getDoorEventClassFamily();
        List<String> listenerFQNs = new LinkedList<>();

        tecf.addListener(new DoorEventClassFamily.Listener() {
            @Override
            public void onEvent(final InfoResponseEvent infoResponseEvent, String s) {
                Log.d("InfoResponseEvent", "***Info Response***");
                doorAndGarageStatus = infoResponseEvent.getInfoObject();

                if (MainActivity.getInstance() != null && MainActivity.getInstance().getDoorFragment() != null) {
                    MainActivity.getInstance().runOnUiThread(new Runnable() {
                        public void run() {
                            MainActivity.getInstance().getDoorFragment().setAllViewsEnableState(true);

                            // set button action if the action was never set before
                            if (!MainActivity.getInstance().getDoorFragment().isInitiated()) {
                                MainActivity.getInstance().getDoorFragment().setGarageAction(!doorAndGarageStatus.getGarageOpened());
                                MainActivity.getInstance().getDoorFragment().setDoorAction(!doorAndGarageStatus.getDoorOpened());
                            }
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
            public void onEvent(DoorClosedEvent doorClosedEvent, String s) {
                Log.d("DoorClosedEvent", "***Door Closed***");
                MainActivity.getInstance().runOnUiThread(new Runnable() {
                    public void run() {
                        if (MainActivity.getInstance() != null && MainActivity.getInstance().getDoorFragment() != null) {
                            MainActivity.getInstance().getDoorFragment().setDoorAction(true);
                        }
                    }
                });
            }

            @Override
            public void onEvent(DoorOpenedEvent doorOpenedEvent, String s) {
                Log.d("DoorOpenedEvent", "***Door Opened***");
                MainActivity.getInstance().runOnUiThread(new Runnable() {
                    public void run() {
                        if (MainActivity.getInstance() != null && MainActivity.getInstance().getDoorFragment() != null) {
                            MainActivity.getInstance().getDoorFragment().setDoorAction(false);
                            MainActivity.getInstance().getDoorFragment().getGarageIcon().setImageResource(R.drawable.ic_garage_open);
                        }
                    }
                });
            }

            @Override
            public void onEvent(GarageClosedEvent garageClosedEvent, String s) {
                Log.d("GarageClosedEvent", "***Garage Closed***");
                MainActivity.getInstance().runOnUiThread(new Runnable() {
                    public void run() {
                        if (MainActivity.getInstance() != null && MainActivity.getInstance().getDoorFragment() != null) {
                            MainActivity.getInstance().getDoorFragment().setGarageAction(true);
                            MainActivity.getInstance().getDoorFragment().getGarageIcon().setImageResource(R.drawable.ic_garage);
                        }
                    }
                });
            }

            @Override
            public void onEvent(GarageOpenedEvent garageOpenedEvent, String s) {
                Log.d("GarageOpenedEvent", "***Garage Opened***");
                MainActivity.getInstance().runOnUiThread(new Runnable() {
                    public void run() {
                        if (MainActivity.getInstance() != null && MainActivity.getInstance().getDoorFragment() != null) {
                            MainActivity.getInstance().getDoorFragment().setGarageAction(false);
                        }
                    }
                });
            }
        });
    }

    public void sendOpenDoor() {
        tecf.sendEventToAll(new OpenDoorEvent());

    }

    public void sendCloseDoor() {
        tecf.sendEventToAll(new CloseDoorEvent());
    }

    public void sendCloseGarage() {
        tecf.sendEventToAll(new CloseGarageEvent());
    }

    public void sendOpenGarage() {
        tecf.sendEventToAll(new OpenGarageEvent());
    }

    public boolean garageIsOpen() {
        return doorAndGarageStatus.getGarageOpened();
    }

    public boolean doorIsOpen() {
        return doorAndGarageStatus.getDoorOpened();
    }

    public InfoObject getDoorAndGarageStatus() {
        return doorAndGarageStatus;
    }

    public void sendInfoRequestEvent() {
        tecf.sendEventToAll(new InfoRequestEvent());
    }

}
