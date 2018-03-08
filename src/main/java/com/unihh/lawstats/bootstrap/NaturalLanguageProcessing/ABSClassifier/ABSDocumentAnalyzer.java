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
        verdict.setRevisionSuccess(computerRevisionSuccess(resultList));

        if(verdict.getRevisionSuccess() == -99){
            return null;
        }

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

        if(highestResult.get().getText().isEmpty()){
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


    public List<Result> retrieveResultsForDocumentString(String documentText){
        NLPLawUtils nlpLawUtils = new NLPLawUtils();
        AbSentiment abSentiment;
        if(_abSentiment != null){
            abSentiment = _abSentiment;
        }
        else {
            abSentiment = new AbSentiment("src/main/resources/config/ABSConfiguration.txt");
        }
        List<Result> resultList = new ArrayList<>();
        List<String> sentenceList = nlpLawUtils.splitDocumentIntoSpecifiedSentences(documentText, 10);


        for(String sentence: sentenceList){
            if(sentence != null) {
                Result result = abSentiment.analyzeText(sentence);
                resultList.add(result);
            }
        }

       return resultList;
    }


    public void setAbSentiment(AbSentiment abSentiment){
        _abSentiment = abSentiment;
    }
}
