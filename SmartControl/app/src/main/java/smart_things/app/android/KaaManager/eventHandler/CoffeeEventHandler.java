package smart_things.app.android.KaaManager.eventHandler;

import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import org.kaaproject.kaa.client.KaaClient;
import org.kaaproject.kaa.client.event.EventFamilyFactory;
import org.kaaproject.kaa.client.event.FindEventListenersCallback;

import java.util.LinkedList;
import java.util.List;

import smart_things.app.android.R;
import smart_things.app.android.gui.MainActivity;
import smart_things.coffee.schema.AlreadyBrewingEvent;
import smart_things.coffee.schema.BrewingFinishedEvent;
import smart_things.coffee.schema.CoffeeEventClassFamily;
import smart_things.coffee.schema.ConnectEvent;
import smart_things.coffee.schema.DisconnectEvent;
import smart_things.coffee.schema.InfoObject;
import smart_things.coffee.schema.InfoRequestEvent;
import smart_things.coffee.schema.InfoResponseEvent;
import smart_things.coffee.schema.LowWaterEvent;
import smart_things.coffee.schema.NoCarafeEvent;
import smart_things.coffee.schema.NoWaterEvent;
import smart_things.coffee.schema.SetCupsEvent;
import smart_things.coffee.schema.SetHotplateTimeEvent;
import smart_things.coffee.schema.SetStrengthEvent;
import smart_things.coffee.schema.StartBrewingEvent;
import smart_things.coffee.schema.StopBrewingEvent;
import smart_things.coffee.schema.StrengthTypes;
import smart_things.coffee.schema.TechnicalErrorEvent;
import smart_things.coffee.schema.ToggleBrewingTypeEvent;

/**
 * Created by Lia on 13.02.2017.
 */

public class CoffeeEventHandler {
    private KaaClient kaaClient;
    private CoffeeEventClassFamily tecf;
    private InfoObject coffeeStatus = null;

    /**
     * constructor
     *
     * @param kaaClient
     */
    public CoffeeEventHandler(KaaClient kaaClient) {
        this.kaaClient = kaaClient;

        EventFamilyFactory eventFamilyFactory = kaaClient.getEventFamilyFactory();
        tecf = eventFamilyFactory.getCoffeeEventClassFamily();

        final List<String> FQNs = new LinkedList<>();
        FQNs.add(CoffeeEventHandler.class.getName());

        kaaClient.findEventListeners(FQNs, new FindEventListenersCallback() {
            @Override
            public void onEventListenersReceived(List<String> eventListeners) {
                // Some code
                FQNs.addAll(eventListeners);
                System.out.println();
            }

            @Override
            public void onRequestFailed() {
                // Some code
            }
        });

        List<String> listenerFQNs = new LinkedList<>();

        tecf.addListener(new CoffeeEventClassFamily.Listener() {

            //Message Low Water
            @Override
            public void onEvent(LowWaterEvent event, String source) {
                MainActivity.getInstance().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (MainActivity.getInstance().getCurrentFocus() != null) {
                            Snackbar.make(MainActivity.getInstance().findViewById(R.id.coffee_fragment),
                                    "Wenig Wasser: Es werden weniger als die angegebene Tassenzahl gekocht"
                                    , Snackbar.LENGTH_LONG).show();
                        }
                    }
                });


            }

            @Override
            public void onEvent(ConnectEvent connectEvent, String s) {
                MainActivity.getInstance().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (MainActivity.getInstance().getCurrentFocus() != null) {
                            Snackbar.make(MainActivity.getInstance().findViewById(R.id.coffee_fragment),
                                    "Verbindung zum CoffeeServer hergestellt", Snackbar.LENGTH_LONG).show();
                        }
                    }
                });

            }

            @Override
            public void onEvent(DisconnectEvent disconnectEvent, String s) {

                MainActivity.getInstance().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (MainActivity.getInstance().getCurrentFocus() != null) {
                            Snackbar.make(MainActivity.getInstance().findViewById(R.id.coffee_fragment),
                                    "Verbindung zum CoffeeServer getrennt", Snackbar.LENGTH_LONG).show();
                        }
                    }
                });

            }

            @Override
            public void onEvent(StartBrewingEvent startBrewingEvent, String s) {
                MainActivity.getInstance().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (MainActivity.getInstance().getCurrentFocus() != null) {
                            Snackbar.make(MainActivity.getInstance().findViewById(R.id.coffee_fragment),
                                    "Kaffee wird gekocht", Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
            }


            @Override
            public void onEvent(StopBrewingEvent stopBrewingEvent, String s) {
                MainActivity.getInstance().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (MainActivity.getInstance().getCurrentFocus() != null) {
                            Snackbar.make(MainActivity.getInstance().findViewById(R.id.coffee_fragment),
                                    "Kochen wird gestoppt", Snackbar.LENGTH_LONG).show();
                        }
                    }
                });

            }


            //Message brewing finished
            @Override
            public void onEvent(BrewingFinishedEvent event, String source) {
                Log.d("CoffeeMachineEvHand", "*****BrewingFinishedEvent RECEIVED*****");
                MainActivity.getInstance().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (MainActivity.getInstance().getCurrentFocus() != null) {
                            Snackbar.make(MainActivity.getInstance().findViewById(R.id.coffee_fragment),
                                    "Kaffee ist fertig", Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
            }

            //Message  no water
            @Override
            public void onEvent(NoWaterEvent event, String source) {
                MainActivity.getInstance().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (MainActivity.getInstance().getCurrentFocus() != null) {
                            Snackbar.make(MainActivity.getInstance().findViewById(R.id.coffee_fragment),
                                    "Kein Wasser in der Maschine", Snackbar.LENGTH_LONG).show();
                        }
                    }
                });

            }

            // Message no carafe
            @Override
            public void onEvent(NoCarafeEvent event, String source) {
                MainActivity.getInstance().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (MainActivity.getInstance().getCurrentFocus() != null) {
                            Snackbar.make(MainActivity.getInstance().getCurrentFocus(),
                                    "Keine Karaffee in der Maschine. Bitte Karaffe reinstellen", Snackbar.LENGTH_LONG)
                                    .show();
                        }
                    }
                });

            }

            // Message technical Error
            @Override
            public void onEvent(TechnicalErrorEvent event, String source) {
                Log.d("CoffeeMachineEvHand", "*****TechnicalErrorEvent RECEIVED*****");

            }

            //Message already Brewing
            @Override
            public void onEvent(AlreadyBrewingEvent event, String source) {
                MainActivity.getInstance().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        View v = MainActivity.getInstance().getCurrentFocus();
                        boolean v2 = MainActivity.getInstance().getCoffeeFragment().isVisible();
                        if (MainActivity.getInstance().getCurrentFocus() != null) {
                            Snackbar.make(MainActivity.getInstance().getCurrentFocus(),
                                    "Kaffee wird bereits gekocht", Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
            }

            // Message Info Response
            @Override
            public void onEvent(InfoResponseEvent infoResponse, String source) {
                Log.d("CoffeeMachineEvHand", "*****InfoResponseEvent RECEIVED*****");
                coffeeStatus = infoResponse.getInfoObject();
                final InfoResponseEvent event = infoResponse;
                if (MainActivity.getInstance() != null && MainActivity.getInstance().getCoffeeFragment() != null) {
                    MainActivity.getInstance().runOnUiThread(new Runnable() {
                        public void run() {
                            MainActivity.getInstance().getCoffeeFragment().setAllViewsEnableState(true);
                        }
                    });
                }
                if (MainActivity.getInstance() != null && MainActivity.getInstance().getStatusFragment() != null) {
                    MainActivity.getInstance().runOnUiThread(new Runnable() {
                        public void run() {
                            MainActivity.getInstance().getStatusFragment().updateStatus();
                            MainActivity.getInstance().getCoffeeFragment().setGuiElementInitState();
                        }
                    });
                }
            }
        });
    }

    public void sendStartBrewingEvent() {
        tecf.sendEventToAll(new StartBrewingEvent());

    }

    public void sendSetCupCountEvent(int cupCount) {
        tecf.sendEventToAll(new SetCupsEvent(cupCount));
    }

    public void sendSetStrengthEvent(StrengthTypes strength) {
        tecf.sendEventToAll(new SetStrengthEvent(strength));
    }

    public void sendStopBrewingEvent() {
        tecf.sendEventToAll(new StopBrewingEvent());
    }

    public void sendRequestStateEvent() {
        tecf.sendEventToAll(new InfoRequestEvent());
    }

    public void sendToggleBrewingTypeEvent() {
        tecf.sendEventToAll(new ToggleBrewingTypeEvent());
    }

    public void sendHotPlateTypeEvent(int hotplateTime) {
        tecf.sendEventToAll(new SetHotplateTimeEvent(hotplateTime));
    }

    /**
     * getter of the coffeeMachine status
     *
     * @return the InfoObject of the coffeeMachine
     */
    public InfoObject getCoffeeStatus() {
        return coffeeStatus;
    }

}
