package com.unihh.lawstats.NaturalLawLanguageProcessing.Watson;

import com.ibm.watson.developer_cloud.natural_language_understanding.v1.NaturalLanguageUnderstanding;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalysisResults;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalyzeOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.EntitiesOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.Features;
import com.unihh.lawstats.PropertyManager;

/**
 * @author Phillip
 */
public class LawNLUCommunicator {
    static String usernameNLU = PropertyManager.getLawProperty(PropertyManager.WATSON_NLU_USERNAME);
    static String passwordNLU = PropertyManager.getLawProperty(PropertyManager.WATSON_NLU_PASSWORD);


    /**
     * Gets a JSON String from the Watson Natural Language Understanding service.
     * Sets all necessary configurations, including user credentials and model ID.
     * @param modelId The ID specifing which model shall be used.
     * @param text The text to analyze
     * @return a JSON String containing all the information about the analysis.
     */
    public String retrieveEntities(String modelId, String text) {
        NaturalLanguageUnderstanding service = new NaturalLanguageUnderstanding(
                NaturalLanguageUnderstanding.VERSION_DATE_2017_02_27,
                usernameNLU,
                passwordNLU
        );
        service.setEndPoint("https://gateway-fra.watsonplatform.net/natural-language-understanding/api");
        service.setUsernameAndPassword(usernameNLU, passwordNLU);


        EntitiesOptions entitiesOptions = new EntitiesOptions.Builder()
                .emotion(false)
                .sentiment(false)
                .model(modelId)
                .build();


        Features features = new Features.Builder()
                .entities(entitiesOptions)
                .build();


        AnalyzeOptions parameters = new AnalyzeOptions.Builder()
                .text(text)
                .features(features)
                .build();



        AnalysisResults response = service.analyze(parameters).execute();



        return response.toString();
    }






}
