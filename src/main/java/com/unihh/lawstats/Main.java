package com.unihh.lawstats;

import com.unihh.lawstats.bootstrap.Converter.PDFToTextConverter;
import com.unihh.lawstats.bootstrap.VerdictDownloader;

public class Main {

    public static void main(String[] args){
        VerdictDownloader verdictDownloader = new VerdictDownloader();
        PDFToTextConverter pdfToTextConverter = new PDFToTextConverter();
        try {
            pdfToTextConverter.convertPDFToText("C:\\Users\\Phillip\\Documents\\verdicts\\verdict17785.pdf");
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
