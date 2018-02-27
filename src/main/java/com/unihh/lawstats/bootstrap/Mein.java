package com.unihh.lawstats.bootstrap;

import com.unihh.lawstats.backend.repositories.VerdictRepository;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


// This is the code snippet for getting the verdict repo in the bootstrapping phse
public class Mein {

    public void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("beansDefinitionForBootstrapPhase.xml");
        VerdictRepository verdictRepository = context.getBean("verdictRepository", VerdictRepository.class);


        System.out.println("Test");
    }
}
