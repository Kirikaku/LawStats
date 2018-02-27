package com.unihh.lawstats.backend.controller;

import com.unihh.lawstats.backend.repositories.VerdictRepository;
import com.unihh.lawstats.core.model.Attributes;
import com.unihh.lawstats.core.model.Verdict;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class FilterController {

    private Map<Attributes, List<String>> selectedAttributesMap = new HashMap<Attributes, List<String>>();

    private List<Verdict> selectedVerdicts = new ArrayList<>();

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

    public List<Verdict> getQueriedVerdicts(Model model){
        selectedAttributesMap.forEach((attributes, strings) -> {

            if(1 == 1){}

        });

        return selectedVerdicts;
    }

}
