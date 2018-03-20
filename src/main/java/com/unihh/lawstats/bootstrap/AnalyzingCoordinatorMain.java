package com.unihh.lawstats.bootstrap;

import com.unihh.lawstats.PropertyManager;
import com.unihh.lawstats.backend.repository.VerdictRepoService;
import com.unihh.lawstats.backend.repository.VerdictRepository;
import com.unihh.lawstats.core.mapping.NoDocketnumberFoundException;
import com.unihh.lawstats.core.model.Verdict;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import uhh_lt.ABSA.ABSentiment.AbSentiment;

import java.io.File;
import java.util.Arrays;

@Slf4j
public class AnalyzingCoordinatorMain {

    public static void main(String[] args) {
        //necessary for solr
        ApplicationContext context = new ClassPathXmlApplicationContext("beansDefinitionForBootstrapPhase.xml");
        VerdictRepoService verdictRepoService = new VerdictRepoService(context.getBean("verdictRepository", VerdictRepository.class));


        AbSentiment abSentiment = new AbSentiment(PropertyManager.getLawProperty(PropertyManager.ABS_CONFIGURATION));
        AnalyzingCoordinator analyzingCoordinator = new AnalyzingCoordinator(abSentiment);


        File file = new File(args[0]);


        File[] listOfFiles = file.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].getName().endsWith(".pdf")) {
                try {
                    Verdict verdict = analyzingCoordinator.analyzeDocument(listOfFiles[i], false);
                    if (verdict != null) {
                        verdictRepoService.save(verdict);
                    }
                } catch (NoDocketnumberFoundException ex) {
                    ex.printStackTrace();
                }
            }
        }

    }
}




