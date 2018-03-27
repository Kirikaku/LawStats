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


    /**
     * Adds a certain amount of entries to a given set.
     * @param values A List of trainingsdata entries. (Or any list of String arrays)
     * @param valueSet The set the entries shall be added to.
     * @param limit The number of entries that shall be added to the set.
     * @return The set the entries were added to,
     */
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


    /**
     * Converts a trainingsdata row into an array representing the trainingsdata entry.
     * Columns int the row are seperated with a tab.
     * @param rowList List of trainingsdata rows (List of Strings)
     * @return a List of trainingsdata entry arrays
     */
    public List<String[]> getValuesFromRows(List<String> rowList) {
        List<String[]> rowValueList = new ArrayList<>();


        for (String row : rowList) {
            String[] rowValues;
            rowValues = row.split("\\t");
            rowValueList.add(rowValues);
        }

        return rowValueList;
    }


    /**
     * Reads all rows from a trainingsdata file (or any other file).
     * @param filePath The path to the file.
     * @return A List of Strings where one String is one row of the file
     */
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


    /**
     * Writes a list of trainingsdata entry arrays to a basefile.
     * One trainingsdata entry array will be one row in the file.
     * The values within one row will be seperated by a tab (\t).
     * @param sentenceEntryList A List of trainingsdata entry arrays
     * @param filePath The path to the file the rows shall be written to.
     */
    public void writeRowsForBaseFile(List<String[]> sentenceEntryList, String filePath) {

        for (String[] sentenceEntryArray : sentenceEntryList) {

            try {
                FileOutputStream fos = new FileOutputStream(filePath, true);

                String row = sentenceEntryArray[0] + "\t" + sentenceEntryArray[1] + "\t" + sentenceEntryArray[2] + "\n";
                IOUtils.write(row, fos, "UTF-8");

                fos.close();

            } catch (IOException e) {
                e.printStackTrace();

            }

        }
    }

    /**
     * Writes a set of trainingsdata entry arrays to a file in the correct form for AbSentiment.
     * The trainingsdata set provided will be splitted into trainings- and testdata.
     * The ratio between trainings- and testdata can be specified in the config file.
     * Both are written in the form required by AbSentiment.
     * Testdata file will have additional columns to enable convenient model evaluation.
     * One trainingsdata entry array will be one row in a file.
     * The values within one row will be seperated by a tab (\t).
     * The trainingsdata will have the form: [ID] <TAB> [Sentence] <TAB> [Label]
     * @param valueSet A set of trainingsdata entry arrays.
     * @param basePath The folder where the file should be stored at.
     */
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


    /**
     * Writes one row to a AbSentiment trainingsdata file.
     * @param valueArray A trainingsdata entry array
     * @param filename The name of the file the row shall be written to.
     * @param ongoingIndex Number to be able to create a unique ID.
     */
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

    /**
     * Writes one row to an AbSentiment testdata file.
     * @param valueArray A trainingsdata entry array
     * @param filename The name of the file the row shall be written to.
     * @param ongoingIndex Number to be able to create a unique ID.
     */
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
