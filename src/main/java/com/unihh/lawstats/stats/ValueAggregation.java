package com.unihh.lawstats.stats;

public class ValueAggregation {


    private String value;
    private int succesfullRevisions;
    private int unsuccesfullRevisions;
    private double outcomeRatio;

    public ValueAggregation(String value){
        this.value = value;
    }



    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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
