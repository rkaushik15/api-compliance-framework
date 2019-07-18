package org.ga4gh.RefgetUtilities;

import org.ga4gh.ComplianceFramework.Server;

public class RefgetSession {

    private static Server refgetServer;


    public static Server getRefgetServer() {
        return refgetServer;
    }

    public static void setRefgetServer(Server refgetServer) {
        RefgetSession.refgetServer = refgetServer;
    }
}
