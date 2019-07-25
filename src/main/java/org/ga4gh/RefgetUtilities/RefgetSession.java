package org.ga4gh.RefgetUtilities;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
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
        properties.put("circular_supported", serviceInfoResponse.get("service.circular_supported"));
        properties.put("algorithms", serviceInfoResponse.get("service.algorithms"));
        properties.put("subsequence_limit", serviceInfoResponse.get("service.subsequence_limit"));
        properties.put("supported_api_versions", serviceInfoResponse.get("service.supported_api_versions"));

        refgetServer.setServerProperties(properties);
    }

    public static Server getRefgetServer() {
        return refgetServer;
    }
}
