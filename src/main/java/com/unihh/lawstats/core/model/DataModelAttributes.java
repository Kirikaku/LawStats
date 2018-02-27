package com.unihh.lawstats.core.model;

public enum DataModelAttributes implements Attributes {

    DocketNumber("Aktenzeichen"),
    Senate("Senat"),
    Judges("Richter"),
    DateVerdict("Entscheidungsdatum"),
    ForeDecisionRACCourt("Oberlandesgericht"),
    ForeDecisionRCCourt("Landesgericht"),
    ForeDecisionDCCourt("Amtsgericht"),
    ForeDecisionRACDateVerdict("Entscheidungsdatum Oberlandesgericht"),
    ForeDecisionRCDateVerdict("Entscheidungsdatum Landsgericht"),
    ForeDecisionDCDateVerdict("Entscheidungsdatum Amtsgericht");

    private final String displayName;

    DataModelAttributes(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}