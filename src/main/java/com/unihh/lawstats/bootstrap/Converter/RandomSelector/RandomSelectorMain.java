package com.unihh.lawstats.bootstrap.Converter.RandomSelector;

public class RandomSelectorMain {


    public static void main(String[] args){
        RandomDocumentSelector randomDocumentSelector = new RandomDocumentSelector();
        randomDocumentSelector.selectRandomDocuments(2000, 17756, 80984,"C:\\Users\\Phillip\\Documents\\verdicts","C:\\Users\\Phillip\\Documents\\verdictsSelectedNeuFormatiert");
    }

}
