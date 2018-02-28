package com.unihh.lawstats.core.model;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.SolrDocument;
import org.springframework.format.annotation.DateTimeFormat;

import java.net.URL;
import java.util.Date;
import java.util.List;

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
    private Long dateVerdict;
    //Oberlandesgericht
    @Field
    private String foreDecisionRACCourt;
    @Field
    private Long foreDecisionRACVerdictDate;
    //Landesgericht
    @Field
    private String foreDecisionRCCourt;
    @Field
    private Long foreDecisionRCVerdictDate;
    //Amtsgericht
    @Field
    private String foreDecisionDCCourt;
    @Field
    private Long foreDecisionDCVerdictDate;

    private List<String> decisionSentences;

    private int documentNumber;

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

    public Long getDateVerdict() {
        return dateVerdict;
    }

    public void setDateVerdict(Long dateVerdict) {
        this.dateVerdict = dateVerdict;
    }

    public String getForeDecisionRACCourt() {
        return foreDecisionRACCourt;
    }

    public void setForeDecisionRACCourt(String foreDecisionRACCourt) {
        this.foreDecisionRACCourt = foreDecisionRACCourt;
    }

    public Long getForeDecisionRACVerdictDate() {
        return foreDecisionRACVerdictDate;
    }

    public void setForeDecisionRACVerdictDate(Long foreDecisionRACVerdictDate) {
        this.foreDecisionRACVerdictDate = foreDecisionRACVerdictDate;
    }

    public String getForeDecisionRCCourt() {
        return foreDecisionRCCourt;
    }

    public void setForeDecisionRCCourt(String foreDecisionRCCourt) {
        this.foreDecisionRCCourt = foreDecisionRCCourt;
    }

    public Long getForeDecisionRCVerdictDate() {
        return foreDecisionRCVerdictDate;
    }

    public void setForeDecisionRCVerdictDate(Long foreDecisionRCVerdictDate) {
        this.foreDecisionRCVerdictDate = foreDecisionRCVerdictDate;
    }

    public String getForeDecisionDCCourt() {
        return foreDecisionDCCourt;
    }

    public void setForeDecisionDCCourt(String foreDecisionDCCourt) {
        this.foreDecisionDCCourt = foreDecisionDCCourt;
    }

    public Long getForeDecisionDCVerdictDate() {
        return foreDecisionDCVerdictDate;
    }

    public void setForeDecisionDCVerdictDate(Long foreDecisionDCVerdictDate) {
        this.foreDecisionDCVerdictDate = foreDecisionDCVerdictDate;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Verdict && this.getDocketNumber().equals(((Verdict) obj).getDocketNumber());
    }

    @Override
    public int hashCode() {
        return this.getDocketNumber().hashCode();
    }

    public List<String> getDecisionSentences() {
        return decisionSentences;
    }

    public void setDecisionSentences(List<String> decisionSentences) {
        this.decisionSentences = decisionSentences;
    }

    public int getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(int documentNumber) {
        this.documentNumber = documentNumber;
    }
}
