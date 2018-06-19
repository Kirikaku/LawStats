package com.unihh.lawstats.naturalLawLanguageProcessing.MainMethods;

import com.unihh.lawstats.naturalLawLanguageProcessing.Preprocessing.Formatting.FormattingManager;

/**
 * @author Phillip
 */
public class FormattingMain {


    /**
     * Formats documents using multiple threads.
     * @param args
     */
    public static void main(String[] args){
        int numberOfThreads = 5;
        int startIndex = 20000;
        int endIndex = 50000;
        String sourceBasePath = "a path";//path to the folder where the files to format lie
        String targetBasePath =  "a target path";//path to the folder where the formatted files shall be stored

        FormattingManager formattingManager = new FormattingManager();
        formattingManager.formatMultithread(numberOfThreads, startIndex, endIndex, sourceBasePath, targetBasePath);
    }
}
