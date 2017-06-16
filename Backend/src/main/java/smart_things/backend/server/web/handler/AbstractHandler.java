package smart_things.backend.server.web.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import smart_things.backend.server.database.DBManager;
import smart_things.backend.server.rules.RuleWorker;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Abstract http request handler class that implements important basic methods for handling server requests (e.g. Ajax)
 * on the admin web ui server. With this class as superclass for a specific http handler it is possible to send
 * simple basic JSON responses like success and error or files as reply on a GET request.
 * <p>
 * Created by Jan on 18.01.2017.
 */
public abstract class AbstractHandler implements HttpHandler {
    //The root path of the basic web ui source files (like index.html)
    protected static final String rootPath = "WebUI/";
    //Basic http response codes
    private static final int HTTP_OK = 200;
    private static final int HTTP_NOT_FOUND = 404;
    //White list of all paths (inside the root path) that are allowed to handle unchecked GET requests directly
    protected static ArrayList<String> pathWhiteList = null;

    //Build up the white list for all paths inside the root path that are allowed to handle GET requests directly
    static {
        pathWhiteList = new ArrayList<String>();
        pathWhiteList.add("js/");
        pathWhiteList.add("css/");
        pathWhiteList.add("fonts/");
        pathWhiteList.add("img/");
        pathWhiteList.add("log/");
    }

    //Database manager that is able to read/write the database
    protected DBManager dbManager = null;
    //Rule worker that can operate directly on the rules
    protected RuleWorker ruleWorker = null;

    /**
     * Creates a new http request handler using a given database manager to work with the database.
     *
     * @param dbManager  The database manager that has access to the database
     * @param ruleWorker The rule worker that can operate on the rules directly
     */
    protected AbstractHandler(DBManager dbManager, RuleWorker ruleWorker) {
        this.dbManager = dbManager;
        this.ruleWorker = ruleWorker;
    }

    /**
     * Replies to the server request by sending a simple response string to the requesting client.
     *
     * @param httpExchange The HttpExchange object that contains further information about the incoming request
     * @param response     The response string that shall be replied to the requesting client
     * @throws IOException In case of an unexpected stream problem while sending the reply
     */
    protected void writeResponse(HttpExchange httpExchange, String response) throws IOException {
        //Send 200 (OK) http response code
        httpExchange.sendResponseHeaders(HTTP_OK, response.length());
        //Create output stream and send the reply to the requesting client
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        //Close stream after sending
        os.close();
    }

    /**
     * Replies to the server request by sending a simple serialized JSON object as response to the requesting client.
     *
     * @param httpExchange The HttpExchange object that contains further information about the incoming request
     * @param response     The response JSON object that shall be replied to the requesting client
     * @throws IOException In case of an unexpected stream problem while sending the reply
     */
    protected void writeResponse(HttpExchange httpExchange, JSONObject response) throws IOException {
        writeResponse(httpExchange, response.toJSONString());
    }

    /**
     * Replies to the server request by sending a simple serialized JSON array object as response to the requesting
     * client.
     *
     * @param httpExchange The HttpExchange object that contains further information about the incoming request
     * @param response     The response JSON array object that shall be replied to the requesting client
     * @throws IOException In case of an unexpected stream problem while sending the reply
     */
    protected void writeResponse(HttpExchange httpExchange, JSONArray response) throws IOException {
        writeResponse(httpExchange, response.toJSONString());
    }

    /**
     * Replies to the server request by sending a simple serialized JSON success string ({error: false}) to the
     * requesting client.
     *
     * @param httpExchange The HttpExchange object that contains further information about the incoming request
     * @throws IOException In case of an unexpected stream problem while sending the reply
     */
    protected void sendSuccess(HttpExchange httpExchange) throws IOException {
        //Create JSON object
        JSONObject response = new JSONObject();
        response.put("error", false);
        //Send the object as string
        writeResponse(httpExchange, response);
    }

    /**
     * Replies to the server request by sending a simple serialized JSON error string ({error: true, msg: "..."})
     * to the requesting client including a given error message.
     *
     * @param httpExchange The HttpExchange object that contains further information about the incoming request
     * @param errorMessage The error message that shall be send to the requesting client
     * @throws IOException In case of an unexpected stream problem while sending the reply
     */
    protected void sendError(HttpExchange httpExchange, String errorMessage) throws IOException {
        //Create JSON object
        JSONObject response = new JSONObject();
        response.put("error", true);
        response.put("msg", errorMessage);
        //Send the object as string
        writeResponse(httpExchange, response.toJSONString());
    }

    /**
     * Replies to the server request by sending the content of a file to the requesting client. The file
     * corresponding to the given file handle will be read and its content will be sent raw. In case of that the
     * regarding file does not exist, the 404 error code and a error message will be sent.
     *
     * @param httpExchange The HttpExchange object that contains further information about the incoming request
     * @param file         The handle of the file to send
     * @throws IOException In case of an unexpected stream problem while sending the reply
     */
    protected void serveFile(HttpExchange httpExchange, File file) throws IOException {
        //OutputStream that is used to serve the file
        OutputStream os;

        //Check if the concerning file does not exist
        if (!file.isFile()) {
            //File does not exist or is not a file, so reject with 404 error and error message
            //Prepare response
            String response = "File not found";
            httpExchange.sendResponseHeaders(HTTP_NOT_FOUND, response.length());
            //Send response
            os = httpExchange.getResponseBody();
            os.write(response.getBytes());

            //Close the output stream
            os.close();
        }

        // Object exists and is a file, so accept with response code 200
        //Prepare the response
        httpExchange.sendResponseHeaders(HTTP_OK, 0);
        os = httpExchange.getResponseBody();

        //Create a stream to read the file
        FileInputStream fs = new FileInputStream(file);

        //Read the file with buffer
        final byte[] buffer = new byte[0x10000];
        int count = 0;
        while ((count = fs.read(buffer)) >= 0) {
            //Write the file to the output stream in order to send it
            os.write(buffer, 0, count);
        }

        //Close the input stream after the whole file was read and sent
        fs.close();

        //Close the output stream
        os.close();
    }
}