package com.unihh.lawstats.backend.controller;

import com.unihh.lawstats.backend.repositories.VerdictRepository;
import com.unihh.lawstats.core.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.unihh.lawstats.core.model.DataModelAttributes.*;

/*
@Controller
public class FilterController {

    private Map<DataModelAttributes, List<Input>> selectedAttributesMap = new HashMap<>();

    private Set<Verdict> selectedVerdicts = new HashSet<>();

    private ArrayList<Input> inputList = new ArrayList<>();

    private List<String> dataModelAttributesList = Collections.singletonList(TableAttributes.RevisionSuccess.getDisplayName());

    @Autowired
    VerdictRepository verdictRepository;

    @GetMapping("/input")
    public String inputForm(Model model) {
        if (input.getAttribute() == DateVerdict | ForeDecisionRACDateVerdict |  ForeDecisionRCDateVerdict | ForeDecisionDCDateVerdict )
        {
            model.addAttribute("input", new DateInput());
        }
        else
            model.addAttribute("input", new StringInput());
            {
        }
        return "input";
    }

    @RequestMapping(value = "/input", method = RequestMethod.POST)
    public String submit(@ModelAttribute("input") final Input input, final ModelMap model) {
        model.addAttribute("attribute", input.getAttribute());
        if (input instanceof DateInput) {
            model.addAttribute("start", input.getStart());
            model.addAttribute("end", input.getEnd());
        }
        else {
            model.addAttribute("value", input.getValue());
        }
        inputList.add(input);
        selectedAttributesMap.put(input.getAttribute(),inputList);
        return "redirect:/";
    }

    @PostMapping("/input")
    public String inputSubmit(@ModelAttribute Input input) {
        return "result";
    }

    public Set<Verdict> getQueriedVerdicts() {
        // First add a list of Verdict to out Set
        Map.Entry<DataModelAttributes, List<String>> entry = selectedAttributesMap.entrySet().iterator().next();
        selectedVerdicts.addAll(getVerdictListForAttribute(entry.getKey(), entry.getValue()));

        //Second for every attribute add the verdicts to the set and create the intersection.
        //We connect the attributes with an AND
        selectedAttributesMap.forEach((dataModelAttributes, strings) -> selectedVerdicts.retainAll(getVerdictListForAttribute(dataModelAttributes, strings)));

        return selectedVerdicts;
    }

    private Collection<? extends Verdict> getVerdictListForAttribute(DataModelAttributes key, List<String> value) {
        switch (key) {
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
*/