package com.unihh.lawstats.bootstrap.Converter.Counter;

public class CounterMain {


    public static void main(String[] args){

        ColonCounter colonCounter = new ColonCounter();
        String sourceBasePath = "C:\\Users\\Phillip\\Documents\\Studium\\Praktikum Sprachtechnologie\\Google Drive\\Verdicts\\TXT";

        colonCounter.countColon(5000, 7000, 8000, sourceBasePath);


    }
}
