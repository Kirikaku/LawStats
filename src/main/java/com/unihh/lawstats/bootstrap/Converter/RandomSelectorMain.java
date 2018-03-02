package com.unihh.lawstats.bootstrap.Converter;

public class RandomSelectorMain {


    public static void main(String[] args){
        RandomDocumentSelector randomDocumentSelector = new RandomDocumentSelector();
        randomDocumentSelector.selectRandomDocuments(2000, "C:\\Users\\Phillip\\Documents\\verdicts","C:\\Users\\Phillip\\Documents\\verdictsSelectedNeuFormatiert");
    }
}
