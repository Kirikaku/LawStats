package com.unihh.lawstats.bootstrap.Watson;

import com.unihh.lawstats.bootstrap.Converter.RandomDocumentSelector;

public class CounterMain {


    public static void main(String[] args){

        RandomDocumentSelector randomDocumentSelector = new RandomDocumentSelector();

        randomDocumentSelector.countColon(5000, "C:\\Users\\Phillip\\Documents\\Studium\\Praktikum Sprachtechnologie\\Google Drive\\Verdicts\\TXT");


    }
}
