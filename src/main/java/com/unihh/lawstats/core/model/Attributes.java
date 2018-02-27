package com.unihh.lawstats.core.model;

public enum Attributes {

    DocketNumber("Aktenzeichen"),
    Senate("Senat"),
    Judges("Richter"),
    DateVerdict("Entscheidungsdatum"),
    ForeDecisionRACCourt("Oberlandesgericht"),
    ForeDecisionRACDateVerdict("Entscheidungsdatum Oberlandesgericht"),
    ForeDecisionRCCourt("Landesgericht"),
    ForeDecisionRCDateVerdict("Entscheidungsdatum Landsgericht"),
    ForeDecisionDCCourt("Amtsgericht"),
    ForeDecisionDCDateVerdict("Entscheidungsdatum Amtsgericht");

    private final String displayName;

    Attributes(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
