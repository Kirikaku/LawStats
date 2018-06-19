package com.unihh.lawstats.stats;

public class CourtAggregation {


    private String court;
    private int succesfullRevisions;
    private int unsuccesfullRevisions;
    private double outcomeRatio;

    public CourtAggregation(String court){
        this.court = court;
    }



    public String getCourt() {
        return court;
    }

    public void setCourt(String court) {
        this.court = court;
    }

    public void incrementSuccesfullRevisions(){
        succesfullRevisions++;
    }

    public void incrementUnsuccesfullRevisions(){
        unsuccesfullRevisions++;
    }

    public int getSuccesfullRevisions() {
        return succesfullRevisions;
    }

    public void setSuccesfullRevisions(int succesfullRevisions) {
        this.succesfullRevisions = succesfullRevisions;
    }

    public int getUnsuccesfullRevisions() {
        return unsuccesfullRevisions;
    }

    public void setUnsuccesfullRevisions(int unsuccesfullRevisions) {
        this.unsuccesfullRevisions = unsuccesfullRevisions;
    }

    public double getOutcomeRatio() {
        return outcomeRatio;
    }

    public void setOutcomeRatio(double outcomeRatio) {
        this.outcomeRatio = outcomeRatio;
    }

}
