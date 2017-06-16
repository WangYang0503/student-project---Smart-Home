package smart_things.backend.server.web;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Filter that is used to filter the post parameters from the incoming requests. Therefore it parses the requested URI.
 * The post parameters are then available on the HttpExchange attribute 'parameters' for further use.
 * <p>
 * Created by Jan on 20.01.2017.
 */
public class ParameterFilter extends Filter {

    @Override
    public String description() {
        return "Parses the requested URI for geht and post parameters";
    }

    /**
     * Processes the filter on the given HttpExchange object.
     *
     * @param exchange The HttpExchange to process the filter on
     * @param chain    The chain to use
     * @throws IOException In case of a unexpected stream or I/O problem
     */
    @Override
    public void doFilter(HttpExchange exchange, Chain chain)
            throws IOException {
        //Call corresponding methods to parse the parameters
        parseGetParameters(exchange);
        parsePostParameters(exchange);
        //Apply the filter to the cain
        chain.doFilter(exchange);
    }

    /**
     * Parses the given HttpExchange for get parameters and saves them as map in the attribute 'parameters'.
     *
     * @param exchange The HttpExchange to extract the get parameters from
     * @throws UnsupportedEncodingException In case of an unsupported encoding
     */
    private void parseGetParameters(HttpExchange exchange)
            throws UnsupportedEncodingException {
        //Create the map to store the parameters in
        Map<String, Object> parameters = new HashMap<String, Object>();
        //Fetch URI from exchange and perform parameter query on it
        URI requestedUri = exchange.getRequestURI();
        String query = requestedUri.getRawQuery();
        parseQuery(query, parameters);

        //Add parameters to the regarding exchange attribute
        exchange.setAttribute("parameters", parameters);
    }

    /**
     * Parses the given HttpExchange for post parameters and saves them as map in the attribute 'parameters'.
     *
     * @param exchange The HttpExchange to extract the post parameters from
     * @throws UnsupportedEncodingException In case of an unsupported encoding
     */
    private void parsePostParameters(HttpExchange exchange)
            throws IOException {

        //Only take post parameters
        if (!("post".equalsIgnoreCase(exchange.getRequestMethod()))) {
            return;
        }
        //Create the map to store the parameters in
        Map<String, Object> parameters =
                (Map<String, Object>) exchange.getAttribute("parameters");
        //Use stream readers to read the parameters
        InputStreamReader isr =
                new InputStreamReader(exchange.getRequestBody(), "utf-8");
        BufferedReader br = new BufferedReader(isr);
        //Perform parameter query
        String query = br.readLine();
        parseQuery(query, parameters);
    }

    /**
     * Queries the string for parameters; therefore the key-value pairs are taken, split up and added to the
     * given parameter map.
     *
     * @param query      The string to perform the query on
     * @param parameters The map in which the parameters shall be stored
     * @throws UnsupportedEncodingException In case of an unsupported encoding
     */
    private void parseQuery(String query, Map<String, Object> parameters)
            throws UnsupportedEncodingException {

        //Check the validity of the query string
        if (query == null) {
            return;
        }

        //Split string up to key-value pairs
        String pairs[] = query.split("[&]");

        //Iterate over the pairs
        for (String pair : pairs) {
            //Split key and value
            String param[] = pair.split("[=]");

            //Key and value of the current parameter
            String key = null;
            String value = null;

            //Read the key, as far it exists, and store it
            if (param.length > 0) {
                key = URLDecoder.decode(param[0],
                        System.getProperty("file.encoding"));
            }

            //Read the value, as far as it exists, and store it
            if (param.length > 1) {
                value = URLDecoder.decode(param[1],
                        System.getProperty("file.encoding"));
            }

            //Check if the key is already in the map; in this case, do not add the value
            if (parameters.containsKey(key)) {
                return;
            }

            //Add key and value to the map
            parameters.put(key, value);
        }
    }
}
