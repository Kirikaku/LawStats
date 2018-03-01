package com.unihh.lawstats.core.model.input;

import com.unihh.lawstats.core.model.DataModelAttributes;

public interface Input {

    DataModelAttributes getAttribute();

    void setAttribute(DataModelAttributes attribute);

    InputType getInputType();

}
