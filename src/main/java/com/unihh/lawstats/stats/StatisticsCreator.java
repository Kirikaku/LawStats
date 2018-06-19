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
        System.out.println("Huhu");
        SolrResultPage resultsPage = (SolrResultPage) verdictRepoService.findAll().iterator().next();
        _allVerdicts = resultsPage.getContent();
        System.out.println("Hallo");
    }



    private List<CourtAggregation> aggregateRevisionOutcome(){
        Map<String, CourtAggregation> aggrValues = new HashMap<>();

        for(Verdict verdict: _allVerdicts){
            String court = verdict.getForeDecisionDCCourt();

            if(court != null && !court.equals("")){
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

        return new ArrayList<CourtAggregation>(aggrValues.values());
    }


    public CourtStatistic createCourtStatistic(){
        List<CourtAggregation> aggregatedCourts = aggregateRevisionOutcome();
        int counter = 0;
        CourtStatistic courtStatistic = new CourtStatistic(aggregatedCourts.size());
        String[] labels = courtStatistic.getLabels();
        int[] succesfullRevisions = courtStatistic.getSuccesfullRevisions();
        int[] unsuccesfullRevisions = courtStatistic.getUnsuccesfullRevisions();


        for(CourtAggregation courtAggr: aggregatedCourts){
            labels[counter] = courtAggr.getCourt();
            succesfullRevisions[counter] = courtAggr.getSuccesfullRevisions();
            unsuccesfullRevisions[counter] = courtAggr.getUnsuccesfullRevisions();

            counter++;
        }

        return courtStatistic;
    }



}
