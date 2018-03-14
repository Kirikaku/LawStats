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


    private VerdictRepoService verdictRepoService;
    private AbSentimentService abSentimentService;

    @Autowired
    public FileProcessService(AbSentimentService abSentimentService,
                              VerdictRepoService verdictRepoService) {
        this.abSentimentService = abSentimentService;
        this.verdictRepoService = verdictRepoService;
    }

    public Verdict start(boolean isDeployMode, File workfile) {
        AtomicReference<Verdict> verdict = new AtomicReference<>();
        AnalyzingCoordinator coordinator = new AnalyzingCoordinator(abSentimentService.getAbSentiment());
        final boolean[] fileAnalyzed = {false};

        Runnable thread = () -> {
            try {
                verdict.set(coordinator.analyzeDocument(workfile, isDeployMode));
                if (verdict.get() != null) {
                    verdict.get().setDocumentNumber(0);
                    verdictRepoService.save(verdict.get()); // only safe, when successfully analyze
                }
            } catch (NoDocketnumberFoundException ex) {
                verdict.set(null);
            }
            fileAnalyzed[0] = true;
        };
        thread.run();

        int counter = 0;
        while (counter < 60 && !fileAnalyzed[0]) {
            try {
                sleep(1000);
                counter++;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        if (fileAnalyzed[0]) {
            return verdict.get();
        } else {
            return null;
        }
    }


    public boolean checkPDF(File workfile) {
        return workfile.getName().endsWith(".pdf");
    }
}
