package com.unihh.lawstats.bootstrap.NaturalLanguageProcessing.Watson.NLU;

import com.ibm.watson.developer_cloud.natural_language_understanding.v1.NaturalLanguageUnderstanding;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalysisResults;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalyzeOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.EntitiesOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.Features;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;

public class LawEntityExtractor {
    static String usernameNLU = "e20dcd7b-4d20-418f-8d2d-3df229599ce7";
    static String passwordNLU = "PtIenHyRZxzG";


    public String extractEntities(String modelId, String text) {
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


        System.out.println(response);

        return response.toString();
    }



    public void findDecisionSentences(String jsonString) {
        JSONObject jsonObject = new JSONObject(jsonString);
        String fullDocumentText = jsonObject.getString("text");

        JSONArray mentionsJsonArray = jsonObject.getJSONArray("mentions");
        Iterator<Object> iterableMentionsJsonArray = mentionsJsonArray.iterator();

        while (iterableMentionsJsonArray.hasNext()) {


        }

    }


}
