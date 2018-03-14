package com.unihh.lawstats.bootstrap.NaturalLanguageProcessing.Watson.Classifier;

import com.ibm.watson.developer_cloud.natural_language_classifier.v1.NaturalLanguageClassifier;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.model.Classification;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.model.Classifier;

import java.io.File;

public class WatsonLawClassifier {

    NaturalLanguageClassifier _service;
    String _classifierID;



    /**
     * Sets the api end point url and the user credentials.
     * Setting the api end point is vital for the application to work.
     */
    public WatsonLawClassifier() {
        _service = new NaturalLanguageClassifier();
        _service.setEndPoint("https://gateway-fra.watsonplatform.net/natural-language-classifier/api");
        this._service.setUsernameAndPassword("5c91eced-33b4-4c62-b138-1a7018f2c580", "ry2vejcJG7dK");
    }



    /**
     * Trains a new classifier. It is important that the end point api and the user credentials are already set. (Is set in the constructor)
     * Sets the classifier ID
     *
     * @return the classifier ID
     */
    public String trainClassifier() {

        Classifier classifier = (Classifier) this._service.createClassifier("PerformanceTestClassifier", "en", new File("C:\\Users\\Phillip\\Praktikum Sprachtechnologie\\weather_data_train.csv")).execute();
        _classifierID = classifier.getId();

        return _classifierID;
    }



    /**
     * Retrieves the status of the classifier. Especially usefull when training the classifier.
     * There is no opther way to check weather or not the training is finished.
     * @param classifierId - String the classifier ID.
     * @return a IBM CLassifier Status object
     */
    public Classifier.Status retrieveStatus(String classifierId){
        Classifier classifier =  _service.getClassifier(classifierId).execute();

        return classifier.getStatus();
    }


    /**
     * Classifies a given sentence. The model must already be trained. The model to use is specified by the model ID.
     * @param sentence - String the sentence to classify
     * @param classifierId - String the classifier ID
     * @return classification result returned by the IBM Watson CLassifier
     */
    public Classification classifySentence(String sentence, String classifierId){
        Classification classification = _service.classify(classifierId, sentence).execute();
        return classification;
    }




    public String getClassifierID() {
        return _classifierID;
    }

    public void setClassifierID(String classifierID) {
        this._classifierID = classifierID;
    }


}
