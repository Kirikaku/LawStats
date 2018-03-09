package com.unihh.lawstats.backend.service;

import org.springframework.stereotype.Service;
import uhh_lt.ABSA.ABSentiment.AbSentiment;

@Service
public class AbSentimentService {

    private AbSentiment abSentimentService;

    public AbSentimentService(){
        this.abSentimentService = new AbSentiment("/config/ABSConfiguration.txt");
    }

    public AbSentiment getAbSentimentService() {
        return abSentimentService;
    }
}
