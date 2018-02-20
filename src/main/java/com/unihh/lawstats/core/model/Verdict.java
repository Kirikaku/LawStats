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
    private List<String> judgeList;
    private Date dateVerdict;
    //Oberlandesgericht
    private ForeDecision foreDecisionRAC;
    //Landesgericht
    private ForeDecision foreDecisionRC;
    //Amtsgericht
    private ForeDecision foreDecisionDC;

}
