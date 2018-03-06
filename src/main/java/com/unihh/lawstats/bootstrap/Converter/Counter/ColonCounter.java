package com.unihh.lawstats.bootstrap.Converter.Counter;

import com.unihh.lawstats.bootstrap.NaturalLanguageProcessing.NLPLawUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashSet;
import java.util.Set;

public class ColonCounter {


    public void countColon(int numberOfDocuments, int startIndex, int endIndex, String sourceBasePath){

        Set<String> pathList = new HashSet<>();
        int colonCounter = 0;
        int twoColonCounter = 0;


        NLPLawUtils watsonLawUtilities = new NLPLawUtils();

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




}
