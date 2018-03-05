package com.unihh.lawstats.backend.repositories;

import com.unihh.lawstats.core.model.Verdict;
import org.springframework.data.solr.repository.SolrCrudRepository;

import java.util.Collection;
import java.util.Date;
import java.util.List;

public interface VerdictRepository extends SolrCrudRepository<Verdict, String> {

    List<Verdict> findAllByDocketNumberStartingWith(String[] docketNumberList);

    List<Verdict> findAllByRevisionSuccess(int revisionSuccess);

    List<Verdict> findAllBySenateContainingIgnoreCase(String senate);

    List<Verdict> findByJudgeListContainingIgnoreCase(String judges);

    List<Verdict> findAllByDateVerdictBetween(Long date1, Long date2);

    List<Verdict> findAllByForeDecisionRACCourtStartingWith(String[] court);

    List<Verdict> findAllByForeDecisionRACVerdictDateBetween(Long date1, Long date2);

    List<Verdict> findAllByForeDecisionRCCourtStartingWith(String[] court);

    List<Verdict> findAllByForeDecisionRCVerdictDateBetween(Long date1, Long date2);

    List<Verdict> findAllByForeDecisionDCCourtStartingWith(String[] court);

    List<Verdict> findAllByForeDecisionDCVerdictDate(Long date1, Long date2);
}