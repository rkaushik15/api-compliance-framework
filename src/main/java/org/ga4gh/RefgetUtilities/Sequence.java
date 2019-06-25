package org.ga4gh.RefgetUtilities;


import org.json.simple.JSONObject;

/**
 * Class that holds information about a particular sequence.
 */
public class Sequence {

    /**
     * The name of the sequence.
     */
    private String name;

    /**
     * The entire sequence as String.
     */
    private String sequence;

    /**
     * Whether the sequence is circular or not.
     */
    private boolean is_circular;

    /**
     * The MD5 hash value of the sequence.
     */
    private String md5;

    /**
     * The SHA512 hash value of the sequence.
     */
    private String sha512;

    /**
     * The length of the entire sequence.
     */
    private int size;

    public Sequence(JSONObject jsonObj) {
        this(jsonObj, jsonObj.get("name") + ".faa");
    }

    public Sequence(JSONObject jsonObj, String sequenceFileName) {
        this.name = (String) jsonObj.get("name");
        this.md5 = (String) jsonObj.get("md5");
        this.sha512 = (String) jsonObj.get("sha512");
        this.is_circular = jsonObj.get("circular").equals("1");
        this.size = (int) jsonObj.get("size");
        this.sequence = RefgetUtilities.readSequence(sequenceFileName);
    }

    /**
     * Getter method to retrieve the name of the sequence.
     * @return the name of the sequence.
     */
    public String getName() {
        return name;
    }

    /**
     * Getter method to retrieve the entire sequence.
     * @return the entire sequence as String.
     **/
    public String getSequence() {
        return sequence;
    }

    /**
     * Getter method to retrieve the value of is_circular.
     * @return true or false depending on whether the sequence is circular or not.
     */
    public boolean isCircular() {
        return is_circular;
    }

    /**
     * Getter method to retrieve the MD5 hash value of the sequence.
     * @return the MD5 hash value of the sequence as String.
     */
    public String getMd5() {
        return md5;
    }

    /**
     * Getter method to retrieve the SHA512 hash value of the sequence.
     * @return the SHA512 hash value of the sequence as String.
     */
    public String getSha512() {
        return sha512;
    }

    /**
     * Getter method to retrieve the length of the sequence.
     * @return the length of the sequence.
     */
    public int getSize() {
        return size;
    }
}
