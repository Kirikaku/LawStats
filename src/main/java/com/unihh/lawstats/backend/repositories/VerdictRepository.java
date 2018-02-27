package com.unihh.lawstats.backend.repositories;

import com.unihh.lawstats.core.model.Verdict;
import org.springframework.data.solr.repository.SolrCrudRepository;

import java.util.Date;
import java.util.List;

public interface VerdictRepository extends SolrCrudRepository<Verdict, String> {

    List<Verdict> findAllByDocketNumber(List<String> docketNumberList);

    List<Verdict> findAllByRevisionSuccess(int revisionSuccess);

    List<Verdict> findAllBySenate(List<String> senate);

    List<Verdict> findAllByJudgeList(List<String> judges);

    List<Verdict> findAllByDateVerdictBetween(Date date1, Date date2);

    List<Verdict> findAllByForeDecisionRACCourt(List<String> court);

    List<Verdict> findAllByForeDecisionRACVerdictDateBetween(Date date1, Date date2);

    List<Verdict> findAllByForeDecisionRCCourt(List<String> court);

    List<Verdict> findAllByForeDecisionRCVerdictDateBetween(Date date1, Date date2);

    List<Verdict> findAllByForeDecisionDCCourt(List<String> court);

    List<Verdict> findAllByForeDecisionDCVerdictDate(Date date1, Date date2);
}