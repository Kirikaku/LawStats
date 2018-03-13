package com.unihh.lawstats.bootstrap.NaturalLanguageProcessing.ABSClassifier;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.*;

@Slf4j
public class TrainingDataManager {

    TrainingDataUtils _trainingsDataUtils;

    public TrainingDataManager(){
        _trainingsDataUtils = new TrainingDataUtils();
    }




    public void createTrainingsDataBaseFiles() {
        TrainingsDataManagerHelper trainingsDataManagerHelper = new TrainingsDataManagerHelper();
        List<String[]> allDecisionSentences = new ArrayList<>();
        List<String[]> revisionsErfolgList = new ArrayList<>();
        List<String[]> revisionsTeilErfolgList = new ArrayList<>();
        List<String[]> revisionsMisserfolgList = new ArrayList<>();
        List<String[]> revisionsIrrelevantList = new ArrayList<>();
        String jsonAnnotation = null;


        File folder = new File("C:\\Users\\Phillip\\Documents\\Studium\\Praktikum Sprachtechnologie\\TU Classifier\\corpus-61937530-1d90-11e8-8cb7-7100b456226f\\gt"); //TODO properties
        File[] allFiles = folder.listFiles();



        allDecisionSentences.addAll(trainingsDataManagerHelper.retrieveImportantSentencesFromAnnotations(allFiles));


        for (String[] sentenceEntryArray : allDecisionSentences) {

            if (sentenceEntryArray[0].length() >= 26) {
                switch (sentenceEntryArray[1].toLowerCase()) {

                    case "irrelevant":
                        revisionsIrrelevantList.add(sentenceEntryArray);
                        break;
                    case "revisionsteilerfolg":
                        revisionsTeilErfolgList.add(sentenceEntryArray);
                        break;
                    case "revisionserfolg":
                        revisionsErfolgList.add(sentenceEntryArray);
                        break;
                    case "revisionsmisserfolg":
                        revisionsMisserfolgList.add(sentenceEntryArray);
                        break;
                }
            }
        }


        String basePath = "C:\\Users\\Phillip\\Documents\\Studium\\Praktikum Sprachtechnologie\\TU Classifier\\TrainingsData2\\"; //TODO properties
        String erfolgOutfile = "erfolg.tsv";
        String teilErfolgOutfile = "teilerfolg.tsv";
        String misserfolgOutfile = "misserfolg.tsv";
        String irrelevantOutfile = "irrelevant.tsv";


        _trainingsDataUtils.writeRowsForBaseFile(revisionsErfolgList, basePath + erfolgOutfile);
        _trainingsDataUtils.writeRowsForBaseFile(revisionsTeilErfolgList, basePath + teilErfolgOutfile);
        _trainingsDataUtils.writeRowsForBaseFile(revisionsMisserfolgList, basePath + misserfolgOutfile);
        _trainingsDataUtils.writeRowsForBaseFile(revisionsIrrelevantList, basePath + irrelevantOutfile);

    }







    public void createTrainingDataFromBaseFiles(String basePathParameter) {
        List<String> revisionsErfolgRowList;
        List<String> revisionsTeilErfolgRowList;
        List<String> revisionsMisserfolgRowList;
        List<String> revisionsIrrelevantRowList;
        List<String[]> revisionsErfolgValuesList;
        List<String[]> revisionsTeilErfolgValuesList;
        List<String[]> revisionsMisserfolgValuesList;
        List<String[]> revisionsIrrelevantValuesList;

        String basePath = basePathParameter;
        String erfolgFilename = "erfolg.tsv"; //TODO properties
        String teilErfolgFilename = "teilerfolg.tsv";
        String misserfolgFilename = "misserfolg.tsv";
        String irrelevantFilename = "irrelevant.tsv";




        revisionsErfolgRowList = _trainingsDataUtils.getAllRowsFromDocument(basePath + erfolgFilename);
        revisionsTeilErfolgRowList = _trainingsDataUtils.getAllRowsFromDocument(basePath + teilErfolgFilename);
        revisionsMisserfolgRowList = _trainingsDataUtils.getAllRowsFromDocument(basePath + misserfolgFilename);
        revisionsIrrelevantRowList = _trainingsDataUtils.getAllRowsFromDocument(basePath + irrelevantFilename);


        revisionsErfolgValuesList = _trainingsDataUtils.getValuesFromRows(revisionsErfolgRowList);
        revisionsTeilErfolgValuesList = _trainingsDataUtils.getValuesFromRows(revisionsTeilErfolgRowList);
        revisionsMisserfolgValuesList = _trainingsDataUtils.getValuesFromRows(revisionsMisserfolgRowList);
        revisionsIrrelevantValuesList = _trainingsDataUtils.getValuesFromRows(revisionsIrrelevantRowList);


        writeTrainingFileForValueLists(basePath, revisionsErfolgValuesList, revisionsTeilErfolgValuesList, revisionsMisserfolgValuesList, revisionsIrrelevantValuesList);

    }




    private void writeTrainingFileForValueLists(String basePath, List<String[]> erfolgValueList, List<String[]> teilerfolgValueList, List<String[]> misserfolgValueList, List<String[]> irrelevantValueList) {
        Set<String[]> trainingDataValues = new HashSet<>();
        int limit = erfolgValueList.size()*2;



        trainingDataValues.addAll(erfolgValueList);
        trainingDataValues.addAll(misserfolgValueList);
        trainingDataValues.addAll(teilerfolgValueList);
        trainingDataValues = _trainingsDataUtils.addValuesToSet(irrelevantValueList, trainingDataValues, limit);


        _trainingsDataUtils.writeRowsForABSData(trainingDataValues, basePath);

    }






}

