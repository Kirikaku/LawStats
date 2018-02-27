package com.unihh.lawstats.core.model;

public enum TableAttributes implements Attributes {

    RevisionSuccess("Revision erfolgreich");

    private final String displayName;

    TableAttributes(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }
}
