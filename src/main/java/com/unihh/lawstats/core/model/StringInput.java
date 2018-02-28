package com.unihh.lawstats.core.model;


public class StringInput implements Input {

    private Attributes attribute;
    private String value;

    public Attributes getAttribute() {
        return attribute;
    }

    public void setAttribute(Attributes attribute) {
        this.attribute = attribute;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
