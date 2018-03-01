package com.unihh.lawstats.core.model.input;


import com.unihh.lawstats.core.model.DataModelAttributes;

public class StringInput implements Input {

    private DataModelAttributes attribute;
    private String value;

    public DataModelAttributes getAttribute() {
        return attribute;
    }

    public void setAttribute(DataModelAttributes attribute) {
        this.attribute = attribute;
    }

    @Override
    public InputType getInputType() {
        return InputType.String;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        return value.hashCode() + 31 * attribute.hashCode() + 31;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof StringInput && ((StringInput) obj).getValue().equalsIgnoreCase(value) && ((StringInput) obj).getAttribute().equals(attribute);
    }
}
