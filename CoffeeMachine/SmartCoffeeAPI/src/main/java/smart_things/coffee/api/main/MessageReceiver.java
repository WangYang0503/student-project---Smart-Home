package smart_things.coffee.api.main;

import smart_things.coffee.api.messages.StatusCodes;
import smart_things.coffee.api.messages.StrengthCodes;
import smart_things.coffee.api.messages.WaterLevelCodes;
import smart_things.coffee.api.model.CoffeeMachine;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observer;

/**
 * Thread for receiving, handling and processing all the messages that are sent
 * from the coffee machine to the client. Differentiates between received reply
 * messages and received polling messages. The next reply message for a
 * outstanding command can be read with the method "getNextReplyMessage".
 * Polling messages will be split up and saved into the CoffeeMachineModel for
 * requesting. If a connection error occurs, a connection error handler will be used
 * to handle it.
 *
 * @author Jan
 * @version 1.01
 */
class MessageReceiver extends Thread {
    // Head on which polling messages can be identified
    private static final String POLLING_HEAD = "32";
    // Maps for converting codes
    private static HashMap<String, StatusCodes> statusCodesMap = null;
    private static HashMap<String, StrengthCodes> brewingStrengthCodesMap = null;
    private static HashMap<String, WaterLevelCodes> waterLevelCodesMap = null;

    //Static method for building static hash maps
    static {
        // Initialize hash maps:
        buildMaps();
    }

    // InputStream from which the messages are received
    private InputStream stream = null;
    //Error handler that is called in case of a connection error
    private Runnable connectionErrorHandler = null;
    // Last received reply on a command; null, when no new reply was received
    private volatile String lastCmdReply = null;
    // Last polling message for checking on differences
    private String lastPollingMessage = "";
    // Model of a coffee machine; used to manage polling attributes
    private CoffeeMachine model = null;

    /**
     * Creates a new message receiver on a given input stream on that messages
     * can be received.
     *
     * @param stream                 The input stream on which message data can be received
     * @param connectionErrorHandler The runnable error handler that handles connection errors
     */
    public MessageReceiver(InputStream stream, Runnable connectionErrorHandler) {
        this.stream = stream;
        this.connectionErrorHandler = connectionErrorHandler;

        // Initialize coffee machine model
        this.model = new CoffeeMachine();
    }

    /**
     * Builds static HashMaps from the code enums.
     */
    public static void buildMaps() {
        // HashMap for status codes
        statusCodesMap = new HashMap<String, StatusCodes>();
        for (StatusCodes code : StatusCodes.values()) {
            statusCodesMap.put(code.toString(), code);
        }

        // HashMap for strength codes
        brewingStrengthCodesMap = new HashMap<String, StrengthCodes>();
        for (StrengthCodes code : StrengthCodes.values()) {
            brewingStrengthCodesMap.put(code.toString(), code);
        }

        // HashMap for water level codes
        waterLevelCodesMap = new HashMap<String, WaterLevelCodes>();
        for (WaterLevelCodes code : WaterLevelCodes.values()) {
            waterLevelCodesMap.put(code.toString(), code);
        }
    }

    /**
     * Run-method of the inherited thread. Receives messages and processes them.
     * Differentiates between received reply messages and received polling
     * messages.
     */
    @Override
    public void run() {
        // Initialize necessary receiving variables
        // Index of the read data
        int red = -1;
        // Buffer of 5 KiB
        byte[] buffer = new byte[5 * 1024];
        // Stores received data as byte
        byte[] data;
        // Stores the final string
        StringBuffer text;
        try {
            // Receives all data from coffee machine until the thread gets
            // killed
            while ((red = stream.read(buffer)) > -1) {
                // Read data
                data = new byte[red];
                System.arraycopy(buffer, 0, data, 0, red);

                // Convert data to a hexadecimal string (hexadecimal)
                //Empty storage string
                text = new StringBuffer();
                //Iterate over each character
                for (int i = 0; i < data.length; i++) {
                    /* Transform each element (character) first from byte to integer (&0xFF),
                    then convert it to hexadecimal and append it to the storage string*/
                    text.append(HexConverter.encodeHex(data[i] & 0xFF));
                }

                // Process converted message
                processMessage(text.toString());
            }
        } catch (IOException e) {
            // Connection error
            System.err.println("Connection error: " + e.getMessage());
            //Handle the connection error using the connectionErrorHandler
            connectionErrorHandler.run();
        }
    }

    /**
     * Adds a polling observer to the coffee machine model that will be notified
     * on changes to the model.
     *
     * @param observer The observer to add
     */
    public void addPollingObserver(Observer observer) {
        model.addObserver(observer);
    }

    /**
     * Waits till the next reply on a before sent command is received and
     * returns it as a hexadecimal string.
     *
     * @return The latest received reply message
     */
    public String getNextReplyMessage() {
        // Wait for next message
        // TODO implement timeout and throw NoReplyException
        while (lastCmdReply == null) {
            // Signal to scheduler
            yield();
        }
        // Get newest message, clean the storage variable and return
        String message = lastCmdReply;
        lastCmdReply = null;
        return message;
    }

    /**
     * Retrieves a model of the current machine containing the latest polling
     * values (number of cups, brewing strength...).
     *
     * @return The coffee machine model
     */
    public CoffeeMachine getCoffeeMachineModel() {
        return model;
    }

    /**
     * Processes a received message. Differentiates between received reply
     * messages and received polling messages and calls the corresponding method
     * on it.
     *
     * @param message The message to process
     */
    private void processMessage(String message) {
        // Polling message or reply message?
        if (message.substring(0, 2).equals(POLLING_HEAD)) {
            // Polling message
            // Check for changes
            if (!this.lastPollingMessage.equals(message)) {
                processPollingMessage(message);
                this.lastPollingMessage = message;
            }
        } else {
            // Reply message
            processReplyMessage(message);
        }
    }

    /**
     * Processes a polling message. It will be split up and the individual
     * parameters will be saved into the CoffeeMachineModel for requesting.
     *
     * @param message The polling message to process
     */
    private void processPollingMessage(String message) {
        ArrayList<String> components = new ArrayList<String>();
        for (int i = 0; i < message.length() / 2; i++) {
            components.add(message.substring(i * 2, (i * 2) + 2));
        }
        // Process status
        // Get status from map
        StatusCodes status = statusCodesMap.get(components.get(1));
        // Check for null
        if (status == null) {
            status = StatusCodes.UNKNOWN;
        }
        // Does the status make a statement about the activity status?
        if (status.isStatusSpecified()) {
            model.setStatus(status.getStatus());
        }
        // Does the status make a statement about the brewing type?
        if (status.isBrewingTypeSpecified()) {
            model.setFilter(status.isFilter());
        }
        // Does the status make a statement about the carafe status?
        if (status.isCarafeStatusSpecified()) {
            model.setCarafe(status.isCarafe());
        }
        // Process water level
        model.setWaterLevel(waterLevelCodesMap.get(components.get(2)));
        // Process WiFi strength
        model.setWifiStrength(components.get(3));
        // Process brewing strength
        model.setBrewingStrength(brewingStrengthCodesMap.get(components.get(4)));
        // Process number of cups
        model.setNumberOfCups(Integer.parseInt(String.valueOf(components.get(5).charAt(1)), 16));

        model.changed();
    }

    /**
     * Processes a reply message from a before sent command. The message will be
     * stored in the storage variable "lastCmdReply" and can be requested with
     * the "getNextReplyMessage" method.
     *
     * @param message The reply message to process
     */
    private void processReplyMessage(String message) {
        lastCmdReply = message;
    }
}
