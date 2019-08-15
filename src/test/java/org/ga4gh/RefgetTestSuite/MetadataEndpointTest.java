package org.ga4gh.RefgetTestSuite;

import io.restassured.response.Response;
import org.ga4gh.ComplianceFramework.Constants;
import org.ga4gh.ComplianceFramework.Server;
import org.ga4gh.ComplianceFramework.TestingFramework;
import org.ga4gh.RefgetUtilities.RefgetSession;
import org.ga4gh.RefgetUtilities.RefgetUtilities;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.util.HashMap;
import java.util.Map;

public class MetadataEndpointTest {

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
        context.setAttribute("metadata_result", 1);
    }

    @AfterClass
    public void setOverview(ITestContext context){
        JSONObject summaryObject = new JSONObject();
        summaryObject.put("name", "Metadata");
        summaryObject.put("result", context.getAttribute("metadata_result"));

        ((JSONObject) RefgetSession.testObject.get("summary")).put("metadata", summaryObject);
        ((JSONObject) RefgetSession.testObject.get("test_results")).put("metadata", classArray);
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
            context.setAttribute("metadata_result", 0);
        }
        classArray.add(tempTestObject);
    }

    @Test
    public void getSequenceMetadata() {
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Accept", Constants.METADATA_ACCEPT_HEADER);

        //firing request
        Response response = RefgetUtilities.getMetadataResponse(refgetServer, Constants.NON_CIRCULAR_SEQUENCE_MD5, headerMap);
        tempQueryArray.add("Response: " + response.getBody().asString());

        //testing
        Assert.assertTrue(TestingFramework.checkSuccess(response));
    }

    @Test
    public void getSequenceMetadataWithoutAcceptHeader() {
        //firing request
        Response response = RefgetUtilities.getMetadataResponse(refgetServer, Constants.NON_CIRCULAR_SEQUENCE_MD5);
        tempQueryArray.add("Response: " + response.getBody().asString());

        //testing
        Assert.assertTrue(TestingFramework.checkSuccess(response));
    }

    @Test
    public void getSequenceMetadataSha512() {
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Accept", Constants.METADATA_ACCEPT_HEADER);

        //firing request
        Response response = RefgetUtilities.getMetadataResponse(refgetServer, Constants.NON_CIRCULAR_SEQUENCE_SHA512, headerMap);
        tempQueryArray.add("Response: " + response.getBody().asString());

        //testing
        Assert.assertTrue(TestingFramework.checkSuccess(response));
    }

    @Test
    public void getCircularSequenceMetadata() {
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Accept", Constants.METADATA_ACCEPT_HEADER);

        //firing request
        Response response = RefgetUtilities.getMetadataResponse(refgetServer, Constants.CIRCULAR_SEQUENCE_MD5, headerMap);
        tempQueryArray.add("Response: " + response.getBody().asString());

        //testing
        Assert.assertTrue(TestingFramework.checkSuccess(response));
    }

    @Test
    public void getMetadataMd5(){
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Accept", Constants.METADATA_ACCEPT_HEADER);

        //firing request
        Response response = RefgetUtilities.getMetadataResponse(refgetServer, Constants.NON_CIRCULAR_SEQUENCE_MD5, headerMap);
        tempQueryArray.add("Response: " + response.getBody().asString());

        HashMap serviceJson = response.jsonPath().get("metadata");
        boolean flag = serviceJson.containsKey("md5");

        //testing
        Assert.assertTrue(TestingFramework.checkSuccess(response));
        Assert.assertTrue(flag);
    }

    @Test
    public void getMetadataTrunc512() {
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Accept", Constants.METADATA_ACCEPT_HEADER);

        //firing request
        Response response = RefgetUtilities.getMetadataResponse(refgetServer, Constants.NON_CIRCULAR_SEQUENCE_MD5, headerMap);
        tempQueryArray.add("Response: " + response.getBody().asString());

        HashMap serviceJson = response.jsonPath().get("metadata");
        boolean flag = serviceJson.containsKey("trunc512");

        //testing
        Assert.assertTrue(TestingFramework.checkSuccess(response));
        Assert.assertTrue(flag);
    }

    @Test
    public void getMetadataLength() {
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Accept", Constants.METADATA_ACCEPT_HEADER);

        //firing request
        Response response = RefgetUtilities.getMetadataResponse(refgetServer, Constants.NON_CIRCULAR_SEQUENCE_MD5, headerMap);
        tempQueryArray.add("Response: " + response.getBody().asString());

        HashMap serviceJson = response.jsonPath().get("metadata");
        boolean flag = serviceJson.containsKey("length");

        //testing
        Assert.assertTrue(TestingFramework.checkSuccess(response));
        Assert.assertTrue(flag);
    }

    @Test
    public void getMetadataAliases() {
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Accept", Constants.METADATA_ACCEPT_HEADER);

        //firing request
        Response response = RefgetUtilities.getMetadataResponse(refgetServer, Constants.NON_CIRCULAR_SEQUENCE_MD5, headerMap);
        tempQueryArray.add("Response: " + response.getBody().asString());

        HashMap serviceJson = response.jsonPath().get("metadata");
        boolean flag = serviceJson.containsKey("aliases");

        //testing
        Assert.assertTrue(TestingFramework.checkSuccess(response));
        Assert.assertTrue(flag);
    }

    @Test
    public void getInvalidSequenceMetadata(){
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Accept", Constants.METADATA_ACCEPT_HEADER);

        //firing request
        Response response = RefgetUtilities.getMetadataResponse(refgetServer, "dummy_id", headerMap);
        tempQueryArray.add("Response: " + response.getBody().asString());

        //testing
        Assert.assertEquals(TestingFramework.getStatusCode(response), 404);
    }

    @Test
    public void getMetadataWithInvalidHeader(){
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Accept", "dummy_header");

        //firing request
        Response response = RefgetUtilities.getMetadataResponse(refgetServer, Constants.NON_CIRCULAR_SEQUENCE_MD5, headerMap);
        tempQueryArray.add("Response: " + response.getBody().asString());

        //testing
        Assert.assertEquals(TestingFramework.getStatusCode(response), 406);
    }

    @Test
    public void getMetadataResponseCheck(){
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Accept", Constants.METADATA_ACCEPT_HEADER);

        //firing request
        Response response = RefgetUtilities.getMetadataResponse(refgetServer, Constants.NON_CIRCULAR_SEQUENCE_MD5, headerMap);
        tempQueryArray.add("Response: " + response.getBody().asString());

        //testing
        Assert.assertTrue(TestingFramework.checkSuccess(response));
        Assert.assertTrue(TestingFramework.checkHeaderPresent(response,"Content-Type"));
        Assert.assertTrue(TestingFramework.validateResponseHeader(response, "Content-Type", Constants.METADATA_RESPONSE_CONTENT_TYPE_HEADER));
    }
}
