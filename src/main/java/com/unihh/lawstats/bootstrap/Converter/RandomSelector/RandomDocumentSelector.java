package com.unihh.lawstats.bootstrap.Converter.RandomSelector;

import com.unihh.lawstats.bootstrap.Converter.Formatting.Formatter;
import com.unihh.lawstats.bootstrap.Watson.WatsonLawUtilities;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.*;

public class RandomDocumentSelector {

    public void selectRandomDocuments(int numberOfDocuments, int startIndex, int endIndex, String sourceBasePath, String targetBasePath) {

        Set<String> pathList = new HashSet<>();

        for (int i = startIndex; i <= endIndex; i++) {
            pathList.add("\\verdict" + i + ".txt");
        }

        int counter = 0;

        for (String path : pathList) {

            if (counter < numberOfDocuments) {

                File file = new File(sourceBasePath + path);
                try {
                    FileInputStream fileInputStream = new FileInputStream(file);
                    String content = IOUtils.toString(fileInputStream, "UTF-8");
                    content = Formatter.formatTextForString(content);

                    //This only works if the ending is .txt or 3 characters
                    String cleanPath = path.substring(0, path.length() - 4);
                    cleanPath = cleanPath + "_cleanNeu.txt";


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