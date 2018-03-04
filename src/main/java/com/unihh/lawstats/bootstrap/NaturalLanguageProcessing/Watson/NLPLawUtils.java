package com.unihh.lawstats.bootstrap.NaturalLanguageProcessing.Watson;


import java.util.*;

public class NLPLawUtils {


    public String splitFirstSentence(String documentText) {
        String temporaryDocumentText = new String(documentText);
        int dotIndex = -1;
        String sentence;


        dotIndex = temporaryDocumentText.indexOf(".");

        if (dotIndex == -1) {
            sentence = temporaryDocumentText;
        }
        else {
            sentence = temporaryDocumentText.substring(0, dotIndex + 1);
        }


        return sentence;
    }




    public List<String> splitDocumentIntoSentences(String documentText) {
        String temporaryDocumentText = new String(documentText);
        int dotIndex = -1;
        String _sentence;
        List<String> _sentenceList = new ArrayList<>();

        while (temporaryDocumentText != null && !temporaryDocumentText.isEmpty()) {

            dotIndex = temporaryDocumentText.indexOf(".");

            if (dotIndex == -1) {
                _sentence = temporaryDocumentText;
                temporaryDocumentText = "";
            } else {
                _sentence = temporaryDocumentText.substring(0, dotIndex + 1);
                temporaryDocumentText = temporaryDocumentText.substring(dotIndex + 1);
            }

            _sentenceList.add(_sentence);

        }

        return _sentenceList;
    }




}
