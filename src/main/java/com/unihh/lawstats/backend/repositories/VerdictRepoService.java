package com.unihh.lawstats.backend.repositories;

import com.unihh.lawstats.core.model.*;
import com.unihh.lawstats.core.model.input.DateInput;
import com.unihh.lawstats.core.model.input.Input;
import com.unihh.lawstats.core.model.input.StringInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class VerdictRepoService {

    @Autowired
    VerdictRepository verdictRepository;

    public Collection<? extends Verdict> getVerdictsForAttribute(DataModelAttributes key, Set<Input> value) {
        Set<Verdict> verdictSetForAttribute = new HashSet<>();
        for (Input input: value) {
            switch (key) {
                case DocketNumber:
                    verdictSetForAttribute.addAll(verdictRepository.findAllByDocketNumberStartingWith(((StringInput)input).getValue().split(" ")));
                    break;
                case Senate:
                    verdictSetForAttribute.addAll(verdictRepository.findAllBySenateContainingIgnoreCase(((StringInput)input).getValue()));
                    break;
                case Judges:
                    verdictSetForAttribute.addAll(verdictRepository.findByJudgeListContainingIgnoreCase(((StringInput)input).getValue()));
                    break;
                case DateVerdict:
                    DateInput dateInput = (DateInput) input;
                    verdictSetForAttribute.addAll(verdictRepository.findAllByDateVerdictBetween(dateInput.getStart(), dateInput.getEnd()));
                    break;
                case ForeDecisionRACCourt:
                    verdictSetForAttribute.addAll(verdictRepository.findAllByForeDecisionRACCourtStartingWith(((StringInput)input).getValue().split(" ")));
                    break;
                case ForeDecisionRACDateVerdict:
                    DateInput dateInputRAC = (DateInput) input;
                    verdictSetForAttribute.addAll(verdictRepository.findAllByForeDecisionRACVerdictDateBetween(dateInputRAC.getStart(), dateInputRAC.getEnd()));
                    break;
                case ForeDecisionRCCourt:
                    verdictSetForAttribute.addAll(verdictRepository.findAllByForeDecisionRCCourtStartingWith(((StringInput)input).getValue().split(" ")));
                    break;
                case ForeDecisionRCDateVerdict:
                    DateInput dateInputRC = (DateInput) input;
                    verdictSetForAttribute.addAll(verdictRepository.findAllByForeDecisionRCVerdictDateBetween(dateInputRC.getStart(), dateInputRC.getEnd()));
                    break;
                case ForeDecisionDCCourt:
                    verdictSetForAttribute.addAll(verdictRepository.findAllByForeDecisionDCCourtStartingWith(((StringInput)input).getValue().split(" ")));
                    break;
                case ForeDecisionDCDateVerdict:
                    DateInput dateInputDC = (DateInput) input;
                    verdictSetForAttribute.addAll(verdictRepository.findAllByForeDecisionRCVerdictDateBetween(dateInputDC.getStart(), dateInputDC.getEnd()));
                    break;
                default:
                    return Collections.emptyList();
            }
        }
        return verdictSetForAttribute;
    }




}
