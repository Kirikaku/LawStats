package com.unihh.lawstats.core.model;

/**
 * This enum contains all searchable properties in solr for the verdict object
 */
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

    public static DataModelAttributes valueOfDisplayName(String displayName) {
        for(DataModelAttributes attributes : DataModelAttributes.values()){
            if(attributes.displayName.equals(displayName)){
                return attributes;
            }
        }
        return null;
    }
}
