package com.unihh.lawstats.bootstrap.Converter.Formatting;

import com.unihh.lawstats.bootstrap.Converter.PDFToTextConverter;

public class RunnableFormatter implements Runnable {

    FormattingManager _formattingManager;
    int _endIndex;
    String _sourceBasePath;
    String _targetBasePath;


    public RunnableFormatter(FormattingManager formattingManager, int endIndex, String sourceBasePath, String targetBasePath){
        _formattingManager = formattingManager;
        _endIndex = endIndex;
        _sourceBasePath = sourceBasePath;
        _targetBasePath = targetBasePath;

    }

    public void run(){
        int counter = _formattingManager.getAndIncrementCounter();
        PDFToTextConverter pdfToTextConverter = new PDFToTextConverter();

        while(counter<=_endIndex){
            //pdfToTextConverter.convertPDFToText(basePath+"\\verdict"+counter+".pdf");

            Formatter.formatText(_sourceBasePath+"\\verdict"+counter+".txt", _targetBasePath+"\\verdict"+counter+"_clean.txt");
            counter = _formattingManager.getAndIncrementCounter();
        }
    }
}
