package com.unihh.lawstats.core.model;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.SolrDocument;

import java.util.Set;

/**
 * This class represents one document with the extracted date from watson
 */
@SolrDocument(solrCoreName = "verdict")
public class Verdict {

    @Field
    @Id
    private String docketNumber;
    @Field
    private int revisionSuccess;
    @Field
    private String senate;
    @Field

    private String[] judgeList;
    @Field

    private Set<String> judgeSet;

    private String dateVerdict;
    //Oberlandesgericht
    @Field
    private String foreDecisionRACCourt;
    @Field
    private String foreDecisionRACVerdictDate;
    //Landesgericht
    @Field
    private String foreDecisionRCCourt;
    @Field
    private String foreDecisionRCVerdictDate;
    //Amtsgericht
    @Field
    private String foreDecisionDCCourt;
    @Field
    private String foreDecisionDCVerdictDate;

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

    public String[] getJudgeList() {
        return judgeList;
    }

    public void setJudgeList(String[] judgeList) {
        this.judgeList = judgeList;
    }

    public String getDateVerdict() {
        return dateVerdict;
    }

    public void setDateVerdict(String dateVerdict) {
        this.dateVerdict = dateVerdict;
    }

    public String getForeDecisionRACCourt() {
        return foreDecisionRACCourt;
    }

    public void setForeDecisionRACCourt(String foreDecisionRACCourt) {
        this.foreDecisionRACCourt = foreDecisionRACCourt;
    }

    public String getForeDecisionRACVerdictDate() {
        return foreDecisionRACVerdictDate;
    }

    public void setForeDecisionRACVerdictDate(String foreDecisionRACVerdictDate) {
        this.foreDecisionRACVerdictDate = foreDecisionRACVerdictDate;
    }

    public String getForeDecisionRCCourt() {
        return foreDecisionRCCourt;
    }

    public void setForeDecisionRCCourt(String foreDecisionRCCourt) {
        this.foreDecisionRCCourt = foreDecisionRCCourt;
    }

    public String getForeDecisionRCVerdictDate() {
        return foreDecisionRCVerdictDate;
    }

    public void setForeDecisionRCVerdictDate(String foreDecisionRCVerdictDate) {
        this.foreDecisionRCVerdictDate = foreDecisionRCVerdictDate;
    }

    public String getForeDecisionDCCourt() {
        return foreDecisionDCCourt;
    }

    public void setForeDecisionDCCourt(String foreDecisionDCCourt) {
        this.foreDecisionDCCourt = foreDecisionDCCourt;
    }

    public String getForeDecisionDCVerdictDate() {
        return foreDecisionDCVerdictDate;
    }

    public void setForeDecisionDCVerdictDate(String foreDecisionDCVerdictDate) {
        this.foreDecisionDCVerdictDate = foreDecisionDCVerdictDate;
    }

    public Set<String> getJudgeSet() {
        return judgeSet;
    }

    public void setJudgeSet(Set<String> judgeSet) {
        this.judgeSet = judgeSet;
    }
}
