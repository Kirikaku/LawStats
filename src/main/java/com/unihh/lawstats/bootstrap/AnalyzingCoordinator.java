package com.unihh.lawstats.bootstrap;

import com.unihh.lawstats.bootstrap.Converter.Formatting.Formatter;
import com.unihh.lawstats.bootstrap.Converter.PDFToTextConverter;
import com.unihh.lawstats.bootstrap.NaturalLanguageProcessing.ABSClassifier.ABSDocumentAnalyzer;
import com.unihh.lawstats.bootstrap.NaturalLanguageProcessing.Watson.NLU.LawEntityExtractor;
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
     * this method analyse the given files and creates a verdict of it
     *
     * @return the verdict of the given file
     * @throws NoDocketnumberFoundException when no DocketNumber was found
     */
    public Verdict analyzeDocument(File fileToAnalyze) throws NoDocketnumberFoundException {
        PDFToTextConverter pdfToTextConverter = new PDFToTextConverter();
        LawEntityExtractor lawEntityExtractor = new LawEntityExtractor();
        Mapper verdictMapper = new Mapper();
        String documentText = null;
        String jsonNLUResponse = null;


        String path = fileToAnalyze.getPath();
        String pathTxt = path.replace(".pdf", ".txt");


        pdfToTextConverter.convertPDFToText(path);
        documentText = Formatter.formatText(pathTxt);
        if(documentText == null || documentText.equals("")){
            return null;
        }

        //10:864de4a5-5bab-495e-8080-2f1185d1b38d
        jsonNLUResponse = lawEntityExtractor.extractEntities("10:a6285191-9eae-41c0-befb-bffaf2e9e587", documentText); //TODO model id von config holen

        // Throws the NoDocketnumberFoundException
        Verdict verdict = verdictMapper.mapJSONStringToVerdicObject(jsonNLUResponse);



        List<Result> classifierReults = _absDocumentAnalyzer.retrieveResultsForDocumentString(documentText);
        verdict = _absDocumentAnalyzer.analyzeABSResultsAndPutItInVerdict(classifierReults, verdict);

        if(verdict == null){
            return null;
        }


        BGHVerdictUtil bghVerdictUtil = new BGHVerdictUtil();
        verdict.setDocumentNumber(Integer.valueOf(bghVerdictUtil.retrieveBGHVerdictNumberForFileName(fileToAnalyze.getName())));

        return verdict;
    }

}
