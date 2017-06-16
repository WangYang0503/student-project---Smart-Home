package com.smartHome.SmartHomeApp.kaa;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.smartHome.SmartHomeApp.R;
import com.smartHome.SmartHomeApp.gui.CarActivity;
import com.smartHome.SmartHomeApp.gui.CoffeeActivity;
import com.smartHome.SmartHomeApp.gui.GarageActivity;
import com.smartHome.SmartHomeApp.gui.LightsActivity;
import com.smartHome.SmartHomeApp.gui.MainActivity;
import com.smartHome.SmartHomeApp.gui.SmokeDetectorActivity;
import com.smartHome.SmartHomeApp.kaa.eventHandler.CoffeeMachineEventHandler;
import com.smartHome.SmartHomeApp.kaa.eventHandler.GarageEventHandler;
import com.smartHome.SmartHomeApp.kaa.eventHandler.LightEventHandler;
import com.smartHome.SmartHomeApp.kaa.eventHandler.MindstormsEventHandler;
import com.smartHome.SmartHomeApp.kaa.eventHandler.SmokeDetectorEventHandler;

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

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * Created by Alex on 31.08.16.
 * <p/>
 * kaa settings
 * <p/>
 * connects to the kaa server
 */
public class KaaManager {

    // kaa user credentials
    private static final String USER_EXTERNAL_ID = "coffee_user";
    private static final String USER_ACCESS_TOKEN = "coffee_user_token";

    private static KaaClient kaaClient;
    private static boolean connected = false;
    private static boolean neverConnected = true;
    private static boolean neverFailed = true;

    private static CoffeeMachineEventHandler coffeeMachineEventHandler = null;
    private static MindstormsEventHandler mindstormsEventHandler = null;
    private static LightEventHandler lightEventHandler = null;
    private static GarageEventHandler garageEventHandler = null;
    private static SmokeDetectorEventHandler smokeDetectorEventHandler = null;

    /**
     * tries to connect to kaa server @MAX_CONNECTION_TRIES times
     */
    public static void connectToKaaServer() {
        if (!wifiIsConnected()) {
            showNoWifiWarningMessage();
            Log.d("KaaManager", "showNoWifiWarningMessage() passed");
        }
        connectToKaa();
    }

    /**
     * connect to the  kaa server and attach the kaa user
     */
    private static void connectToKaa() {
        if (kaaClient == null) {
            kaaClient = Kaa.newClient(new AndroidKaaPlatformContext(getActivity()), new SimpleKaaClientStateListener() {
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
        coffeeMachineEventHandler = new CoffeeMachineEventHandler(kaaClient);
        mindstormsEventHandler = new MindstormsEventHandler(kaaClient);
        lightEventHandler = new LightEventHandler(kaaClient);
        garageEventHandler = new GarageEventHandler(kaaClient);
        smokeDetectorEventHandler = new SmokeDetectorEventHandler(kaaClient);

        // send request event to get the initial coffeeInfoObject
        coffeeMachineEventHandler.sendRequestStateEvent();
        smokeDetectorEventHandler.sendRequestEvent();
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
            connManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        } catch (Exception e) {
            // could not get wifi status
            e.printStackTrace();
            return false;
        }

        return mWifi.isConnected();
    }

    /**
     * creates a popup message which informs the user that the mobile phone is not connected
     * to any wifi network. There is a button to quick jump to the phone's wifi settings.
     */
    private static void showNoWifiWarningMessage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                //TODO: put these strings in resources
                .setTitle("Achtung: Überprüfen Sie Ihre Internetverbindung")
                .setMessage("Diese App funktioniert nur innerhalb des Kaa-Netzwerkes. Drücken " +
                        "Sie  Verbinden um ihr Wifi zu ändern")
                .setPositiveButton(R.string.alert_dialog_con,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Intent gpsOptionsIntent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                                gpsOptionsIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                getActivity().startActivityForResult(gpsOptionsIntent, 0);
                            }
                        }
                )
                .setNegativeButton(R.string.alert_dialog_cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // Do nothing
                            }
                        }
                )
                .setCancelable(true);
        AlertDialog al = builder.create();
        al.show();
    }

    public static void displayToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    public static CoffeeMachineEventHandler getCoffeeMachineEventHandler() {
        return coffeeMachineEventHandler;
    }

    public static MindstormsEventHandler getMindstormsEventHandler() {
        return mindstormsEventHandler;
    }

    public static LightEventHandler getLightEventHandler() {

        return lightEventHandler;
    }

    public static GarageEventHandler getGarageEventHandler() {
        return garageEventHandler;
    }

    public static SmokeDetectorEventHandler getSmokeDetectorEventHandler() {
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
                Log.d("ConnectionToKaa", "Recover");

                // enable buttons in MainActivity if Main is currently active
                final Activity currentActivity = getActivity();

                if (!neverFailed && !connected) {
                    currentActivity.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getActivity(), R.string.kaaConnectionRecover, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                neverConnected = false;
                connected = true;

                if (currentActivity instanceof MainActivity) {
                    try {
                        currentActivity.runOnUiThread(new Runnable() {
                            public void run() {
                                ((MainActivity) currentActivity).updateButtonEnableState();

                            }
                        });
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
                //TODO: enable radioButtons and set water lvl, if CoffeeActivity is currently active.
            }

            @Override
            public FailoverDecision onFailover(FailoverStatus failoverStatus) {
                super.onFailover(failoverStatus);
                // disable buttons in MainActivity if Main is currently active
                final Activity currentActivity = getActivity();

                try {
                    if (!neverConnected && connected) {
                        currentActivity.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(getActivity(), R.string.kaaConnectionLost, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

                if (!wifiIsConnected()) {
                    try {
                        currentActivity.runOnUiThread(new Runnable() {
                            public void run() {
                                showNoWifiWarningMessage();
                            }
                        });
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
                connected = false;
                neverFailed = false;

                if (currentActivity instanceof MainActivity) {
                    try {
                        currentActivity.runOnUiThread(new Runnable() {
                            public void run() {
                                ((MainActivity) currentActivity).updateButtonEnableState();
                            }
                        });
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                } else if (currentActivity instanceof CoffeeActivity
                        || currentActivity instanceof CarActivity
                        || currentActivity instanceof LightsActivity
                        || currentActivity instanceof GarageActivity
                        || currentActivity instanceof SmokeDetectorActivity) {
                    currentActivity.finish();
                    try {
                        currentActivity.runOnUiThread(new Runnable() {
                            public void run() {
                                ((MainActivity) getActivity()).updateButtonEnableState();
                            }
                        });
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
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

    /**
     * returns the currently active Activity
     * <p>
     * moved from CoffeeNotifications
     * Source: https://stackoverflow.com/questions/11411395/how-to-get-current-foreground-activity-context-in-android/28423385#28423385
     */
    public static Activity getActivity() {
        Class activityThreadClass = null;
        Object activityThread = null;
        Field activitiesField = null;
        Map<Object, Object> activities = null;

        try {
            activityThreadClass = Class.forName("android.app.ActivityThread");
            activityThread = activityThreadClass.getMethod("currentActivityThread").invoke(null);
            activitiesField = activityThreadClass.getDeclaredField("mActivities");
            activitiesField.setAccessible(true);
            activities = (Map<Object, Object>) activitiesField.get(activityThread);

        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (activities == null) {
            return MainActivity.getInstance();
        }

        for (Object activityRecord : activities.values()) {
            Class activityRecordClass = activityRecord.getClass();
            Field pausedField = null;
            try {
                pausedField = activityRecordClass.getDeclaredField("paused");
                pausedField.setAccessible(true);
                if (!pausedField.getBoolean(activityRecord)) {
                    Field activityField = activityRecordClass.getDeclaredField("activity");
                    activityField.setAccessible(true);
                    return (Activity) activityField.get(activityRecord);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }

        // if no valid instance found, return the Main activity if available;
        // or null, if this isn't available, too.
        return MainActivity.getInstance();
    }
}