package com.unihh.lawstats.NaturalLawLanguageProcessing;

import com.unihh.lawstats.NaturalLawLanguageProcessing.ABSClassifier.ABSDocumentAnalyzer;
import com.unihh.lawstats.NaturalLawLanguageProcessing.Preprocessing.Formatting.Formatter;
import com.unihh.lawstats.NaturalLawLanguageProcessing.Preprocessing.PDFToTextConverter;
import com.unihh.lawstats.NaturalLawLanguageProcessing.Watson.LawNLUCommunicator;
import com.unihh.lawstats.PropertyManager;
import com.unihh.lawstats.core.mapping.BGHVerdictUtil;
import com.unihh.lawstats.core.mapping.Mapper;
import com.unihh.lawstats.core.mapping.NoDocketnumberFoundException;
import com.unihh.lawstats.core.model.Verdict;
import uhh_lt.ABSA.ABSentiment.AbSentiment;
import uhh_lt.ABSA.ABSentiment.type.Result;

import java.io.File;
import java.util.List;

/**
 * @author Phillip
 */
public class AnalyzingCoordinator {

    ABSDocumentAnalyzer _absDocumentAnalyzer;

    public AnalyzingCoordinator() {
        _absDocumentAnalyzer = new ABSDocumentAnalyzer();
    }

    public AnalyzingCoordinator(AbSentiment abSentiment) {
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
        if (documentText == null || documentText.equals("")) {
            return null;
        }


        //This try-catch block is needed in order to handle the unpredictable behaviour of Watson
        //Watson will time out or deny requests once two many requests have been sent
        //Therefore we pause the application for 10seconds if this happens
        try {
            jsonNLUResponse = lawNLUCommunicator.retrieveEntities(PropertyManager.getLawProperty(PropertyManager.WATSON_NLU_MODELID), documentText);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                Thread.sleep(10000);
            } catch (InterruptedException iE) {
                iE.printStackTrace();
                return null;
            }
            return null;
        }

        //Try-catch block ss needed to prevent program shutdown due to unexpected problems while mapping
        try {
            verdict = verdictMapper.mapJSONStringToVerdicObject(jsonNLUResponse);  // Throws the NoDocketnumberFoundException
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        classifierResults = _absDocumentAnalyzer.retrieveABSResultsForDocumentText(documentText);
        verdict = _absDocumentAnalyzer.analyzeABSResultsAndUpdateVerdict(classifierResults, verdict);

        //checks if the analyzes failed
        if (verdict == null) {
            return null;
        }

        verdictMapper.setMinimumDateForLastForeDecision(verdict.getDecisionSentences()[0], verdict); //We have only one sentence

        try {
            verdict.setDocumentNumber(Integer.valueOf(bghVerdictUtil.retrieveBGHVerdictNumberForFileName(fileToAnalyze.getName())));
        } catch (final NumberFormatException nFE) {
            //nFE.printStackTrace();
        }

        return verdict;
    }

}
