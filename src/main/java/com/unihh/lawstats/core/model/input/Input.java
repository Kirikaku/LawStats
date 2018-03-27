package com.unihh.lawstats.core.model.input;

import com.unihh.lawstats.core.model.attributes.DataModelAttributes;

/**
 * An Input is combination of an attribute and a value
 */
public interface Input {

    DataModelAttributes getAttribute();

    void setAttribute(DataModelAttributes attribute);

    InputType getInputType();

}
