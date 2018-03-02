package com.unihh.lawstats.backend.controller;

import com.unihh.lawstats.core.model.SearchVerdict;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

@Controller
@Service("ListController")
public class ListController {

    List<SearchVerdict> searchedVerdicts = new ArrayList<>();

    @Autowired
    FilterController filterController;


    @GetMapping(value = "/listVerdicts")
    public String listVerdicts(Model model) {

        searchedVerdicts = filterController.getSearchVerdictList();

        model.addAttribute("verdicts", searchedVerdicts);

        return "verdictListTable";
    }

    @RequestMapping(value = "/filter/searchVerdict/{id}/{revisionSuccess}")
    public String getList(Model model, @PathVariable int id, @PathVariable int revisionSuccess){
        searchedVerdicts.add(filterController.getSearchVerdictForID(id));
        return "/";
    }

}
