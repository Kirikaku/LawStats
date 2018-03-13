package com.unihh.lawstats.bootstrap;

import com.unihh.lawstats.bootstrap.Converter.Formatting.Formatter;
import com.unihh.lawstats.bootstrap.Converter.PDFToTextConverter;
import com.unihh.lawstats.bootstrap.NaturalLanguageProcessing.ABSClassifier.ABSDocumentAnalyzer;
import com.unihh.lawstats.bootstrap.NaturalLanguageProcessing.Watson.LawNLUCommunicator;
import com.unihh.lawstats.core.mapping.BGHVerdictUtil;
import com.unihh.lawstats.core.mapping.Mapper;
import com.unihh.lawstats.core.mapping.NoDocketnumberFoundException;
import com.unihh.lawstats.core.model.Verdict;
import uhh_lt.ABSA.ABSentiment.AbSentiment;
import uhh_lt.ABSA.ABSentiment.type.Result;


import java.io.File;
import java.util.List;

public class AnalyzingCoordinator {

    ABSDocumentAnalyzer _absDocumentAnalyzer;

    public AnalyzingCoordinator(){
        _absDocumentAnalyzer = new ABSDocumentAnalyzer();
    }

    public AnalyzingCoordinator(AbSentiment abSentiment){
        _absDocumentAnalyzer = new ABSDocumentAnalyzer();
        _absDocumentAnalyzer.setAbSentiment(abSentiment);
    }

    /**
     * This method analyse the given files and creates a verdict of it
     *
     * @return the verdict of the given file
     * @throws NoDocketnumberFoundException when no DocketNumber was found
     */
    public Verdict analyzeDocument(File fileToAnalyze, boolean isDeployMode) throws NoDocketnumberFoundException {

        PDFToTextConverter pdfToTextConverter = new PDFToTextConverter();
        LawNLUCommunicator lawNLUCommunicator = new LawNLUCommunicator();
        Mapper verdictMapper = new Mapper();
        BGHVerdictUtil bghVerdictUtil = new BGHVerdictUtil();
        String documentText = null;
        String jsonNLUResponse = null;
        List<Result> classifierResults;
        Verdict verdict;
        String path;
        String pathTxt;

        //Gets the source and target path
        path = fileToAnalyze.getPath();
        pathTxt = path.replace(".pdf", ".txt");


        //Convert and format
        pdfToTextConverter.convertPDFToText(path, isDeployMode);
        documentText = Formatter.formatText(pathTxt);

        //checks for formatting errors
        if(documentText == null || documentText.equals("")){
            return null;
        }


        jsonNLUResponse = lawNLUCommunicator.retrieveEntities("10:a6285191-9eae-41c0-befb-bffaf2e9e587", documentText); //TODO properties


        verdict = verdictMapper.mapJSONStringToVerdicObject(jsonNLUResponse);  // Throws the NoDocketnumberFoundException


        classifierResults = _absDocumentAnalyzer.retrieveABSResultsForDocumentText(documentText);
        verdict = _absDocumentAnalyzer.analyzeABSResultsAndUpdateVerdict(classifierResults, verdict);

        //checks if the analyzes failed
        if(verdict == null){
            return null;
        }


        verdictMapper.setMinimumDateForLastForeDecision(verdict.getDecisionSentences()[0], verdict); //We have only one sentence



        try {
            verdict.setDocumentNumber(Integer.valueOf(bghVerdictUtil.retrieveBGHVerdictNumberForFileName(fileToAnalyze.getName())));
        }catch(NumberFormatException nFE){
            nFE.printStackTrace();
        }


        return verdict;
    }

}
