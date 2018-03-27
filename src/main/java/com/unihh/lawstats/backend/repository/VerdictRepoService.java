package com.unihh.lawstats.backend.repository;

import com.unihh.lawstats.core.model.Verdict;
import com.unihh.lawstats.core.model.attributes.DataModelAttributes;
import com.unihh.lawstats.core.model.input.DateInput;
import com.unihh.lawstats.core.model.input.Input;
import com.unihh.lawstats.core.model.input.StringInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service(value = "verdictRepoService")
/**
 * This service is the repository for other classes und should used for the connection to the databse
 */
public class VerdictRepoService {

    private VerdictRepository verdictRepository;

    @Autowired
    public VerdictRepoService(VerdictRepository verdictRepository) {
        this.verdictRepository = verdictRepository;
    }

    /**
     * This method search for wanted verdicts
     * @return a list of verdict which contains all given attributes
     */
    public List<Verdict> findVerdictByAttributesAndValue(Map<DataModelAttributes, Set<Input>> searchMap) {
        return verdictRepository.findVerdictByAttributesAndValues(searchMap);
    }

    /**
     * This method return all possibilities of values for given attribute
     */
    public List<String> getAllTermsOfGivenAttribute(DataModelAttributes attributes) {
        return verdictRepository.findAllValuesForStringAttribute(attributes);
    }

    /**
     * This method returns a set of verdicts by given attribute and value
     * @param key the attribute
     * @param value the value of the attribute
     */
    private Collection<Verdict> getVerdictsForAttributeAndValue(DataModelAttributes key, String value) {
        StringInput stringInput = new StringInput();
        stringInput.setAttribute(key);
        stringInput.setValue(value);
        Set<Input> inputSet = new HashSet<>();
        inputSet.add(stringInput);
        return getVerdictsForAttribute(key, inputSet);
    }

    /**
     * This method is the lesser dynamic variant for searching all verdict for given attributes with value
     * @param key the attribute
     * @param value the set of values
     */
    public Collection<Verdict> getVerdictsForAttribute(DataModelAttributes key, Set<Input> value) {
        SearchFormatter searchFormatter = new SearchFormatter();
        Set<Verdict> verdictSetForAttribute = new HashSet<>();
        for (Input input : value) {
            switch (key) {
                case DocketNumber:
                    List<Verdict> verdictList = new ArrayList<>();
                    String[] valueArray = searchFormatter.formatString(((StringInput) input).getValue());
                    verdictList.addAll(verdictRepository.findAllByDocketNumberStartingWith(valueArray));
                    verdictSetForAttribute.addAll(verdictList.stream().filter(verdict -> Arrays.stream(valueArray).allMatch(s -> verdict.getDocketNumber().contains(s))).collect(Collectors.toList()));
                    break;
                case Senate:
                    verdictSetForAttribute.addAll(verdictRepository.findAllBySenateContaining((searchFormatter.formatString(((StringInput) input).getValue()))[0]));
                    break;
                case Judges:
                    verdictSetForAttribute.addAll(verdictRepository.findByJudgeListContainingIgnoreCase((searchFormatter.formatString(((StringInput) input).getValue()))[0]));
                    break;
                case DateVerdict:
                    DateInput dateInput = (DateInput) input;
                    verdictSetForAttribute.addAll(verdictRepository.findAllByDateVerdictBetween(dateInput.getStart(), dateInput.getEnd()));
                    break;
                case ForeDecisionRACCourt:
                    verdictSetForAttribute.addAll(verdictRepository.findAllByForeDecisionRACCourtStartingWith((searchFormatter.formatString(((StringInput) input).getValue()))));
                    break;
                case ForeDecisionRACDateVerdict:
                    DateInput dateInputRAC = (DateInput) input;
                    verdictSetForAttribute.addAll(verdictRepository.findAllByForeDecisionRACVerdictDateBetween(dateInputRAC.getStart(), dateInputRAC.getEnd()));
                    break;
                case ForeDecisionRCCourt:
                    verdictSetForAttribute.addAll(verdictRepository.findAllByForeDecisionRCCourtStartingWith((searchFormatter.formatString(((StringInput) input).getValue()))));
                    break;
                case ForeDecisionRCDateVerdict:
                    DateInput dateInputRC = (DateInput) input;
                    verdictSetForAttribute.addAll(verdictRepository.findAllByForeDecisionRCVerdictDateBetween(dateInputRC.getStart(), dateInputRC.getEnd()));
                    break;
                case ForeDecisionDCCourt:
                    verdictSetForAttribute.addAll(verdictRepository.findAllByForeDecisionDCCourtStartingWith((searchFormatter.formatString(((StringInput) input).getValue()))));
                    break;
                case ForeDecisionDCDateVerdict:
                    DateInput dateInputDC = (DateInput) input;
                    verdictSetForAttribute.addAll(verdictRepository.findAllByForeDecisionDCVerdictDateBetween(dateInputDC.getStart(), dateInputDC.getEnd()));
                    break;
                default:
                    return Collections.emptyList();
            }
        }
        return verdictSetForAttribute;
    }

    /**
     * This method save the verdict
     * @param verdict verdict which should saved
     */
    public void save(Verdict verdict) {
        if (verdict != null) {
            Collection<Verdict> verdicts = getVerdictsForAttributeAndValue(DataModelAttributes.DocketNumber, verdict.getDocketNumber());
            if (!verdicts.isEmpty()) {
                verdictRepository.deleteById(verdict.getId());
            }
            verdictRepository.save(verdict);
        }
    }
}