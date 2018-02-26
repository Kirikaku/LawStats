package com.unihh.lawstats.backend.controller;

// just for testing purposes

import java.util.Date;

import com.unihh.lawstats.core.model.Attributes;
import org.springframework.format.annotation.DateTimeFormat;

public class Input {

    private Attributes attributes;
    private String tag;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date start;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date end;

    public Attributes getAttribute() {
        return attributes;
    }

    public void setAttribute(Attributes attributes) {
        this.attributes = attributes;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

}