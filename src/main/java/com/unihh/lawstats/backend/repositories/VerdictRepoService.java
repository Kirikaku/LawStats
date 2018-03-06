package com.unihh.lawstats.backend.repositories;

import com.unihh.lawstats.backend.repositories.SearchFormatter;
import com.unihh.lawstats.backend.repositories.VerdictRepository;
import com.unihh.lawstats.core.model.Verdict;
import com.unihh.lawstats.core.model.attributes.DataModelAttributes;
import com.unihh.lawstats.core.model.input.DateInput;
import com.unihh.lawstats.core.model.input.Input;
import com.unihh.lawstats.core.model.input.StringInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.*;

@Service(value = "verdictRepoService")
public class VerdictRepoService {

    private VerdictRepository verdictRepository;

    @Autowired
    public VerdictRepoService(VerdictRepository verdictRepository){
        this.verdictRepository = verdictRepository;
    }

    public Collection<? extends Verdict> getVerdictsForAttribute(DataModelAttributes key, Set<Input> value) {
        SearchFormatter searchFormatter = new SearchFormatter();
        Set<Verdict> verdictSetForAttribute = new HashSet<>();
        for (Input input : value) {
            switch (key) {
                case DocketNumber:
                    verdictSetForAttribute.addAll(verdictRepository.findAllByDocketNumberStartingWith(searchFormatter.formatString(((StringInput) input).getValue())));
                    break;
                case Senate:
                    verdictSetForAttribute.addAll(verdictRepository.findAllBySenateContaining((searchFormatter.formatString(((StringInput) input).getValue()))[0]));
                    break;
                case Judges:
                    verdictSetForAttribute.addAll(verdictRepository.findByJudgeListContainingIgnoreCase((searchFormatter.formatString(((StringInput) input).getValue()))[0]));
                    break;
                case DateVerdict:
                    DateInput dateInput = (DateInput) input;
                    verdictSetForAttribute.addAll(verdictRepository.findAllByDateVerdictBetween(dateInput.getStart(), dateInput.getEnd()));
                    break;
                case ForeDecisionRACCourt:
                    verdictSetForAttribute.addAll(verdictRepository.findAllByForeDecisionRACCourtStartingWith((searchFormatter.formatString(((StringInput) input).getValue()))));
                    break;
                case ForeDecisionRACDateVerdict:
                    DateInput dateInputRAC = (DateInput) input;
                    verdictSetForAttribute.addAll(verdictRepository.findAllByForeDecisionRACVerdictDateBetween(dateInputRAC.getStart(), dateInputRAC.getEnd()));
                    break;
                case ForeDecisionRCCourt:
                    verdictSetForAttribute.addAll(verdictRepository.findAllByForeDecisionRCCourtStartingWith((searchFormatter.formatString(((StringInput) input).getValue()))));
                    break;
                case ForeDecisionRCDateVerdict:
                    DateInput dateInputRC = (DateInput) input;
                    verdictSetForAttribute.addAll(verdictRepository.findAllByForeDecisionRCVerdictDateBetween(dateInputRC.getStart(), dateInputRC.getEnd()));
                    break;
                case ForeDecisionDCCourt:
                    verdictSetForAttribute.addAll(verdictRepository.findAllByForeDecisionDCCourtStartingWith((searchFormatter.formatString(((StringInput) input).getValue()))));
                    break;
                case ForeDecisionDCDateVerdict:
                    DateInput dateInputDC = (DateInput) input;
                    verdictSetForAttribute.addAll(verdictRepository.findAllByForeDecisionDCVerdictDateBetween(dateInputDC.getStart(), dateInputDC.getEnd()));
                    break;
                default:
                    return Collections.emptyList();
            }
        }
        return verdictSetForAttribute;
    }

    public void save(Verdict verdict) {
        if (verdict != null) {
            SearchFormatter searchFormatter = new SearchFormatter();
            List<Verdict> verdicts = verdictRepository.findAllByDocketNumberStartingWith(searchFormatter.formatString(verdict.getDocketNumber()));
            if (!verdicts.isEmpty()) {
                verdictRepository.delete(verdict.getDocketNumber());
            }
            verdictRepository.save(verdict);
        }
    }
}
