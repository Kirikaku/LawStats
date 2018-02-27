package com.unihh.lawstats.bootstrap.Watson;


import com.ibm.watson.developer_cloud.natural_language_classifier.v1.NaturalLanguageClassifier;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.model.Classification;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.model.Classifier;

import java.io.File;

public class LawClassifier {
    NaturalLanguageClassifier _service = new NaturalLanguageClassifier();

    public static void main(String[] args) {


        //LawClassifier lawClassifier = new LawClassifier();
        LawEntityExtractor lawEntityExtractor = new LawEntityExtractor();
        //lawClassifier.trainClassifier();
        lawEntityExtractor.extractEntities("dude", "ein text");
    }

    public LawClassifier() {
        this._service.setUsernameAndPassword("056d44aa-83e3-4adf-9c57-c6f006164a2b", "5EaHAYwFVLEc");
    }

    public void trainClassifier() {
        try {
            Thread.sleep(10000L);
        } catch (InterruptedException var2) {
            var2.printStackTrace();
        }

        System.out.println("jetzt gehts looos");
        Classifier classifier = (Classifier) this._service.createClassifier("PerformanceTestClassifier", "de", new File("C:\\Users\\Phillip\\Documents\\Studium\\Praktikum Sprachtechnologie\\IBM Classifier\\weather_data_train.csv")).execute();
        System.out.println(classifier);
    }



    public void classifySentence(String classifierId, String sentence){
        Classification classification = _service.classify(classifierId, sentence).execute();


    }




}