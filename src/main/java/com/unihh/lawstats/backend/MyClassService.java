package com.unihh.lawstats.backend;

import com.unihh.lawstats.backend.repositories.VerdictRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MyClassService {

    @Autowired
    VerdictRepository verdictRepository;

//    public void processFile(File file){
//        //verdictRepository.save()
//    }
}
