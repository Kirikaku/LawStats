package com.unihh.lawstats.core.model;

public enum Attributes {

    DocketNumber(""),
    RevisionSuccess(""),
    Senate("Senat"),
    Judges("Richter"),
    DateVerdict(""),
    ForeDecisionRACCourt(""),
    ForeDecisionRACDateVerdict(""),
    ForeDecisionRCCourt(""),
    ForeDecisionRCDateVerdict(""),
    ForeDecisionDCCourt(""),
    ForeDecisionDCDateVerdict("");

    private final String displayName;

    Attributes(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
