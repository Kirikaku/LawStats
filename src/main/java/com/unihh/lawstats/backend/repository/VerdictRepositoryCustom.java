package com.unihh.lawstats.backend.repository;

import com.unihh.lawstats.core.model.Verdict;
import com.unihh.lawstats.core.model.attributes.DataModelAttributes;
import com.unihh.lawstats.core.model.input.Input;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface VerdictRepositoryCustom {

    /**
     * This method return a List of verdicts which contains the given values
     * @param mapForSearching the map with all attributes and values
     */
    List<Verdict> findVerdictByAttributesAndValues(Map<DataModelAttributes, Set<Input>> mapForSearching);

    /**
     * This method return all value for given attribute
     */
    List<String> findAllValuesForStringAttribute(DataModelAttributes attribute);
}
