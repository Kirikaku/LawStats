package com.unihh.lawstats.bootstrap;

import com.unihh.lawstats.bootstrap.AnalyzingCoordinator;
import com.unihh.lawstats.core.mapping.NoDocketnumberFoundException;
import com.unihh.lawstats.core.model.Verdict;

import java.io.File;
import java.util.Map;

public class AnalyzingCoordinatorMain {



    public static void main(String[] args){

        AnalyzingCoordinator analyzingCoordinator = new AnalyzingCoordinator();

        Verdict verdict = new Verdict();
        try {
            verdict = analyzingCoordinator.analyzeDocument(new File("C:\\Users\\Phillip\\Documents\\Studium\\Praktikum Sprachtechnologie\\Firsttest\\verdict70001.pdf"));
        } catch (NoDocketnumberFoundException ex){
            ex.printStackTrace();
        }

        System.out.println(verdict);
    }
}
