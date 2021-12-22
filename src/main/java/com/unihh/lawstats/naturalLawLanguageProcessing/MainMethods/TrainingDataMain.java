package com.unihh.lawstats.naturalLawLanguageProcessing.MainMethods;

import com.unihh.lawstats.naturalLawLanguageProcessing.ABSClassifier.TrainingDataManager;

/**
 * @author Phillip
 */
public class TrainingDataMain {

    /**
     * Creates four basefiles for each label category.
     * One file only contains sentences with the corresponding label.
     * The four labels are: "Erfolg", "Teilerfolg", "Misserfolg" and "Irrelevant"
     * @param args
     */
    public static void main(String[] args) {
        TrainingDataManager trainingDataManager = new TrainingDataManager();
        trainingDataManager.createTrainingsDataBaseFiles();


    }
}
