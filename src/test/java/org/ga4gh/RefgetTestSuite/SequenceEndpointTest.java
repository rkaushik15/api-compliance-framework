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
    private Server refgetServer = new Server("http://localhost:5000");;

    @Test
    public void getValidSequence() throws IOException, ParseException {
        Sequence validSeq = RefgetUtilities.getValidSequenceObject();

        //firing request
        Response response = RefgetUtilities.getSequenceResponse(refgetServer, validSeq.getMd5());

        //testing
        Assert.assertTrue(ResponseProcessor.checkSuccess(response));
        Assert.assertEquals(ResponseProcessor.getBodyString(response), validSeq.getSequence());
    }

    @Test
    public void getValidSequenceWithStartParameter() throws IOException, ParseException {
        Sequence validSeq = RefgetUtilities.getValidSequenceObject();

        //firing request
        Response response = RefgetUtilities.getSequenceResponse(refgetServer, validSeq.getMd5(), 0, null);

        //testing
        Assert.assertTrue(ResponseProcessor.checkSuccess(response));
        Assert.assertEquals(ResponseProcessor.getBodyString(response), validSeq.getSequence());
    }

    @Test
    public void getValidSequenceWithStartAndEndParameter() throws IOException, ParseException {
        Sequence validSeq = RefgetUtilities.getValidSequenceObject();

        //firing request
        Response response = RefgetUtilities.getSequenceResponse(refgetServer, validSeq.getMd5(), 0, validSeq.getSequence().length());

        //testing
        Assert.assertTrue(ResponseProcessor.checkSuccess(response));
        Assert.assertEquals(ResponseProcessor.getBodyString(response), validSeq.getSequence());
    }

    @Test
    public void getInvalidSequence() throws IOException, ParseException {

        //firing request
        Response response = RefgetUtilities.getSequenceResponse(refgetServer, "invalid_seq");

        //testing
        Assert.assertFalse(ResponseProcessor.checkSuccess(response));
        Assert.assertEquals(ResponseProcessor.getStatusCode(response), 404);
    }
}
