package smart_things.smoke.simulator.kaaManager;

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

import smart_things.smoke.simulator.MainActivity;
import smart_things.smoke.simulator.kaaManager.eventHandler.SmokeDeviceEventHandler;


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

    private static SmokeDeviceEventHandler smokeDeviceEventHandler = null;

    /**
     * connect to the  kaa server and attach the kaa user
     */
    public static void connectToKaa() {

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


                    smokeDeviceEventHandler = new SmokeDeviceEventHandler(kaaClient);
                } else {
                    Log.e("KaaManager.connectToKaa", "Could not attach user!");
                }
            }
        });
    }

    public static void setKaaClient(KaaClient kaaClient) {
        KaaManager.kaaClient = kaaClient;
    }

    public static KaaClient getKaaClient() {
        return kaaClient;
    }

    public static SmokeDeviceEventHandler getSmokeDeviceEventHandler() {
        return smokeDeviceEventHandler;
    }
}