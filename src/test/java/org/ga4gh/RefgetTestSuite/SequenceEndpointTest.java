package org.ga4gh.RefgetTestSuite;

import io.restassured.response.Response;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.ga4gh.ComplianceFramework.*;
import org.ga4gh.RefgetUtilities.RefgetSession;
import org.ga4gh.RefgetUtilities.RefgetUtilities;
import org.ga4gh.RefgetUtilities.Sequence;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
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
     * The server instance.
     */
    private Server refgetServer;

    @BeforeClass
    public void setRefgetServer(){
        refgetServer = RefgetSession.getRefgetServer();
    }

    @Test
    public void getSequence() throws IOException, ParseException {
        Sequence validSeq = RefgetUtilities.getValidSequenceObject("I");

        //firing request
        Response response = RefgetUtilities.getSequenceResponse(refgetServer, validSeq.getMd5());

        //testing
        Assert.assertTrue(TestingFramework.checkSuccess(response));
        Assert.assertEquals(TestingFramework.getBodyString(response), validSeq.getSequence());
    }

    @Test
    public void getSequenceSha512() throws IOException, ParseException {
        ArrayList algorithms = (ArrayList) refgetServer.getServerProperties().get(Constants.REFGET_PROPERTY_ALGORITHMS);
        if(!algorithms.contains("trunc512"))
            throw new SkipException("Test skipped as server does not support the algorithm");

        Sequence validSeq = RefgetUtilities.getValidSequenceObject("I");

        //firing request
        Response response = RefgetUtilities.getSequenceResponse(refgetServer, validSeq.getSha512());

        //testing
        Assert.assertTrue(TestingFramework.checkSuccess(response));
        Assert.assertEquals(TestingFramework.getBodyString(response), validSeq.getSequence());
    }

    @Test
    public void getSequenceWithStartParameter() throws IOException, ParseException {
        Sequence validSeq = RefgetUtilities.getValidSequenceObject("I");

        //firing request
        Response response = RefgetUtilities.getSequenceResponse(refgetServer, validSeq.getMd5(), 0, null);

        //testing
        Assert.assertTrue(TestingFramework.checkSuccess(response));
        Assert.assertEquals(TestingFramework.getBodyString(response), validSeq.getSequence());
    }

    @Test
    public void getSequenceWithStartAndEndParameter() throws IOException, ParseException {
        Sequence validSeq = RefgetUtilities.getValidSequenceObject("I");

        //firing request
        Response response = RefgetUtilities.getSequenceResponse(refgetServer, validSeq.getMd5(), 0, validSeq.getSequence().length());

        //testing
        Assert.assertTrue(TestingFramework.checkSuccess(response));
        Assert.assertEquals(TestingFramework.getBodyString(response), validSeq.getSequence());
    }

    @Test
    public void getSequenceWithStartAndEndParameterSuccessCases() throws IOException, ParseException {
        Sequence validSeq = RefgetUtilities.getValidSequenceObject("I");

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
            Assert.assertTrue(TestingFramework.checkSuccess(response));
            Assert.assertTrue(TestingFramework.validateResponseHeader(response, "Content-Length", Integer.toString(testCase.getRight())));
            Assert.assertEquals(TestingFramework.getBodyString(response), validSeq.getSequence().substring(testCase.getLeft()==null?0:testCase.getLeft(), testCase.getMiddle()==null?230218:testCase.getMiddle()));
        }
    }

    @Test
    public void getSequenceWithInvalidStartAndEndParameterErrorCases() throws IOException, ParseException {
        Sequence validSeq = RefgetUtilities.getValidSequenceObject("I");

        List<String> testCases = new ArrayList<>();
        testCases.add("?start=abc&end=20");
        testCases.add("?start=-10&end=-29");
        testCases.add("?start=abc");


        for (String testCase : testCases) {
            //firing requests
            String id = validSeq.getMd5();
            Response response = RefgetUtilities.getSequenceResponse(refgetServer, id + testCase);

            //testing
            Assert.assertEquals(TestingFramework.getStatusCode(response), 400);
        }
    }

    @Test
    public void getSequenceWithOutOfBoundStartAndEndParameterErrorCases() throws IOException, ParseException {
        Sequence validSeq = RefgetUtilities.getValidSequenceObject("NC");

        List<Pair<Integer, Integer>> testCases = new ArrayList<>();
        testCases.add(Pair.of(67, 5387));
        testCases.add(Pair.of(5386, 5375));
        testCases.add(Pair.of(5386, 5386));
        testCases.add(Pair.of(5386, 5));

        for (Pair<Integer, Integer> testCase : testCases) {
            //firing requests
            Response response = RefgetUtilities.getSequenceResponse(refgetServer, validSeq.getMd5(), testCase.getLeft(), testCase.getRight());

            //testing
            Assert.assertEquals(TestingFramework.getStatusCode(response), 416);
        }
    }

    @Test
    public void getSequenceWithHeaders() throws IOException, ParseException {
        Sequence validSeq = RefgetUtilities.getValidSequenceObject("I");

        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Accept", Constants.SEQUENCE_ACCEPT_HEADER);

        //firing request
        Response response = RefgetUtilities.getSequenceResponse(refgetServer, validSeq.getMd5(), headerMap);

        //testing
        Assert.assertTrue(TestingFramework.checkSuccess(response));
        Assert.assertEquals(TestingFramework.getBodyString(response), validSeq.getSequence());
    }

    @Test
    public void getSequenceWithInvalidHeaders() throws IOException, ParseException {
        Sequence validSeq = RefgetUtilities.getValidSequenceObject("I");

        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Accept", "invalid/accept_header");

        //firing request
        Response response = RefgetUtilities.getSequenceResponse(refgetServer, validSeq.getMd5(), headerMap);

        //testing
        Assert.assertFalse(TestingFramework.checkSuccess(response));
        Assert.assertEquals(TestingFramework.getStatusCode(response), 406);
    }

    @Test
    public void getSequenceWithRangeHeaders() throws IOException, ParseException {
        Sequence validSeq = RefgetUtilities.getValidSequenceObject("I");

        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Accept", Constants.SEQUENCE_ACCEPT_HEADER);
        headerMap.put("Range", "bytes=10-19");

        //firing request
        Response response = RefgetUtilities.getSequenceResponse(refgetServer, validSeq.getMd5(), headerMap);

        //testing
        Assert.assertEquals(TestingFramework.getStatusCode(response), 206);
        Assert.assertTrue(TestingFramework.validateResponseHeader(response, "Content-Length", Integer.toString(10)));
        Assert.assertEquals(TestingFramework.getBodyString(response), validSeq.getSequence().substring(10, 20));
    }

    @Test
    public void getSequenceWithRangeHeadersSuccessCases() throws IOException, ParseException {
        Sequence validSeq = RefgetUtilities.getValidSequenceObject("I");

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
            Assert.assertEquals(TestingFramework.getStatusCode(response), 206);
            Assert.assertTrue(TestingFramework.validateResponseHeader(response, "Content-Length", Integer.toString(testCase.getRight())));
            Assert.assertEquals(TestingFramework.getBodyString(response), validSeq.getSequence().substring(testCase.getLeft(), testCase.getMiddle()>230217?230218:(testCase.getMiddle()+1)));
        }
    }

    @Test
    public void getSequenceWithInvalidRangeHeadersErrorCases() throws IOException, ParseException {
        Sequence validSeq = RefgetUtilities.getValidSequenceObject("I");

        List<String> testCases = new ArrayList<>();
        testCases.add("units=20-30");
        testCases.add("bytes=ab-19");
        testCases.add("bytes=-10--19");
        testCases.add("bytes=10--19");
        testCases.add("bytes=-10-");
        testCases.add("bytes==10-19");


        for (String testCase : testCases) {
            Map<String, String> headerMap = new HashMap<>();
            headerMap.put("Accept", Constants.SEQUENCE_ACCEPT_HEADER);
            headerMap.put("Range", testCase);

            //firing request
            Response response = RefgetUtilities.getSequenceResponse(refgetServer, validSeq.getMd5(), headerMap);

            //testing
            Assert.assertEquals(TestingFramework.getStatusCode(response), 400);
        }
    }

    @Test
    public void getSequenceWithOutOfBoundRangeHeadersErrorCases() throws IOException, ParseException {
        Sequence validSeq = RefgetUtilities.getValidSequenceObject("NC");

        List<Pair<Integer, Integer>> testCases = new ArrayList<>();
        testCases.add(Pair.of(5200, 19));
        testCases.add(Pair.of(59, 50));
        testCases.add(Pair.of(5385, 5382));
        testCases.add(Pair.of(5387, 5391));
        testCases.add(Pair.of(5386, 5387));
        testCases.add(Pair.of(9999, 99999));

        for (Pair<Integer, Integer> testCase : testCases) {
            Map<String, String> headerMap = new HashMap<>();
            headerMap.put("Accept", Constants.SEQUENCE_ACCEPT_HEADER);
            headerMap.put("Range", "bytes=" + testCase.getLeft() + "-" + testCase.getRight());

            //firing request
            Response response = RefgetUtilities.getSequenceResponse(refgetServer, validSeq.getMd5(), headerMap);

            //testing
            Assert.assertEquals(TestingFramework.getStatusCode(response), 416);
        }
    }

    @Test
    public void getCircularSequence() throws IOException, ParseException {
        boolean circular = (boolean) refgetServer.getServerProperties().get(Constants.REFGET_PROPERTY_CIRCULAR_SUPPORTED);
        if(!circular)
            throw new SkipException("Test skipped as server does not support circular sequences");

        Sequence validSeq = RefgetUtilities.getValidCircularSequenceObject();

        //firing request
        Response response = RefgetUtilities.getSequenceResponse(refgetServer, validSeq.getMd5(), 0, validSeq.getSequence().length());

        //testing
        Assert.assertTrue(TestingFramework.checkSuccess(response));
        Assert.assertEquals(TestingFramework.getBodyString(response), validSeq.getSequence());
    }

    @Test
    public void getCircularSequenceSuccessCases() throws IOException, ParseException {
        boolean circular = (boolean) refgetServer.getServerProperties().get(Constants.REFGET_PROPERTY_CIRCULAR_SUPPORTED);
        if(!circular)
            throw new SkipException("Test skipped as server does not support circular sequences");

        Sequence validSeq = RefgetUtilities.getValidCircularSequenceObject();

        List<Triple<Integer, Integer, Integer>> testCases = new ArrayList<>();
        testCases.add(Triple.of(5374, 5, 17));
        testCases.add(Triple.of(5374, 0, 12));
        testCases.add(Triple.of(5380, 25, 31));

        for (Triple<Integer, Integer, Integer> testCase : testCases) {
            //firing request
            Response response = RefgetUtilities.getSequenceResponse(refgetServer, validSeq.getMd5(), testCase.getLeft(), testCase.getMiddle());

            //testing
            Assert.assertTrue(TestingFramework.checkSuccess(response));
            Assert.assertEquals((Integer)TestingFramework.getBodyString(response).length(), testCase.getRight());
        }
    }

    @Test
    public void getCircularSequenceWithCircularSupportErrorCases() throws IOException, ParseException {
        boolean circular = (boolean) refgetServer.getServerProperties().get(Constants.REFGET_PROPERTY_CIRCULAR_SUPPORTED);
        if(!circular)
            throw new SkipException("Test skipped as server does not support circular sequences");

        Sequence validSeq = RefgetUtilities.getValidSequenceObject("I");

        //firing request
        Response response = RefgetUtilities.getSequenceResponse(refgetServer, validSeq.getMd5(), 220218, 671);

        //testing
        Assert.assertEquals(TestingFramework.getStatusCode(response), 416);
    }

    @Test
    public void getCircularSequenceWithoutCircularSupportErrorCases() throws IOException, ParseException {
        boolean circular = (boolean) refgetServer.getServerProperties().get(Constants.REFGET_PROPERTY_CIRCULAR_SUPPORTED);
        if(circular)
            throw new SkipException("Test skipped as server supports circular sequences");

        Sequence validSeq = RefgetUtilities.getValidSequenceObject("I");
        Sequence validCircSeq = RefgetUtilities.getValidCircularSequenceObject();

        //firing request
        Response response = RefgetUtilities.getSequenceResponse(refgetServer, validSeq.getMd5(), 220218, 671);

        //testing
        Assert.assertEquals(TestingFramework.getStatusCode(response), 501);

        //firing request
        response = RefgetUtilities.getSequenceResponse(refgetServer, validCircSeq.getMd5(), 20, 4);

        //testing
        Assert.assertEquals(TestingFramework.getStatusCode(response), 501);
    }

    @Test
    public void getInvalidSequence() {

        //firing request
        Response response = RefgetUtilities.getSequenceResponse(refgetServer, "invalid_seq");

        //testing
        Assert.assertFalse(TestingFramework.checkSuccess(response));
        Assert.assertEquals(TestingFramework.getStatusCode(response), 404);
    }
}
