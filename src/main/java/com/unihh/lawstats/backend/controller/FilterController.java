package com.unihh.lawstats.backend.controller;

import com.unihh.lawstats.backend.repository.SearchFormatter;
import com.unihh.lawstats.backend.repository.VerdictRepoService;
import com.unihh.lawstats.backend.service.DataAttributeVerdictService;
import com.unihh.lawstats.core.mapping.VerdictDateFormatter;
import com.unihh.lawstats.core.model.SearchVerdict;
import com.unihh.lawstats.core.model.Verdict;
import com.unihh.lawstats.core.model.attributes.DataModelAttributes;
import com.unihh.lawstats.core.model.attributes.TableAttributes;
import com.unihh.lawstats.core.model.input.DateInput;
import com.unihh.lawstats.core.model.input.Input;
import com.unihh.lawstats.core.model.input.InputType;
import com.unihh.lawstats.core.model.input.StringInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.unihh.lawstats.core.model.attributes.TableAttributes.RevisionNotSuccess;
import static com.unihh.lawstats.core.model.attributes.TableAttributes.RevisionPartlySuccess;
import static com.unihh.lawstats.core.model.attributes.TableAttributes.RevisionSuccess;

@Controller
@Service("FilterController")
public class FilterController {

    @Autowired
    private DataAttributeVerdictService dataAttributeVerdictService;
    @Autowired
    private VerdictRepoService verdictRepoService;
    // Out Map with all from user selected attributes
    private Map<DataModelAttributes, Set<Input>> selectedAttributesMap = new HashMap<>();
    // All verdicts which are related to our selection
    private Set<Verdict> verdictsInUse = new HashSet<>();
    // All combinations of attributes
    private List<SearchVerdict> searchVerdictList = new ArrayList<>();
    // A list of all attribute displaynames
    private List<String> attributeList = new ArrayList<>();
    // This field is used for setting the id to SearchVerdicts. It is important for the listing of verdicts
    private int nextIdForSearchVerdict = 0;

    /**
     * Have to set the definitive columns in our table
     */
    public FilterController() {
        attributeList.add(RevisionSuccess.getDisplayName());
        attributeList.add(TableAttributes.RevisionNotSuccess.getDisplayName());
        attributeList.add(TableAttributes.RevisionPartlySuccess.getDisplayName());
    }

    /**
     * This method is related to the url: /filter
     * it resets all (new context) and returns the filter html
     */
    @GetMapping("/filter")
    public String filter(Model model) {
        resetAll();
        return "filter";
    }

    /**
     * This method is related to URL /filter/reset
     * It resets all value
     */
    @RequestMapping(value = "/filter/reset")
    public void resetAll() {
        selectedAttributesMap = new HashMap<>();
        attributeList = new ArrayList<>();
        attributeList.add(RevisionSuccess.getDisplayName());
        attributeList.add(TableAttributes.RevisionNotSuccess.getDisplayName());
        attributeList.add(TableAttributes.RevisionPartlySuccess.getDisplayName());
        searchVerdictList = new ArrayList<>();
        verdictsInUse = new HashSet<>();
        nextIdForSearchVerdict = 0;
    }

    /**
     * This method will create a stringinput, its represents the search attribute of the user
     */
    @PutMapping(value = "/input/string/{attribute}/{value}")
    public void inputString(@PathVariable String attribute, @PathVariable String value) {
        value = value.replace("__", "."); //a point in the url is not possible, so we replaced them with underscore
        DataModelAttributes dataModelAttributes = DataModelAttributes.valueOf(attribute);
        StringInput stringInput = new StringInput();
        stringInput.setValue(value);
        stringInput.setAttribute(dataModelAttributes);
        addInputToMap(stringInput, dataModelAttributes);
    }

    /**
     * This method will create a stringinput, but without the value.
     * This means all combinations are wanted
     */
    @RequestMapping(value = "/input/string/{attribute}")
    public void inputStringWithoutValue(@PathVariable String attribute) {
        inputString(attribute, "");
    }

    /**
     * This method will create a dateinput, its represents the search attribute of the user
     */
    @RequestMapping("/input/date/{attribute}/{dateStart}/to/{dateEnd}")
    public void inputDate(@PathVariable String attribute, @PathVariable long dateStart, @PathVariable long dateEnd) {
        DataModelAttributes dataModelAttributes = DataModelAttributes.valueOf(attribute);
        DateInput dateInput = new DateInput();
        dateInput.setStart(dateStart);
        dateInput.setEnd(dateEnd);
        dateInput.setAttribute(dataModelAttributes);
        addInputToMap(dateInput, dataModelAttributes);
    }

    /**
     * This method start the search of our SearchVerdicts
     */
    @RequestMapping("/filter/searchVerdicts")
    public String startSearch() {

        // First get all Verdicts which are related to the Attributes
        verdictsInUse = getQueriedVerdicts();

        // Second create all SearchVerdict to create all combinations
        searchVerdictList = getAllCombinationsOfSearchVerdicts();

        addVerdictsToSearchVerdicts();

        deleteAllUnnecessarySearchVerdicts();

        return "verdictTable";
    }


    private void deleteAllUnnecessarySearchVerdicts() {
        searchVerdictList = searchVerdictList.stream().filter(searchVerdict1 -> !searchVerdict1.getRelatedVerdictsWithRevisionPartlySuccessful().isEmpty() || !searchVerdict1.getRelatedVerdictsWithRevisionNotSuccessful().isEmpty() || !searchVerdict1.getRelatedVerdictsWithRevisionSuccessful().isEmpty()).collect(Collectors.toList());
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
        return searchVerdictList;
    }


    /**
     * This method will create a list with all SearchVerdict objects, which the user is looking for
     *
     * @return all serachVerdict objects which are involved
     */
    private List<SearchVerdict> getAllCombinationsOfSearchVerdicts() {
        List<Map<DataModelAttributes, Input>> allCombinationList = new ArrayList<>();
        createInputsWhenAnEmptyExists();
        createMapWithAllCombinations(selectedAttributesMap, new LinkedList<>(selectedAttributesMap.keySet()).listIterator(), new HashMap<>(), allCombinationList);

        return createSearchVerdictsOfAllCombinations(allCombinationList);
    }

    /**
     * When an empty value exists, then this means that we want to get all all values of it
     */
    private void createInputsWhenAnEmptyExists() {
        selectedAttributesMap.forEach((dataModelAttributes, inputs) -> inputs.forEach(input -> {
            if (input.getInputType().equals(InputType.String)) { // When we have a stringInput object
                StringInput stringInput = (StringInput) input;
                if (stringInput.getValue().isEmpty()) { // And the value is empty (we want all combinations)
                    createAllCombinationsFromEmptyStringInput(stringInput);
                }
            }
        }));
    }

    /**
     * This method creates all combinations of given attribute
     */
    private void createAllCombinationsFromEmptyStringInput(StringInput stringInput) {
       // verdictRepoService.getAllTermsOfGivenAttribute(stringInput.getAttribute()).forEach(s -> {
        verdictsInUse.forEach(verdict -> dataAttributeVerdictService.dataAttributeToVerdictValue(stringInput.getAttribute(), verdict).forEach(s -> {
            StringInput verdictSpecificStringInput = new StringInput();
            verdictSpecificStringInput.setAttribute(stringInput.getAttribute());
            verdictSpecificStringInput.setValue(s);
            addInputToMap(verdictSpecificStringInput, verdictSpecificStringInput.getAttribute());
        }));
    }

    /**
     * This method takes the map with all combinations and put them into the SearchVerdict object
     *
     * @param allCombinationList the List with all combinations
     * @return a list with created SearchVerdict
     */
    private List<SearchVerdict> createSearchVerdictsOfAllCombinations(List<Map<DataModelAttributes, Input>> allCombinationList) {
        List<SearchVerdict> searchVerdictList = new ArrayList<>();

        allCombinationList.forEach(attributesStringMap -> {
            SearchVerdict sv = new SearchVerdict(nextIdForSearchVerdict);
            nextIdForSearchVerdict += 1;
            sv.setCombinationMap(attributesStringMap);
            searchVerdictList.add(sv);
        });

        return searchVerdictList;
    }

    /**
     * This mehtod return the searchVerdict object for given id, otherwise you get null
     */
    public SearchVerdict getSearchVerdictForID(int id) {
        for (SearchVerdict searchVerdict : searchVerdictList)
            if (Objects.equals(searchVerdict.getId(), id)) {
                return searchVerdict;
            }
        return null;
    }

    /**
     * This method creates recursively all combinations of Attributes and values
     *
     * @param hashMap            the with all attributes and values
     * @param listIterator       the iterator which will be used to create a tree
     * @param solutionMap        the map with with one combination
     * @param allCombinationlist list with all maps of combination
     */
    private void createMapWithAllCombinations(Map<DataModelAttributes, Set<Input>> hashMap, ListIterator<DataModelAttributes> listIterator, Map<DataModelAttributes, Input> solutionMap, List<Map<DataModelAttributes, Input>> allCombinationlist) {
        if (!listIterator.hasNext()) {
            Map<DataModelAttributes, Input> entry = new HashMap<>();

            for (DataModelAttributes key : solutionMap.keySet()) {
                entry.put(key, solutionMap.get(key));
            }

            allCombinationlist.add(entry);
        } else {
            DataModelAttributes key = listIterator.next();

            Set<Input> list = hashMap.get(key);

            for (Input value : list) {
                solutionMap.put(key, value);
                createMapWithAllCombinations(hashMap, listIterator, solutionMap, allCombinationlist);
                solutionMap.remove(key);
            }

            listIterator.previous();
        }
    }

    /**
     * This methods adds all related Verdicts to the SearchVerdicts objects
     */
    private void addVerdictsToSearchVerdicts() {
//        for (SearchVerdict searchVerdict : searchVerdictList) {
//            searchVerdict.addAll(verdictRepoService.testVerdictThing(convertSearchVerdictCombinationMap(searchVerdict)));
//        }
        SearchFormatter searchFormatter = new SearchFormatter();
        for (Verdict verdict : verdictsInUse) {
            for (SearchVerdict searchVerdict : searchVerdictList) {
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
                        // when there is one value of the verdict attribute, which contains all values of the searchVerdict
                        if (!verdictContainsValueOfSearchVerdict(verdict, stringInput)) {
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

    private Map<DataModelAttributes, Set<Input>> convertSearchVerdictCombinationMap(SearchVerdict searchVerdict) {
        Map<DataModelAttributes, Set<Input>> map = new HashMap<>();
        searchVerdict.getCombinationMap().forEach((dataModelAttributes, input) -> {
            Set<Input> set = new HashSet<>();
            set.add(input);
            map.put(dataModelAttributes, set);
        });

        return map;
    }

    private boolean verdictContainsValueOfSearchVerdict(Verdict verdict, StringInput stringInput) {
        SearchFormatter searchFormatter = new SearchFormatter();
        String[] stringArray = searchFormatter.formatString(stringInput.getValue());
        List<String> verdictValues = dataAttributeVerdictService.dataAttributeToVerdictValue(stringInput.getAttribute(), verdict);
        verdictValues = verdictValues.stream().map(String::toLowerCase).collect(Collectors.toList());
        return verdictValues.stream().anyMatch(o -> Arrays.stream(stringArray).allMatch(o::contains));
    }

    /**
     * This method get all Verdicts which are involved by giving attributes
     * All Attributes are connected with an AND
     */
    private Set<Verdict> getQueriedVerdicts() {
        //return new HashSet<>(verdictRepoService.testVerdictThing(selectedAttributesMap));
        Set<Verdict> verdictSet = new HashSet<>(); //make set empty

        // First add a list of Verdict to out Set
        if (selectedAttributesMap.entrySet().iterator().hasNext()) {
            Map.Entry<DataModelAttributes, Set<Input>> entry = selectedAttributesMap.entrySet().iterator().next();
            verdictSet.addAll(verdictRepoService.getVerdictsForAttribute(entry.getKey(), entry.getValue()));

            //Second for every attrbute add the verdicts to the set and create the intersection.
            //We connect the attributes with an AND
            selectedAttributesMap.forEach((dataModelAttributes, strings) -> verdictSet.retainAll(verdictRepoService.getVerdictsForAttribute(dataModelAttributes, strings)));
        }

        return verdictSet;
    }

    private void addInputToMap(Input input, DataModelAttributes dataModelAttributes) {
        if (selectedAttributesMap.containsKey(dataModelAttributes)) {
            Set<Input> inputList = selectedAttributesMap.get(dataModelAttributes);
            inputList.add(input);
            selectedAttributesMap.put(dataModelAttributes, inputList);
        } else {
            Set<Input> inputList = new HashSet<>();
            inputList.add(input);
            selectedAttributesMap.put(dataModelAttributes, inputList);
        }

        if (!attributeList.contains(dataModelAttributes.getDisplayName())) {
            attributeList.add(dataModelAttributes.getDisplayName());
        }
    }

    /**
     * This method returns the value for a given SearchVerdict and attribute
     */
    public String getValueForAttributeAndVerdict(SearchVerdict searchVerdict, String attribute) {
        VerdictDateFormatter verdictDateFormatter = new VerdictDateFormatter();
        if (DataModelAttributes.valueOfDisplayName(attribute) != null) {
            Input input = searchVerdict.getValueForKey(DataModelAttributes.valueOfDisplayName(attribute));
            if (!attribute.toLowerCase().contains("datum")) {
                return ((StringInput) input).getValue();
            } else {
                DateInput dateInput = ((DateInput) input);
                return verdictDateFormatter.formatVerdictDateToString(dateInput.getStart()) + " - " + verdictDateFormatter.formatVerdictDateToString(dateInput.getEnd());
            }
        } else {
            switch (TableAttributes.valueOfDisplayName(attribute)) {
                case RevisionSuccess:
                    return String.valueOf(searchVerdict.getRelatedVerdictsWithRevisionSuccessful().size());
                case RevisionNotSuccess:
                    return String.valueOf(searchVerdict.getRelatedVerdictsWithRevisionNotSuccessful().size());
                case RevisionPartlySuccess:
                    return String.valueOf(searchVerdict.getRelatedVerdictsWithRevisionPartlySuccessful().size());
                default:
                    return "Nicht implementiert";
            }
        }
    }


    /**
     * This method returns the percent value for a given SearchVerdict and attribute
     */
    public double getPercentValue(SearchVerdict searchVerdict, String attribute) {
        double p;
        if (searchVerdict.getAllRelatedVerdicts().size() != 0) {
            switch (TableAttributes.valueOfDisplayName(attribute)) {
                case RevisionSuccess:
                    p = ((double) searchVerdict.getRelatedVerdictsWithRevisionSuccessful().size()) / searchVerdict.getAllRelatedVerdicts().size() * 100;
                    return p;
                case RevisionPartlySuccess:
                    p = ((double) searchVerdict.getRelatedVerdictsWithRevisionPartlySuccessful().size()) / searchVerdict.getAllRelatedVerdicts().size() * 100;
                    return p;
                case RevisionNotSuccess:
                    p = ((double) searchVerdict.getRelatedVerdictsWithRevisionNotSuccessful().size()) / searchVerdict.getAllRelatedVerdicts().size() * 100;
                    return p;
                default:
                    return 0;
            }
        }
        return 0;
    }

    public String getFormattedPercentValue(SearchVerdict searchVerdict, String attribute) {
        DecimalFormat pct = new DecimalFormat("#.##");

        if (TableAttributes.valueOfDisplayName(attribute).equals(RevisionSuccess) ||
            TableAttributes.valueOfDisplayName(attribute).equals(RevisionPartlySuccess) ||
            TableAttributes.valueOfDisplayName(attribute).equals(RevisionNotSuccess)) {
            double p = getPercentValue(searchVerdict, attribute);

            return pct.format(p) + "%";
        }
        return "";
    }
}
