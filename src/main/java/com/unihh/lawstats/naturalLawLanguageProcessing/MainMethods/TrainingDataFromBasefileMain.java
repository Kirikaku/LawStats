package com.unihh.lawstats.naturalLawLanguageProcessing.MainMethods;

import com.unihh.lawstats.naturalLawLanguageProcessing.ABSClassifier.TrainingDataManager;

/**
 * @author Phillip
 */
public class TrainingDataFromBasefileMain {

    /**
     * Creates a trainingdata - and testdata file for AbSentiment.
     * The trainingsdata file can be used directly for training the AbSentiment model.
     * @param args
     */
    public static void main(String[] args){
        TrainingDataManager trainingDataManager = new TrainingDataManager();
        trainingDataManager.createTrainingDataFromBaseFiles();
    }
}
