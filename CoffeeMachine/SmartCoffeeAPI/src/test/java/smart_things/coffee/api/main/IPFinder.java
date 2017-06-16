package smart_things.coffee.api.main;

import java.io.IOException;

/**
 * Created by Jan on 28.02.2017.
 */
public class IPFinder {
    public static void main(String args[]) {
        String ip_prefix = "172.24.1.";
        int ip_suffix = 58;
        int port = 2081;

        while (ip_suffix < 255) {
            SmartCoffeeMachine machine = new SmartCoffeeMachine(ip_prefix + ip_suffix, port,
                    () -> System.err.println("Unexpected connction error during runtime."));
            try {
                System.out.println("Trying to connect to: " + (ip_prefix + ip_suffix));
                machine.connect();
                System.out.println("*****CONNECTED*****");
                System.exit(0);
            } catch (IOException e) {
                System.err.println("IOException: " + e.getMessage());
                ip_suffix++;
            }
        }
    }
}
