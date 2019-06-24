package org.ga4gh.ComplianceFramework;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Class containing some utility functions used by other classes.
 */
public class Utilities {

    /**
     * Method to ectract the contents of a file to a String object.
     * @param filename The name of the file to be read. The file must be present in the 'resources' directory.
     * @return The contents of the file in the form off a String.
     */
    public static String readFileToString(String filename){
        //a combination of BufferedReader with StringBuilder is used to extract the file contents.
        //this method has shown high performance in terms of memory management and speed, as compared to other methods.
        Path path = Paths.get(Constants.RESOURCE_DIR + filename);
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = Files.newBufferedReader(path, Constants.ENCODING_UTF8)){
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

}
