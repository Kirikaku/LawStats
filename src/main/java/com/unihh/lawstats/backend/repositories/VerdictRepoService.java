package com.unihh.lawstats.backend.repositories;

import com.unihh.lawstats.core.model.DataModelAttributes;
import com.unihh.lawstats.core.model.SearchVerdict;
import com.unihh.lawstats.core.model.Verdict;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class VerdictRepoService {

    @Autowired
    VerdictRepository verdictRepository;

    public Collection<? extends Verdict> getVerdictsForAttribute(DataModelAttributes key, List<String> value) {
        Set<Verdict> verdictSetForAttribute = new HashSet<>();
        for (String string : value) {
            switch (key) {
                case DocketNumber:
                    verdictSetForAttribute.addAll(verdictRepository.findAllByDocketNumber(string));
                    break;
                case Senate:
                    verdictSetForAttribute.addAll(verdictRepository.findAllBySenate(string));
                    break;
                case Judges:
                    verdictSetForAttribute.addAll(verdictRepository.findAllByJudgeList(string));
                    break;
                case DateVerdict:
                    verdictSetForAttribute.addAll(verdictRepository.findAllByDateVerdictBetween(new Date(0).getTime(), new Date(Long.MAX_VALUE).getTime()));
                    break;
                case ForeDecisionRACCourt:
                    verdictSetForAttribute.addAll(verdictRepository.findAllByForeDecisionRACCourt(string));
                    break;
                case ForeDecisionRACDateVerdict:
                    verdictSetForAttribute.addAll(verdictRepository.findAllByForeDecisionRACVerdictDateBetween(new Date(0).getTime(), new Date(Long.MAX_VALUE).getTime()));
                    break;
                case ForeDecisionRCCourt:
                    verdictSetForAttribute.addAll(verdictRepository.findAllByForeDecisionRCCourt(string));
                    break;
                case ForeDecisionRCDateVerdict:
                    verdictSetForAttribute.addAll(verdictRepository.findAllByForeDecisionRCVerdictDateBetween(new Date(0).getTime(), new Date(Long.MAX_VALUE).getTime()));
                    break;
                case ForeDecisionDCCourt:
                    verdictSetForAttribute.addAll(verdictRepository.findAllByForeDecisionDCCourt(string));
                    break;
                case ForeDecisionDCDateVerdict:
                    verdictSetForAttribute.addAll(verdictRepository.findAllByForeDecisionRCVerdictDateBetween(new Date(0).getTime(), new Date(Long.MAX_VALUE).getTime()));
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
    public List<SearchVerdict> getAllCombinationsOfSearchVerdicts(Map<DataModelAttributes, List<String>> selectedAttributesMap) {
        List<Map<DataModelAttributes, String>> allCombinationList = new ArrayList<>();
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
    private List<SearchVerdict> createSearchVerdictsOfAllCombinations(List<Map<DataModelAttributes, String>> allCombinationList) {
        List<SearchVerdict> searchVerdictList = new ArrayList<>();

        allCombinationList.forEach(attributesStringMap -> {
            SearchVerdict sv = new SearchVerdict();
            sv.setCombinationMap(attributesStringMap);
            searchVerdictList.add(sv);
        });

        return searchVerdictList;
    }

    private void createMapWithAllCombinations(Map<DataModelAttributes, List<String>> hashMap,
                                              ListIterator<DataModelAttributes> listIterator,
                                              Map<DataModelAttributes, String> solutionMap,
                                              List<Map<DataModelAttributes, String>> allCombinationlist) {
        if (!listIterator.hasNext()) {
            Map<DataModelAttributes, String> entry = new HashMap<>();

            for (DataModelAttributes key : solutionMap.keySet()) {
                entry.put(key, solutionMap.get(key));
            }

            allCombinationlist.add(entry);
        } else {
            DataModelAttributes key = listIterator.next();

            List<String> list = hashMap.get(key);

            for (String value : list) {
                solutionMap.put(key, value);
                createMapWithAllCombinations(hashMap, listIterator, solutionMap, allCombinationlist);
                solutionMap.remove(key);
            }

            listIterator.previous();
        }
    }
}
