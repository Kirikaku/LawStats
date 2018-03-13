package com.unihh.lawstats.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import uhh_lt.ABSA.ABSentiment.AbSentiment;

/**
 * This service has the abSentiment for our analyzing.
 * it save a lot of time to initialized once
 */
@Service
public class AbSentimentService {

    private AbSentiment abSentiment;

    @Autowired
    public AbSentimentService(Environment environment) {
        String isDeployModus = environment.getProperty("deploy.mode");
        if (Boolean.valueOf(isDeployModus)) {
            this.abSentiment = new AbSentiment("/config/SpringABSConfiguration.txt");
        } else {
            this.abSentiment = new AbSentiment("/config/ABSConfiguration.txt");
        }
    }

    public AbSentiment getAbSentiment() {
        return abSentiment;
    }
}
