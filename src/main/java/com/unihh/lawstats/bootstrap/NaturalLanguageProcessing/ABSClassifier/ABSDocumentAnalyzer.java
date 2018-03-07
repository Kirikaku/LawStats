package com.unihh.lawstats.bootstrap.NaturalLanguageProcessing.ABSClassifier;

import com.unihh.lawstats.core.model.Verdict;
import uhh_lt.ABSA.ABSentiment.type.Result;

import java.util.ArrayList;
import java.util.List;

import com.unihh.lawstats.bootstrap.NaturalLanguageProcessing.NLPLawUtils;
import uhh_lt.ABSA.ABSentiment.AbSentiment;
import uhh_lt.ABSA.ABSentiment.type.Result;

import java.util.ArrayList;
import java.util.List;

public class ABSDocumentAnalyzer {

    public Verdict analyzeABSResultsAndPutItInVerdict(List<Result> resultList, Verdict verdict) {
        verdict.setRevisionSuccess(computerRevisionSuccess(resultList));

        List<String> importantSentences = new ArrayList<>();
        resultList.forEach(result -> {
            if(!result.getRelevance().equals("irrelevant")){
                importantSentences.add(result.getText());
            }
        });

        String[] stringArray = new String[importantSentences.size()];
        verdict.setDecisionSentences((importantSentences.toArray(stringArray)));
        return verdict;
    }

    private int computerRevisionSuccess(List<Result> resultList) {
        final int[] cardinalityOfRevisionSuccessOver0Point6 = {0};
        final int[] cardinalityOfRevisionNotSuccessOver0Point6 = {0};
        final int[] cardinalityOfRevisionPartiallySuccessOver0Point6 = {0};

        resultList.forEach(result -> {
            switch (result.getRelevance()) {
                case "revisionsErfolg":
                    if (result.getRelevanceScore() >= 0.6) {
                        cardinalityOfRevisionSuccessOver0Point6[0]++;
                    }
                    break;
                case "revisionsMisserfolg":
                    if (result.getRelevanceScore() >= 0.6) {
                        cardinalityOfRevisionNotSuccessOver0Point6[0]++;
                    }
                    break;
                case "revisionsTeilerfolg":
                    if (result.getRelevanceScore() >= 0.6) {
                        cardinalityOfRevisionPartiallySuccessOver0Point6[0]++;
                    }
                    break;
                default:
                    break;
            }
        });
        return Integer.compare(cardinalityOfRevisionSuccessOver0Point6[0], cardinalityOfRevisionNotSuccessOver0Point6[0]);
    }


    public List<Result> retrieveResultsForDocumentString(String documentText){
        NLPLawUtils nlpLawUtils = new NLPLawUtils();
        AbSentiment abSentiment = new AbSentiment("src/main/resources/ABSConfiguration.txt");
        List<Result> resultList = new ArrayList<>();
        List<String> sentenceList = nlpLawUtils.splitDocumentIntoSpecifiedSentences(documentText, 10);


        for(String sentence: sentenceList){
           Result result = abSentiment.analyzeText(sentence);
           resultList.add(result);
        }

       return resultList;
    }
}
