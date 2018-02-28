package com.unihh.lawstats.backend.repositories;

import com.unihh.lawstats.core.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class VerdictRepoService {

    @Autowired
    VerdictRepository verdictRepository;

    public Collection<? extends Verdict> getVerdictsForAttribute(DataModelAttributes key, List<Input> value) {
        Set<Verdict> verdictSetForAttribute = new HashSet<>();
        for (Input input: value) {
            switch (key) {
                case DocketNumber:
                    verdictSetForAttribute.addAll(verdictRepository.findAllByDocketNumberContainingIgnoreCase(((StringInput)input).getValue()));
                    break;
                case Senate:
                    verdictSetForAttribute.addAll(verdictRepository.findAllBySenateContainingIgnoreCase(((StringInput)input).getValue()));
                    break;
                case Judges:
                    verdictSetForAttribute.addAll(verdictRepository.findByJudgeListContainingIgnoreCase(((StringInput)input).getValue()));
                    break;
                case DateVerdict:
                    DateInput dateInput = (DateInput) input;
                    verdictSetForAttribute.addAll(verdictRepository.findAllByDateVerdictBetween(dateInput.getStart(), dateInput.getEnd()));
                    break;
                case ForeDecisionRACCourt:
                    verdictSetForAttribute.addAll(verdictRepository.findAllByForeDecisionRACCourtContainingIgnoreCase(((StringInput)input).getValue()));
                    break;
                case ForeDecisionRACDateVerdict:
                    DateInput dateInputRAC = (DateInput) input;
                    verdictSetForAttribute.addAll(verdictRepository.findAllByForeDecisionRACVerdictDateBetween(dateInputRAC.getStart(), dateInputRAC.getEnd()));
                    break;
                case ForeDecisionRCCourt:
                    verdictSetForAttribute.addAll(verdictRepository.findAllByForeDecisionRCCourtContainingIgnoreCase(((StringInput)input).getValue()));
                    break;
                case ForeDecisionRCDateVerdict:
                    DateInput dateInputRC = (DateInput) input;
                    verdictSetForAttribute.addAll(verdictRepository.findAllByForeDecisionRCVerdictDateBetween(dateInputRC.getStart(), dateInputRC.getEnd()));
                    break;
                case ForeDecisionDCCourt:
                    verdictSetForAttribute.addAll(verdictRepository.findAllByForeDecisionDCCourtContainingIgnoreCase(((StringInput)input).getValue()));
                    break;
                case ForeDecisionDCDateVerdict:
                    DateInput dateInputDC = (DateInput) input;
                    verdictSetForAttribute.addAll(verdictRepository.findAllByForeDecisionRCVerdictDateBetween(dateInputDC.getStart(), dateInputDC.getEnd()));
                    break;
                default:
                    return Collections.emptyList();
            }
        }
        return verdictSetForAttribute;
    }

    /**
     * This method will create a list with all SearchVerdict objects, which the user is looking for
     *
     * @return all serachVerdict objects which are involved
     */
    public List<SearchVerdict> getAllCombinationsOfSearchVerdicts(Map<DataModelAttributes, List<Input>> selectedAttributesMap) {
        List<Map<DataModelAttributes, Input>> allCombinationList = new ArrayList<>();
        createMapWithAllCombinations(selectedAttributesMap,
                new LinkedList<>(selectedAttributesMap.keySet()).listIterator(),
                new HashMap<>(),
                allCombinationList);

        return createSearchVerdictsOfAllCombinations(allCombinationList);
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
            SearchVerdict sv = new SearchVerdict();
            sv.setCombinationMap(attributesStringMap);
            searchVerdictList.add(sv);
        });

        return searchVerdictList;
    }

    private void createMapWithAllCombinations(Map<DataModelAttributes, List<Input>> hashMap,
                                              ListIterator<DataModelAttributes> listIterator,
                                              Map<DataModelAttributes, Input> solutionMap,
                                              List<Map<DataModelAttributes, Input>> allCombinationlist) {
        if (!listIterator.hasNext()) {
            Map<DataModelAttributes, Input> entry = new HashMap<>();

            for (DataModelAttributes key : solutionMap.keySet()) {
                entry.put(key, solutionMap.get(key));
            }

            allCombinationlist.add(entry);
        } else {
            DataModelAttributes key = listIterator.next();

            List<Input> list = hashMap.get(key);

            for (Input value : list) {
                solutionMap.put(key, value);
                createMapWithAllCombinations(hashMap, listIterator, solutionMap, allCombinationlist);
                solutionMap.remove(key);
            }

            listIterator.previous();
        }
    }
}
