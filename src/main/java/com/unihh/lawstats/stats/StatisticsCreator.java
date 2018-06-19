package com.unihh.lawstats.stats;

import com.unihh.lawstats.backend.repository.VerdictRepoService;
import com.unihh.lawstats.core.model.Verdict;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.query.result.SolrResultPage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticsCreator {



    private List<Verdict> _allVerdicts;

    public StatisticsCreator(VerdictRepoService verdictRepoService){

        SolrResultPage resultsPage = (SolrResultPage) verdictRepoService.findAll().iterator().next();
        _allVerdicts = resultsPage.getContent();

    }



    private List<CourtAggregation> aggregateRevisionOutcome(){
        Map<String, CourtAggregation> aggrValues = new HashMap<>();

        for(Verdict verdict: _allVerdicts){
            String court = verdict.getForeDecisionRACCourt();

            boolean isVerdictRelevant = court != null && !court.equals("") &&
                    (court.contains("olg") || court.contains("oberlandesgericht") || court.contains("oberlandesgerichts"));

            if(isVerdictRelevant){

                if(court.contains("oberlandesgericht")){
                    court = court.replace("oberlandesgericht", "olg");
                }
                else if(court.contains("oberlandesgerichts")){
                    court = court.replace("oberlandesgerichts", "olg");
                }


                CourtAggregation courtAggr = aggrValues.get(court);

                if(courtAggr == null){
                    courtAggr = new CourtAggregation(court);
                    aggrValues.put(court, courtAggr);
                }


                if(verdict.getRevisionSuccess() == 1){
                    courtAggr.incrementSuccesfullRevisions();
                }
                else if(verdict.getRevisionSuccess() == -1){
                    courtAggr.incrementUnsuccesfullRevisions();
                }
            }
        }

        List<CourtAggregation> courtAggregations = new ArrayList<CourtAggregation>(aggrValues.values());

        for(CourtAggregation courtAggregation: courtAggregations){
            if(courtAggregation.getUnsuccesfullRevisions() != 0){
                double unsuccesfullRevisions = (double) courtAggregation.getUnsuccesfullRevisions();
                double ratio = courtAggregation.getSuccesfullRevisions()/unsuccesfullRevisions;
                courtAggregation.setOutcomeRatio(ratio);
            }
            else{
                courtAggregation.setOutcomeRatio(courtAggregation.getSuccesfullRevisions());
            }
        }

        return courtAggregations;
    }


    public CourtStatistic createCourtStatistic(){
        List<CourtAggregation> aggregatedCourts = aggregateRevisionOutcome();
        int counter = 0;
        CourtStatistic courtStatistic = new CourtStatistic(aggregatedCourts.size());
        String[] labels = courtStatistic.getLabels();
        int[] succesfullRevisions = courtStatistic.getSuccesfullRevisions();
        int[] unsuccesfullRevisions = courtStatistic.getUnsuccesfullRevisions();
        double[] outcomeRatio = courtStatistic.getOutcomeRatio();


        for(CourtAggregation courtAggr: aggregatedCourts){
            labels[counter] = courtAggr.getCourt();
            succesfullRevisions[counter] = courtAggr.getSuccesfullRevisions();
            unsuccesfullRevisions[counter] = courtAggr.getUnsuccesfullRevisions();
            outcomeRatio[counter] = courtAggr.getOutcomeRatio();

            counter++;
        }

        return courtStatistic;
    }



}
