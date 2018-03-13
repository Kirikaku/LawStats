package com.unihh.lawstats.bootstrap.NaturalLanguageProcessing.ABSClassifier;

import com.unihh.lawstats.bootstrap.Converter.Formatting.Formatter;
import com.unihh.lawstats.bootstrap.NaturalLanguageProcessing.NLPLawUtils;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;

public class TrainingsDataManagerHelper {


    public List<String[]> retrieveImportantSentencesFromAnnotations(File[] allFiles){
        List<String[]> importantSentences = new ArrayList<>();


        for (int i = 0; i < allFiles.length; i++) {

            File file = allFiles[i];
            String jsonAnnotation;

            try {
                FileInputStream fis = new FileInputStream(file);
                jsonAnnotation = IOUtils.toString(fis, "UTF-8");
                importantSentences.addAll(findDecisionSentences(jsonAnnotation));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return importantSentences;
    }



    private List<String[]> findDecisionSentences(String jsonString) {

        //Declare variables
        JSONObject jsonObject;
        List<String[]> decisionSentenceList;
        NLPLawUtils nlpLawUtils;
        List<String> firstSentences;
        String fileName;
        String fullDocumentText;
        JSONArray mentionsJsonArray;
        Iterator<Object> mentionsArrayIterator;


        //Initialize variables
        jsonObject = new JSONObject(jsonString);
        nlpLawUtils = new NLPLawUtils();
        decisionSentenceList = new ArrayList<>();

        fileName = jsonObject.getString("name");

        fullDocumentText = jsonObject.getString("text");
        fullDocumentText = Formatter.replaceNewLines(fullDocumentText);
        firstSentences = nlpLawUtils.splitDocumentIntoSpecifiedSentences(fullDocumentText, 5); //TODO evtl. properties

        mentionsJsonArray = jsonObject.getJSONArray("mentions");
        mentionsArrayIterator = mentionsJsonArray.iterator();




        initialDefaultFill(firstSentences, decisionSentenceList, fileName);

        analyzeMentions(mentionsArrayIterator, decisionSentenceList, fullDocumentText, fileName);


        return decisionSentenceList;
    }




    private List<String[]> analyzeMentions(Iterator<Object> mentionsIterator, List<String[]> decisionSentenceList, String fullDocumentText, String fileName){

        while (mentionsIterator.hasNext()) {
            JSONObject mentionObject;
            String mentionType;
            String sentence;
            String revisionType;


            mentionObject = (JSONObject) mentionsIterator.next();
            mentionType = mentionObject.getString("type");


            if (mentionType.contains("Revision")) {

                sentence = retrieveMentionSentenceFromDocument(fullDocumentText, mentionObject);
                revisionType = determineRevisionType(mentionType);
                updateDecisionSentenceList(decisionSentenceList, sentence, revisionType, fileName);
            }
        }

        return decisionSentenceList;
    }





    private void initialDefaultFill(List<String> firstSentences, List<String[]> decisionSentenceList, String fileName) {

        for (String earlySentence : firstSentences) {
            String[] valueArray = new String[3];
            String formattedSentence = earlySentence;

            formattedSentence = formattedSentence.replaceAll("\\s", " ");
            formattedSentence = formattedSentence.trim();


            valueArray[0] = formattedSentence;
            valueArray[1] = "irrelevant";
            valueArray[2] = fileName;

            decisionSentenceList.add(valueArray);
        }

    }





    private void updateDecisionSentenceList(List<String[]> decisionSentenceList, String sentence, String revisionType, String fileName) {
        boolean isInList = false;


        for (String[] sentenceEntryArray : decisionSentenceList) {
            if (sentenceEntryArray[0].contains(sentence)) {
                sentenceEntryArray[1] = revisionType;
                isInList = true;
            }
        }


        if (!isInList) {
            String[] valueArray = new String[3];

            valueArray[0] = sentence;
            valueArray[1] = revisionType;
            valueArray[2] = fileName;

            decisionSentenceList.add(valueArray);
        }
    }





    private String retrieveMentionSentenceFromDocument(String documentText, JSONObject mentionObject) {
        int beginIndex = mentionObject.getInt("begin");
        int endIndex = mentionObject.getInt("end");

        String sentence = documentText.substring(beginIndex, endIndex);

        return sentence;
    }




    private String determineRevisionType(String mentionType) {
        String revisionType = null;


        switch (mentionType.toLowerCase()) {
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



}
