package com.unihh.lawstats.bootstrap;

import com.unihh.lawstats.bootstrap.Converter.Formatting.Formatter;
import com.unihh.lawstats.bootstrap.Converter.PDFToTextConverter;
import com.unihh.lawstats.bootstrap.NaturalLanguageProcessing.Watson.NLU.LawEntityExtractor;
import com.unihh.lawstats.core.mapping.Mapper;
import com.unihh.lawstats.core.model.Verdict;


import java.io.File;
import java.text.ParseException;

public class AnalyzingCoordinator {

    public Verdict analyzeDocument(File fileToAnalyze) {
        Verdict verdict = new Verdict();
        PDFToTextConverter pdfToTextConverter = new PDFToTextConverter();
        LawEntityExtractor lawEntityExtractor = new LawEntityExtractor();
        Mapper verdictMapper = new Mapper();
        String documentText = null;
        String jsonNLUResponse = null;


        String path = fileToAnalyze.getPath();
        String pathTxt = path.replace(".pdf", ".txt");


        pdfToTextConverter.convertPDFToText(path);
        documentText = Formatter.formatText(pathTxt);
        jsonNLUResponse = lawEntityExtractor.extractEntities("10:864de4a5-5bab-495e-8080-2f1185d1b38d", documentText); //TODO model id von config holen


        try {
            verdict = verdictMapper.mapJSONStringToVerdicObject(jsonNLUResponse);
        } catch (ParseException pE) {
            System.out.println("Der JSON String konnte nicht auf ein Verdict Objekt gemappt werden.");
            //TODO hier sollte vernünftig geloggt werden
            pE.printStackTrace();
        }


        //TODO hier kommt es drauf an ob Watson oder TU Darm. Classifier benutzt wird
        verdict.setRevisionSuccess(1);

        String jsonClassifierResponse = "";

        //


        return verdict;
    }

}
