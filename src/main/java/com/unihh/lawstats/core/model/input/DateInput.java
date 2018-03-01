package com.unihh.lawstats.core.model.input;


import com.unihh.lawstats.backend.service.DataAttributeVerdictService;
import com.unihh.lawstats.core.model.Attributes;
import com.unihh.lawstats.core.model.DataModelAttributes;

import java.io.DataInput;

public class DateInput implements Input {

    private long start;
    private long end;
    private DataModelAttributes attribute;

    public DataModelAttributes getAttribute() {
        return attribute;
    }

    public void setAttribute(DataModelAttributes attribute) {
        this.attribute = attribute;
    }

    @Override
    public InputType getInputType() {
        return InputType.Date;
    }

    public long getStart() {
        return start;
    }

    public long getEnd() {
        if (end == 0) {
            end = Long.MAX_VALUE;
        }
        return end;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    @Override
    public int hashCode() {
        return ((Long.hashCode(start) + 31) * Long.hashCode(end) + 31);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof DateInput && ((DateInput) obj).getStart() == start && ((DateInput) obj).getEnd() == end;

    }
}





