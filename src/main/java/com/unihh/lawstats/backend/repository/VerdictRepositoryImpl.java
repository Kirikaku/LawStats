package com.unihh.lawstats.backend.repository;

import com.unihh.lawstats.core.model.Verdict;
import com.unihh.lawstats.core.model.attributes.DataModelAttributes;
import com.unihh.lawstats.core.model.input.DateInput;
import com.unihh.lawstats.core.model.input.Input;
import com.unihh.lawstats.core.model.input.InputType;
import com.unihh.lawstats.core.model.input.StringInput;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.solr.core.SolrOperations;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.SimpleTermsQuery;
import org.springframework.data.solr.core.query.TermsQuery;

import java.util.*;

import static com.unihh.lawstats.core.model.attributes.DataModelAttributes.*;

public class VerdictRepositoryImpl implements VerdictRepositoryCustom {

    @Autowired
    Environment environment;

    @Autowired
    SolrOperations solrTemplateSpring;

    private Map<DataModelAttributes, List<String>> attributesWithValuesMap;

    @Override
    public List<Verdict> findVerdictByAttributesAndValues(Map<DataModelAttributes, Set<Input>> mapForSearching) {
        createNewMap(); // set all to empty

        mapForSearching.forEach((attributes, inputs) -> {
            switch (attributes) { // for every given attribute put it in our map
                case DocketNumber:
                    inputs.forEach(input -> putStringInputInMapForAttribute(attributes, input));
                    break;
                case Senate:
                    inputs.forEach(input -> putStringInputInMapForAttribute(attributes, input));
                    break;
                case Judges:
                    inputs.forEach(input -> putStringInputInMapForAttribute(attributes, input));
                    break;
                case DateVerdict:
                    inputs.forEach(input -> putDateInputInMapForAttribute(attributes, input));
                    break;
                case ForeDecisionRACCourt:
                    inputs.forEach(input -> putStringInputInMapForAttribute(attributes, input));
                    break;
                case ForeDecisionRACDateVerdict:
                    inputs.forEach(input -> putDateInputInMapForAttribute(attributes, input));
                    break;
                case ForeDecisionRCCourt:
                    inputs.forEach(input -> putStringInputInMapForAttribute(attributes, input));
                    break;
                case ForeDecisionRCDateVerdict:
                    inputs.forEach(input -> putDateInputInMapForAttribute(attributes, input));
                    break;
                case ForeDecisionDCCourt:
                    inputs.forEach(input -> putStringInputInMapForAttribute(attributes, input));
                    break;
                case ForeDecisionDCDateVerdict:
                    inputs.forEach(input -> putDateInputInMapForAttribute(attributes, input));
                    break;
                default:
                    break;
            }
        });

        String queryString = buildQuery().trim();
        if (!queryString.isEmpty()) {
            Query query = new SimpleQuery(queryString.substring(0, queryString.length() - 4));
//            SolrClient solrclient = new HttpSolrClient(environment.getProperty("solr.address.two"));
//            SolrOperations solrTemplate = new SolrTemplate(solrclient);
            return solrTemplateSpring.queryForPage(query.setRows(Integer.MAX_VALUE), Verdict.class).getContent();
        }
        return Collections.emptyList();
    }

    @Override
    public List<String> findAllValuesForStringAttribute(DataModelAttributes attribute) {
        TermsQuery query;
        switch (attribute) {
            case DocketNumber:
                query = SimpleTermsQuery.queryBuilder().fields("docketNumber").build();
                break;
            case Senate:
                query = SimpleTermsQuery.queryBuilder().fields("senate").build();
                break;
            case Judges:
                query = SimpleTermsQuery.queryBuilder().fields("judgeList").build();
                break;
            case ForeDecisionRACCourt:
                query = SimpleTermsQuery.queryBuilder().fields("foreDecisionRACCourt").build();
                break;
            case ForeDecisionRCCourt:
                query = SimpleTermsQuery.queryBuilder().fields("foreDecisionRCCourt").build();
                break;
            case ForeDecisionDCCourt:
                query = SimpleTermsQuery.queryBuilder().fields("foreDecisionDCCourt").build();
                break;
            default:
                query = SimpleTermsQuery.queryBuilder().build();
                break;
        }
        List<String> arrayList = new ArrayList<>();
        SolrClient solrclient = new HttpSolrClient(environment.getProperty("solr.address.two"));
        SolrOperations solrTemplate = new SolrTemplate(solrclient);
        solrTemplate.queryForTermsPage(query).getContent().forEach(termsFieldEntry -> arrayList.add(termsFieldEntry.getValue()));
        return arrayList;
    }

    private String buildQuery() {
        StringBuilder sb = new StringBuilder();

        attributesWithValuesMap.forEach((attributes, strings) -> {
            switch (attributes) {
                case DocketNumber:
                    sb.append(createStringQueryForOneAttribute("docketNumber", strings));
                    break;
                case Senate:
                    sb.append(createStringQueryForOneAttribute("senate", strings));
                    break;
                case Judges:
                    sb.append(createStringQueryForOneAttribute("judgeList", strings));
                    break;
                case DateVerdict:
                    sb.append(createDataQueryForOneAttribute("dateVerdict", strings));
                    break;
                case ForeDecisionRACCourt:
                    sb.append(createStringQueryForOneAttribute("foreDecisionRACCourt", strings));
                    break;
                case ForeDecisionRACDateVerdict:
                    sb.append(createDataQueryForOneAttribute("foreDecisionRACVerdictDate", strings));
                    break;
                case ForeDecisionRCCourt:
                    sb.append(createStringQueryForOneAttribute("foreDecisionRCCourt", strings));
                    break;
                case ForeDecisionRCDateVerdict:
                    sb.append(createDataQueryForOneAttribute("foreDecisionRCVerdictDate", strings));
                    break;
                case ForeDecisionDCCourt:
                    sb.append(createStringQueryForOneAttribute("foreDecisionDCCourt", strings));
                    break;
                case ForeDecisionDCDateVerdict:
                    sb.append(createDataQueryForOneAttribute("foreDecisionDCVerdictDate", strings));
                    break;
                default:
                    break;
            }
        });

        return sb.toString();
    }

    private String createDataQueryForOneAttribute(String attribute, List<String> strings) {
        if (strings.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        strings.forEach(s -> sb.append(attribute).append(":").append(s).append(" OR "));

        return "(" + sb.substring(0, sb.length() - 4) + ") AND ";
    }

    private String createStringQueryForOneAttribute(String attribute, List<String> strings) {
        // Create String for Strings with whitespace AND between
        if (strings.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        strings.forEach(s -> sb.append("(").append(getQueryForOneString(s, attribute)).append(") OR "));
        //strings.forEach(s -> sb.append(attribute).append(":*").append(s.replace(" ", "\\ ")).append("* OR "));
        return "(" + sb.substring(0, sb.length() - 4) + ") AND ";
    }

    private String getQueryForOneString(String s, String attribute) {
        StringBuilder sb = new StringBuilder();
        Arrays.stream(s.split(" ")).forEach(s1 -> sb.append(attribute).append(":*").append(s1.replace(".", "")
                .replace("/", "* AND " + attribute + ":*")).append("* AND "));
        return sb.substring(0, sb.length() - 4);
    }

    private void putDateInputInMapForAttribute(DataModelAttributes attributes, Input input) {
        if (input.getInputType().equals(InputType.Date)) {
            List<String> savedList = attributesWithValuesMap.get(attributes);
            savedList.add(createStringFromDateInput((DateInput) input));
        }
    }

    private String createStringFromDateInput(DateInput input) {
        return "[" + input.getStart() + " TO " + input.getEnd() + "]";
    }

    private void putStringInputInMapForAttribute(DataModelAttributes attributes, Input input) {
        if (input.getInputType().equals(InputType.String)) {
            List<String> savedList = attributesWithValuesMap.get(attributes);
            savedList.add(((StringInput) input).getValue());
        }
    }

    private void createNewMap() {
        attributesWithValuesMap = new HashMap<>();
        attributesWithValuesMap.put(DocketNumber, new ArrayList<>());
        attributesWithValuesMap.put(Senate, new ArrayList<>());
        attributesWithValuesMap.put(Judges, new ArrayList<>());
        attributesWithValuesMap.put(DateVerdict, new ArrayList<>());
        attributesWithValuesMap.put(DataModelAttributes.ForeDecisionRACCourt, new ArrayList<>());
        attributesWithValuesMap.put(DataModelAttributes.ForeDecisionRACDateVerdict, new ArrayList<>());
        attributesWithValuesMap.put(DataModelAttributes.ForeDecisionRCCourt, new ArrayList<>());
        attributesWithValuesMap.put(DataModelAttributes.ForeDecisionRCDateVerdict, new ArrayList<>());
        attributesWithValuesMap.put(DataModelAttributes.ForeDecisionDCCourt, new ArrayList<>());
        attributesWithValuesMap.put(DataModelAttributes.ForeDecisionDCDateVerdict, new ArrayList<>());
    }
}
