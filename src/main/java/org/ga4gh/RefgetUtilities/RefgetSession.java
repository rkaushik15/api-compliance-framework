package org.ga4gh.RefgetUtilities;

import io.restassured.path.json.JsonPath;
import org.ga4gh.ComplianceFramework.Constants;
import org.ga4gh.ComplianceFramework.Server;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RefgetSession {

    private static Server refgetServer;

    public static JSONObject testObject = new JSONObject();

    public static JSONArray resultsArray = new JSONArray();

    public static void setupEnvironment(String server){
        refgetServer = new Server(server);
        String serverName = System.getProperty("name");
        if(serverName == null) {
            serverName = "Refget Server";
        }
        refgetServer.setServerName(serverName);

        JsonPath serviceInfoResponse = RefgetUtilities.getServiceInfoResponse(refgetServer).jsonPath();

        Map<String, Object> properties = new HashMap<>();
        properties.put(Constants.REFGET_PROPERTY_CIRCULAR_SUPPORTED, serviceInfoResponse.get(Constants.REFGET_PROPERTY_SERVICE+ "." + Constants.REFGET_PROPERTY_CIRCULAR_SUPPORTED));
        properties.put(Constants.REFGET_PROPERTY_ALGORITHMS, serviceInfoResponse.get(Constants.REFGET_PROPERTY_SERVICE+ "." + Constants.REFGET_PROPERTY_ALGORITHMS));
        properties.put(Constants.REFGET_PROPERTY_SUBSEQUENCE_LIMIT, serviceInfoResponse.get(Constants.REFGET_PROPERTY_SERVICE+ "." + Constants.REFGET_PROPERTY_SUBSEQUENCE_LIMIT));
        properties.put(Constants.REFGET_PROPERTY_SUPPORTED_API_VERSION, serviceInfoResponse.get(Constants.REFGET_PROPERTY_SERVICE+ "." + Constants.REFGET_PROPERTY_SUPPORTED_API_VERSION));

        refgetServer.setServerProperties(properties);

        testObject.put("server_name", refgetServer.getServerName());
        testObject.put("base_url", refgetServer.getBaseUrl());
        testObject.put("date_time", java.time.LocalDateTime.now().toString());
        testObject.put("test_results", new JSONObject());
        testObject.put("summary", new JSONObject());
    }

    public static Server getRefgetServer() {
        return refgetServer;
    }
}
