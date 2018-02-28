package com.unihh.lawstats.core.model;


public class DateInput implements Input {


    private long start;
    private long end;
    private Attributes attribute;

    public Attributes getAttribute() {
        return attribute;
    }

    public void setAttribute(Attributes attribute) {
        this.attribute = attribute;
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
}





