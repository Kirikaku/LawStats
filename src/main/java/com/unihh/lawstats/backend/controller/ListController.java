package com.unihh.lawstats.backend.controller;

import com.unihh.lawstats.core.model.SearchVerdict;
import com.unihh.lawstats.core.model.Verdict;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

@Controller
@Service("ListController")
public class ListController {

    List<Verdict> searchedVerdicts = new ArrayList<>();
    SearchVerdict searchedVerdict;

    @Autowired
    FilterController filterController;

    public List<Verdict> getSearchedVerdicts(int id, int revisionSuccess) {

        searchedVerdict = filterController.getSearchVerdictForID(id);

        switch (revisionSuccess) {
            case 0:
                return searchedVerdict.getRelatedVerdictsWithRevisionNotSuccessful();
            case 1:
                return searchedVerdict.getRelatedVerdictsWithRevisionPartlySuccessful();
            case 2:
                return searchedVerdict.getRelatedVerdictsWithRevisionSuccessful();
        }
        return null;
    }

    /**
     * This method links to the selected verdictList
     */

    @RequestMapping(value = "/filter/listVerdicts/{id}/{revisionSuccess}")
    public String getList(Model model, @PathVariable int id, @PathVariable int revisionSuccess) {

        searchedVerdicts = getSearchedVerdicts(id, revisionSuccess);
        model.addAttribute("id", id);
        model.addAttribute("revisionSuccess", revisionSuccess);
        return "verdictList";
    }

    @RequestMapping(value = "/linkVerdicts")
    public String getLink(Model model)
    {
        return "linkVerdicts";
    }

}
