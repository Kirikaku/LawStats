package com.unihh.lawstats.naturalLawLanguageProcessing;


import com.unihh.lawstats.naturalLawLanguageProcessing.Preprocessing.Formatting.Formatter;

import java.util.*;

/**
 * @author Phillip
 */
public class NLPLawUtils {

    /**
     * Splits and returns the first sentence of a text.
     * @param documentText The text from which the sentence shall be splitted.
     * @return The first sentence of the text.
     */
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


    /**
     * Splits a document into its sentences.
     * @param documentText The text which is to be splitted into sentences.
     * @return A List of the sentences.
     */
    public List<String> splitDocumentIntoSentences(String documentText) {

        return splitDocumentIntoSpecifiedSentences(documentText, Integer.MAX_VALUE);
    }


    /**
     * Splits up the first sentences. The number of sentences to be splitted must be specified.
     * @param documentText The text where the sentences shall be splitted from.
     * @param numberOfSentences The number of sentences that shall be splitted.
     * @return A List of the splitted up sentences.
     */
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
