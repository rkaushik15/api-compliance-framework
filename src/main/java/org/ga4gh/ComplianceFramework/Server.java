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
    private static Logger log = LogManager.getLogger(Server.class);

    /**
     * The base URL/address of the server.
     */
    private String baseUrl;

    /**
     * Map containing other properties of the server
     */
    private Map<String, Object> serverProperties;

    /**
     * Constructor to initialise a Server object using base URL of the server.
     * @param baseUrl The base URL of the new server that is initialised.
     */
    public Server(String baseUrl) {
        this.baseUrl = baseUrl;
        log.debug("Server Base URL set to: " + this.baseUrl);
    }

    /**
     * Constructor to initialise a Server object using base URL and other properties of the server.
     * @param baseUrl The base URL of the new server that is initialised.
     * @param serverProperties The Map containing other properties of the server.
     */
    public Server(String baseUrl, Map<String, Object> serverProperties) {
        this.baseUrl = baseUrl;
        this.serverProperties = serverProperties;
        log.debug("Server Base URL set to: " + this.baseUrl);
        log.debug("Server properties set to: " + this.serverProperties);
    }

    /**
     * Getter method to retrieve the base URL of the server.
     * @return the base URL of the server.
     */
    public String getBaseUrl() {
        return baseUrl;
    }

    /**
     * Setter method to set the base URL of the server to a specific value.
     * @param baseUrl The address that is to be set as base URL of the server.
     */
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        log.debug("Server Base URL set to: " + this.baseUrl);
    }

    /**
     * Method to retrieve the final API endpoint of a service.
     * @param path The path to the service/resource
     * @return The final endpoint/path pointing to the web service/resource.
     */
    public String getEndpoint(String path){
        return baseUrl + path;
    }

    /**
     * Getter method to retreive the properties map of the server.
     * @return the properties of the server as a Map.
     */
    public Map<String, Object> getServerProperties() {
        return serverProperties;
    }

    /**
     * Getter method to retrieve a specific property of the server.
     * @return the property of the server.
     */
    public Object getServerProperty(String property) {
        return serverProperties.get(property);
    }

    /**
     * Setter method to set the properties of the server to specific values.
     * @param serverProperties The properties that are to be associated with the server.
     */
    public void setServerProperties(Map<String, Object> serverProperties) {
        this.serverProperties = serverProperties;
        log.debug("Server properties set to: " + this.serverProperties);
    }
}
