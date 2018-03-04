package com.unihh.lawstats.backend;

import com.unihh.lawstats.backend.repositories.VerdictRepository;
import com.unihh.lawstats.bootstrap.AnalyzingCoordinator;
import com.unihh.lawstats.core.model.Verdict;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Observable;
import java.util.concurrent.atomic.AtomicReference;

import static java.lang.Thread.sleep;

@Service
public class FileProcessService extends Observable {


    @Autowired
    VerdictRepository verdictRepository;
    private AnalyzingCoordinator analyzingCoordinator = new AnalyzingCoordinator();
    private File workfile;
    private boolean fileAnalyzed = false;

    public FileProcessService() {
    }

    public void setFile(File file) {
        workfile = file;
    }

    public Verdict start() {
        AtomicReference<Verdict> verdict = new AtomicReference<>();
        AnalyzingCoordinator analyzingCoordinator = new AnalyzingCoordinator();
        Runnable thread = () -> {
            verdict.set(analyzingCoordinator.analyzeDocument(workfile));
            fileAnalyzed = true;
            verdictRepository.save(verdict.get());
        };
        thread.run();

        int counter = 0;
        while(counter < 60 && !fileAnalyzed){
            try {
                sleep(1000);
                counter++;
            } catch (Exception ex){
                ex.printStackTrace();
            }
        }

        if(fileAnalyzed){
            return verdict.get();
        } else {
            return null;
        }
    }


    public boolean checkPDF() {
        return workfile.toString().contains(".pdf");
    }
}
