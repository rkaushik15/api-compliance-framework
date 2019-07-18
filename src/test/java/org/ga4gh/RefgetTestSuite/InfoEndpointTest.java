package org.ga4gh.RefgetTestSuite;

import io.restassured.response.Response;
import org.ga4gh.ComplianceFramework.Constants;
import org.ga4gh.ComplianceFramework.Server;
import org.ga4gh.ComplianceFramework.TestingFramework;
import org.ga4gh.RefgetUtilities.RefgetSession;
import org.ga4gh.RefgetUtilities.RefgetUtilities;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class InfoEndpointTest {

    /**
     * The server instance.
     */
    private Server refgetServer;

    @BeforeClass
    public void setRefgetServer(){
        refgetServer = RefgetSession.getRefgetServer();
    }

    @Test
    public void getServiceInfo(){
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Accept", Constants.INFO_ACCEPT_HEADER);

        //firing request
        Response response = RefgetUtilities.getServiceInfoResponse(refgetServer, headerMap);

        //testing
        Assert.assertTrue(TestingFramework.checkSuccess(response));
    }

    @Test
    public void getServiceInfoWithoutAcceptHeader(){
        //firing request
        Response response = RefgetUtilities.getServiceInfoResponse(refgetServer);

        //testing
        Assert.assertTrue(TestingFramework.checkSuccess(response));
    }

    @Test
    public void getServiceInfoCircular(){
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Accept", Constants.INFO_ACCEPT_HEADER);

        //firing request
        Response response = RefgetUtilities.getServiceInfoResponse(refgetServer, headerMap);

        HashMap serviceJson = response.jsonPath().get("service");
        Boolean flag = serviceJson.containsKey("circular_supported");

        //testing
        Assert.assertTrue(TestingFramework.checkSuccess(response));
        Assert.assertTrue(flag);
    }

    @Test
    public void getServiceInfoAlgorithms(){
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Accept", Constants.INFO_ACCEPT_HEADER);

        //firing request
        Response response = RefgetUtilities.getServiceInfoResponse(refgetServer, headerMap);

        HashMap serviceJson = response.jsonPath().get("service");
        Boolean flag = serviceJson.containsKey("algorithms");

        //testing
        Assert.assertTrue(TestingFramework.checkSuccess(response));
        Assert.assertTrue(flag);
    }

    @Test
    public void getServiceInfoSubsequenceLimit(){
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Accept", Constants.INFO_ACCEPT_HEADER);

        //firing request
        Response response = RefgetUtilities.getServiceInfoResponse(refgetServer, headerMap);

        HashMap serviceJson = response.jsonPath().get("service");
        Boolean flag = serviceJson.containsKey("subsequence_limit");

        //testing
        Assert.assertTrue(TestingFramework.checkSuccess(response));
        Assert.assertTrue(flag);
    }

    @Test
    public void getServiceInfoSupportedApi(){
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Accept", Constants.INFO_ACCEPT_HEADER);

        //firing request
        Response response = RefgetUtilities.getServiceInfoResponse(refgetServer, headerMap);

        HashMap serviceJson = response.jsonPath().get("service");
        Boolean flag = serviceJson.containsKey("supported_api_versions");

        //testing
        Assert.assertTrue(TestingFramework.checkSuccess(response));
        Assert.assertTrue(flag);
    }

    @Test
    public void getServiceInfoWithInvalidHeader(){
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Accept", "dummy_header");

        //firing request
        Response response = RefgetUtilities.getServiceInfoResponse(refgetServer, headerMap);

        //testing
        Assert.assertEquals(TestingFramework.getStatusCode(response), 406);
    }
}
