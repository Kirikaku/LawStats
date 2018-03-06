package com.unihh.lawstats.backend.service;

import com.unihh.lawstats.backend.repository.VerdictRepoService;
import com.unihh.lawstats.bootstrap.AnalyzingCoordinator;
import com.unihh.lawstats.core.mapping.NoDocketnumberFoundException;
import com.unihh.lawstats.core.model.Verdict;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Observable;
import java.util.concurrent.atomic.AtomicReference;

import static java.lang.Thread.sleep;

/**
 * This service is the file process service for a uploaded file
 */
@Service
public class FileProcessService extends Observable {


    @Autowired
    VerdictRepoService verdictRepoService;
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
        AnalyzingCoordinator coordinator = new AnalyzingCoordinator();
        Runnable thread = () -> {
            try {
                verdict.set(coordinator.analyzeDocument(workfile));
                verdictRepoService.save(verdict.get()); // only safe, when successfully analyze
            } catch (NoDocketnumberFoundException ex) {
                verdict.set(null);
            }
            fileAnalyzed = true;
        };
        thread.run();

        int counter = 0;
        while (counter < 60 && !fileAnalyzed) {
            try {
                sleep(1000);
                counter++;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        if (fileAnalyzed) {
            return verdict.get();
        } else {
            return null;
        }
    }


    public boolean checkPDF() {
        return workfile.toString().contains(".pdf");
    }
}
