package com.unihh.lawstats.backend.controller;

import com.unihh.lawstats.backend.repositories.VerdictRepoService;
import com.unihh.lawstats.backend.service.DataAttributeVerdictService;
import com.unihh.lawstats.core.mapping.VerdictDateFormatter;
import com.unihh.lawstats.core.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
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
    private List<String> attributeList = new ArrayList<>();

    @Autowired
    DataAttributeVerdictService dataAttributeVerdictService;

    @Autowired
    VerdictRepoService verdictRepoService;

    public FilterController() {
        attributeList.add(TableAttributes.RevisionSuccess.getDisplayName());
        attributeList.add(TableAttributes.RevisionNotSuccess.getDisplayName());
        attributeList.add(TableAttributes.RevisionAPartOfSuccess.getDisplayName());
    }

    @RequestMapping(value = "/filter/reset")
    public void resetAll() {
        selectedAttributesMap = new HashMap<>();
        attributeList = new ArrayList<>();
        attributeList.add(TableAttributes.RevisionSuccess.getDisplayName());
        attributeList.add(TableAttributes.RevisionNotSuccess.getDisplayName());
        attributeList.add(TableAttributes.RevisionAPartOfSuccess.getDisplayName());
        searchVerdict = new ArrayList<>();
        verdictsInUse = new HashSet<>();
    }

    @PutMapping(value = "/input/string/{attribute}/{value}")
    public void inputString(@PathVariable String attribute, @PathVariable String value) {
        value = value.replace("__", ".");
        DataModelAttributes dataModelAttributes = DataModelAttributes.valueOf(attribute);
        StringInput stringInput = new StringInput();
        stringInput.setValue(value);
        stringInput.setAttribute(dataModelAttributes);
        addInputToMap(stringInput, dataModelAttributes);
    }

    @RequestMapping("/input/date/{attribute}/{dateStart}/to/{dateEnd}")
    public void inputDate(@PathVariable String attribute, @PathVariable long dateStart, @PathVariable long dateEnd) {
        DataModelAttributes dataModelAttributes = DataModelAttributes.valueOf(attribute);
        DateInput dateInput = new DateInput();
        dateInput.setStart(dateStart);
        dateInput.setEnd(dateEnd);
        dateInput.setAttribute(dataModelAttributes);
        addInputToMap(dateInput, dataModelAttributes);
    }

    private void addInputToMap(Input input, DataModelAttributes dataModelAttributes) {
        if (selectedAttributesMap.containsKey(dataModelAttributes)) {
            List<Input> inputList = selectedAttributesMap.get(dataModelAttributes);
            inputList.add(input);
            selectedAttributesMap.put(dataModelAttributes, inputList);
        } else {
            List<Input> inputList = new ArrayList<>();
            inputList.add(input);
            selectedAttributesMap.put(dataModelAttributes, inputList);
        }

        if (!attributeList.contains(dataModelAttributes.getDisplayName())) {
            attributeList.add(dataModelAttributes.getDisplayName());
        }
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
                    VerdictDateFormatter verdictDateFormatter = new VerdictDateFormatter();
                    if (attribute.toString().contains("Date")) {
                        DateInput dateInput = (DateInput) searchVerdict.getValueForKey(attribute);
                        long date = verdictDateFormatter.formateStringToLong(dataAttributeVerdictService.dataAttributeToVerdictValue(attribute, verdict).get(0));
                        if (date > dateInput.getEnd() || date < dateInput.getStart()) {
                            isRelated = false;
                        }

                    } else {
                        StringInput stringInput = (StringInput) searchVerdict.getValueForKey(attribute);
                        if (dataAttributeVerdictService.dataAttributeToVerdictValue(attribute, verdict).stream()
                                .map(String::toLowerCase)
                                .noneMatch(s -> s.contains(stringInput.getValue().toLowerCase()))) {
                            isRelated = false;
                        }
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
        VerdictDateFormatter verdictDateFormatter = new VerdictDateFormatter();
        if (DataModelAttributes.valueOfDisplayName(attribute) != null) {
            Input input = searchVerdict.getValueForKey(DataModelAttributes.valueOfDisplayName(attribute));
            if (!attribute.toLowerCase().contains("datum")) {
                return ((StringInput) input).getValue();
            } else {
                DateInput dateInput = ((DateInput) input);
                return verdictDateFormatter.formatVerdictDateToString(dateInput.getStart()) + " - " +
                        verdictDateFormatter.formatVerdictDateToString(dateInput.getEnd());
            }
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
