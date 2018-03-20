package com.unihh.lawstats.bootstrap.NaturalLanguageProcessing.Watson;

import com.ibm.watson.developer_cloud.natural_language_understanding.v1.NaturalLanguageUnderstanding;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalysisResults;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalyzeOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.EntitiesOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.Features;
import com.unihh.lawstats.PropertyManager;
import com.unihh.lawstats.bootstrap.Converter.Formatting.Formatter;
import com.unihh.lawstats.bootstrap.NaturalLanguageProcessing.NLPLawUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.*;

public class LawNLUCommunicator {
    static String usernameNLU = PropertyManager.getLawProperty(PropertyManager.WATSON_NLU_USERNAME); //TODO properties DONE
    static String passwordNLU = PropertyManager.getLawProperty(PropertyManager.WATSON_NLU_PASSWORD);


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


        //TODO Bad Request Exception Watson is not available
        AnalysisResults response = service.analyze(parameters).execute();



        return response.toString();
    }






}
