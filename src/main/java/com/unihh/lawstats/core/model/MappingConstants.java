package com.unihh.lawstats.core.model;

public enum MappingConstants {
    VerdictDocketNumberNotFound("404 NOT FOUND");

    private final String value;

    MappingConstants(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
