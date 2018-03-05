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

    /**
     * This method returns a list of Verdicts for certain attributes
     *
     * @param id              Verdict ID
     * @param revisionSuccess Attribute
     * @return List of Verdicts
     */
    public List<Verdict> getSearchedVerdicts(int id, int revisionSuccess) {

        searchedVerdict = filterController.getSearchVerdictForID(id);

        switch (revisionSuccess) {
            case -1:
                return searchedVerdict.getRelatedVerdictsWithRevisionNotSuccessful();
            case 0:
                return searchedVerdict.getRelatedVerdictsWithRevisionPartlySuccessful();
            case 1:
                return searchedVerdict.getRelatedVerdictsWithRevisionSuccessful();
            default:
                return null;
        }

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

    /**
     * This method creates a link to a viable Verdictlist
     *
     * @param id        Verdict id
     * @param attribute Verdict attribute
     * @return Link to VerdictList
     */
    public String getLink(int id, String attribute) {
        int attr = 0;
        switch (attribute) {
            case "Revision erfolgreich":
                attr = 1;
                break;
            case "Revision teilweise erfolgreich":
                attr = 0;
                break;
            case "Revision nicht erfolgreich":
                attr = -1;
        }
        return "/filter/listVerdicts/" + id + "/" + attr;
    }
}
