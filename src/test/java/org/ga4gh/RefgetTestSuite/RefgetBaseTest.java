package org.ga4gh.RefgetTestSuite;

import org.ga4gh.ComplianceFramework.Server;
import org.ga4gh.RefgetUtilities.RefgetSession;
import org.testng.annotations.BeforeTest;

public class RefgetBaseTest {

    @BeforeTest
    public void setup(){
        Server refgetServer = new Server("http://refget.herokuapp.com");
        RefgetSession.setRefgetServer(refgetServer);
    }
}
