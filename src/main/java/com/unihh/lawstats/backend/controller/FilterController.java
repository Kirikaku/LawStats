package com.unihh.lawstats.backend.controller;

import com.unihh.lawstats.backend.repositories.VerdictRepoService;
import com.unihh.lawstats.backend.service.DataAttributeVerdictService;
import com.unihh.lawstats.core.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

@Controller
@Service("FilterController")
public class FilterController {

    // Out Map with all from user selected attributes
    private Map<DataModelAttributes, List<Input>> selectedAttributesMap = new HashMap<>();

    // All verdicts which are related to our selection
    private Set<Verdict> verdictsInUse = new HashSet<>();

    // All combinations of attributes
    private List<SearchVerdict> searchVerdict = new ArrayList<>();

    // A list of all attribute displaynames
    private List<String> attributeList = Arrays.asList(TableAttributes.RevisionSuccess.getDisplayName(),
            TableAttributes.RevisionNotSuccess.getDisplayName(),
            TableAttributes.RevisionAPartOfSuccess.getDisplayName(),
            DataModelAttributes.Judges.getDisplayName());

    @Autowired
    DataAttributeVerdictService dataAttributeVerdictService;

    @Autowired
    VerdictRepoService verdictRepoService;

    @GetMapping("/input")
    public String inputForm(Model model) {
        return "input";
    }

    @PostMapping("/input")
    public String inputSubmit(@ModelAttribute Input input) {
        return "result";
    }

    /**
     * This method returns all attributes displaynames
     */
    public List<String> getAttributesDisplayname() {
        return attributeList;
    }

    /**
     * This method returns all combinations of attributes.
     * One object represents one row
     */
    public List<SearchVerdict> getSearchVerdictList() {
        return searchVerdict;
    }

    /**
     * This method start the search of our SearchVerdicts
     */
    @RequestMapping("/filter/searchVerdicts")
    public String startSearch() {
        StringInput input = new StringInput();
        input.setAttribute(DataModelAttributes.Judges);
        input.setValue("Gschwander");
        selectedAttributesMap.put(DataModelAttributes.Judges, Arrays.asList(input));

        verdictsInUse = getQueriedVerdicts();

        searchVerdict = verdictRepoService.getAllCombinationsOfSearchVerdicts(selectedAttributesMap);

        addVerdictsToSearchVerdicts();

        return "verdictTable";
    }

    /**
     * This methods adds all related Verdicts to the SearchVerdicts objects
     */
    private void addVerdictsToSearchVerdicts() {
        for (Verdict verdict : verdictsInUse) {
            for (SearchVerdict searchVerdict : searchVerdict) {
                boolean isRelated = true;
                for (DataModelAttributes attribute : searchVerdict.getCombinationMap().keySet()) {
                    if (!dataAttributeVerdictService.dataAttributeToVerdictValue(attribute, verdict).contains(searchVerdict.getValueForKey(attribute))) {
                        isRelated = false;
                    }
                }

                if (isRelated) {
                    searchVerdict.addVerdictToList(verdict);
                }
            }
        }
    }

    /**
     * This method get all Verdicts which are involved by giving attributes
     * All Attributes are connected with an AND
     */
    private Set<Verdict> getQueriedVerdicts() {
        Set<Verdict> verdictSet = new HashSet<>(); //make set empty

        // First add a list of Verdict to out Set
        if (selectedAttributesMap.entrySet().iterator().hasNext()) {
            Map.Entry<DataModelAttributes, List<Input>> entry = selectedAttributesMap.entrySet().iterator().next();
            verdictSet.addAll(verdictRepoService.getVerdictsForAttribute(entry.getKey(), entry.getValue()));

            //Second for every attrbute add the verdicts to the set and create the intersection.
            //We connect the attributes with an AND
            selectedAttributesMap.forEach((dataModelAttributes, strings) -> verdictSet.retainAll(verdictRepoService.getVerdictsForAttribute(dataModelAttributes, strings)));
        }

        return verdictSet;
    }

    /**
     * This method return the value for given SearchVerdict and given attribut
     */
    public String getValueForAttributeAndVerdict(SearchVerdict searchVerdict, String attribute) {
        if (DataModelAttributes.valueOfDisplayName(attribute) != null) {
            Input input = searchVerdict.getValueForKey(DataModelAttributes.valueOfDisplayName(attribute));
            if (input != null) {
                return ((StringInput) input).getValue();
            }
            return "";
        } else {
            switch (TableAttributes.valueOfDisplayName(attribute)) {
                case RevisionSuccess:
                    return String.valueOf(searchVerdict.getRelatedVerdictsWithRevisionSuccessful().size());
                case RevisionNotSuccess:
                    return String.valueOf(searchVerdict.getRelatedVerdictsWithRevisionNotSuccessful().size());
                case RevisionAPartOfSuccess:
                    return String.valueOf(searchVerdict.getRelatedVerdictsWithRevisionAPartOfSuccessful().size());
                default:
                    return "Nicht implementiert";
            }
        }
    }
}
