package com.unihh.lawstats.core.model;


import com.unihh.lawstats.core.model.attributes.DataModelAttributes;
import com.unihh.lawstats.core.model.input.Input;

import java.util.*;

/**
 * This Object represents the the special combination of attributes for the search
 */
public class SearchVerdict {

    private Map<DataModelAttributes, Input> combinationMap = new HashMap();
    private List<Verdict> relatedVerdictsWithRevisionSuccessful = new ArrayList<>();
    private List<Verdict> relatedVerdictsWithRevisionNotSuccessful = new ArrayList<>();
    private List<Verdict> relatedVerdictsWithRevisionAPartOfSuccessful = new ArrayList<>();
    private List<Verdict> allRelatedVerdicts = new ArrayList<>();
    private int id;


    public SearchVerdict(int id) {
        this.id = id;
    }

    public Map<DataModelAttributes, Input> getCombinationMap() {
        return combinationMap;
    }

    public List<Verdict> getRelatedVerdictsWithRevisionSuccessful() {
        return Collections.unmodifiableList(relatedVerdictsWithRevisionSuccessful);
    }

    public List<Verdict> getRelatedVerdictsWithRevisionNotSuccessful() {
        return Collections.unmodifiableList(relatedVerdictsWithRevisionNotSuccessful);
    }

    public List<Verdict> getRelatedVerdictsWithRevisionPartlySuccessful() {
        return Collections.unmodifiableList(relatedVerdictsWithRevisionAPartOfSuccessful);
    }

    public  List<Verdict> getAllRelatedVerdicts(){
        return  Collections.unmodifiableList(allRelatedVerdicts);
    }

    public void addVerdictToList(Verdict verdict){
        allRelatedVerdicts.add(verdict);

        if(verdict.getRevisionSuccess() == 1){
            relatedVerdictsWithRevisionSuccessful.add(verdict);
        } else if (verdict.getRevisionSuccess() == 0){
            relatedVerdictsWithRevisionAPartOfSuccessful.add(verdict);
        } else {
            relatedVerdictsWithRevisionNotSuccessful.add(verdict);
        }
    }

    public void addAll(Collection<Verdict> collection){
        collection.forEach(this::addVerdictToList);
    }

    public void setCombinationMap(Map<DataModelAttributes, Input> combinationMap) {
        this.combinationMap = combinationMap;
    }

    public Input getValueForKey(DataModelAttributes key){
        return combinationMap.get(key);
    }

    public int getId() {
        return id;
    }
}