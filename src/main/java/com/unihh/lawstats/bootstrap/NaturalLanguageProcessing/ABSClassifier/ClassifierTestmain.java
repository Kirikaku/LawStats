package com.unihh.lawstats.bootstrap.NaturalLanguageProcessing.ABSClassifier;

import uhh_lt.ABSA.ABSentiment.AbSentiment;
import uhh_lt.ABSA.ABSentiment.type.Result;

public class ClassifierTestmain {


    public static void main(String[] args){
        AbSentiment abSentiment = new AbSentiment("src/main/resources/config/ABSConfiguration.txt");
        Result result = abSentiment.analyzeText("BUNDESGERICHTSHOF 3 StR 499/17 BESCHLUSS vom 29-11-2017 in der Strafsache gegen wegen bandenmäßiger Einfuhr von Betäubungsmitteln in nicht geringer Menge u_a_ " +

                "Der 3_ Strafsenat des Bundesgerichtshofs hat auf Antrag des Generalbundesanwalts und nach Anhörung der Beschwerdeführerin am 29-11-2017 gemäß § 349 Abs_ 2, § 354 Abs_ 1 analog StPO einstimmig beschlossen: " +
                "Die Revision der Angeklagten gegen das Urteil des Landgerichts Kleve vom 3-7-2017 wird als unbegründet verworfen; jedoch wird die Urteilsformel dahingehend geändert, dass die bei der Angeklagten sichergestellten 150 € eingezogen werden.");


        System.out.println("hi");
    }
}
