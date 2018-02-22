package com.unihh.lawstats.backend;

import com.unihh.lawstats.backend.repositories.VerdictRepository;
import com.unihh.lawstats.core.model.Verdict;

public class ImportTestData {

    private final VerdictRepository verdictRepository;

    public ImportTestData(VerdictRepository verdictRepository) {
        this.verdictRepository = verdictRepository;
        createTestData();
    }

    private void createTestData() {
        Verdict verdict1 = new Verdict();
        verdict1.setDocketNumber("First Aktenzeichen");
        verdict1.setRevisionSuccess(0);
        verdict1.setSenate("Senate");
        verdict1.setJudgeList(new String[]{"Mueller", "Richter"});
        verdict1.setDateVerdict("1-1-2018");
        verdict1.setForeDecisionRACCourt("OLG Hamburg");
        verdict1.setForeDecisionRACVerdictDate("04-09-2016");
        verdict1.setForeDecisionRCCourt("Landesgericht Lueneburg");
        verdict1.setForeDecisionDCVerdictDate("03-5-2015");
        verdict1.setForeDecisionDCCourt("Amtsgericht Karlsruhe");
        verdict1.setForeDecisionDCVerdictDate("1-02-2014");

        verdictRepository.save(verdict1);
    }


}
