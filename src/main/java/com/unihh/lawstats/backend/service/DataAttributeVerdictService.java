package com.unihh.lawstats.backend.service;

import com.unihh.lawstats.core.mapping.VerdictDateFormatter;
import com.unihh.lawstats.core.model.DataModelAttributes;
import com.unihh.lawstats.core.model.Verdict;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class DataAttributeVerdictService {

    public List<String> dataAttributeToVerdictValue (DataModelAttributes attribute, Verdict verdict){
        VerdictDateFormatter verdictDateFormatter = new VerdictDateFormatter();
        switch (attribute){
            case DocketNumber:
                return Collections.singletonList(verdict.getDocketNumber());
            case Senate:
                return Collections.singletonList(verdict.getSenate());
            case Judges:
                return Arrays.asList(verdict.getJudgeList());
            case DateVerdict:
                return Collections.singletonList(verdictDateFormatter.formatVerdictDateToString(verdict.getDateVerdict()));
            case ForeDecisionRACCourt:
                return Collections.singletonList(verdict.getForeDecisionRACCourt());
            case ForeDecisionRACDateVerdict:
                return Collections.singletonList(verdictDateFormatter.formatVerdictDateToString(verdict.getForeDecisionRACVerdictDate()));
            case ForeDecisionRCCourt:
                return Collections.singletonList(verdict.getForeDecisionRCCourt());
            case ForeDecisionRCDateVerdict:
                return Collections.singletonList(verdictDateFormatter.formatVerdictDateToString(verdict.getForeDecisionRCVerdictDate()));
            case ForeDecisionDCCourt:
                return Collections.singletonList(verdict.getForeDecisionDCCourt());
            case ForeDecisionDCDateVerdict:
                return Collections.singletonList(verdictDateFormatter.formatVerdictDateToString(verdict.getForeDecisionDCVerdictDate()));
        }
        return Collections.emptyList();
    }
}
