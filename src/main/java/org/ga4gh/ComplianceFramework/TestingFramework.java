package org.ga4gh.ComplianceFramework;

import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Class containing basic methods used by most of the test cases while checking for API compliance.
 * These methods would improve re-usability of code and reduce overall test development costs.
 */
public class TestingFramework {

    /**
     * The logger object used by log4j to record application logs.
     */
    private static Logger log = LogManager.getLogger(TestingFramework.class.getName());

    /**
     * Method to compare a response body to a local file and validate if both are same.
     * @param response The response sent by the server. The body of this response needs to be validated against a local file.
     * @param filename The name of the file which contains the expected response body for validation.
     * @return true or false depending on whether the response body is same as contents of the file or not.
     */
    public static boolean validateResponseBodyWithLocalFile(Response response, String filename) {
        log.info("Extracting the file contents to a String");

        //a combination of BufferedReader with StringBuilder is used to extract the file contents.
        //this method has shown high performance in terms of memory management and speed, as compared to other methods.
        Path path = Paths.get(Constants.RESOURCE_DIR + filename);
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = Files.newBufferedReader(path, Constants.ENCODING_UTF8)){
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String fileString = sb.toString();
        log.debug("File data: " + fileString);
        String responseString = ResponseProcessor.getBodyString(response);
        log.debug("Extracted response body: " + responseString);
        boolean flag = responseString.equals(fileString);
        log.debug("Response compare flag: " + flag);
        return flag;
    }

    /**
     * Method to check if a particular header is present in a response sent by the server.
     * @param response The response sent by the server.
     * @param headerName The name of the particular header that needs to be searched in the response.
     * @return true or false depending on whether the response contains the header or not.
     */
    public static boolean checkHeaderPresent(Response response, String headerName) {
        boolean flag = false;
        log.info("Extracting all headers from response");
        Headers headerList = response.headers();
        log.debug("Extracted headers: " + headerList);
        for(Header header : headerList){
            if(header.getName().equals(headerName)){
                flag = true;
                break;
            }
        }
        return flag;
    }

    /**
     * Method to validate the value of a header in a response to an expected value.
     * @param response The response sent by the server.
     * @param headerName The name of the particular header, the value of which needs to be validated.
     * @param headerValue The expected value of the header.
     * @return true or false depending on whether the header value is the same as expected value or not.
     */
    public static boolean validateResponseHeader(Response response, String headerName, String headerValue) {
        boolean flag = false;
        log.info("Checking if required header is present in the response");
        if(checkHeaderPresent(response, headerName)){
            log.debug("Header: " + headerName + " present. Comparing values");
            String responseValue = response.header(headerName);
            log.debug("Header: " + headerName + " Response value: " + responseValue);
            if(responseValue.equals(headerValue)){
                flag = true;
            }
        }
        log.debug("Header validation flag: " + flag);
        return flag;
    }
}
