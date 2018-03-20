package com.unihh.lawstats.bootstrap.MainMethods;

import com.unihh.lawstats.bootstrap.Converter.Formatting.FormattingManager;

public class FormattingMain {

    public static void main(String[] args){
        int numberOfThreads = 1;
        int startIndex = 20000;
        int endIndex = 50000;
        String sourceBasePath = "a path";//path to the folder where the files to format lie
        String targetBasePath =  "a target path";//path to the folder where the formatted files shall be stored

        FormattingManager formattingManager = new FormattingManager();
        formattingManager.formatMultithread(numberOfThreads, startIndex, endIndex, sourceBasePath, targetBasePath);
    }
}
