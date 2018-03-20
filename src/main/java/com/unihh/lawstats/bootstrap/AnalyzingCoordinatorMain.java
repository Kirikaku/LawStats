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

            ApplicationContext context = new ClassPathXmlApplicationContext("beansDefinitionForBootstrapPhase.xml");
            VerdictRepoService verdictRepoService = new VerdictRepoService(context.getBean("verdictRepository", VerdictRepository.class));

            AbSentiment abSentiment = new AbSentiment(PropertyManager.getLawProperty(PropertyManager.ABS_CONFIGURATION)); //TODO properties DONE
            AnalyzingCoordinator analyzingCoordinator = new AnalyzingCoordinator(abSentiment);
            int limit = 1000; //TODO evtl. properties oder args ABGELEHNT


            if (args.length != 0) {
                File file = new File(args[0]);

                if (file.exists() && file.listFiles() != null) {

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

                } else {
                    // log.error("Dir does not exists");
                }
            } else {
                //log.error("No parameter was given, pls give me a dir to all pdfs");
            }
        }



}

