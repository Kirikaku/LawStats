package com.unihh.lawstats.bootstrap.Converter;

import com.unihh.lawstats.bootstrap.Watson.WatsonLawUtilities;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.*;

public class RandomDocumentSelector {



    public void countColon(int numberOfDocuments, String sourceBasePath){
        int startIndex = 70000;
        int endIndex = 80000;
        Set<String> pathList = new HashSet<>();
        int colonCounter = 0;
        int twoColonCounter = 0;


        WatsonLawUtilities watsonLawUtilities = new WatsonLawUtilities();

        for(int i = startIndex; i<=endIndex; i++){
            pathList.add("\\verdict"+i+"_clean.txt");
        }

        int counter = 0;

        for(String path: pathList){

            if(counter<numberOfDocuments) {

                File file = new File(sourceBasePath + path);
                try {
                    FileInputStream fileInputStream = new FileInputStream(file);
                    String content = IOUtils.toString(fileInputStream,"UTF-8");

                    String firstSentence = watsonLawUtilities.splitFirstSentence(content);

                    if(firstSentence.contains(":")){
                        colonCounter++;
                        firstSentence = firstSentence.replaceFirst(":", "_");

                        if(firstSentence.contains(":")){
                            twoColonCounter++;
                        }
                    }


                    counter++;




                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else{
                return;
            }

        }

        System.out.println("Ein Doppelpunkt:"+colonCounter);
        System.out.println("Zwei Doppelpunkt:"+twoColonCounter);
        System.out.println("Gesamt Anzahl:"+counter);

        double doubleCounter = (double) counter;

        double oneColonRelation = colonCounter/doubleCounter;
        double twoColonRelation = twoColonCounter/doubleCounter;

        System.out.println("Verhältnis von ein Doppelpunkt zu Gesamtanzahl:"+oneColonRelation);
        System.out.println("Verhältnis von zwei Doppelpunkt zu Gesamtanzahl:"+twoColonRelation);


    }



    public void selectRandomDocuments(int numberOfDocuments, String sourceBasePath, String targetBasePath){
        int startIndex = 17756;
        int endIndex = 80984;
        Set<String> pathList = new HashSet<>();

        for(int i = startIndex; i<=endIndex; i++){
            pathList.add("\\verdict"+i+".txt");
        }

        int counter = 0;

        for(String path: pathList){

            if(counter<numberOfDocuments) {

                File file = new File(sourceBasePath + path);
                try {
                    FileInputStream fileInputStream = new FileInputStream(file);
                    String content = IOUtils.toString(fileInputStream,"UTF-8");
                    content = Formatter.formatTextForString(content);


                    String cleanPath = path.substring(0,path.length()-4);
                    cleanPath = cleanPath+"_cleanNeu.txt";


                    FileOutputStream fos = new FileOutputStream(targetBasePath + cleanPath);
                    IOUtils.write(content, fos, "UTF-8");
                    counter++;

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else{
                return;
            }

        }


    }
}
