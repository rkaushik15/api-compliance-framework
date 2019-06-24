package org.ga4gh.RefgetTestSuite;

import io.restassured.response.Response;
import org.ga4gh.ComplianceFramework.RequestsRestAssured;
import org.ga4gh.ComplianceFramework.ResponseProcessor;
import org.ga4gh.ComplianceFramework.Server;
import org.ga4gh.RefgetUtilities.RefgetUtilities;
import org.ga4gh.RefgetUtilities.Sequence;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * Class containing all tests for the refget /sequence endpoint.
 */
public class SequenceEndpointTest {

    /**
     * Request object to be used to fire API requests.
     */
    private RequestsRestAssured request = new RequestsRestAssured();

    /**
     * The server instance.
     */
    private Server refgetServer;

    @Test
    public void getValidSequence() throws IOException, ParseException {
        Sequence validSeq = RefgetUtilities.getValidSequenceObject();
        refgetServer = new Server("http://localhost:5000");
        String finalEndpoint = refgetServer.getEndpoint("/sequence/" + validSeq.getMd5());

        //firing request
        Response response = request.GET(finalEndpoint);

        //testing
        Assert.assertTrue(ResponseProcessor.checkSuccess(response));
        Assert.assertEquals(ResponseProcessor.getBodyString(response), validSeq.getSequence());
    }

    @Test
    public void getValidSequenceWithStartParameter() throws IOException, ParseException {
        Sequence validSeq = RefgetUtilities.getValidSequenceObject();
        refgetServer = new Server("http://localhost:5000");
        String finalEndpoint = refgetServer.getEndpoint("/sequence/" + validSeq.getMd5());
        Map<String, String> parameterMap = new HashMap<>();
        parameterMap.put("start", "0");

        //firing request
        Response response = request.GETWithQueryParams(finalEndpoint, parameterMap);

        //testing
        Assert.assertTrue(ResponseProcessor.checkSuccess(response));
        Assert.assertEquals(ResponseProcessor.getBodyString(response), validSeq.getSequence());
    }

    @Test
    public void getValidSequenceWithStartAndEndParameter() throws IOException, ParseException {
        Sequence validSeq = RefgetUtilities.getValidSequenceObject();
        refgetServer = new Server("http://localhost:5000");
        String finalEndpoint = refgetServer.getEndpoint("/sequence/" + validSeq.getMd5());
        Map<String, String> parameterMap = new HashMap<>();
        parameterMap.put("start", "0");
        parameterMap.put("end", Integer.toString(validSeq.getSequence().length()));

        //firing request
        Response response = request.GETWithQueryParams(finalEndpoint, parameterMap);

        //testing
        Assert.assertTrue(ResponseProcessor.checkSuccess(response));
        Assert.assertEquals(ResponseProcessor.getBodyString(response), validSeq.getSequence());
    }

    @Test
    public void getInvalidSequence() throws IOException, ParseException {
        refgetServer = new Server("http://localhost:5000");
        String finalEndpoint = refgetServer.getEndpoint("/sequence/" + "invalid_id");

        //firing request
        Response response = request.GET(finalEndpoint);

        //testing
        Assert.assertFalse(ResponseProcessor.checkSuccess(response));
        Assert.assertEquals(ResponseProcessor.getStatusCode(response), 404);
    }
}
