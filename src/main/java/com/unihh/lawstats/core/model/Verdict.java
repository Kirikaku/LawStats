package com.unihh.lawstats.core.model;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;
import org.springframework.format.annotation.DateTimeFormat;

import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * This class represents one document with the extracted date from watson
 */
@SolrDocument(solrCoreName = "verdict")
public class Verdict {

    @Field("docketNumber")
    @Indexed(type = "text_general")
    @Id
    private String docketNumber;
    @Field("revisionSuccess")
    @Indexed(type = "long")
    private int revisionSuccess;
    @Field
    @Indexed(type = "text_general")
    private String senate;
    @Field
    @Indexed(type = "strings")
    private String[] judgeList;
    @Field(value = "dateVerdict")
    @Indexed(type = "long")
    private Long dateVerdict;
    //Oberlandesgericht
    @Field
    @Indexed(type = "text_general")
    private String foreDecisionRACCourt;
    @Field(value = "foreDecisionRACVerdictDate")
    @Indexed(type = "long")
    private Long foreDecisionRACVerdictDate;
    //Landesgericht
    @Field
    @Indexed(type = "text_general")
    private String foreDecisionRCCourt;
    @Field("foreDecisionRCVerdictDate")
    @Indexed(type = "long")
    private Long foreDecisionRCVerdictDate;
    //Amtsgericht
    @Field
    @Indexed(type = "text_general")
    private String foreDecisionDCCourt;
    @Field("foreDecisionDCVerdictDate")
    @Indexed(type = "long")
    private Long foreDecisionDCVerdictDate;

    @Field
    @Indexed(type = "strings")
    private String[] decisionSentences;

    @Field
    @Indexed(type = "long")
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
        if (senate != null){
            this.senate = senate;
        }
        else {
            this.senate = "";
        }
    }

    public String[] getJudgeList() {
        return judgeList;
    }

    public void setJudgeList(String[] judgeList) {
        this.judgeList = Arrays.stream(judgeList).filter(Objects::nonNull).toArray(String[]::new);
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

    public String[] getDecisionSentences() {
        return decisionSentences;
    }

    public void setDecisionSentences(String[] decisionSentences) {
        this.decisionSentences = decisionSentences;
    }

    public int getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(int documentNumber) {
        this.documentNumber = documentNumber;
    }
}
