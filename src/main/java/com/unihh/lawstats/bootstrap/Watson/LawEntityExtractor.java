package com.unihh.lawstats.bootstrap.Watson;

import com.ibm.watson.developer_cloud.natural_language_understanding.v1.NaturalLanguageUnderstanding;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalysisResults;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalyzeOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.EntitiesOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.Features;

public class LawEntityExtractor {
    static String usernameNLU = "e20dcd7b-4d20-418f-8d2d-3df229599ce7";
    static String passwordNLU = "PtIenHyRZxzG";


    public String extractEntities(String modelid) {
        NaturalLanguageUnderstanding service = new NaturalLanguageUnderstanding(
                NaturalLanguageUnderstanding.VERSION_DATE_2017_02_27,
                usernameNLU,
                passwordNLU
        );
        service.setEndPoint("https://gateway-fra.watsonplatform.net/natural-language-understanding/api");
        service.setUsernameAndPassword(usernameNLU, passwordNLU);

        String text = "IBM is an American multinational technology " +
                "company headquartered in Armonk, New York, " +
                "United States, with operations in over 170 countries.";

        EntitiesOptions entitiesOptions = new EntitiesOptions.Builder()
                .emotion(false)
                .sentiment(false)
              //  .model(modelId)
                .build();



        Features features = new Features.Builder()
                .entities(entitiesOptions)
                .build();

        AnalyzeOptions parameters = new AnalyzeOptions.Builder()
                .text(text)
                .features(features)
                .build();

        AnalysisResults response = service
                .analyze(parameters)
                .execute();

        System.out.println(response);

        return response.toString();
    }
}
