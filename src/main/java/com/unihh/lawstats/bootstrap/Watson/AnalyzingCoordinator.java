package com.unihh.lawstats.bootstrap.Watson;

import com.unihh.lawstats.bootstrap.Converter.ConverterMain;
import com.unihh.lawstats.bootstrap.Converter.PDFToTextConverter;
import com.unihh.lawstats.core.model.Verdict;

import java.io.File;
import java.util.Date;

public class AnalyzingCoordinator {


    public Verdict analyzeDocument(File fileToAnalyze){
        Verdict verdict = new Verdict();
        verdict.setForeDecisionDCCourt("Landgericht Erfurt");
        verdict.setDocketNumber("12345");
        verdict.setForeDecisionRCCourt("Oberlandesgericht Ulm");
        verdict.setDateVerdict(new Date(2015, 04, 12));

        PDFToTextConverter pdfToTextConverter = new PDFToTextConverter();
       // ConverterMain

        String path = fileToAnalyze.getPath();






        return verdict;
    }

}
