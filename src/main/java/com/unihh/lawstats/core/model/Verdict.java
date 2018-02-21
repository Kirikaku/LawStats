package com.unihh.lawstats.core.model;

import java.util.Date;
import java.util.List;

/**
 * This class represents one document with the extracted date from watson
 */
public class Verdict {

    private String docketNumber;
    private int revisionSuccess;
    private String senate;
    private String[] judgeList;
    private String dateVerdict;
    //Oberlandesgericht
    private String foreDecisionRACCourt;
    private String foreDecisionRACVerdictDate;
    //Landesgericht
    private String foreDecisionRCCourt;
    private String foreDecisionRCVerdictDate;
    //Amtsgericht
    private String foreDecisionDCCourt;
    private String foreDecisionDCVerdictDate;

}
