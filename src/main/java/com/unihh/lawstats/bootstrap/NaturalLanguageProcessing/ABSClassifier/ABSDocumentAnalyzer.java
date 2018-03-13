package com.unihh.lawstats.bootstrap.NaturalLanguageProcessing.ABSClassifier;

import com.unihh.lawstats.core.model.Verdict;
import uhh_lt.ABSA.ABSentiment.type.Result;

import java.util.ArrayList;
import java.util.List;

import com.unihh.lawstats.bootstrap.NaturalLanguageProcessing.NLPLawUtils;
import uhh_lt.ABSA.ABSentiment.AbSentiment;

import java.util.concurrent.atomic.AtomicReference;

public class ABSDocumentAnalyzer {

    AbSentiment _abSentiment;

    public Verdict analyzeABSResultsAndPutItInVerdict(List<Result> resultList, Verdict verdict) {
        verdict.setRevisionSuccess(determineRevisionOutcome(resultList));

        if (verdict.getRevisionSuccess() == -99) {
            return null;
        }

        List<String> importantSentences = new ArrayList<>();
        resultList.forEach(result -> {
            if (!result.getRelevance().equals("irrelevant")) {
                importantSentences.add(result.getText());
            }
        });

        String[] stringArray = new String[importantSentences.size()];
        verdict.setDecisionSentences((importantSentences.toArray(stringArray)));
        return verdict;
    }


    private int determineRevisionOutcome(List<Result> resultList) {
        AtomicReference<Result> highestResult = new AtomicReference<>(new Result(""));
        highestResult.get().setRelevanceScore(0);
        resultList.forEach(result -> {
            switch (result.getRelevance()) {
                case "revisionsErfolg":
                case "revisionsMisserfolg":
                case "revisionsTeilerfolg":
                    if (result.getRelevanceScore() >= 0.4 && result.getRelevanceScore() > highestResult.get().getRelevanceScore()) {
                        highestResult.set(result);
                    }
                    break;
                default:
                    break;
            }
        });

        if (highestResult.get().getText().isEmpty()) {
            return -99;
        }

        switch (highestResult.get().getRelevance()) {
            case "revisionsErfolg":
                return 1;
            case "revisionsMisserfolg":
                return -1;
            case "revisionsTeilerfolg":
                return 0;
            default:
                return -99;
        }
    }


    /**
     * Classifies a document by analyzing the first 10 sentences
     *
     * @param documentText - The full Document Text as String
     * @return List<Result> - A List of ABS results, where one result is the analyzes of one sentence
     */
    public List<Result> retrieveABSResultsForDocumentText(String documentText) {
        NLPLawUtils nlpLawUtils = new NLPLawUtils();
        List<Result> resultList = new ArrayList<>();
        List<String> sentenceList = nlpLawUtils.splitDocumentIntoSpecifiedSentences(documentText, 10);


        if (_abSentiment == null) {
            setAbSentiment(new AbSentiment("src/main/resources/config/ABSConfiguration.txt")); //TODO properties
        }


        for (String sentence : sentenceList) {
            if (sentence != null) {
                Result result = _abSentiment.analyzeText(sentence);
                resultList.add(result);
            }
        }


        return resultList;
    }


    public void setAbSentiment(AbSentiment abSentiment) {
        _abSentiment = abSentiment;
    }
}
