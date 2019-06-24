package org.ga4gh.RefgetUtilities;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.ga4gh.ComplianceFramework.Constants;

import org.ga4gh.ComplianceFramework.TestingFramework;
import org.ga4gh.ComplianceFramework.Utilities;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

/**
 * Class containing some utility functions used for testing refget API servers.
 */
public class RefgetUtilities {

    /**
     * The logger object used by log4j to record application logs.
     */
    private static Logger log = LogManager.getLogger(RefgetUtilities.class.getName());

    /**
     * Method to read the Checksum JSON file and return object of a particular sequence as a JSONObject. The object contains some metadata associated with the sequence.
     * @param ch The name of the sequence whose object is returned.
     * @return The JSONObject of the particular sequence. The object contains fields "md5", "sha512", "is_circular" and "size".
     * @throws IOException if there are errors while reading the file.
     * @throws ParseException if there are errors in converting the file to a JSONObject. Can occur if file does not follow proper JSON syntax.
     */
    public static JSONObject readChecksumsJSON(String ch) throws IOException, ParseException {
        Object obj = new JSONParser().parse(new FileReader(Constants.RESOURCE_DIR + "checksums.json"));
        JSONObject fileObj = (JSONObject) obj;
        return (JSONObject) fileObj.get(ch);
    }

    /**
     * Method to read the full sequence from a file and return as a String.
     * @param filename The name of the file to be read. The file must be present in the 'resources' directory.
     * @return The full sequence as a String.
     */
    public static String readSequence(String filename){
        String sequence = Utilities.readFileToString(filename);
        sequence = sequence.replaceAll("\\r\\n|\\r|\\n", "");
        return sequence;
    }

    /**
     * Method to return a Sequence object of a valid sequence.
     * @return Sequence object of a valid sequence.
     * @throws IOException if there are errors while reading the file.
     * @throws ParseException if there are errors in converting the file to a JSONObject. Can occur if file does not follow proper JSON syntax.
     */
    public static Sequence getValidSequenceObject() throws IOException, ParseException {
        Sequence seqObj = new Sequence();
        JSONObject seqChecksumObj = readChecksumsJSON("I");
        log.debug("Extracted JSONObject: " + seqChecksumObj);
        seqObj.setName("I");
        seqObj.setMd5((String)seqChecksumObj.get("md5"));
        seqObj.setSha512((String)seqChecksumObj.get("sha512"));
        seqObj.isCircular(((String)seqChecksumObj.get("is_circular")).equals("1"));
        seqObj.setSequence(readSequence("I.faa"));
        log.debug("Extracted sequence from file: " + seqObj.getSequence());
        seqObj.setSize(seqObj.getSequence().length());
        return seqObj;
    }
}
