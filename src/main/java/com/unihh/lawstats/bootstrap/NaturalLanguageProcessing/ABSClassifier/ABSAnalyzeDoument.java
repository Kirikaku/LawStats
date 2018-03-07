package com.unihh.lawstats.bootstrap.NaturalLanguageProcessing.ABSClassifier;

import com.unihh.lawstats.core.model.Verdict;
import uhh_lt.ABSA.ABSentiment.type.Result;

import java.util.List;

import com.unihh.lawstats.bootstrap.NaturalLanguageProcessing.NLPLawUtils;
import uhh_lt.ABSA.ABSentiment.AbSentiment;
import uhh_lt.ABSA.ABSentiment.type.Result;

import java.util.ArrayList;
import java.util.List;

public class ABSAnalyzeDoument {



    public List<Result> retrieveResultsForDocumentString(String documentText){
        NLPLawUtils nlpLawUtils = new NLPLawUtils();
        AbSentiment abSentiment = new AbSentiment("resources/config/ABSConfiguration.txt");
        List<Result> resultList = new ArrayList<>();
        List<String> sentenceList = nlpLawUtils.splitDocumentIntoSpecifiedSentences(documentText, 10);


        for(String sentence: sentenceList){
           Result result = abSentiment.analyzeText(sentence);
           resultList.add(result);
        }

       return resultList;
    }


    public Verdict analyzeABSResultsAndPutItInVerdict(List<Result> resultList, Verdict verdict){
        return null;
    }
}
