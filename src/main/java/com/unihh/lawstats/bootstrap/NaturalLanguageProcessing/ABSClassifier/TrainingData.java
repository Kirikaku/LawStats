package com.unihh.lawstats.bootstrap.NaturalLanguageProcessing.ABSClassifier;

import com.unihh.lawstats.bootstrap.Converter.Formatting.Formatter;
import com.unihh.lawstats.bootstrap.NaturalLanguageProcessing.Watson.NLU.LawEntityExtractor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class TrainingData {


    public void createTrainingsData() {
        LawEntityExtractor lawEntityExtractor = new LawEntityExtractor();
        String jsonAnnotation = null;
        File folder = new File("C:\\Users\\Phillip\\Documents\\Studium\\Praktikum Sprachtechnologie\\TU Classifier\\corpus-61937530-1d90-11e8-8cb7-7100b456226f\\gt");
        File[] allFiles = folder.listFiles();
        Map<String, String[]> _allDecisionSentences = new HashMap<>();
        List<Map.Entry<String, String[]>> _revisionsErfolgList = new ArrayList<>();
        List<Map.Entry<String, String[]>> _revisionsTeilErfolgList = new ArrayList<>();
        List<Map.Entry<String, String[]>> _revisionsMisserfolgList = new ArrayList<>();
        List<Map.Entry<String, String[]>> _revisionsIrrelevantList = new ArrayList<>();


        for (int i = 0; i < allFiles.length; i++) {

            File file = allFiles[i];

            try {
                FileInputStream fis = new FileInputStream(file);
                jsonAnnotation = IOUtils.toString(fis, "UTF-8");

            } catch (Exception e) {
                e.printStackTrace();
            }


            _allDecisionSentences.putAll(lawEntityExtractor.findDecisionSentences(jsonAnnotation));
        }


        for (Map.Entry<String, String[]> sentenceEntry : _allDecisionSentences.entrySet()) {
            String[] valueArray = sentenceEntry.getValue();
            String key = sentenceEntry.getKey();
            if (key.length() < 12) {

            } else {
                switch (valueArray[0].toLowerCase()) {

                    case "irrelevant":
                        _revisionsIrrelevantList.add(sentenceEntry);
                        break;
                    case "revisionsteilerfolg":
                        _revisionsTeilErfolgList.add(sentenceEntry);
                        break;
                    case "revisionserfolg":
                        _revisionsErfolgList.add(sentenceEntry);
                        break;
                    case "revisionsmisserfolg":
                        _revisionsMisserfolgList.add(sentenceEntry);
                        break;
                }
            }
        }


        _allDecisionSentences.forEach((s, strings) -> {

            System.out.println("Key: " + s + "\n Label: " + strings[0] + "\n Dateiname:" + strings[1]);
        });


        String basePath = "C:\\Users\\Phillip\\Documents\\Studium\\Praktikum Sprachtechnologie\\TU Classifier\\TrainingsData\\";
        String erfolgOutfile = "erfolg.tsv";
        String teilErfolgOutfile = "teilerfolg.tsv";
        String misserfolgOutfile = "misserfolg.tsv";
        String irrelevantOutfile = "irrelevant.tsv";


        writeRows(_revisionsErfolgList, basePath + erfolgOutfile);
        writeRows(_revisionsTeilErfolgList, basePath + teilErfolgOutfile);
        writeRows(_revisionsMisserfolgList, basePath + misserfolgOutfile);
        writeRows(_revisionsIrrelevantList, basePath + irrelevantOutfile);

    }


    public void createTrainingDataFromBaseFiles(String basePathParameter) {
        List<String> revisionsErfolgRowList = new ArrayList<>();
        List<String> revisionsTeilErfolgRowList = new ArrayList<>();
        List<String> revisionsMisserfolgRowList = new ArrayList<>();
        List<String> revisionsIrrelevantRowList = new ArrayList<>();
        List<String[]> revisionsErfolgValuesList = new ArrayList<>();
        List<String[]> revisionsTeilErfolgValuesList = new ArrayList<>();
        List<String[]> revisionsMisserfolgValuesList = new ArrayList<>();
        List<String[]> revisionsIrrelevantValuesList = new ArrayList<>();

        String basePath = basePathParameter;
        String erfolgFilename = "erfolgClean.tsv";
        String teilErfolgFilename = "teilerfolgClean.tsv";
        String misserfolgFilename = "misserfolgClean.tsv";
        String irrelevantFilename = "irrelevantClean.tsv";

        revisionsErfolgRowList = getAllRowsFromDocument(basePath + erfolgFilename);
        revisionsTeilErfolgRowList = getAllRowsFromDocument(basePath + teilErfolgFilename);
        revisionsMisserfolgRowList = getAllRowsFromDocument(basePath + misserfolgFilename);
        revisionsIrrelevantRowList = getAllRowsFromDocument(basePath + irrelevantFilename);


        revisionsErfolgValuesList = getValuesFromRows(revisionsErfolgRowList);
        revisionsTeilErfolgValuesList = getValuesFromRows(revisionsTeilErfolgRowList);
        revisionsMisserfolgValuesList = getValuesFromRows(revisionsMisserfolgRowList);
        revisionsIrrelevantValuesList = getValuesFromRows(revisionsIrrelevantRowList);


        writeTrainingFileForValueLists(basePath, revisionsErfolgValuesList, revisionsTeilErfolgValuesList, revisionsMisserfolgValuesList, revisionsIrrelevantValuesList);



    }


    private void writeTrainingFileForValueLists(String basePath, List<String[]> erfolgValueList, List<String[]> teilerfolgValueList, List<String[]> misserfolgValueList, List<String[]> irrelevantValueList) {
        Set<String[]> trainingDataValues = new HashSet<>();
        int limit = erfolgValueList.size();

        trainingDataValues.addAll(erfolgValueList);
        trainingDataValues = addValuesToSet(misserfolgValueList, trainingDataValues, limit);
        trainingDataValues = addValuesToSet(irrelevantValueList, trainingDataValues, limit);

        writeRowsForFinalData(trainingDataValues, basePath);

    }

    private Set<String[]> addValuesToSet(List<String[]> values, Set<String[]> valueSet, int limit) {
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

    private List<String[]> getValuesFromRows(List<String> rowList) {
        List<String[]> rowValueList = new ArrayList<>();


        for (String row : rowList) {
            String[] rowValues;
            rowValues = row.split("\\t");
            rowValueList.add(rowValues);
        }

        return rowValueList;
    }


    private List<String> getAllRowsFromDocument(String filePath) {
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


    private void writeRows(List<Map.Entry<String, String[]>> sentenceEntryList, String fileName) {
        for (Map.Entry<String, String[]> sentenceEntry : sentenceEntryList) {
            try {
                FileOutputStream fos = new FileOutputStream(fileName, true);

                String[] valueArray = sentenceEntry.getValue();


                String row = sentenceEntry.getKey() + "\t" + valueArray[0] + "\t" + valueArray[1] + "\n";
                IOUtils.write(row, fos, "UTF-8");

                fos.close();

            } catch (IOException e) {
                e.printStackTrace();

            }
        }
    }


    private void writeRowsForFinalData(Set<String[]> valueSet, String basePath) {
        long trainingsDataAmount = Math.round(valueSet.size()*0.8);
        long testDataAmount = valueSet.size()-trainingsDataAmount;
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

