package com.unihh.lawstats.backend.controller;

public enum Attribute {
    SENATE("Senat"), JUDGE("Richter"), DOCKETNUMBER("Aktenverzeichnisnummer");

    private final String displayName;

    Attribute(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

