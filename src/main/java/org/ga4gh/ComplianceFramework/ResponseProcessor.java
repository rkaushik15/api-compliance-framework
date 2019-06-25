package org.ga4gh.ComplianceFramework;

import io.restassured.path.json.JsonPath;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Class containing various methods to process the responses of the web requests/API calls.
 */
public class ResponseProcessor {

    /**
     * The logger object used by log4j to record application logs.
     */
    private static Logger log = LogManager.getLogger(ResponseProcessor.class);

    /**
     * Method used to extract the body of the response sent by the server, as plain text (using String object).
     * @param response The response object received from the server, in response to a web request.
     * @return The response body as a String object.
     */
    public static String getBodyString(Response response){
        log.info("Extracting response body as String");
        String responseString =  response.getBody().toString();
        log.debug("Extracted body: " + responseString);
        return responseString;
    }

    /**
     * Method used to extract the body of the response sent by the server, as a JSON (using JsonPath object).
     * @param response The response object received from the server, in response to a web request.
     * @return The response body as a JsonPath object.
     */
    public static JsonPath getBodyJSON(Response response){
        log.info("Extracting response body as JSON");
        JsonPath responseJSON = response.jsonPath();
        log.debug("Extracted body: " + responseJSON);
        return responseJSON;
    }

    /**
     * Method used to extract the body of the response sent by the server, as XML (using XmlPath object).
     * @param response The response sent by the server.
     * @return The response body as a XmlPath object.
     */
    public static XmlPath getBodyXML(Response response){
        log.info("Extracting response body as XML");
        XmlPath responseXML = response.xmlPath();
        log.debug("Extracted body: " + responseXML);
        return responseXML;
    }

    /**
     * Method used to extract the response/status code of the response sent by the server.
     * @param response The response sent by the server.
     * @return The response/status code of the response.
     */
    public static int getStatusCode(Response response){
        log.info("Extracting response status code");
        int statusCode = response.getStatusCode();
        log.debug("Extracted status code: " + statusCode);
        return statusCode;
    }

    /**
     * Method used to extract the response/status line/message of the response sent by the server.
     * @param response The response sent by the server.
     * @return The response/status line/message of the response.
     */
    public static String getStatusMessage(Response response){
        log.info("Extracting response status message");
        String statusMessage = response.getStatusLine();
        log.debug("Extracted status code: " + statusMessage);
        return statusMessage;
    }

    /**
     * Method used to check if the response is SUCCESS, i.e., the response/status code is 200.
     * @param response The response sent by the server.
     * @return true or false depending on whether the response is SUCCESS or not.
     */
    public static boolean checkSuccess(Response response){
        log.info("Checking if response was SUCCESS");
        boolean flag = (response.statusCode() == 200);
        log.debug("Response success flag: " + flag);
        return flag;
    }
}
