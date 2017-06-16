package smart_things.app.android.KaaManager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.kaaproject.kaa.client.AndroidKaaPlatformContext;
import org.kaaproject.kaa.client.Kaa;
import org.kaaproject.kaa.client.KaaClient;
import org.kaaproject.kaa.client.SimpleKaaClientStateListener;
import org.kaaproject.kaa.client.channel.TransportConnectionInfo;
import org.kaaproject.kaa.client.channel.failover.FailoverDecision;
import org.kaaproject.kaa.client.channel.failover.FailoverStatus;
import org.kaaproject.kaa.client.channel.failover.strategies.DefaultFailoverStrategy;
import org.kaaproject.kaa.client.event.registration.UserAttachCallback;
import org.kaaproject.kaa.common.endpoint.gen.SyncResponseResultType;
import org.kaaproject.kaa.common.endpoint.gen.UserAttachResponse;

import smart_things.app.android.KaaManager.eventHandler.CoffeeEventHandler;
import smart_things.app.android.KaaManager.eventHandler.DoorEventHandler;
import smart_things.app.android.KaaManager.eventHandler.LightEventHandler;
import smart_things.app.android.KaaManager.eventHandler.MindstormsEventHandler;
import smart_things.app.android.KaaManager.eventHandler.SmokeDeviceEventHandler;
import smart_things.app.android.gui.MainActivity;


/**
 * Created by Lia on 13.02.2017.
 */

public class KaaManager {

    // kaa user credentials
    private static final String USER_EXTERNAL_ID = "coffee_user";
    private static final String USER_ACCESS_TOKEN = "coffee_user_token";

    private static KaaClient kaaClient;
    private static boolean connected = false;
    private static boolean neverConnected = true;
    private static boolean neverFailed = true;

    private static CoffeeEventHandler coffeeMachineEventHandler = null;
    private static MindstormsEventHandler mindstormsEventHandler = null;
    private static LightEventHandler lightEventHandler = null;
    private static DoorEventHandler doorEventHandler = null;
    private static SmokeDeviceEventHandler smokeDetectorEventHandler = null;

    /**
     * tries to connect to kaa server @MAX_CONNECTION_TRIES times
     */
    public static void connectToKaaServer() {
        if (!wifiIsConnected()) {

            Log.d("KaaManager", "showNoWifiWarningMessage() passed");
        }
        connectToKaa();
    }

    /**
     * connect to the  kaa server and attach the kaa user
     */
    private static void connectToKaa() {

        if (kaaClient == null) {
            kaaClient = Kaa.newClient(new AndroidKaaPlatformContext(MainActivity.getInstance()), new SimpleKaaClientStateListener() {
                @Override
                public void onStarted() {
                    Log.d("KaaManager.connectToKaa", "*****Server started*****");
                }

                @Override
                public void onStopped() {
                    Log.d("KaaManager.connectToKaa", "*****Server stopped*****");
                }
            });
            kaaClient.start();
        }

        /**
         * user
         */
        kaaClient.attachUser(USER_EXTERNAL_ID, USER_ACCESS_TOKEN, new UserAttachCallback() {
            @Override
            public void onAttachResult(UserAttachResponse response) {
                if (response.getResult() == SyncResponseResultType.SUCCESS) {
                    Log.i("KaaManager.connectToKaa", "User successfully attached!");
                    Log.d("ConnectionToKaa", "UserAttachResponse");

                    setFailoverStrategy();
                    onUserAttached();
                } else {
                    Log.e("KaaManager.connectToKaa", "Could not attach user!");
                }
            }
        });
    }

    private static void onUserAttached() {
        Log.d("KaaManager", "onUserAttached() called");
        coffeeMachineEventHandler = new CoffeeEventHandler(kaaClient);
        mindstormsEventHandler = new MindstormsEventHandler(kaaClient);
        lightEventHandler = new LightEventHandler(kaaClient);
        doorEventHandler = new DoorEventHandler(kaaClient);
        smokeDetectorEventHandler = new SmokeDeviceEventHandler(kaaClient);

        // send request event to get the initial coffeeInfoObject
        coffeeMachineEventHandler.sendRequestStateEvent();
        smokeDetectorEventHandler.sendRequestEvent();
        mindstormsEventHandler.sendInfoRequestEvent();
        doorEventHandler.sendInfoRequestEvent();
    }

    /**
     * checks weather the phone is connected to any wifi network or not
     *
     * @return true, if the phone is connected to a wifi network
     * false otherwise
     * <p>
     * return false if the status can't be detected
     */

    private static boolean wifiIsConnected() {
        ConnectivityManager connManager;
        NetworkInfo mWifi;
        try {
            connManager = (ConnectivityManager) MainActivity.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
            mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        } catch (Exception e) {
            // could not get wifi status
            e.printStackTrace();
            return false;
        }

        return mWifi.isConnected();
    }

    public static CoffeeEventHandler getCoffeeMachineEventHandler() {
        return coffeeMachineEventHandler;
    }

    public static MindstormsEventHandler getMindstormsEventHandler() {
        return mindstormsEventHandler;
    }

    public static LightEventHandler getLightEventHandler() {

        return lightEventHandler;
    }

    public static DoorEventHandler getDoorEventHandler() {
        return doorEventHandler;
    }

    public static SmokeDeviceEventHandler getSmokeDetectorEventHandler() {
        return smokeDetectorEventHandler;
    }

    public static void setKaaClient(KaaClient kaaClient) {
        KaaManager.kaaClient = kaaClient;
    }

    public static KaaClient getKaaClient() {
        return kaaClient;
    }

    private static void setFailoverStrategy() {
        // Overriding default failover strategy
        kaaClient.setFailoverStrategy(new DefaultFailoverStrategy() {

            @Override
            public void onRecover(TransportConnectionInfo connectionInfo) {
                super.onRecover(connectionInfo);
                //make call to external service, add extra logging or do anything else
                connected = true;
                // send request event to get the initial coffeeInfoObject
                if (MainActivity.getInstance().getStatusFragment() != null) {
                    MainActivity.getInstance().runOnUiThread(new Runnable() {
                        public void run() {
                            MainActivity.getInstance().getStatusFragment().updateStatus();
                        }
                    });
                }
                Log.d("ConnectionToKaa", "Recover");
            }

            @Override
            public FailoverDecision onFailover(FailoverStatus failoverStatus) {
                super.onFailover(failoverStatus);
                connected = false;

                if (MainActivity.getInstance().getStatusFragment() != null) {
                    MainActivity.getInstance().runOnUiThread(new Runnable() {
                        public void run() {
                            MainActivity.getInstance().getStatusFragment().updateStatus();
                        }
                    });
                }

                if (failoverStatus == FailoverStatus.OPERATION_SERVERS_NA) {
                    Log.d("ConnectionToKaa", "OPERATION_SERVERS_NA");
                    return new FailoverDecision(FailoverDecision.FailoverAction.USE_NEXT_BOOTSTRAP);
                } else {
                    Log.d("ConnectionToKaa", failoverStatus.name());
                    return super.onFailover(failoverStatus);
                }
            }
        });
    }


    public static boolean isConnected() {
        return connected;
    }

}

