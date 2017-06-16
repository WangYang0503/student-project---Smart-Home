package smart_things.coffee.api.main;

import smart_things.coffee.api.exceptions.AlreadyBrewingException;
import smart_things.coffee.api.exceptions.LowWaterException;
import smart_things.coffee.api.exceptions.NoCarafeException;
import smart_things.coffee.api.exceptions.NoWaterException;
import smart_things.coffee.api.messages.CommandCodes;
import smart_things.coffee.api.messages.StrengthCodes;
import smart_things.coffee.api.model.CoffeeMachine;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Observer;

/**
 * This is a API that can be used to communicate with the
 * "Smarter Coffee Machine" (see http://smarter.am/coffee/) as a client. It
 * allows to send data to the coffee machine, including commands like brewing
 * and changing the cup size and to receive data from it like errors and state
 * values. Error handling is established and a connection error handler can be used to handle connection
 * exceptions during runtime.
 *
 * @author Jan
 * @version 1.01
 *          TODO: Method for: 10007e - Reset
 *          TODO: Method for: 0f007e - WiFi-Reset
 */
public class SmartCoffeeMachine {
    // Common constants
    // Minimum number of cups
    public static final int CUPS_MIN = 1;
    // Maximum number of cups
    public static final int CUPS_MAX = 12;
    // Minimum time in minutes for the hot plate before auto turning off
    public static final int HOTPLATE_MIN = 5;
    // Maximum time in minutes for the hot plate before auto turning off
    public static final int HOTPLATE_MAX = 40;

    // Host of the coffee machine
    private String host = "";
    // Port on which to connect to the coffee machine
    private int port = -1;
    //Connection error handler for handling connection errors during runtime
    private Runnable connectionErrorHandler;

    // Variables to keep connection and to send data
    private Socket socket = null;
    private OutputStream out = null;
    private PrintStream writer = null;

    // Variables to receive messages
    private MessageReceiver receiver = null;

    // Variables to handle status messages
    private ReplyCodeHandler statusHandler = null;

    /**
     * Creates a new SmartCofeeMachine object that is able to connect to the
     * machine using the given host name and connection port.
     *
     * @param host Host name of the coffee machine to connect to (an IP-Address for example)
     * @param port Port on which to connect to the coffee machine. Usual: 2081
     * @param connectionErrorHandler Handler that can be used to handle connection error events
     */
    public SmartCoffeeMachine(String host, int port, Runnable connectionErrorHandler) {
        // Check arguments for validity
        if (!isLegalHost(host)) {
            throw new IllegalArgumentException("Invalid host.");
        }
        if (!isLegalPort(port)) {
            throw new IllegalArgumentException("Invalid port.");
        }
        // Assign connection attributes
        this.host = host;
        this.port = port;
        this.connectionErrorHandler = connectionErrorHandler;

        // Initialize message handlers
        statusHandler = new ReplyCodeHandler();
    }

    /**
     * Establishes a connection to the coffee machine. If a connection cannot be established, a timeout will throw a
     * SocketTimeoutException after 20 seconds.
     *
     * @return True, if the connection establishment was successful, false otherwise
     * @throws UnknownHostException In case of that the host can not be found in this network
     * @throws IOException          In case of a connection error
     */
    public boolean connect() throws IOException {

        int timeout = 20000;

        // Check whether a connection already exists
        if (isConnected()) {
            throw new IllegalStateException("Already connected.");
        }

        // Create socket
        socket = new Socket();

        //Set timeout
        socket.setSoTimeout(timeout);

        //Try to connect with timeout
        socket.connect(new InetSocketAddress(this.host, this.port), timeout);

        // Create stream and writer from the socket
        out = socket.getOutputStream();
        writer = new PrintStream(out, true, "UTF-8");

        // MessageReceiver for receiving messages from the coffee machine
        receiver = new MessageReceiver(socket.getInputStream(), connectionErrorHandler);
        receiver.start();

        // Check if connection establishment was successful
        return isConnected();
    }

    /**
     * Disconnects from the coffee machine.
     *
     * @throws IOException In case of a connection error
     */
    public void disconnect() throws IOException {
        // Check for null and close socket
        if (socket != null) {
            socket.close();
        }
        // Check for null and stop MessageReceiver
        if (receiver != null) {
            receiver.interrupt();
        }
    }

    /**
     * Checks whether there is a connection to the coffee machine or not.
     *
     * @return True, if a connection exists, false otherwise.
     */
    public boolean isConnected() {
        return (socket != null) && (socket.isConnected()) && (!socket.isClosed());
    }

    /**
     * Retrieves a model of the current machine containing the latest received
     * values and status information.
     *
     * @return The coffee machine model
     */
    public CoffeeMachine getCoffeeMachineModel() {
        if (receiver == null) {
            throw new IllegalStateException("Not connected.");
        }
        return receiver.getCoffeeMachineModel();
    }

    /**
     * Adds a polling observer that will be notified on coffee machine polling
     * changes like setting the numbers of cups to brew coffee for.
     *
     * @param observer The observer to add
     */
    public void addPollingObserver(Observer observer) {
        // Check connection
        if (!isConnected()) {
            throw new IllegalStateException("Not connected.");
        }
        receiver.addPollingObserver(observer);
    }

    /**
     * Lets the coffee machine brew coffee with the current settings.
     *
     * @throws IOException             In case of a connection error
     * @throws LowWaterException       In case of that the coffee machine has a low water level
     * @throws NoWaterException        In case of that the coffee machine has no water anymore
     * @throws NoCarafeException       In case of that the coffee machine has no carafe. Deactivate
     *                                 the carafe detection to start brewing without a carafe
     * @throws AlreadyBrewingException In case of that the coffee machine is already brewing
     */
    public void brewCoffee()
            throws IOException, AlreadyBrewingException, NoCarafeException, NoWaterException, LowWaterException {
        // Check connection
        if (!isConnected()) {
            throw new IllegalStateException("Not connected.");
        }
        // Send magical code for brewing to coffee machine
        send(CommandCodes.BREW_COFFEE);

        // Receive and handle answer
        // Receive and handle answer
        String message = receiver.getNextReplyMessage();
        statusHandler.handleAll(message);
    }

    /**
     * Lets the coffee machine stop brewing coffee immediately.
     *
     * @throws IOException In case of a connection error
     */
    public void stopBrewing() throws IOException {
        // Check connection
        if (!isConnected()) {
            throw new IllegalStateException("Not connected.");
        }

        // Send magical code for stop brewing to coffee machine
        send(CommandCodes.STOP_BREWING);

        // Dont handle messages, because the machine will not send anything
    }

    /**
     * Changes the number of cups at the coffee machine. This number is
     * responding to the amount of coffee that should be brewed.
     *
     * @param cups The number of cups to brew coffee for. See constants CUPS_MIN and CUPS_MAX for
     *             bounds of this value
     * @throws IOException In case of a connection error
     */
    public void setCupsNumber(int cups) throws IOException {
        // Check parameter for validity
        if ((cups < CUPS_MIN) || (cups > CUPS_MAX)) {
            throw new IllegalArgumentException(
                    "Number of cups is out of bounds! See CUPS_MIN and CUPS_MAX for bounds.");
        }

        // Check connection
        if (!isConnected()) {
            throw new IllegalStateException("Not connected.");
        }

        // Convert cups number to hex
        String cupsParam = HexConverter.encodeHex(cups);

        // Send magical code for changing cups to coffee machine
        send(CommandCodes.CHANGE_CUPS.build(cupsParam));

        // Receive and handle answer
        String message = receiver.getNextReplyMessage();
        statusHandler.handleDefault(message);
    }

    /**
     * Changes the brewing strength at the coffee machine. This value is
     * responding to the strength the of coffee that should be brewed.
     *
     * @param strength The strength of the wanted coffee. Use constants STRENGTH_WEAK,
     *                 STRENGTH_MEDIUM or STRENGTH_STRONG (accords to integers 1 - 3)
     * @throws IOException In case of a connection error
     */
    public void setBrewingStrength(StrengthCodes strength) throws IOException {
        int strengthNumber = 0;
        // Check parameter for validity
        switch (strength) {
            case WEAK:
                //Strength: Weak
                strengthNumber = 0;
                break;
            case MEDIUM:
                //Strength: Medium
                strengthNumber = 1;
                break;
            case STRONG:
                //Strength: Strong
                strengthNumber = 2;
                break;
            default:
                //Illegal argument
                throw new IllegalArgumentException("Illegal brewing strength argument. Only StrengthCodes.WEAK"
                        + ", StrengthCodes.MEDIUM and StrengthCodes.STRONG are allowed.");

        }
        // Check connection
        if (!isConnected()) {
            throw new IllegalStateException("Not connected.");
        }

        // Convert strength number to hex
        String strengthParam = HexConverter.encodeHex(strengthNumber);

        // Send magical code for changing strength to coffee machine
        send(CommandCodes.CHANGE_STRENGTH.build(strengthParam));

        // Receive and handle answer
        String message = receiver.getNextReplyMessage();
        statusHandler.handleDefault(message);
    }

    /**
     * Changes to brewing type of the coffee machine (using either a filter or
     * the grinder). Filter usage will be changed to grinder usage and vice
     * versa.
     *
     * @throws IOException In case of a connection error
     */
    public void changeBrewingType() throws IOException {
        // Check connection
        if (!isConnected()) {
            throw new IllegalStateException("Not connected.");
        }

        // Send magical code for changing the brewing type to filter
        send(CommandCodes.CHANGE_BREWING_TYPE.toString());

        // Receive and handle answer
        String message = receiver.getNextReplyMessage();
        statusHandler.handleDefault(message);
    }

    /**
     * Changes the time how long the hot plate of the coffee machine will be on
     * before auto turning off.
     *
     * @param time The time how long the hot plate will be on in minutes. See constants HOTPLATE_MIN
     *             and HOTPLATE_MAX for bounds of this value
     * @throws IOException In case of a connection error
     */
    public void setHotPlateTime(int time) throws IOException {
        // Check parameter for validity
        if ((time < HOTPLATE_MIN) || (time > HOTPLATE_MAX)) {
            throw new IllegalArgumentException(
                    "Hotplate time is out of bounds! See HOTPLATE_MIN and HOTPLATE_MAX for bounds.");
        }

        // Check connection
        if (!isConnected()) {
            throw new IllegalStateException("Not connected.");
        }

        // Convert strength number to hex
        String timeParam = HexConverter.encodeHex(time);

        // Send magical code for changing strength to coffee machine
        send(CommandCodes.CHANGE_HOTPLATE_TIME.build(timeParam));

        // Receive and handle answer
        String message = receiver.getNextReplyMessage();
        statusHandler.handleDefault(message);
    }

    /**
     * Returns the given host name of the coffee machine.
     *
     * @return The given host name to connect on (an IP-Address for example)
     */
    public String getHost() {
        return host;
    }

    /**
     * Saves the host name of the coffee machine. Note: A possibly existing
     * connection will not be aborted. It is necessary to disconnect and connect
     * again to make the changes work.
     *
     * @param host The given host name to connect on (an IP-Address for example)
     */
    public void setHost(String host) {
        // Check parameter for validity
        if (!isLegalHost(host)) {
            throw new IllegalArgumentException("Invalid host.");
        }
        this.host = host;
    }

    /**
     * Returns the given connection port of the coffee machine.
     *
     * @return The given port to connect on
     */
    public int getPort() {
        return port;
    }

    /**
     * Sets the port of the coffee machine. Note: A possibly existing connection
     * will not be aborted. It is necessary to disconnect and connect again to
     * make the changes work.
     *
     * @param port The given port to connect on
     */
    public void setPort(int port) {
        // Check parameter for validity
        if (!isLegalPort(port)) {
            throw new IllegalArgumentException("Invalid port.");
        }
        this.port = port;
    }

    /**
     * Sends a message in hexadecimal code to the coffee machine. The message
     * will be decoded to ASCII before sending.
     *
     * @param hexMessage The message to send to the coffee machine in hexadecimal code
     */
    private void send(String hexMessage) {
        // Check connection
        if (!isConnected()) {
            throw new IllegalStateException("Not connected.");
        }
        // Decode hex message and send
        writer.print(HexConverter.decodeHex(hexMessage));
        writer.flush();
    }

    /**
     * Overloaded wrapper method for send(String hexMessage). Makes it possible
     * to pass a CommandCode directly without using a implicit string
     * conversion.
     *
     * @param code The CommandCode to send to the coffee machine.
     */
    private void send(CommandCodes code) {
        send(code.toString());
    }

    /**
     * Checks whether a given host name is legal or not.
     *
     * @param host The host name to check
     * @return True, if the host name is legal, false otherwise
     */
    private boolean isLegalHost(String host) {
        return !host.equals("");
    }

    /**
     * Checks whether a given port is legal or not.
     *
     * @param port The port to check
     * @return True, if the port is legal, false otherwise
     */
    private boolean isLegalPort(int port) {
        return (port > 0);
    }
}