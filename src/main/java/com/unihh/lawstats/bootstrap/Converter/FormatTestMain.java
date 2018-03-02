package com.unihh.lawstats.bootstrap.Converter;

import uhh_lt.ABSA.ABSentiment.AbSentiment;
import uhh_lt.ABSA.ABSentiment.type.Result;

public class FormatTestMain {

    public static void main(String[] args){
       FormattingManager formattingManager = new FormattingManager();
       //formattingManager.formatMultithread(1, 17756, 50000);
        //formattingManager.formatMultithread(1, 40000, 60000);
        formattingManager.formatMultithread(10, 17807, 80973, "C:\\Users\\Phillip\\Documents\\verdictsSelected");



    }


}
