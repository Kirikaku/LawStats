package com.unihh.lawstats.stats;

public class CourtStatistic {

    private String[] labels;
    private int[] succesfullRevisions;
    private int[] unsuccesfullRevisions;


    public CourtStatistic(int arraySize){
        labels = new String[arraySize];
        succesfullRevisions = new int[arraySize];
        unsuccesfullRevisions = new int[arraySize];
    }

    public CourtStatistic(){}





    public String[] getLabels() {
        return labels;
    }

    public void setLabels(String[] labels) {
        this.labels = labels;
    }

    public int[] getSuccesfullRevisions() {
        return succesfullRevisions;
    }

    public void setSuccesfullRevisions(int[] succesfullRevisions) {
        this.succesfullRevisions = succesfullRevisions;
    }

    public int[] getUnsuccesfullRevisions() {
        return unsuccesfullRevisions;
    }

    public void setUnsuccesfullRevisions(int[] unsuccesfullRevisions) {
        this.unsuccesfullRevisions = unsuccesfullRevisions;
    }


}
