package com.unihh.lawstats.backend.repository;

import com.unihh.lawstats.core.model.Verdict;
import com.unihh.lawstats.core.model.attributes.Attributes;
import com.unihh.lawstats.core.model.attributes.DataModelAttributes;
import com.unihh.lawstats.core.model.input.Input;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface VerdictRepositoryCustom {

    List<Verdict> findVerdictByAttributesAndValues(Map<DataModelAttributes,Set<Input>> mapForSearching);

    List<String> findAllValuesForStringAttribute(DataModelAttributes attribute);
}
