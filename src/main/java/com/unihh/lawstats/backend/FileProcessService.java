package com.unihh.lawstats.backend;

import com.unihh.lawstats.backend.repositories.VerdictRepository;
import com.unihh.lawstats.core.mapping.Mapper;
import com.unihh.lawstats.core.model.Verdict;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class FileProcessService extends Thread {

    @Autowired
    VerdictRepository verdictRepository;

    private File workfile;
    //private Verdict object;


    public FileProcessService() {
    }

    public void setFile(File file){
        workfile = file;
    }

    public void run() {
        //TODO wait for methods
        // Methods handle, converting, Watson communication and Mapping

        //verdictRepository.save(object);
    }

    public boolean checkPDF() {
        return workfile.toString().contains(".pdf");
    }
}
