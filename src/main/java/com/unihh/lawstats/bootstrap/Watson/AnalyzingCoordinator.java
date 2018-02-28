package com.unihh.lawstats.bootstrap.Watson;

import com.unihh.lawstats.bootstrap.Converter.Formatter;
import com.unihh.lawstats.bootstrap.Converter.PDFToTextConverter;
import com.unihh.lawstats.core.mapping.Mapper;
import com.unihh.lawstats.core.model.Verdict;


import java.io.File;
import java.util.Date;

public class AnalyzingCoordinator {


    public Verdict analyzeDocument(File fileToAnalyze){
        Verdict verdict = new Verdict();
        verdict.setForeDecisionDCCourt("Landgericht Erfurt");
        verdict.setDocketNumber("12345");
        verdict.setForeDecisionRCCourt("Oberlandesgericht Ulm");
        verdict.setDateVerdict(new Date(2015, 04, 12).getTime());

        PDFToTextConverter pdfToTextConverter = new PDFToTextConverter();
        LawEntityExtractor lawEntityExtractor = new LawEntityExtractor();
        Mapper verdictMapper = new Mapper();


        String documentText = null;
        String jsonNLUResponse = null;

        String path = fileToAnalyze.getPath();
        String pathTxt = path.replace(".pdf",".txt");


        pdfToTextConverter.convertPDFToText(path);
        documentText = Formatter.formatText(path);
        jsonNLUResponse = lawEntityExtractor.extractEntities("modelid", documentText); //TODO model id von config holen
        //TODO mapper aufrufen und jsonNLUResponse zu verdict objekt konvertieren

        //TODO hier kommt es drauf an ob Watson oder TU Darm. Classifier benutzt wird



        String jsonClassifierResponse = "";

        //








        return verdict;
    }

}
