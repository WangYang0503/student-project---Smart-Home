package smart_things.coffee.api.main;

import org.junit.Before;
import smart_things.coffee.api.messages.StrengthCodes;

import java.util.Observable;
import java.util.Observer;

import static org.junit.Assert.*;

/**
 * Created by Jan on 14.10.2016.
 */
public class SmartCoffeeMachineTest {

    private SmartCoffeeMachine machine;
    private String host = "192.168.0.20";
    private int port = 1000;

    @Before
    public void setUp() throws Exception {
        machine = new SmartCoffeeMachine(host, port,
                () -> System.err.println("Unexpected connction error during runtime."));
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void constructor1() throws Exception {
        new SmartCoffeeMachine("", 1000, () -> System.err.println("Unexpected connction error during runtime."));
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void constructor2() throws Exception {
        new SmartCoffeeMachine(host, 0, () -> System.err.println("Unexpected connction error during runtime."));
    }

    @org.junit.Test
    public void disconnect() throws Exception {
        machine.disconnect();
    }

    @org.junit.Test
    public void isConnected() throws Exception {
        assertFalse(machine.isConnected());
    }

    @org.junit.Test(expected = IllegalStateException.class)
    public void getCoffeeMachineModel() throws Exception {
        assertNotNull(machine.getCoffeeMachineModel());
    }

    @org.junit.Test(expected = IllegalStateException.class)
    public void addPollingObserver() throws Exception {
        machine.addPollingObserver(new Observer() {
            public void update(Observable o, Object arg) {

            }
        });
    }

    @org.junit.Test(expected = IllegalStateException.class)
    public void brewCoffee() throws Exception {
        machine.brewCoffee();
    }

    @org.junit.Test(expected = IllegalStateException.class)
    public void stopBrewing() throws Exception {
        machine.stopBrewing();
    }

    @org.junit.Test(expected = IllegalStateException.class)
    public void setCupsNumber1() throws Exception {
        machine.setCupsNumber(4);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void setCupsNumber2() throws Exception {
        machine.setCupsNumber(0);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void setCupsNumber3() throws Exception {
        machine.setCupsNumber(14);
    }

    @org.junit.Test(expected = IllegalStateException.class)
    public void setBrewingStrength() throws Exception {
        machine.setBrewingStrength(StrengthCodes.STRONG);
    }

    @org.junit.Test(expected = IllegalStateException.class)
    public void changeBrewingType() throws Exception {
        machine.changeBrewingType();
    }

    @org.junit.Test(expected = IllegalStateException.class)
    public void setHotPlateTime1() throws Exception {
        machine.setHotPlateTime(5);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void setHotPlateTime2() throws Exception {
        machine.setHotPlateTime(50);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void setHotPlateTime3() throws Exception {
        machine.setHotPlateTime(4);
    }

    @org.junit.Test
    public void getHost() throws Exception {
        assertEquals(machine.getHost(), host);
    }

    @org.junit.Test
    public void setHost1() throws Exception {
        String host = "192.168.0.99";
        machine.setHost(host);
        assertEquals(machine.getHost(), host);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void setHost2() throws Exception {
        machine.setHost("");
    }

    @org.junit.Test
    public void getPort() throws Exception {
        assertEquals(machine.getPort(), port);
    }

    @org.junit.Test
    public void setPort1() throws Exception {
        int port = 3000;
        machine.setPort(port);
        assertEquals(machine.getPort(), port);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void setPort2() throws Exception {
        machine.setPort(-1);
    }

}