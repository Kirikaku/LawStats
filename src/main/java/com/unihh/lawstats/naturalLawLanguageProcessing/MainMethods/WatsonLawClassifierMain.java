package com.unihh.lawstats.naturalLawLanguageProcessing.MainMethods;

import com.ibm.watson.developer_cloud.natural_language_classifier.v1.model.Classification;
import com.unihh.lawstats.PropertyManager;
import com.unihh.lawstats.naturalLawLanguageProcessing.Watson.Classifier.WatsonLawClassifier;

/**
 * @author Phillip
 */
public class WatsonLawClassifierMain {

    /**
     * Example usage for the Watson Classifier.
     * The Watson Classifier is not used in our software.
     * This is only an example for following groups.
     *
     * @param args
     */
    public static void main(String[] args) {
        WatsonLawClassifier watsonLawClassifier = new WatsonLawClassifier();
        String classifierID = PropertyManager.getLawProperty(PropertyManager.WATSON_CLASSIFIER_MODELID);


        //retrieves the Status
        /*
        Classifier.Status classifierStatus = watsonLawClassifier.retrieveStatus(classifierId);
        System.out.println(classifierStatus.toString());
        */


        //trains the classifier
        //watsonLawClassifier.trainClassifier();


        //for performance measuring
        Long startTime = System.currentTimeMillis();
        System.out.println(startTime);


        //Classifies the three sentences 10 times.
        //Was implemented to measure the performance
        //can be used as an example for the usage
        for (int i = 0; i < 10; i++) {
            Classification classification;

            classification = watsonLawClassifier.classifySentence("Is it rainy today?", classifierID);
            //do something usefull with the classification here
            //e.g. store all classifications in a list so you can process them later
            System.out.println(classification);

            classification = watsonLawClassifier.classifySentence("How hot is it?", classifierID);
            System.out.println(classification);

            classification = watsonLawClassifier.classifySentence("Are you stupid?", classifierID);
            System.out.println(classification);

        }


        //for performance measuring
        Long endTime = System.currentTimeMillis();
        Double timeDifference = (endTime - startTime) / 1000.0;
        System.out.println(endTime);
        System.out.println("Time difference in seconds" + timeDifference);

    }

}



