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

    List<Verdict> findAllByDateVerdictBetween(Long date1, Long date2);

    List<Verdict> findAllByForeDecisionRACCourt(List<String> court);

    List<Verdict> findAllByForeDecisionRACVerdictDateBetween(Long date1, Long date2);

    List<Verdict> findAllByForeDecisionRCCourt(List<String> court);

    List<Verdict> findAllByForeDecisionRCVerdictDateBetween(Long date1, Long date2);

    List<Verdict> findAllByForeDecisionDCCourt(List<String> court);

    List<Verdict> findAllByForeDecisionDCVerdictDate(Long date1, Long date2);
}