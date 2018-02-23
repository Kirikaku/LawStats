package com.unihh.lawstats.core.model;

import java.util.Date;
import java.util.Set;

/**
 * This class represents one document with the extracted date from watson
 */
public class Verdict {

    private String docketNumber;
    private int revisionSuccess;
    private String senate;
    private Set<String> judgeSet;
    // TODO update Date Type
    private Date dateVerdict;
    //Oberlandesgericht
    private ForeDecision foreDecisionRAC;
    //Landesgericht
    private ForeDecision foreDecisionRC;
    //Amtsgericht
    private ForeDecision foreDecisionDC;



    public String getDocketNumber() {
        return docketNumber;
    }

    public void setDocketNumber(String docketNumber) {
        this.docketNumber = docketNumber;
    }

    public int getRevisionSuccess() {
        return revisionSuccess;
    }

    public void setRevisionSuccess(int revisionSuccess) {
        this.revisionSuccess = revisionSuccess;
    }

    public String getSenate() {
        return senate;
    }

    public void setSenate(String senate) {
        this.senate = senate;
    }

    public Set<String> getJudgeSet() {
        return judgeSet;
    }

    public void setJudgeSet(Set<String> judgeSet) {
        this.judgeSet = judgeSet;
    }

    public Date getDateVerdict() {
        return dateVerdict;
    }

    public void setDateVerdict(Date dateVerdict) {
        this.dateVerdict = dateVerdict;
    }

    public ForeDecision getForeDecisionRAC() {
        return foreDecisionRAC;
    }

    public void setForeDecisionRAC(ForeDecision foreDecisionRAC) {
        this.foreDecisionRAC = foreDecisionRAC;
    }

    public ForeDecision getForeDecisionRC() {
        return foreDecisionRC;
    }

    public void setForeDecisionRC(ForeDecision foreDecisionRC) {
        this.foreDecisionRC = foreDecisionRC;
    }

    public ForeDecision getForeDecisionDC() {
        return foreDecisionDC;
    }

    public void setForeDecisionDC(ForeDecision foreDecisionDC) {
        this.foreDecisionDC = foreDecisionDC;
    }
}
