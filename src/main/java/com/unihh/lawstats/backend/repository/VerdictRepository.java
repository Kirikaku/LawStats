package com.unihh.lawstats.backend.repository;

import com.unihh.lawstats.core.model.Verdict;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface VerdictRepository extends CrudRepository<Verdict, Long>, VerdictRepositoryCustom {

    List<Verdict> findAllByDocketNumberStartingWith(String[] docketNumberList);

    void deleteById(Long id);

    List<Verdict> findAllByRevisionSuccess(int revisionSuccess);

    List<Verdict> findAllBySenateContaining(String senate);

    List<Verdict> findByJudgeListContainingIgnoreCase(String judges);

    List<Verdict> findAllByDateVerdictBetween(Long date1, Long date2);

    List<Verdict> findAllByForeDecisionRACCourtStartingWith(String[] court);

    List<Verdict> findAllByForeDecisionRACVerdictDateBetween(Long date1, Long date2);

    List<Verdict> findAllByForeDecisionRCCourtStartingWith(String[] court);

    List<Verdict> findAllByForeDecisionRCVerdictDateBetween(Long date1, Long date2);

    List<Verdict> findAllByForeDecisionDCCourtStartingWith(String[] court);

    List<Verdict> findAllByForeDecisionDCVerdictDateBetween(Long date1, Long date2);

    @Override
    List<Verdict> findAll();


}