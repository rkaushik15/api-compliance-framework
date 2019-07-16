package org.ga4gh.RefgetTestSuite;

import io.restassured.response.Response;
import org.ga4gh.ComplianceFramework.Constants;
import org.ga4gh.ComplianceFramework.Server;
import org.ga4gh.ComplianceFramework.TestingFramework;
import org.ga4gh.RefgetUtilities.RefgetUtilities;
import org.ga4gh.RefgetUtilities.Sequence;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MetadataEndpointTest {

    /**
     * The server instance.
     */
    private Server refgetServer = new Server("http://refget.herokuapp.com");

    @Test
    public void getSequenceMetadata() {
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Accept", Constants.METADATA_ACCEPT_HEADER);

        //firing request
        Response response = RefgetUtilities.getMetadataResponse(refgetServer, Constants.NON_CIRCULAR_SEQUENCE_MD5, headerMap);

        //testing
        Assert.assertTrue(TestingFramework.checkSuccess(response));
    }

    @Test
    public void getSequenceMetadataWithoutAcceptHeader() {
        //firing request
        Response response = RefgetUtilities.getMetadataResponse(refgetServer, Constants.NON_CIRCULAR_SEQUENCE_MD5);

        //testing
        Assert.assertTrue(TestingFramework.checkSuccess(response));
    }

    @Test
    public void getSequenceMetadataSha512() {
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Accept", Constants.METADATA_ACCEPT_HEADER);

        //firing request
        Response response = RefgetUtilities.getMetadataResponse(refgetServer, Constants.NON_CIRCULAR_SEQUENCE_SHA512, headerMap);

        //testing
        Assert.assertTrue(TestingFramework.checkSuccess(response));
    }

    @Test
    public void getCircularSequenceMetadata() {
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Accept", Constants.METADATA_ACCEPT_HEADER);

        //firing request
        Response response = RefgetUtilities.getMetadataResponse(refgetServer, Constants.CIRCULAR_SEQUENCE_MD5, headerMap);

        //testing
        Assert.assertTrue(TestingFramework.checkSuccess(response));
    }

    @Test
    public void getMetadataMd5(){
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Accept", Constants.METADATA_ACCEPT_HEADER);

        //firing request
        Response response = RefgetUtilities.getMetadataResponse(refgetServer, Constants.NON_CIRCULAR_SEQUENCE_MD5, headerMap);

        HashMap serviceJson = response.jsonPath().get("metadata");
        Boolean flag = serviceJson.containsKey("md5");

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

        HashMap serviceJson = response.jsonPath().get("metadata");
        Boolean flag = serviceJson.containsKey("trunc512");

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

        HashMap serviceJson = response.jsonPath().get("metadata");
        Boolean flag = serviceJson.containsKey("length");

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

        HashMap serviceJson = response.jsonPath().get("metadata");
        Boolean flag = serviceJson.containsKey("aliases");

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

        //testing
        Assert.assertEquals(TestingFramework.getStatusCode(response), 404);
    }

    @Test
    public void getMetadataWithInvalidHeader(){
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Accept", "dummy_header");

        //firing request
        Response response = RefgetUtilities.getMetadataResponse(refgetServer, Constants.NON_CIRCULAR_SEQUENCE_MD5, headerMap);

        //testing
        Assert.assertEquals(TestingFramework.getStatusCode(response), 406);
    }

    @Test
    public void getMetadataResponseCheck(){
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Accept", Constants.METADATA_ACCEPT_HEADER);

        //firing request
        Response response = RefgetUtilities.getMetadataResponse(refgetServer, Constants.NON_CIRCULAR_SEQUENCE_MD5, headerMap);

        //testing
        Assert.assertTrue(TestingFramework.checkSuccess(response));
        Assert.assertTrue(TestingFramework.checkHeaderPresent(response,"Content-Type"));
        Assert.assertTrue(TestingFramework.validateResponseHeader(response, "Content-Type", Constants.METADATA_RESPONSE_CONTENT_TYPE_HEADER));
    }
}
