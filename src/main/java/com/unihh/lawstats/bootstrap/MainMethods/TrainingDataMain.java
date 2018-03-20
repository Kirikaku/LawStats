package com.unihh.lawstats.bootstrap.MainMethods;

import com.unihh.lawstats.bootstrap.NaturalLanguageProcessing.ABSClassifier.TrainingDataManager;

public class TrainingDataMain {

    public static void main(String[] args) {
        TrainingDataManager trainingDataManager = new TrainingDataManager();
        trainingDataManager.createTrainingsDataBaseFiles();


    }
}
