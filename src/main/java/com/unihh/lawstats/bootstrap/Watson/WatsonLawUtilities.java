package com.unihh.lawstats.bootstrap.Watson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class WatsonLawUtilities {


    public String splitFirstSentence(String documentText){
        String temporaryDocumentText = new String(documentText);
        int dotIndex = -1;
        String sentence;
        Map<String, String> sentenceMap = new HashMap<>();
        String genericValue = "irrelevant";



            dotIndex = temporaryDocumentText.indexOf(".");

            if (dotIndex == -1) {
                sentence = temporaryDocumentText;
                temporaryDocumentText = "";
            } else {
                sentence = temporaryDocumentText.substring(0, dotIndex + 1);
                temporaryDocumentText = temporaryDocumentText.substring(dotIndex+1);
            }



        return sentence;
    }

    public Map<String, String> splitDocumentIntoSentences(String documentText){
        String temporaryDocumentText = new String(documentText);
        int dotIndex = -1;
        String sentence;
        Map<String, String> sentenceMap = new HashMap<>();
        String genericValue = "irrelevant";

        while(temporaryDocumentText != null && !temporaryDocumentText.isEmpty()) {

            dotIndex = temporaryDocumentText.indexOf(".");

            if (dotIndex == -1) {
                sentence = temporaryDocumentText;
                temporaryDocumentText = "";
            } else {
                sentence = temporaryDocumentText.substring(0, dotIndex + 1);
                temporaryDocumentText = temporaryDocumentText.substring(dotIndex+1);
            }

            sentenceMap.put(sentence, genericValue);

        }

        return sentenceMap;
    }


    public void findDecisionSentences(String jsonString){
        JSONObject jsonObject = new JSONObject(jsonString);
        String fullDocumentText = jsonObject.getString("text");

        JSONArray mentionsJsonArray = jsonObject.getJSONArray("mentions");
        Iterator<Object> iterableMentionsJsonArray = mentionsJsonArray.iterator();

        while(iterableMentionsJsonArray.hasNext()){


        }

    }


}
