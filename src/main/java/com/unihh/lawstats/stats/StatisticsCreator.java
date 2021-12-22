package com.unihh.lawstats.stats;

import com.unihh.lawstats.core.model.Verdict;
import org.joda.time.DateTime;

import java.util.*;

public class StatisticsCreator {


    //List of all verdicts in the database
    private List<Verdict> _allVerdicts;


    public StatisticsCreator(List<Verdict> verdicts){
        _allVerdicts = verdicts;
    }


    /**
     * Method to create a Statistic Object on the specified verdict object.
     * @param attributeName The name of the attribute over which tzhe statistic is made
     * @return a statistic object with the aggregations over the revisionoutcome
     */
    public AttributeStatistic createAttributeStatistic(String attributeName){
        List<ValueAggregation> valueAggregations = aggregateRevisionOutcome(attributeName);
        int counter = 0;

        AttributeStatistic attributeStatistic = new AttributeStatistic(valueAggregations.size());
        String[] labels = attributeStatistic.getLabels();
        int[] succesfullRevisions = attributeStatistic.getSuccesfullRevisions();
        int[] unsuccesfullRevisions = attributeStatistic.getUnsuccesfullRevisions();
        double[] outcomeRatio = attributeStatistic.getOutcomeRatio();


        for(ValueAggregation valueAggregation: valueAggregations){
            labels[counter] = valueAggregation.getValue();
            succesfullRevisions[counter] = valueAggregation.getSuccesfullRevisions();
            unsuccesfullRevisions[counter] = valueAggregation.getUnsuccesfullRevisions();
            outcomeRatio[counter] = valueAggregation.getOutcomeRatio();

            counter++;
        }

        return attributeStatistic;
    }


    /**
     * Aggregates the revisionOutcomes for each value class (e.g. OLG Hamburg).
     * @param attributeName The attribute Name over which the value classes are determined
     * @return A List of revisionOutcome aggregations. One element corresponds to one value class.
     */
    private List<ValueAggregation> aggregateRevisionOutcome(String attributeName){
        Map<String, ValueAggregation> aggregatedValues = new HashMap<>();

        for(Verdict verdict: _allVerdicts){

            Object attributeValue = determineAndGetAttributeValue(attributeName, verdict);
            boolean isVerdictRelevant = isVerdictRelevant(attributeName, attributeValue);


            if(isVerdictRelevant){

               String attributeValueString = normalizeAttributeValue(attributeName, attributeValue);
                ValueAggregation valueAggregation = aggregatedValues.get(attributeValueString);


                if(valueAggregation == null){
                    valueAggregation = new ValueAggregation(attributeValueString);
                    aggregatedValues.put(attributeValueString, valueAggregation);
                }


                if(verdict.getRevisionSuccess() == 1){
                    valueAggregation.incrementSuccesfullRevisions();
                }
                else if(verdict.getRevisionSuccess() == -1){
                    valueAggregation.incrementUnsuccesfullRevisions();
                }
            }
        }

        List<ValueAggregation> valueAggregations = new ArrayList<ValueAggregation>(aggregatedValues.values());
        valueAggregations = calculateOutcomeRatios(valueAggregations);

        return valueAggregations;
    }


    /**
     * Determines which is the desired attribute and returns the corresponding value.
     * @param attributeName The name that specifies which attribute shall be used.
     * @param verdict The verdict from which the value is retrieved.
     * @return The value of the desired attribute
     */
    private Object determineAndGetAttributeValue(String attributeName, Verdict verdict){

        Object attributeValue = null;

        if(attributeName.equals("olgs")){
           attributeValue = verdict.getForeDecisionRACCourt();
        }
        else if(attributeName.equals("revisionDate")){
            attributeValue = verdict.getDateVerdict();
        }

        return attributeValue;
    }


    /**
     * Determines if the verdict is relevant for the statistics based on the attributeName.
     * @param attributeName determines which attribute is used-
     * @param attributeValue the value of the attribute
     * @return true if verdict is relevant, false otherwise
     */
    private boolean isVerdictRelevant(String attributeName, Object attributeValue){

        boolean isVerdictRelevant = false;

        if(attributeName.equals("olgs")) {
            String court = (String) attributeValue;

            isVerdictRelevant = court != null && !court.equals("")
                    && (court.contains("olg") || court.contains("oberlandesgericht"));
        }
        else if(attributeName.equals("revisionDate")){
            DateTime date = null;

            if(attributeValue != null) {
                date = new DateTime((long) attributeValue);
            }

            isVerdictRelevant = attributeValue != null && date.getYear() <= 2018;
        }

        return isVerdictRelevant;
    }


    /**
     * Returns a normalized String based on the attributeName.
     * e.g. "Oberlandesgericht" becomes "olg"
     * @param attributeName specifies which attribute is used
     * @param attributeValue the original value of the attribute
     * @return the normalized string of the original attribute value.
     */
    private String normalizeAttributeValue(String attributeName, Object attributeValue){

        String attributeValueString = null;


        if(attributeName.equals("olgs")){
            attributeValueString = (String) attributeValue;

            if(attributeValueString.contains("oberlandesgerichts")){
                attributeValueString = attributeValueString.replace("oberlandesgerichts", "olg");
            }
            else if(attributeValueString.contains("oberlandesgericht")){
                attributeValueString = attributeValueString.replace("oberlandesgericht", "olg");
            }
        }
        else if(attributeName.equals("revisionDate")){

            long revisionDateLong = (long) attributeValue;
            Calendar revisionDate = Calendar.getInstance();
            revisionDate.setTimeInMillis(revisionDateLong);

            attributeValueString = String.valueOf(revisionDate.get(Calendar.YEAR));
        }

        return attributeValueString;
    }


    /**
     *
     * @param valueAggregations
     * @return
     */
    private List<ValueAggregation> calculateOutcomeRatios(List<ValueAggregation> valueAggregations){

        for(ValueAggregation valueAggregation : valueAggregations){
            if(valueAggregation.getUnsuccesfullRevisions() != 0){
                double unsuccesfullRevisions = (double) valueAggregation.getUnsuccesfullRevisions();
                double ratio = valueAggregation.getSuccesfullRevisions()/unsuccesfullRevisions;
                valueAggregation.setOutcomeRatio(ratio);
            }
            else{
                valueAggregation.setOutcomeRatio(valueAggregation.getSuccesfullRevisions());
            }
        }

        return valueAggregations;
    }




}
