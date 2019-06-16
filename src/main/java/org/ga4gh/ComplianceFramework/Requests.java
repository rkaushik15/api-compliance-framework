package org.ga4gh.ComplianceFramework;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

/**
 * Class containing methods used to implement REST calls using REST Assured.
 * These methods are be used by test cases to fire requests at REST servers and receive responses.
 */
public class Requests {

    /**
     * The logger object used by log4j to record application logs.
     */
    private static Logger log = LogManager.getLogger(Requests.class.getName());

    /**
     * Method used to fire GET request.
     * @param uri The uri/endpoint for the server that receives the web request and sends response.
     * @return The response object sent by the server.
     */
    public static Response GET(String uri){
        log.info("Firing GET request to " + uri);
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.contentType(ContentType.JSON);
        Response response = requestSpecification.get(uri);
        log.debug(requestSpecification.log().all());
        return response;
    }

    /**
     * Method used to fire POST request.
     * @param uri The uri/endpoint for the server that receives the web request and sends response.
     * @param body The payload sent along with the request.
     * @return The response object sent by the server.
     */
    public static Response POST(String uri, String body){
        log.info("Firing POST request to " + uri);
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.body(body);
        requestSpecification.contentType(ContentType.JSON);
        Response response = requestSpecification.post(uri);
        log.debug(requestSpecification.log().all());
        return response;
    }

    /**
     * Method used to fire PUT request.
     * @param uri The uri/endpoint for the server that receives the web request and sends response.
     * @param body The payload sent along with the request.
     * @return The response object sent by the server.
     */
    public static Response PUT(String uri, String body){
        log.info("Firing PUT request to " + uri);
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.body(body);
        requestSpecification.contentType(ContentType.JSON);
        Response response = requestSpecification.put(uri);
        log.debug(requestSpecification.log().all());
        return response;
    }

    /**
     * Method used to fire DELETE request.
     * @param uri The uri/endpoint for the server that receives the web request and sends response.
     * @param body The payload sent along with the request.
     * @return The response object sent by the server.
     */
    public static Response DELETE(String uri, String body){
        log.info("Firing DELETE request to " + uri);
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.body(body);
        requestSpecification.contentType(ContentType.JSON);
        Response response = requestSpecification.delete(uri);
        log.debug(requestSpecification.log().all());
        return response;
    }
}
