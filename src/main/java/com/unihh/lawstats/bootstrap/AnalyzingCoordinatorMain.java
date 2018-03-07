package com.unihh.lawstats.bootstrap;

import com.unihh.lawstats.backend.repository.VerdictRepoService;
import com.unihh.lawstats.backend.repository.VerdictRepository;
import com.unihh.lawstats.bootstrap.AnalyzingCoordinator;
import com.unihh.lawstats.core.mapping.NoDocketnumberFoundException;
import com.unihh.lawstats.core.model.Verdict;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;
import java.util.Map;

public class AnalyzingCoordinatorMain {



    public static void main(String[] args){
        ApplicationContext context = new ClassPathXmlApplicationContext("beansDefinitionForBootstrapPhase.xml");
        VerdictRepoService verdictRepoService = new VerdictRepoService(context.getBean("verdictRepository", VerdictRepository.class));

        AnalyzingCoordinator analyzingCoordinator = new AnalyzingCoordinator();

        Verdict verdict = new Verdict();
        try {
            verdict = analyzingCoordinator.analyzeDocument(new File("C:\\Users\\Phillip\\Documents\\Studium\\Praktikum Sprachtechnologie\\Firsttest\\verdict70001.pdf"));
            verdictRepoService.save(verdict);
        } catch (NoDocketnumberFoundException ex){
            ex.printStackTrace();
        }

        System.out.println(verdict);
    }
}
