package com.unihh.lawstats.bootstrap.NaturalLanguageProcessing.ABSClassifier;

import com.unihh.lawstats.bootstrap.Converter.Formatting.Formatter;
import com.unihh.lawstats.bootstrap.NaturalLanguageProcessing.NLPLawUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class TrainingsdataManagerHelper {

    public Map<String, String[]> findDecisionSentences(String jsonString) {

        JSONObject jsonObject = new JSONObject(jsonString);
        Map<String, String[]> decisionSentenceMap = new HashMap<>();
        NLPLawUtils nlpLawUtils = new NLPLawUtils();
        List<String> firstSentences;
        String fileName;
        String fullDocumentText;
        JSONArray mentionsJsonArray;
        Iterator<Object> iterableMentionsJsonArray;


        fileName = jsonObject.getString("name");


        fullDocumentText = jsonObject.getString("text");
        fullDocumentText = Formatter.replaceNewLines(fullDocumentText);
        firstSentences = nlpLawUtils.splitDocumentIntoSpecifiedSentences(fullDocumentText, 5); //TODO evtl. properties

        mentionsJsonArray = jsonObject.getJSONArray("mentions");
        iterableMentionsJsonArray = mentionsJsonArray.iterator();




        initialDefaultFill(firstSentences, decisionSentenceMap, fileName);


        while (iterableMentionsJsonArray.hasNext()) {
            JSONObject mentionObject = (JSONObject) iterableMentionsJsonArray.next();

            String mentionType;
            String sentence = null;
            String revisionType = null;



            mentionType = mentionObject.getString("type");


            if(mentionType.contains("Revision")){

                boolean isInMap = false;

                sentence = retrieveMentionSentenceFromDocument(fullDocumentText, mentionObject);

                revisionType = determineRevisionType(mentionType);

                updateDecisionSentenceList(decisionSentenceMap, sentence, revisionType, fileName);

            }

        }



        return decisionSentenceMap;
    }


    private void initialDefaultFill(List<String> firstSentences, Map<String, String[]> decisionSentenceMap, String fileName){

        for(String earlySentence: firstSentences){
            String[] valueArray = new String[2];
            valueArray[0]="irrelevant";
            valueArray[1]=fileName;

            String formattedSentence = earlySentence.replaceAll("\\s", " ");
            formattedSentence = formattedSentence.trim();

            decisionSentenceMap.put(formattedSentence, valueArray);
        }

    }


    private void updateDecisionSentenceList(Map<String, String[]> decisionSentenceMap, String sentence, String revisionType, String fileName){
        boolean isInMap = false;


        for(Map.Entry<String, String[]> sentenceEntry: decisionSentenceMap.entrySet()){
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
            decisionSentenceMap.put(sentence, valueArray);
        }
    }


    private String retrieveMentionSentenceFromDocument(String documentText, JSONObject mentionObject){
        int beginIndex = mentionObject.getInt("begin");
        int endIndex = mentionObject.getInt("end");

        String sentence = documentText.substring(beginIndex, endIndex);

        return sentence;
    }

    private String determineRevisionType(String mentionType){
        String revisionType = null;


        switch(mentionType.toLowerCase()){
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

        return revisionType;
    }



    private List<String[]> analyzeMentions(Iterator<Object> mentionsIterator){

        return null;
    }

}
