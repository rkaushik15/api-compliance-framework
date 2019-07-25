package org.ga4gh.RefgetTestSuite;

import org.ga4gh.RefgetUtilities.RefgetSession;
import org.testng.annotations.BeforeTest;

public class RefgetBaseTest {

    @BeforeTest
    public void setup(){
        RefgetSession.setupEnvironment("http://refget.herokuapp.com");
    }
}
