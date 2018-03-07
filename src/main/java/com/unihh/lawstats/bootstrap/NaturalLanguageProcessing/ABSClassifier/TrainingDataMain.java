package com.unihh.lawstats.bootstrap.NaturalLanguageProcessing.ABSClassifier;

import com.unihh.lawstats.bootstrap.NaturalLanguageProcessing.Watson.NLU.LawEntityExtractor;
import org.apache.commons.io.IOUtils;
import uhh_lt.ABSA.ABSentiment.AbSentiment;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class TrainingDataMain {

    public static void main(String[] args) {
        TrainingData trainingData = new TrainingData();

        trainingData.createTrainingsData();


    }
}
