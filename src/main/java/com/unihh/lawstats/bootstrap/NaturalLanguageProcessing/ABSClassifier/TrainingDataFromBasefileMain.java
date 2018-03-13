package com.unihh.lawstats.bootstrap.NaturalLanguageProcessing.ABSClassifier;

public class TrainingDataFromBasefileMain {


    public static void main(String[] args){
        TrainingDataManager trainingDataManager = new TrainingDataManager();

        trainingDataManager.createTrainingDataFromBaseFiles("C:\\Users\\Phillip\\Documents\\Studium\\Praktikum Sprachtechnologie\\TU Classifier\\TrainingsData2\\");
    }
}
