package com.unihh.lawstats.bootstrap.Converter.Formatting;

import com.unihh.lawstats.bootstrap.Converter.PDFToTextConverter;

public class RunnableFormatter implements Runnable {

    FormattingManager _formattingManager;
    int _endIndex;
    String _basePath;


    public RunnableFormatter(FormattingManager formattingManager, int endIndex, String basePath){
        _formattingManager = formattingManager;
        _endIndex = endIndex;
        _basePath = basePath;


    }

    public void run(){
        int counter = _formattingManager.getAndIncrementCounter();
        PDFToTextConverter pdfToTextConverter = new PDFToTextConverter();

        while(counter<=_endIndex){
            //pdfToTextConverter.convertPDFToText(basePath+"\\verdict"+counter+".pdf");

            Formatter.formatText(_basePath+"\\verdict"+counter+"_CLEAN.txt", _basePath+"\\verdict"+counter+"_cleanNeu.txt");
            counter = _formattingManager.getAndIncrementCounter();
        }
    }
}
