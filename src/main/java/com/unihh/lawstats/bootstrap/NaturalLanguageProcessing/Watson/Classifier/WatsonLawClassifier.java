package com.unihh.lawstats.bootstrap.NaturalLanguageProcessing.Watson.Classifier;


import com.ibm.watson.developer_cloud.natural_language_classifier.v1.NaturalLanguageClassifier;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.model.Classification;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.model.Classifier;

import java.io.File;

public class WatsonLawClassifier {
    NaturalLanguageClassifier _service;

    public static void main(String[] args) {
        WatsonLawClassifier watsonLawClassifier = new WatsonLawClassifier();
        String classifierId = "77d867x10-nlc-2";

        /*
        Classifier.Status classifierStatus = watsonLawClassifier.retrieveStatus(classifierId);
        System.out.println(classifierStatus.toString());
*/

        //watsonLawClassifier.trainClassifier();



        Long startTime = System.currentTimeMillis();
        System.out.println(startTime);


        for(int i = 0; i<10; i++) {
            Classification classification;
            classification = watsonLawClassifier.classifySentence("Is it rainy today?", classifierId);
            System.out.println(classification);
            classification = watsonLawClassifier.classifySentence("How hot is it?", classifierId);
            System.out.println(classification);
            classification = watsonLawClassifier.classifySentence("Are you stupid?", classifierId);
            System.out.println(classification);

        }


        Long endTime = System.currentTimeMillis();
        Double timeDifference = (endTime - startTime)/1000.0;
        System.out.println(endTime);
        System.out.println("Time difference in seconds" + timeDifference);

    }

    public WatsonLawClassifier() {
        _service = new NaturalLanguageClassifier();
        _service.setEndPoint("https://gateway-fra.watsonplatform.net/natural-language-classifier/api");
        this._service.setUsernameAndPassword("5c91eced-33b4-4c62-b138-1a7018f2c580", "ry2vejcJG7dK");


    }

    public void trainClassifier() {
        Classifier classifier = (Classifier) this._service.createClassifier("PerformanceTestClassifier", "en", new File("C:\\Users\\Phillip\\Praktikum Sprachtechnologie\\weather_data_train.csv")).execute();
        String classifierId = classifier.getId();
        System.out.println(classifierId);
        //classifier.setUrl("https://gateway-fra.watsonplatform.net/natural-language-classifier/api");   ///v1/classifiers/"+classifierId);
        System.out.println(classifier);
    }

    public Classifier.Status retrieveStatus(String classifierId){
        Classifier classifier =  _service.getClassifier(classifierId).execute();

        return classifier.getStatus();
    }


    public Classification classifySentence(String sentence, String classifierId){
        Classification classification = _service.classify(classifierId, sentence).execute();
        return classification;
    }


    public void useTUClassifier(){
        //AbSentiment analyzer = new AbSentiment();

    }
}





