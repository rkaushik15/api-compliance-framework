package org.ga4gh.ComplianceFramework;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.Map;

/**
 * The class that holds the information about a particular web server, such as it's base URL.
 */
public class Server {

    /**
     * The logger object used by log4j to record application logs.
     */
    private static Logger log = LogManager.getLogger(Server.class.getName());

    /**
     * The base URL/address of the server.
     */
    private String BASE_URL;

    /**
     * Map containing other properties of the server
     */
    private Map<String, Object> server_properties;

    /**
     * Constructor to initialise a Server object using base URL of the server.
     * @param BASE_URL The base URL of the new server that is initialised.
     */
    public Server(String BASE_URL) {
        this.BASE_URL = BASE_URL;
        log.debug("Server Base URL set to: " + this.BASE_URL);
    }

    /**
     * Constructor to initialise a Server object using base URL and other properties of the server.
     * @param BASE_URL The base URL of the new server that is initialised.
     * @param server_properties The Map containing other properties of the server.
     */
    public Server(String BASE_URL, Map<String, Object> server_properties) {
        this.BASE_URL = BASE_URL;
        this.server_properties = server_properties;
        log.debug("Server Base URL set to: " + this.BASE_URL);
        log.debug("Server properties set to: " + this.server_properties);
    }

    /**
     * Getter method to retrieve the base URL of the server.
     * @return the base URL of the server.
     */
    public String getBaseURL() {
        return BASE_URL;
    }

    /**
     * Setter method to set the base URL of the server to a specific value.
     * @param BASE_URL The address that is to be set as base URL of the server.
     */
    public void setBaseURL(String BASE_URL) {
        this.BASE_URL = BASE_URL;
        log.debug("Server Base URL set to: " + this.BASE_URL);
    }

    /**
     * Method to retrieve the final API endpoint of a service.
     * @param path The path to the service/resource
     * @return The final endpoint/path pointing to the web service/resource.
     */
    public String getEndpoint(String path){
        return BASE_URL+path;
    }

    /**
     * Getter method to retreive the properties map of the server.
     * @return the properties of the server as a Map.
     */
    public Map<String, Object> getServerProperties() {
        return server_properties;
    }

    /**
     * Setter method to set the properties of the server to specific values.
     * @param server_properties The properties that are to be associated with the server.
     */
    public void setServer_properties(Map<String, Object> server_properties) {
        this.server_properties = server_properties;
        log.debug("Server properties set to: " + this.server_properties);
    }
}
