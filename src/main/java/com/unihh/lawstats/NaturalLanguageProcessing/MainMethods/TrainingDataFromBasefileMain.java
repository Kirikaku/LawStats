package com.unihh.lawstats.NaturalLanguageProcessing.MainMethods;

import com.unihh.lawstats.NaturalLanguageProcessing.ABSClassifier.TrainingDataManager;

public class TrainingDataFromBasefileMain {


    public static void main(String[] args){
        TrainingDataManager trainingDataManager = new TrainingDataManager();
        trainingDataManager.createTrainingDataFromBaseFiles();
    }
}
