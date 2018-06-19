package com.unihh.lawstats.naturalLawLanguageProcessing.MainMethods;

import com.unihh.lawstats.naturalLawLanguageProcessing.Preprocessing.RandomDocumentSelector;

/**
 * @author Phillip
 */
public class RandomSelectorMain {

    /**
     * Selects the specified number of documents randomly.
     * @param args
     */
    public static void main(String[] args){
        int numberOfDocuments = 2000;
        int startIndex = 20000;
        int endIndex = 50000;

        RandomDocumentSelector randomDocumentSelector = new RandomDocumentSelector();
        randomDocumentSelector.selectRandomDocuments(numberOfDocuments, startIndex, endIndex);
    }

}
