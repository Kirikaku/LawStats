package com.unihh.lawstats.bootstrap.MainMethods;

import com.unihh.lawstats.bootstrap.Converter.RandomDocumentSelector;

public class RandomSelectorMain {


    public static void main(String[] args){
        RandomDocumentSelector randomDocumentSelector = new RandomDocumentSelector();   //TODO delete this
        randomDocumentSelector.selectRandomDocuments(2000, 17756, 80984);
        //TODO evtl. properties DONE
    }

}
