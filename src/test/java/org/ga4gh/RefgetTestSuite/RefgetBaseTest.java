package org.ga4gh.RefgetTestSuite;

import org.ga4gh.ComplianceFramework.Constants;
import org.ga4gh.RefgetUtilities.RefgetSession;
import org.testng.ITestContext;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

import java.io.FileWriter;
import java.io.IOException;

public class RefgetBaseTest {

    @BeforeTest
    public void setup(){
        RefgetSession.setupEnvironment("http://refget.herokuapp.com");
    }

    @AfterTest
    public void dumpJSON(ITestContext context) throws IOException {
        RefgetSession.testObject.put("total_tests", context.getAllTestMethods().length);
        RefgetSession.testObject.put("total_passed_tests", context.getPassedTests().size());
        RefgetSession.testObject.put("total_failed_tests", context.getFailedTests().size());
        RefgetSession.testObject.put("total_skipped_tests", context.getSkippedTests().size());
        FileWriter file = new FileWriter(Constants.RESOURCE_DIR + "tests.json");
        file.write(RefgetSession.testObject.toJSONString());
        file.flush();
    }
}
