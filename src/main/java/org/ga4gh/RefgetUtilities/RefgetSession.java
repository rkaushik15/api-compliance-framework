package org.ga4gh.RefgetUtilities;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.ga4gh.ComplianceFramework.Constants;
import org.ga4gh.ComplianceFramework.Server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RefgetSession {

    private static Server refgetServer;

    public static void setupEnvironment(String server){
        refgetServer = new Server(server);
        JsonPath serviceInfoResponse = RefgetUtilities.getServiceInfoResponse(refgetServer).jsonPath();

        Map<String, Object> properties = new HashMap<>();
        properties.put(Constants.REFGET_PROPERTY_CIRCULAR_SUPPORTED, serviceInfoResponse.get(Constants.REFGET_PROPERTY_SERVICE+ "." + Constants.REFGET_PROPERTY_CIRCULAR_SUPPORTED));
        properties.put(Constants.REFGET_PROPERTY_ALGORITHMS, serviceInfoResponse.get(Constants.REFGET_PROPERTY_SERVICE+ "." + Constants.REFGET_PROPERTY_ALGORITHMS));
        properties.put(Constants.REFGET_PROPERTY_SUBSEQUENCE_LIMIT, serviceInfoResponse.get(Constants.REFGET_PROPERTY_SERVICE+ "." + Constants.REFGET_PROPERTY_SUBSEQUENCE_LIMIT));
        properties.put(Constants.REFGET_PROPERTY_SUPPORTED_API_VERSION, serviceInfoResponse.get(Constants.REFGET_PROPERTY_SERVICE+ "." + Constants.REFGET_PROPERTY_SUPPORTED_API_VERSION));

        refgetServer.setServerProperties(properties);
    }

    public static Server getRefgetServer() {
        return refgetServer;
    }
}
