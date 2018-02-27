package com.unihh.lawstats.backend.controller;

import com.unihh.lawstats.backend.repositories.VerdictRepository;
import com.unihh.lawstats.core.model.Attributes;
import com.unihh.lawstats.core.model.DataModelAttributes;
import com.unihh.lawstats.core.model.TableAttributes;
import com.unihh.lawstats.core.model.Verdict;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.*;

@Controller
public class FilterController {

    private Map<DataModelAttributes, List<String>> selectedAttributesMap = new HashMap<DataModelAttributes, List<String>>();

    private Set<Verdict> selectedVerdicts = new HashSet<>();

    private List<Attributes> dataModelAttributesList = Collections.singletonList(TableAttributes.RevisionSuccess);

    @Autowired
    VerdictRepository verdictRepository;

    @GetMapping("/input")
    public String inputForm(Model model) {
        model.addAttribute("input", new Input());
        return "input";
    }

    @PostMapping("/input")
    public String inputSubmit(@ModelAttribute Input input) {
        return "result";
    }

    public Set<Verdict> getQueriedVerdicts() {
        // First add a list of Verdict to out Set
        Map.Entry<DataModelAttributes, List<String>> entry = selectedAttributesMap.entrySet().iterator().next();
        selectedVerdicts.addAll(getVerdictListForAttribute(entry.getKey(), entry.getValue()));

        //Second for every attrbute add the verdicts to the set and create the intersection.
        //We connect the attributes with an AND
        selectedAttributesMap.forEach((dataModelAttributes, strings) -> selectedVerdicts.retainAll(getVerdictListForAttribute(dataModelAttributes, strings)));

        return selectedVerdicts;
    }

    private Collection<? extends Verdict> getVerdictListForAttribute(DataModelAttributes key, List<String> value) {
        switch (key){
            case DocketNumber:
                return verdictRepository.findAllByDocketNumber(value);
            case Senate:
                return verdictRepository.findAllBySenate(value);
            case Judges:
                return verdictRepository.findAllByJudgeList(value);
            case ForeDecisionRACCourt:
                return verdictRepository.findAllByForeDecisionRACCourt(value);
            case ForeDecisionRCCourt:
                return verdictRepository.findAllByForeDecisionRCCourt(value);
            case ForeDecisionDCCourt:
                return verdictRepository.findAllByForeDecisionDCCourt(value);
            default:
                return Collections.emptyList();
        }
    }
}
