package org.ga4gh.ComplianceFramework;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.testng.Assert;

/**
 * Class used to provide interface to some common assertions. Assertions are used to pass or fail test cases.
 */
public class AssertionFramework {

    /**
     * The logger object used by log4j to record application logs.
     */
    private static Logger log = LogManager.getLogger(AssertionFramework.class.getName());

    /**
     * Method to validate if the argument passed is true. An AssertionError is thrown if the argument is not true.
     * @param flag The parameter which is to be checked if true or not.
     */
    public static void validateTrue(boolean flag){
        log.debug("Asserting flag: " + flag);
        Assert.assertTrue(flag);
    }

    /**
     * Method to validate if the argument passed is false. An AssertionError is thrown if the argument is not true.
     * @param flag The parameter which is to be checked if false or not.
     */
    public static void validateFalse(boolean flag){
        log.debug("Asserting flag: " + flag);
        Assert.assertFalse(flag);
    }

    /**
     * Method to validate if the actual value is equal to expected value, i.e., both the integer values are equal. An AssertionError is thrown if the values are not equal.
     * @param actual The actual value to be checked.
     * @param expected The expected value.
     */
    public static void validateStatusCode(int actual, int expected){
        log.debug("Asserting response codes: " + actual + ", " + expected);
        Assert.assertEquals(actual, expected);
    }

}
