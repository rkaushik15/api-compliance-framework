package org.ga4gh.RefgetTestSuite;

import io.restassured.response.Response;
import org.apache.commons.lang3.tuple.Triple;
import org.ga4gh.ComplianceFramework.*;
import org.ga4gh.RefgetUtilities.RefgetUtilities;
import org.ga4gh.RefgetUtilities.Sequence;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    private Server refgetServer = new Server("http://refget.herokuapp.com");

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
    public void getValidSequenceSha512() throws IOException, ParseException {
        Sequence validSeq = RefgetUtilities.getValidSequenceObject();

        //firing request
        Response response = RefgetUtilities.getSequenceResponse(refgetServer, validSeq.getSha512());

        //testing
        Assert.assertTrue(ResponseProcessor.checkSuccess(response));
        Assert.assertEquals(ResponseProcessor.getBodyString(response), validSeq.getSequence());
    }

    @Test
    public void getValidCircularSequence() throws IOException, ParseException {
        Sequence validSeq = RefgetUtilities.getValidCircularSequenceObject();

        //firing request
        Response response = RefgetUtilities.getSequenceResponse(refgetServer, validSeq.getMd5(), 0, validSeq.getSequence().length());

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
    public void getValidSequenceWithStartAndEndParameterCases() throws IOException, ParseException {
        Sequence validSeq = RefgetUtilities.getValidSequenceObject();

        List<Triple<Integer, Integer, Integer>> testCases = new ArrayList<>();
        testCases.add(Triple.of(10, 10, 0));
        testCases.add(Triple.of(10, 20, 10));
        testCases.add(Triple.of(10, 11, 1));
        testCases.add(Triple.of(230208, (Integer)null, 10));
        testCases.add(Triple.of((Integer)null, 5, 5));
        testCases.add(Triple.of(230217, 230218, 1));
        testCases.add(Triple.of(0, (Integer)null, 230218));
        testCases.add(Triple.of((Integer)null, 230218, 230218));
        testCases.add(Triple.of(0, 230218, 230218));
        testCases.add(Triple.of(1, 230218, 230217));
        testCases.add(Triple.of(230217, (Integer)null, 1));
        testCases.add(Triple.of((Integer)null, 0, 0));

        for (Triple<Integer, Integer, Integer> testCase : testCases) {
            //firing requests
            Response response = RefgetUtilities.getSequenceResponse(refgetServer, validSeq.getMd5(), testCase.getLeft(), testCase.getMiddle());

            //testing
            Assert.assertTrue(ResponseProcessor.checkSuccess(response));
            Assert.assertTrue(TestingFramework.validateResponseHeader(response, "Content-Length", Integer.toString(testCase.getRight())));
            Assert.assertEquals(ResponseProcessor.getBodyString(response), validSeq.getSequence().substring(testCase.getLeft()==null?0:testCase.getLeft(), testCase.getMiddle()==null?230218:testCase.getMiddle()));
        }
    }

    @Test
    public void getValidSequenceWithHeaders() throws IOException, ParseException {
        Sequence validSeq = RefgetUtilities.getValidSequenceObject();

        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Accept", Constants.SEQUENCE_ACCEPT_HEADER);

        //firing request
        Response response = RefgetUtilities.getSequenceResponse(refgetServer, validSeq.getMd5(), headerMap);

        //testing
        Assert.assertTrue(ResponseProcessor.checkSuccess(response));
        Assert.assertEquals(ResponseProcessor.getBodyString(response), validSeq.getSequence());
    }

    @Test
    public void getValidSequenceWithInvalidHeaders() throws IOException, ParseException {
        Sequence validSeq = RefgetUtilities.getValidSequenceObject();

        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Accept", "invalid/accept_header");

        //firing request
        Response response = RefgetUtilities.getSequenceResponse(refgetServer, validSeq.getMd5(), headerMap);

        //testing
        Assert.assertFalse(ResponseProcessor.checkSuccess(response));
        Assert.assertEquals(ResponseProcessor.getStatusCode(response), 406);
    }

    @Test
    public void getValidSequenceWithRangeHeaders() throws IOException, ParseException {
        Sequence validSeq = RefgetUtilities.getValidSequenceObject();

        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Accept", Constants.SEQUENCE_ACCEPT_HEADER);
        headerMap.put("Range", "bytes=10-19");

        //firing request
        Response response = RefgetUtilities.getSequenceResponse(refgetServer, validSeq.getMd5(), headerMap);

        //testing
        Assert.assertEquals(ResponseProcessor.getStatusCode(response), 206);
        Assert.assertTrue(TestingFramework.validateResponseHeader(response, "Content-Length", Integer.toString(10)));
        Assert.assertEquals(ResponseProcessor.getBodyString(response), validSeq.getSequence().substring(10, 20));
    }

    @Test
    public void getValidSequenceWithRangeHeadersCases() throws IOException, ParseException {
        Sequence validSeq = RefgetUtilities.getValidSequenceObject();

        List<Triple<Integer, Integer, Integer>> testCases = new ArrayList<>();
        testCases.add(Triple.of(10, 19, 10));
        testCases.add(Triple.of(10, 230217, 230208));
        testCases.add(Triple.of(10, 999999, 230208));
        testCases.add(Triple.of(0, 230217, 230218));
        testCases.add(Triple.of(0, 999999, 230218));
        testCases.add(Triple.of(0, 0, 1));
        testCases.add(Triple.of(230217, 230217, 1));

        for (Triple<Integer, Integer, Integer> testCase : testCases) {
            Map<String, String> headerMap = new HashMap<>();
            headerMap.put("Accept", Constants.SEQUENCE_ACCEPT_HEADER);
            headerMap.put("Range", "bytes="+testCase.getLeft()+"-"+testCase.getMiddle());

            //firing request
            Response response = RefgetUtilities.getSequenceResponse(refgetServer, validSeq.getMd5(), headerMap);

            //testing
            Assert.assertEquals(ResponseProcessor.getStatusCode(response), 206);
            Assert.assertTrue(TestingFramework.validateResponseHeader(response, "Content-Length", Integer.toString(testCase.getRight())));
            Assert.assertEquals(ResponseProcessor.getBodyString(response), validSeq.getSequence().substring(testCase.getLeft(), testCase.getMiddle()>230217?230218:(testCase.getMiddle()+1)));
        }
    }

    @Test
    public void getValidCircularSequenceCases() throws IOException, ParseException {
        Sequence validSeq = RefgetUtilities.getValidCircularSequenceObject();

        List<Triple<Integer, Integer, Integer>> testCases = new ArrayList<>();
        testCases.add(Triple.of(5374, 5, 17));
        testCases.add(Triple.of(5374, 0, 12));
        testCases.add(Triple.of(5380, 25, 31));

        for (Triple<Integer, Integer, Integer> testCase : testCases) {
            //firing request
            Response response = RefgetUtilities.getSequenceResponse(refgetServer, validSeq.getMd5(), testCase.getLeft(), testCase.getMiddle());

            //testing
            Assert.assertTrue(ResponseProcessor.checkSuccess(response));
            Assert.assertEquals((Integer)ResponseProcessor.getBodyString(response).length(), testCase.getRight());
        }
    }

    @Test
    public void getInvalidSequence() {

        //firing request
        Response response = RefgetUtilities.getSequenceResponse(refgetServer, "invalid_seq");

        //testing
        Assert.assertFalse(ResponseProcessor.checkSuccess(response));
        Assert.assertEquals(ResponseProcessor.getStatusCode(response), 404);
    }
}
