package org.ga4gh.ComplianceFramework;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Class containing constants used in the framework, to avoid hardcoding in methods.
 */
public class Constants {
    public final static Charset ENCODING_UTF8 = StandardCharsets.UTF_8;
    public final static Charset ENCODING_UTF16 = StandardCharsets.UTF_16;
    public final static Charset ENCODING_US_ASCII = StandardCharsets.US_ASCII;

    public final static String RESOURCE_DIR = "./resources/";
}
