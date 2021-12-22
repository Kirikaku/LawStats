package com.unihh.lawstats.naturalLawLanguageProcessing.ABSClassifier;

import com.unihh.lawstats.PropertyManager;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.*;

/**
 * @author Phillip
 *
 * Trainingsdata is handled the following way when used in Java.
 * One entry is represented by an Array of size 3.
 * array[0] = the sentence itself
 * array[1] = the label
 * array[2] = the entry ID
 *
 * A trainingsset is a list of these entry arrays.
 */
@Slf4j
public class TrainingDataManager {

    TrainingDataUtils _trainingsDataUtils;

    public TrainingDataManager(){
        _trainingsDataUtils = new TrainingDataUtils();
    }


    /**
     * Creates four Basefiles for the four labels "irrelevant","misserfolg", "erfolg", "teilerfolg" from Watson Knowledge Studio annotations.
     * Source and target folder is specified in the config file.
     */
    public void createTrainingsDataBaseFiles() {
        TrainingsDataManagerHelper trainingsDataManagerHelper = new TrainingsDataManagerHelper();
        List<String[]> allDecisionSentences = new ArrayList<>();
        List<String[]> revisionsErfolgList = new ArrayList<>();
        List<String[]> revisionsTeilErfolgList = new ArrayList<>();
        List<String[]> revisionsMisserfolgList = new ArrayList<>();
        List<String[]> revisionsIrrelevantList = new ArrayList<>();
        String jsonAnnotation = null;


        File folder = new File(PropertyManager.getLawProperty(PropertyManager.TRAINGSDATA_ANNOTATIONS));
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


        String basePath = PropertyManager.getLawProperty(PropertyManager.TRAININGSDATA_BASEFILEPATH);
        String erfolgOutfile = "erfolg.tsv";
        String teilErfolgOutfile = "teilerfolg.tsv";
        String misserfolgOutfile = "misserfolg.tsv";
        String irrelevantOutfile = "irrelevant.tsv";


        _trainingsDataUtils.writeRowsForBaseFile(revisionsErfolgList, basePath + erfolgOutfile);
        _trainingsDataUtils.writeRowsForBaseFile(revisionsTeilErfolgList, basePath + teilErfolgOutfile);
        _trainingsDataUtils.writeRowsForBaseFile(revisionsMisserfolgList, basePath + misserfolgOutfile);
        _trainingsDataUtils.writeRowsForBaseFile(revisionsIrrelevantList, basePath + irrelevantOutfile);

    }


    /**
     * Creates a trainingsdata- and a testdata file for training the AbSentiment model.
     * The data is retrieved from four basefiles (one for each label).
     * The source and target folder is specified in the config file.
     */
    public void createTrainingDataFromBaseFiles() {
        List<String> revisionsErfolgRowList;
        List<String> revisionsTeilErfolgRowList;
        List<String> revisionsMisserfolgRowList;
        List<String> revisionsIrrelevantRowList;
        List<String[]> revisionsErfolgValuesList;
        List<String[]> revisionsTeilErfolgValuesList;
        List<String[]> revisionsMisserfolgValuesList;
        List<String[]> revisionsIrrelevantValuesList;

        String basePath = PropertyManager.getLawProperty(PropertyManager.TRAININGSDATA_BASEFILEPATH);
        String erfolgFilename = "erfolg.tsv";
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


    /**
     * Helper method to create trainingsdata- and testdata file.
     * Handles the amount of sentences used from each label.
     *
     * @param basePath The path to the target folder
     * @param erfolgValueList A list of classified sentences indicating revision success.
     * @param teilerfolgValueList A list of classified sentences indicating partial revision success.
     * @param misserfolgValueList A list of classified sentences indicating no revision success.
     * @param irrelevantValueList A list of classified sentences not relevant to the revision outcome.
     */
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

