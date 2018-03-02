package com.unihh.lawstats.bootstrap.Watson;

import com.unihh.lawstats.core.model.Verdict;

import java.io.File;
import java.util.Map;

public class TestMain {



    public static void main(String[] args){
        WatsonLawUtilities watsonLawUtilities = new WatsonLawUtilities();
        Map<String, String> sentenceMap;

        AnalyzingCoordinator analyzingCoordinator = new AnalyzingCoordinator();

        Verdict verdict = analyzingCoordinator.analyzeDocument(new File("C:\\Users\\Phillip\\Documents\\Studium\\Praktikum Sprachtechnologie\\Firsttest\\verdict70001.pdf"));
        //Verdict verdict = analyzingCoordinator.analyzeDocument(new File("C:\\Users\\lenna\\Documents\\UNI\\3. Semester\\[SEP] Softwareentwicklungspraktikum\\Testdaten\\verdict70117.pdf"));

        System.out.println(verdict);


        /*
        DownloadManager downloadManager = new DownloadManager();
        downloadManager.downloadMultithread(10, 52566, 70000);
        */


    }





}
