package com.unihh.lawstats.core.model;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.SolrDocument;

import java.util.Date;
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
    private Date dateVerdict;
    //Oberlandesgericht
    @Field
    private String foreDecisionRACCourt;
    @Field
    private Date foreDecisionRACVerdictDate;
    //Landesgericht
    @Field
    private String foreDecisionRCCourt;
    @Field
    private Date foreDecisionRCVerdictDate;
    //Amtsgericht
    @Field
    private String foreDecisionDCCourt;
    @Field
    private Date foreDecisionDCVerdictDate;

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

    public Date getDateVerdict() {
        return dateVerdict;
    }

    public void setDateVerdict(Date dateVerdict) {
        this.dateVerdict = dateVerdict;
    }

    public String getForeDecisionRACCourt() {
        return foreDecisionRACCourt;
    }

    public void setForeDecisionRACCourt(String foreDecisionRACCourt) {
        this.foreDecisionRACCourt = foreDecisionRACCourt;
    }

    public Date getForeDecisionRACVerdictDate() {
        return foreDecisionRACVerdictDate;
    }

    public void setForeDecisionRACVerdictDate(Date foreDecisionRACVerdictDate) {
        this.foreDecisionRACVerdictDate = foreDecisionRACVerdictDate;
    }

    public String getForeDecisionRCCourt() {
        return foreDecisionRCCourt;
    }

    public void setForeDecisionRCCourt(String foreDecisionRCCourt) {
        this.foreDecisionRCCourt = foreDecisionRCCourt;
    }

    public Date getForeDecisionRCVerdictDate() {
        return foreDecisionRCVerdictDate;
    }

    public void setForeDecisionRCVerdictDate(Date foreDecisionRCVerdictDate) {
        this.foreDecisionRCVerdictDate = foreDecisionRCVerdictDate;
    }

    public String getForeDecisionDCCourt() {
        return foreDecisionDCCourt;
    }

    public void setForeDecisionDCCourt(String foreDecisionDCCourt) {
        this.foreDecisionDCCourt = foreDecisionDCCourt;
    }

    public Date getForeDecisionDCVerdictDate() {
        return foreDecisionDCVerdictDate;
    }

    public void setForeDecisionDCVerdictDate(Date foreDecisionDCVerdictDate) {
        this.foreDecisionDCVerdictDate = foreDecisionDCVerdictDate;
    }
}
