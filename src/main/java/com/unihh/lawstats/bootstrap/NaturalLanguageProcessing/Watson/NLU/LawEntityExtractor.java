package com.unihh.lawstats.bootstrap.NaturalLanguageProcessing.Watson.NLU;

import com.ibm.watson.developer_cloud.natural_language_understanding.v1.NaturalLanguageUnderstanding;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalysisResults;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalyzeOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.EntitiesOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.Features;
import com.unihh.lawstats.bootstrap.Converter.Formatting.Formatter;
import com.unihh.lawstats.bootstrap.NaturalLanguageProcessing.NLPLawUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.*;

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



    public Map<String, String[]> findDecisionSentences(String jsonString) {
        JSONObject jsonObject = new JSONObject(jsonString);
        Map<String, String[]> _decisionSentenceMap = new HashMap<>();
        NLPLawUtils nlpLawUtils = new NLPLawUtils();


        String fullDocumentText = jsonObject.getString("text");
        fullDocumentText = Formatter.replaceNewLines(fullDocumentText);

        String fileName = jsonObject.getString("name");
        JSONArray mentionsJsonArray = jsonObject.getJSONArray("mentions");
        //jsonObject.remove("mentions");
        String test = jsonObject.toString();



        System.out.println(test);

        Iterator<Object> iterableMentionsJsonArray = mentionsJsonArray.iterator();


        List<String> _firstSentences = nlpLawUtils.splitDocumentIntoSpecifiedSentences(fullDocumentText, 5);


        for(String earlySentence: _firstSentences){
            String[] valueArray = new String[2];
            valueArray[0]="irrelevant";
            valueArray[1]=fileName;
            String formattedSentence = earlySentence.replaceAll("\\s", " ");
            //earlySentence = earlySentence.replaceAll("\\r", " ");
            formattedSentence = formattedSentence.trim();

            _decisionSentenceMap.put(formattedSentence, valueArray);
        }




        while (iterableMentionsJsonArray.hasNext()) {
            JSONObject mentionObject = (JSONObject) iterableMentionsJsonArray.next();

            String type = mentionObject.getString("type");
            String sentence = null;
            String revisionType = null;
            int beginIndex = -1;
            int endIndex = -1;


            if(type.contains("Revision")){
                beginIndex = mentionObject.getInt("begin");
                endIndex = mentionObject.getInt("end");
                sentence = retrieveSentenceFromDocument(fullDocumentText, beginIndex, endIndex);


                switch(type.toLowerCase()){
                    case "revisionsteilerfolg":
                            revisionType = "revisionsTeilerfolg";
                            break;
                    case "revisionserfolg":
                            revisionType = "revisionsErfolg";
                            break;
                    case "revisionsmisserfolg":
                            revisionType = "revisionsMisserfolg";
                            break;
                }


                boolean isInMap = false;



                for(Map.Entry<String, String[]> sentenceEntry: _decisionSentenceMap.entrySet()){
                    if(sentenceEntry.getKey().contains(sentence)){
                        String[] sentenceValues = sentenceEntry.getValue();
                        sentenceValues[0] = revisionType;
                        isInMap = true;
                    }


                }

                if(!isInMap){
                    String[] valueArray = new String[2];
                    valueArray[0] = revisionType;
                    valueArray[1] = fileName;
                    _decisionSentenceMap.put(sentence, valueArray);
                }


            }

        }


        return _decisionSentenceMap;
    }


    private String retrieveSentenceFromDocument(String documentText, int beginIndex, int endIndex){
        String sentence = documentText.substring(beginIndex, endIndex);


        return sentence;
    }


}
