package com.unihh.lawstats.backend;

import com.unihh.lawstats.backend.repositories.VerdictRepository;
import com.unihh.lawstats.bootstrap.Watson.AnalyzingCoordinator;
import com.unihh.lawstats.core.mapping.Mapper;
import com.unihh.lawstats.core.model.Verdict;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class FileProcessService extends Thread {


    private AnalyzingCoordinator analyzingCoordinator = new AnalyzingCoordinator();
/*
    public void start() {
        Runnable thread = () -> {
            verdictRepository.save(analyzingCoordinator.analyzeDocument(workfile));
        };
        thread.run();
    }
*/

    @Autowired
    VerdictRepository verdictRepository;

    @Autowired
    ApplicationContext applicationContext;

    private File workfile;
    //private Verdict object;


    public FileProcessService() {
    }

    public void setFile(File file) {
        workfile = file;
    }


    @Override
    public void run() {
        AnalyzingCoordinator analyzingCoordinator = new AnalyzingCoordinator();
        Verdict object = analyzingCoordinator.analyzeDocument(workfile);
        verdictRepository.save(object);
        //für debugger
        System.out.println("");
    }


    public boolean checkPDF() {
        return workfile.toString().contains(".pdf");
    }
}
