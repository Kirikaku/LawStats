package com.unihh.lawstats.backend;

import com.unihh.lawstats.backend.repositories.VerdictRepository;
import com.unihh.lawstats.core.model.Verdict;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ImportTestData {

    private final VerdictRepository verdictRepository;

    private final List<Integer> revisionSuccessList = Arrays.asList(0,1,2);
    private final List<String> senateList = Arrays.asList("1. Senat", "2. Senat", "3. Senat", "4. Senat");
    private final List<String> judgeList = Arrays.asList("Mueller", "Koblen", "Gschwander", "Meier", "Schulz", "Lampert",
            "Kircher", "Schulz", "Schmidt", "Schneider", "Fischer", "Albrecht", "Bauer", "Baumann", "Beck");
    private final List<String> dateVerdicts = Arrays.asList("01-01-2018", "04-02-2345", "12-04-1995", "1-12-1234", "05-01-2045",
            "2-4-1254", "05-9-2012", "05-3-2005", "14-5-2001", "03-07-2008");
    private final List<String> racCourtList = Arrays.asList("OLG Hamburg", "Oberlandesgericht Lueneburg", "Oberlandegericht Bremen",
            "OLG Koblenz", "Oberlandesgericht Koeln", "Oberlandesgericht Muenchen", "OLG Wiesbaden", "");
    private final List<String> rcCourtList = Arrays.asList("LG Karlsruhe", "Landesgericht Dresden", "Landesgericht Cottbus",
            "LG Kiel", "Landesgericht Flensburg", "LG Stuttgart", "");
    private final List<String>  dcCourtList = Arrays.asList("Amtsgericht Hamburg", "AG Suelzhausen", "Amtsgericht Neukoeln",
            "AG Lummel", "Amtsgericht Bucerius", "AG Pikachu");

    public ImportTestData(VerdictRepository verdictRepository) {
        this.verdictRepository = verdictRepository;
        createTestData();
    }

    private void createTestData() {
        for(int i = 0; i < 10; i++){
            Verdict verdict = new Verdict();
            Random rand = new Random();
            verdict.setDocketNumber(String.valueOf(rand.nextInt()));
            verdict.setRevisionSuccess(revisionSuccessList.get(rand.nextInt(revisionSuccessList.size())));
            verdict.setSenate(senateList.get(rand.nextInt(senateList.size())));
            verdict.setJudgeList(createJudgeArray());
            verdict.setDateVerdict(dateVerdicts.get(rand.nextInt(dateVerdicts.size())));
            verdict.setForeDecisionRACCourt(racCourtList.get(rand.nextInt(racCourtList.size())));
            if(verdict.getForeDecisionRACCourt().isEmpty()){
                verdict.setForeDecisionRACVerdictDate("");
                verdict.setForeDecisionRCCourt("");
                verdict.setForeDecisionRCVerdictDate("");
                verdict.setForeDecisionDCCourt("");
                verdict.setForeDecisionDCVerdictDate("");
            } else {
                verdict.setForeDecisionRACVerdictDate(dateVerdicts.get(rand.nextInt(dateVerdicts.size())));
                verdict.setForeDecisionRCCourt(rcCourtList.get(rand.nextInt(rcCourtList.size())));
                if(verdict.getForeDecisionRCCourt().isEmpty()){
                    verdict.setForeDecisionRCVerdictDate("");
                    verdict.setForeDecisionDCCourt("");
                    verdict.setForeDecisionDCVerdictDate("");
                } else {
                    verdict.setForeDecisionRCVerdictDate(dateVerdicts.get(rand.nextInt(dateVerdicts.size())));
                    verdict.setForeDecisionDCCourt(dcCourtList.get(rand.nextInt(dcCourtList.size())));
                    if(verdict.getForeDecisionDCCourt().isEmpty()){
                        verdict.setForeDecisionDCVerdictDate("");
                    } else {
                        verdict.setForeDecisionDCVerdictDate(dateVerdicts.get(rand.nextInt(dateVerdicts.size())));
                    }
                }
            }
            verdictRepository.save(verdict);
        }
    }

    private String[] createJudgeArray() {
        Random rand = new Random();
        final int bound = rand.nextInt(6);
        String[] judgeArray = new String[5];
        for(int i = 0; i < bound; i++){
            judgeArray[i] = this.judgeList.get(rand.nextInt(this.judgeList.size()));
        }

        return judgeArray;
    }


}