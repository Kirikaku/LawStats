package com.unihh.lawstats.naturalLawLanguageProcessing.ABSClassifier;

import com.unihh.lawstats.naturalLawLanguageProcessing.Preprocessing.Formatting.Formatter;
import com.unihh.lawstats.naturalLawLanguageProcessing.NLPLawUtils;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;

/**
 * @author Phillip
 */
public class TrainingsDataManagerHelper {

    /**
     * Helper method to retrieve the important sentences from all annotation files. (not just one)
     * @param allFiles an Array containing all the files that shall be used.
     * @return a List of trainingsdata entry arrays.
     */
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


    /**
     * Finds all important sentences from one annotation file.
     * @param jsonString A Watson Knowledge Studio annotation JSON.
     * @return A List of trainingsdata entry arrays.
     */
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
        firstSentences = nlpLawUtils.splitDocumentIntoSpecifiedSentences(fullDocumentText, 5);

        mentionsJsonArray = jsonObject.getJSONArray("mentions");
        mentionsArrayIterator = mentionsJsonArray.iterator();




        initialDefaultFill(firstSentences, decisionSentenceList, fileName);

        analyzeMentions(mentionsArrayIterator, decisionSentenceList, fullDocumentText, fileName);


        return decisionSentenceList;
    }


    /**
     * Helper method to get the important sentences from a WKS annotation JSON file.
     * @param mentionsIterator An Iterator over all the mention objects in the JSON WKS annotation file.
     * @param decisionSentenceList A List of trainingsdata entry arrays.
     * @param fullDocumentText The corresponding plain text to the JSON annotation file that is being analyzed.
     * @param fileName The name of the original file. (used for creating an ID later)
     * @return a List of trainingsdata entry arrays.
     */
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


    /**
     * Creates trainingsdata entry arrays for all given sentences and labels them as irrelevant.
     * @param firstSentences A List of plain text sentences.
     * @param decisionSentenceList A list of trainingsdata entry arrays.
     * @param fileName The name of the original file (where the sentences come from). (Used for creating the ID)
     */
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


    /**
     * Helper method to relabel sentences that were labeled differently during the intial fill.
     * @param decisionSentenceList List of trainingsdata entry arrays that shall be updated
     * @param sentence sentence for which the label shall be updated
     * @param revisionType the label to update
     * @param fileName The filename of the original verdict. (used for creating the ID)
     */
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


    /**
     * Helper method to retrieve a sentence from a document String.
     * @param documentText The document text from which the sentence shall be extracted.
     * @param mentionObject The mention object holding the information about the position of the sentence.
     * @return the sentence indicated by the mention object.
     */
    private String retrieveMentionSentenceFromDocument(String documentText, JSONObject mentionObject) {
        int beginIndex = mentionObject.getInt("begin");
        int endIndex = mentionObject.getInt("end");

        String sentence = documentText.substring(beginIndex, endIndex);

        return sentence;
    }


    /**
     *
     * @param mentionType an WKS annotation mention type
     * @return a standardized String for the revision type
     */
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
