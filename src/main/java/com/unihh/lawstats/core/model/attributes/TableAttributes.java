package com.unihh.lawstats.core.model.attributes;

/**
 * This enum contains additional properties for the verdict object
 */
public enum TableAttributes implements Attributes {

    RevisionSuccess("Revision erfolgreich"),
    RevisionNotSuccess("Revision nicht erfolgreich"),
    RevisionPartlySuccess("Revision teilweise erfolgreich"),
    NOTAVAILABLE("Nicht vorhanden");

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
        return NOTAVAILABLE;
    }
}
