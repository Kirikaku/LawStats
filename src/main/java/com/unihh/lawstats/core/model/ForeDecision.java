package com.unihh.lawstats.core.model;

import java.util.Date;

public class ForeDecision {

    private String court;
    private Date dateVerdict;

    public String getCourt() {
        return court;
    }

    public void setCourt(String court) {
        this.court = court;
    }

    public Date getDateVerdict() {
        return dateVerdict;
    }

    public void setDateVerdict(Date dateVerdict) {
        this.dateVerdict = dateVerdict;
    }
}
