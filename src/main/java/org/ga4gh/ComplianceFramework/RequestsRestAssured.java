package org.ga4gh.ComplianceFramework;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

/**
 * Class containing methods used to implement REST calls using REST Assured.
 * These methods are be used by test cases to fire requests at REST servers and receive responses.
 */
public class RequestsRestAssured implements Requests {

    /**
     * The logger object used by log4j to record application logs.
     */
    private static Logger log = LogManager.getLogger(RequestsRestAssured.class);

    /**
     * Method used to fire GET request.
     * @param uri The uri/endpoint for the server that receives the web request and sends response.
     * @return The response object sent by the server.
     */
    public Response GET(String uri){
        log.info("Firing GET request to " + uri);
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.contentType(ContentType.JSON);
        Response response = requestSpecification.get(uri);
        //log.debug(requestSpecification.log().all());
        return response;
    }

    /**
     * Method used to fire GET request with query parameters.
     * @param uri The uri/endpoint for the server that receives the web request and sends response.
     * @param parameterMap The map of query parameters, contains key-value pairs of all the parameters.
     * @return The response object sent by the server.
     */
    public Response GETWithQueryParams(String uri, Map<String, String> parameterMap){
        log.info("Firing GET request to " + uri);
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.queryParams(parameterMap);
        requestSpecification.contentType(ContentType.JSON);
        Response response = requestSpecification.get(uri);
        //log.debug(requestSpecification.log().all());
        return response;
    }

    /**
     * Method used to fire GET request with headers.
     * @param uri The uri/endpoint for the server that receives the web request and sends response.
     * @param headerMap The map of headers, contains key-value pairs of all the headers.
     * @return The response object sent by the server.
     */
    public Response GETWithHeaders(String uri, Map<String, String> headerMap){
        log.info("Firing GET request to " + uri);
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.headers(headerMap);
        requestSpecification.contentType(ContentType.JSON);
        Response response = requestSpecification.get(uri);
        //log.debug(requestSpecification.log().all());
        return response;
    }

    /**
     * Method used to fire GET request with headers and query parameters.
     * @param uri The uri/endpoint for the server that receives the web request and sends response.
     * @param headerMap The map of headers, contains key-value pairs of all the headers.
     * @param parameterMap The map of query parameters, contains key-value pairs of all the parameters.
     * @return The response object sent by the server.
     */
    public Response GETWithHeadersAndQueryParams(String uri, Map<String, String> headerMap, Map<String, String> parameterMap){
        log.info("Firing GET request to " + uri);
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.headers(headerMap);
        requestSpecification.queryParams(parameterMap);
        requestSpecification.contentType(ContentType.JSON);
        Response response = requestSpecification.get(uri);
        //log.debug(requestSpecification.log().all());
        return response;
    }

    /**
     * Method used to fire POST request.
     * @param uri The uri/endpoint for the server that receives the web request and sends response.
     * @param body The payload sent along with the request.
     * @return The response object sent by the server.
     */
    public Response POST(String uri, String body){
        log.info("Firing POST request to " + uri);
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.body(body);
        requestSpecification.contentType(ContentType.JSON);
        Response response = requestSpecification.post(uri);
        //log.debug(requestSpecification.log().all());
        return response;
    }

    /**
     * Method used to fire PUT request.
     * @param uri The uri/endpoint for the server that receives the web request and sends response.
     * @param body The payload sent along with the request.
     * @return The response object sent by the server.
     */
    public Response PUT(String uri, String body){
        log.info("Firing PUT request to " + uri);
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.body(body);
        requestSpecification.contentType(ContentType.JSON);
        Response response = requestSpecification.put(uri);
        //log.debug(requestSpecification.log().all());
        return response;
    }

    /**
     * Method used to fire DELETE request.
     * @param uri The uri/endpoint for the server that receives the web request and sends response.
     * @param body The payload sent along with the request.
     * @return The response object sent by the server.
     */
    public Response DELETE(String uri, String body){
        log.info("Firing DELETE request to " + uri);
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.body(body);
        requestSpecification.contentType(ContentType.JSON);
        Response response = requestSpecification.delete(uri);
        //log.debug(requestSpecification.log().all());
        return response;
    }
}
