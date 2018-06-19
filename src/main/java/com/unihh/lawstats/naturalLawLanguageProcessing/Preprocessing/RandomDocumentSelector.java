package com.unihh.lawstats.NaturalLawLanguageProcessing.Preprocessing;

import com.unihh.lawstats.PropertyManager;
import com.unihh.lawstats.NaturalLawLanguageProcessing.Preprocessing.Formatting.Formatter;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.*;

/**
 * @author Phillip
 */
public class RandomDocumentSelector {

    /**
     * Selects the specified number of documents randomly from the given set of documents.
     * @param numberOfDocuments Number of documents to be selected.
     * @param startIndex Specifies the first verdict that can be selected. Works only if the verdict naming convention is uphold.
     * @param endIndex Specifies the last verdict that can be selected. Works only if the verdict naming convention is uphold.
     */
    public void selectRandomDocuments(int numberOfDocuments, int startIndex, int endIndex) {

        Set<String> pathList = new HashSet<>();
        String sourceBasePath = PropertyManager.getLawProperty(PropertyManager.RANDOMDOCUMENT_SOURCEBASEPATH);
        String targetBasePath = PropertyManager.getLawProperty(PropertyManager.RANDOMDOCUMENT_TARGETBASEPATH);


        for (int i = startIndex; i <= endIndex; i++) {
            pathList.add("\\verdict" + i + ".txt");
        }

        int counter = 0;

        for (String path : pathList) {

            if (counter < numberOfDocuments) {


                try {
                    FileInputStream fileInputStream = new FileInputStream(new File(sourceBasePath + path));
                    String content = IOUtils.toString(fileInputStream, "UTF-8");
                    content = Formatter.formatTextForString(content);

                    //This only works if the ending is .txt or 3 characters
                    String cleanPath = path.substring(0, path.length() - 4);
                    cleanPath = cleanPath + "_clean.txt";


                    FileOutputStream fos = new FileOutputStream(targetBasePath + cleanPath);
                    IOUtils.write(content, fos, "UTF-8");
                    counter++;

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else {
                return;
            }
        }
    }
}
