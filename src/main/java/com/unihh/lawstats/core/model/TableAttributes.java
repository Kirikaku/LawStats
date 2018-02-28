package com.unihh.lawstats.core.model;

/**
 * This enum contains additional properties for the verdict object
 */
public enum TableAttributes implements Attributes {

    RevisionSuccess("Revision erfolgreich"),
    RevisionNotSuccess("Revision nicht erfolgreich"),
    RevisionAPartOfSuccess("Revision teilweise erfolgreich");

    private final String displayName;

    TableAttributes(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    public static TableAttributes valueOfDisplayName(String displayName) {
        for(TableAttributes attributes : TableAttributes.values()){
            if(attributes.displayName.equals(displayName)){
                return attributes;
            }
        }
        return null;
    }
}
