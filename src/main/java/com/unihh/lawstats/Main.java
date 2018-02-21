package com.unihh.lawstats;

import com.unihh.lawstats.bootstrap.Converter.PDFToTextConverter;
import com.unihh.lawstats.bootstrap.VerdictDownloader;

public class Main {

    public static void main(String[] args){
        VerdictDownloader verdictDownloader = new VerdictDownloader();
        PDFToTextConverter pdfToTextConverter = new PDFToTextConverter();
        int startIndex = 17756;
        int endIndex = 25000;
        try {

            for(int i = startIndex; i <= endIndex; i++){
                String path = "C:\\Users\\Phillip\\Documents\\verdicts\\verdict"+i+".pdf";
                pdfToTextConverter.convertPDFToText(path);
            }

            //verdictDownloader.downloadVerdicts();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
