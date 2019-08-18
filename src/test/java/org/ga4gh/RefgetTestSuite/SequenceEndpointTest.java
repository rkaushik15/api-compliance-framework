package org.ga4gh.RefgetTestSuite;

import io.restassured.response.Response;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.ga4gh.ComplianceFramework.*;
import org.ga4gh.RefgetUtilities.RefgetSession;
import org.ga4gh.RefgetUtilities.RefgetUtilities;
import org.ga4gh.RefgetUtilities.Sequence;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.*;

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
    private JSONArray classArray = new JSONArray();
    private JSONObject tempTestObject = new JSONObject();
    private JSONArray tempQueryArray = new JSONArray();


    @BeforeClass
    public void setRefgetServer(ITestContext context){
        refgetServer = RefgetSession.getRefgetServer();
        context.setAttribute("sequence_result", 1);
    }

    @AfterClass
    public void setOverview(ITestContext context){
        JSONObject summaryObject = new JSONObject();
        summaryObject.put("name", "Sequence");
        summaryObject.put("result", context.getAttribute("sequence_result"));

        ((JSONObject) RefgetSession.testObject.get("summary")).put("sequence", summaryObject);
        ((JSONObject) RefgetSession.testObject.get("test_results")).put("sequence", classArray);
    }

    @BeforeMethod
    public void clear(){
        tempTestObject = new JSONObject();
        tempQueryArray = new JSONArray();
    }

    @AfterMethod
    public void setResults(ITestResult result, ITestContext context){
        tempTestObject.put("test_name", result.getName());
        tempTestObject.put("parent", this.getClass().getSimpleName());
        tempTestObject.put("api_query_info", tempQueryArray);
        if(result.getStatus() == ITestResult.SUCCESS)
            tempTestObject.put("result", 1);
        else if(result.getStatus() == ITestResult.SKIP)
            tempTestObject.put("result", 0);
        else if(result.getStatus() == ITestResult.FAILURE) {
            tempTestObject.put("result", -1);
            context.setAttribute("sequence_result", 0);
        }
        classArray.add(tempTestObject);
    }

    @Test(priority=-1)
    public void getValidSequenceTest() throws IOException, ParseException {
        Sequence validSeq = RefgetUtilities.getValidSequenceObject("I");
        Sequence validCircSeq = RefgetUtilities.getValidCircularSequenceObject();

        Assert.assertEquals(validSeq.getName(), "I");
        Assert.assertEquals(validCircSeq.getName(), "NC");

        Assert.assertFalse(validSeq.isCircular());
        Assert.assertTrue(validCircSeq.isCircular());
    }

    @Test
    public void getSequence() throws IOException, ParseException {
        Sequence validSeq = RefgetUtilities.getValidSequenceObject("I");

        //firing request
        Response response = RefgetUtilities.getSequenceResponse(refgetServer, validSeq.getMd5());
        tempQueryArray.add("Response: " + response.getBody().asString());

        //testing
        Assert.assertTrue(TestingFramework.checkSuccess(response));
        Assert.assertEquals(TestingFramework.getBodyString(response).length(), (long) validSeq.getSize());
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
        tempQueryArray.add("Response: " + response.getBody().asString());

        //testing
        Assert.assertTrue(TestingFramework.checkSuccess(response));
        Assert.assertEquals(TestingFramework.getBodyString(response), validSeq.getSequence());
    }

    @Test
    public void getSequenceWithStartParameter() throws IOException, ParseException {
        Sequence validSeq = RefgetUtilities.getValidSequenceObject("I");

        //firing request
        Response response = RefgetUtilities.getSequenceResponse(refgetServer, validSeq.getMd5(), 0, null);
        tempQueryArray.add("Response: " + response.getBody().asString());

        //testing
        Assert.assertTrue(TestingFramework.checkSuccess(response));
        Assert.assertEquals(TestingFramework.getBodyString(response), validSeq.getSequence());
    }

    @Test
    public void getSequenceWithStartAndEndParameter() throws IOException, ParseException {
        Sequence validSeq = RefgetUtilities.getValidSequenceObject("I");

        //firing request
        Response response = RefgetUtilities.getSequenceResponse(refgetServer, validSeq.getMd5(), 0, validSeq.getSequence().length());
        tempQueryArray.add("Response: " + response.getBody().asString());

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
            tempQueryArray.add("Response (" + testCase.toString() + "): " + response.getBody().asString());

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
            tempQueryArray.add("Response (" + testCase + "): " + response.getBody().asString());

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
            tempQueryArray.add("Response (" + testCase.toString() + "): " + response.getBody().asString());

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
        tempQueryArray.add("Response: " + response.getBody().asString());

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
        tempQueryArray.add("Response: " + response.getBody().asString());

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
        tempQueryArray.add("Response: " + response.getBody().asString());

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
            tempQueryArray.add("Response (" + testCase.toString() + "): " + response.getBody().asString());

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
            tempQueryArray.add("Response (" + testCase + "): " + response.getBody().asString());

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
            tempQueryArray.add("Response (" + testCase.toString() + "): " + response.getBody().asString());

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
        tempQueryArray.add("Response: " + response.getBody().asString());

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
            tempQueryArray.add("Response (" + testCase.toString() + "): " + response.getBody().asString());

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
        tempQueryArray.add("Response: " + response.getBody().asString());

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
        tempQueryArray.add("Response: " + response.getBody().asString());

        //testing
        Assert.assertEquals(TestingFramework.getStatusCode(response), 501);

        //firing request
        response = RefgetUtilities.getSequenceResponse(refgetServer, validCircSeq.getMd5(), 20, 4);
        tempQueryArray.add("Response: " + response.getBody().asString());

        //testing
        Assert.assertEquals(TestingFramework.getStatusCode(response), 501);
    }

    @Test
    public void getInvalidSequence() {

        //firing request
        Response response = RefgetUtilities.getSequenceResponse(refgetServer, "invalid_seq");
        tempQueryArray.add("Response: " + response.getBody().asString());

        //testing
        Assert.assertFalse(TestingFramework.checkSuccess(response));
        Assert.assertEquals(TestingFramework.getStatusCode(response), 404);
    }
}
