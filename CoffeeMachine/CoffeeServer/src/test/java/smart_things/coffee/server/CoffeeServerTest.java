package smart_things.coffee.server;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

/**
 * Created by Jan on 16.10.2016.
 */
public class CoffeeServerTest {

    private static final String USER_EXTERNAL_ID = "user";
    private static final String USER_ACCESS_TOKEN = "token";
    private static final String HOST = "192.168.0.99";
    private static final int PORT = 3000;
    private CoffeeServer server = null;

    @Before
    public void setUp() throws Exception {
        server = new CoffeeServer(USER_EXTERNAL_ID, USER_ACCESS_TOKEN, HOST, PORT);
    }

    @After
    public void tearDown() throws Exception {
        try {
            server.stop();
        } catch (IOException e) {
        }
    }

    @Test
    public void initKaa() throws Exception {
        server.initKaa(() -> {
        });
        assertTrue(true);
    }

    @Test(expected = IllegalStateException.class)
    public void initCoffeeMachine1() throws Exception {
        server.initCoffeeMachine();
    }

    @Test(expected = IllegalStateException.class)
    public void initCoffeeMachine2() throws Exception {
        server.initKaa(() -> {

        });
        server.initCoffeeMachine();
    }
}