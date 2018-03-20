package com.unihh.lawstats.bootstrap.MainMethods;

import com.unihh.lawstats.bootstrap.Converter.RandomDocumentSelector;

public class RandomSelectorMain {


    public static void main(String[] args){
        int numberOfDocuments = 2000;
        int startIndex = 20000;
        int endIndex = 50000;

        RandomDocumentSelector randomDocumentSelector = new RandomDocumentSelector();
        randomDocumentSelector.selectRandomDocuments(numberOfDocuments, startIndex, endIndex);
    }

}
