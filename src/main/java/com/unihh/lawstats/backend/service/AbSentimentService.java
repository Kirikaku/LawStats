package com.unihh.lawstats.backend.service;

import org.springframework.stereotype.Service;
import uhh_lt.ABSA.ABSentiment.AbSentiment;

@Service
public class AbSentimentService {

    private AbSentiment abSentimentService;

    public AbSentimentService() {

        try {
            this.abSentimentService = new AbSentiment("/config/ABSConfiguration.txt");
        } catch (Exception ex) {
            ex.printStackTrace();
            this.abSentimentService = new AbSentiment("/config/SpringABSConfiguration.txt");
        }
    }

    public AbSentiment getAbSentimentService() {
        return abSentimentService;
    }
}
