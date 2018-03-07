package com.unihh.lawstats.bootstrap;

import com.unihh.lawstats.backend.repository.VerdictRepoService;
import com.unihh.lawstats.backend.repository.VerdictRepository;
import com.unihh.lawstats.core.mapping.NoDocketnumberFoundException;
import com.unihh.lawstats.core.model.Verdict;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;
import java.util.Arrays;

@Slf4j
public class AnalyzingCoordinatorMain {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("beansDefinitionForBootstrapPhase.xml");
        VerdictRepoService verdictRepoService = new VerdictRepoService(context.getBean("verdictRepository", VerdictRepository.class));

        AnalyzingCoordinator analyzingCoordinator = new AnalyzingCoordinator();
        if (args.length != 0) {
            File file = new File(args[0]);
            if (file.exists() && file.listFiles() != null) {
                final int[] counter = {0};
                Arrays.stream(file.listFiles()).forEach(file1 -> {
                    if (file1.getName().endsWith(".pdf") && counter[0] < 1000) {
                        try {
                            Verdict verdict = analyzingCoordinator.analyzeDocument(file1);
                            if(verdict != null){
                                verdictRepoService.save(verdict);
                            }
                            counter[0]++;
                        } catch (NoDocketnumberFoundException ex) {
                            ex.printStackTrace();
                        }
                    }
                });
            } else {
               // log.error("Dir does not exists");
            }
        } else {
            //log.error("No parameter was given, pls give me a dir to all pdfs");
        }

    }
}

//        ApplicationContext context = new ClassPathXmlApplicationContext("beansDefinitionForBootstrapPhase.xml");
//        VerdictRepoService verdictRepoService = new VerdictRepoService(context.getBean("verdictRepository", VerdictRepository.class));
//
//        AnalyzingCoordinator analyzingCoordinator = new AnalyzingCoordinator();
//
//        Verdict verdict = new Verdict();
//        try {
//            verdict = analyzingCoordinator.analyzeDocument(new File("C:\\Users\\Phillip\\Documents\\Studium\\Praktikum Sprachtechnologie\\Firsttest\\verdict70001.pdf"));
//            verdictRepoService.save(verdict);
//        } catch (NoDocketnumberFoundException ex){
//            ex.printStackTrace();
//        }
//
//        System.out.println(verdict);
