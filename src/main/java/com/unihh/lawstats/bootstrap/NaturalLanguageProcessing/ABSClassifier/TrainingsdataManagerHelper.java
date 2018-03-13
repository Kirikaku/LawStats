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
        Map<String, String[]> _decisionSentenceMap = new HashMap<>();
        NLPLawUtils nlpLawUtils = new NLPLawUtils();


        String fullDocumentText = jsonObject.getString("text");
        fullDocumentText = Formatter.replaceNewLines(fullDocumentText);

        String fileName = jsonObject.getString("name");
        JSONArray mentionsJsonArray = jsonObject.getJSONArray("mentions");
        //jsonObject.remove("mentions");


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
