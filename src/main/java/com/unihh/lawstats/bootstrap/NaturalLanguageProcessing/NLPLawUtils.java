package com.unihh.lawstats.bootstrap.NaturalLanguageProcessing;


import com.unihh.lawstats.bootstrap.Converter.Formatting.Formatter;

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

        return splitDocumentIntoSpecifiedSentences(documentText, Integer.MAX_VALUE);
    }



    public List<String> splitDocumentIntoSpecifiedSentences(String documentText, int numberOfSentences) {
        String temporaryDocumentText = new String(documentText);
        int dotIndex = -1;
        String _sentence;
        List<String> _sentenceList = new ArrayList<>();

        while (temporaryDocumentText != null && !temporaryDocumentText.isEmpty() && !(numberOfSentences<1)) {

            numberOfSentences--;
            dotIndex = temporaryDocumentText.indexOf(".");

            if (dotIndex == -1) {
                _sentence = temporaryDocumentText;
                temporaryDocumentText = "";
            } else {
                _sentence = temporaryDocumentText.substring(0, dotIndex + 1);
                temporaryDocumentText = temporaryDocumentText.substring(dotIndex + 1);
            }

            _sentence = Formatter.replaceNewLines(_sentence);
            _sentence = _sentence.trim();

            _sentenceList.add(_sentence);
        }

        return _sentenceList;
    }



}
