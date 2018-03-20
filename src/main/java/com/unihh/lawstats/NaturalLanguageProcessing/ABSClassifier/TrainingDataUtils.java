package com.unihh.lawstats.NaturalLanguageProcessing.ABSClassifier;

import com.unihh.lawstats.PropertyManager;
import org.apache.commons.io.IOUtils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TrainingDataUtils {



    public Set<String[]> addValuesToSet(List<String[]> values, Set<String[]> valueSet, int limit) {
        int counter = 0;


        for (String[] value : values) {

            if (counter < limit) {
                counter++;
                valueSet.add(value);
            } else {
                break;
            }
        }

        return valueSet;
    }


    public List<String[]> getValuesFromRows(List<String> rowList) {
        List<String[]> rowValueList = new ArrayList<>();


        for (String row : rowList) {
            String[] rowValues;
            rowValues = row.split("\\t");
            rowValueList.add(rowValues);
        }

        return rowValueList;
    }



    public List<String> getAllRowsFromDocument(String filePath) {
        FileInputStream fileInputStream = null;
        List<String> _allRows = null;


        try {
            fileInputStream = new FileInputStream(filePath);
            _allRows = IOUtils.readLines(fileInputStream, "UTF-8");
        } catch (IOException iOE) {
            iOE.printStackTrace();
        }

        return _allRows;
    }



    public void writeRowsForBaseFile(List<String[]> sentenceEntryList, String fileName) {

        for (String[] sentenceEntryArray : sentenceEntryList) {

            try {
                FileOutputStream fos = new FileOutputStream(fileName, true);

                String row = sentenceEntryArray[0] + "\t" + sentenceEntryArray[1] + "\t" + sentenceEntryArray[2] + "\n";
                IOUtils.write(row, fos, "UTF-8");

                fos.close();

            } catch (IOException e) {
                e.printStackTrace();

            }

        }
    }


    public void writeRowsForABSData(Set<String[]> valueSet, String basePath) {

        double trainingsDataRatio = Double.valueOf(PropertyManager.getLawProperty(PropertyManager.RELATION_TRAINING_TO_TESTDATA));

        long trainingsDataAmount = Math.round(valueSet.size()*trainingsDataRatio);

        //only exists in order to make the required ID unique
        int ongoingIndex = 0;


        for (String[] valueArray : valueSet) {
            ongoingIndex++;

            if(trainingsDataAmount > 0) {
                trainingsDataAmount--;
                writeOneRow(valueArray, basePath+"trainingsData.tsv", ongoingIndex);
            }
            else{
                writeOneRowTestData(valueArray, basePath+"testData.tsv", ongoingIndex);
            }
        }
    }


    private void writeOneRow(String[] valueArray, String filename, int ongoingIndex){
        if (valueArray.length == 3) {
            try {
                FileOutputStream fos = new FileOutputStream(filename, true);

                String row = valueArray[2] + "\t" + valueArray[0] + "\t" + valueArray[1] + "\n";
                IOUtils.write(row, fos, "UTF-8");


            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    }


    private void writeOneRowTestData(String[] valueArray, String filename, int ongoingIndex){
        if (valueArray.length == 3) {
            try {
                FileOutputStream fos = new FileOutputStream(filename, true);

                String row = valueArray[2]+ongoingIndex + "\t" + valueArray[0] + "\t" + valueArray[1] +"\t"+valueArray[1]+"\t"+valueArray[1]+":"+valueArray[1]+ "\n";
                IOUtils.write(row, fos, "UTF-8");


            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    }


}
