package com.unihh.lawstats.NaturalLawLanguageProcessing.MainMethods;

import com.unihh.lawstats.PropertyManager;

/**
 * @author Phillip
 */
public class TrainingABSClassifierMain {

    /**
     * Trains the AbSentiment model.
     * The commented out code can be used to compute the features.
     * @param args
     */
    public static void main(String[] args) {
        String[] parameterArray = new String[1];
        parameterArray[0] = PropertyManager.getLawProperty(PropertyManager.ABS_CONFIGURATION);


        //inducing features from training set and background corpus
        // uhh_lt.ABSA.ABSentiment.PreComputeFeatures.main(parameterArray);


        //analyzing the results (not always working)
       // ComputeIdfTermsCategory.computeIdfScores("src/main/resources/config/ABSConfiguration.txt", "src/main/resources/data/trainingsData.tsv", "src/main/resources/data/features/relevance_idf_terms.tsv", false, "relevance");


        // training model
        uhh_lt.ABSA.ABSentiment.TrainAllClassifiers.main(parameterArray);

    }

}
