package com.unihh.lawstats.core.mapping;


import com.unihh.lawstats.core.model.MappingConstants;
import com.unihh.lawstats.core.model.Verdict;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Wichtig:
 * Wenn von Watson keine Entitäten zurück kommt, reagiert der Mapper wie folgt:
 *
 * Aktenzeichen:    "NOTFOUND" - wird später vom AnalyzingCoordinator behandelt
 * Richter:         übergibt leeres String Array
 * //TODO datum klären
 * Datum:           übergibt leeres Datum (0) - 1970?
 * Gericht:         übergibt leeren String
 * (Gerichtsdatum): wird vorerst nicht übergeben
 */
public class Mapper {


    //Liste von Senaten
    private Map<String, String> senateMap = new HashMap();

    public Mapper() {
        setSenates();
    }


    /**
     * Überführt einen String, welcher mit JSON Daten gefüllt ist in ein Verdict Objekt
     *
     * @param jsonText - String, welcher die von Watson ermittelten Entities etc. enthält
     * @return ein Verdict Object, mit den parameterisierten Daten der Entities
     * @throws ParseException - ja, kann passieren
     */
    public Verdict mapJSONStringToVerdicObject(String jsonText) throws ParseException {


        JSONObject json = new JSONObject(jsonText);
        JSONArray jsonArray = json.getJSONArray("entities");

        // Listen für die einzelnen Entities
        List<String> docketnumberL = new ArrayList<>();
        // TODO List Revision Success
        //List<String> senateL = new ArrayList<>();
        Set<String> judgeL = new HashSet<>();
        List<String> dateverdictL = new ArrayList<>();
        List<String> foreDecRACcL = new ArrayList<>();
        List<String> foreDecRACdvL = new ArrayList<>();
        List<String> foreDecRCcL = new ArrayList<>();
        List<String> foreDecRCdvL = new ArrayList<>();
        List<String> foreDecDCcL = new ArrayList<>();
        List<String> foreDecDCdvL = new ArrayList<>();

        // Iterieren durch das JSON Array
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObjectEntity = jsonArray.getJSONObject(i);
            String type = jsonObjectEntity.getString("type");

            //Abfrage des Inhalts | Einsortieren in die zugehörige Liste
            switch (type) {
                case "Aktenzeichen":
                    docketnumberL.add(jsonObjectEntity.getString("text"));
                    break;
                case "Richter":
                    judgeL.add(jsonObjectEntity.getString("text"));
                    break;
                case "Datum":
                    dateverdictL.add(jsonObjectEntity.getString("text"));
                    break;
                case "Gericht":
                    if (jsonObjectEntity.getString("text").contains("Oberlandesgericht")) {
                        foreDecRACcL.add(jsonObjectEntity.getString("text"));
                    } else if (jsonObjectEntity.getString("text").contains("Landesgericht"))
                        foreDecDCcL.add(jsonObjectEntity.getString("text"));
                    else if (jsonObjectEntity.getString("text").contains("Amtsgericht")) {
                        foreDecDCcL.add(jsonObjectEntity.getString("text"));
                        // Bei else passiert hier nichts, Liste bleibt leer - wird beim verdict.set... unten aufgefangen
                        break;
                    }
            }
        }
            // Creating a new Verdict Object
            // Using Date for Date-Elements
            Verdict verdict = new Verdict();

            //Docket Number
            if (!docketnumberL.isEmpty()) {
                verdict.setDocketNumber(mostCommon(docketnumberL));
            } else {
                verdict.setDocketNumber(MappingConstants.VerdictDocketNumberNotFound.getValue());
            }
            //Senate
            if (!verdict.getDocketNumber().isEmpty()) {
                verdict.setSenate(getSenateFromDocketNumber(verdict.getDocketNumber(), docketnumberL));
            } else {
                verdict.setSenate("");
            }
            //Judges
            if (!judgeL.isEmpty()) {
                String[] judgeList = judgeL.toArray(new String[judgeL.size()]);
                verdict.setJudgeList(judgeList);
            } else {
                verdict.setJudgeList(new String[0]);
            }

            //Date Verdict
            if (!dateverdictL.isEmpty()) {
                verdict.setDateVerdict(filterNewestDate(dateverdictL));
            } else {
                //TODO checken wann ist das?
                verdict.setDateVerdict((long) 0);
            }

            //Oberlandesgericht - Court
            if (!foreDecRACcL.isEmpty()) {
                verdict.setForeDecisionRACCourt(mostCommon(foreDecRACcL));
            } else {
                verdict.setForeDecisionRACCourt("");
            }
            //Oberlandesgericht - Datum
            if (!foreDecRACdvL.isEmpty()) {
                //verdict.setForeDecisionRACVerdictDate((filterNewestDate(foreDecRACdvL)));
                verdict.setForeDecisionRACVerdictDate((long) 0);
            } else {
                verdict.setForeDecisionRACVerdictDate((long) 0);
            }
            //Landesgericht - Court
            if (!foreDecRCcL.isEmpty()) {
                verdict.setForeDecisionRCCourt(mostCommon(foreDecRCcL));
            } else {
                verdict.setForeDecisionRCCourt("");
            }
            //Landesgericht - Date
            if (!foreDecRCdvL.isEmpty()) {
                verdict.setForeDecisionRCVerdictDate(filterNewestDate(foreDecRCdvL));
            } else {
                verdict.setForeDecisionRCVerdictDate((long) 0);
            }

            //Amtsgericht - Court
            if (!foreDecDCcL.isEmpty()) {
                verdict.setForeDecisionDCCourt(mostCommon(foreDecDCcL));
            } else {
                verdict.setForeDecisionDCCourt("");
            }
            //Amtsgericht - Date
            if (foreDecDCdvL.isEmpty()) {
                verdict.setForeDecisionDCVerdictDate(filterNewestDate((foreDecDCdvL)));
            } else {
                verdict.setForeDecisionDCVerdictDate((long) 0);
            }

            //Entscheidungssätze
            verdict.setDecisionSentences(new String[0]);
            //sout nur für debugger benötigt
            //System.out.println("");
            return verdict;
        }


    /**
     * Hilfsmethode, sucht das Element einer Stringliste, welches am häufigsten vorkommt.
     * Bei gleicher Kardinalität: Das letzte Element (entspricht random auswahl)
     *
     * @param typelist - Liste von Strings
     * @return - das häugiste Element
     */
    private String mostCommon(List<String> typelist) {
        if (!typelist.isEmpty()) {
            Map<String, Integer> map = new HashMap<>();
            for (String string : typelist) {
                string = normalizeString(string);
                Integer val = map.get(string);
                map.put(string, val == null ? 0 : val + 1);
            }
            Map.Entry<String, Integer> max = null;

            for (Map.Entry<String, Integer> e : map.entrySet()) {
                if (max == null || e.getValue() > max.getValue()) {
                    max = e;
                }
            }
            return max.getKey();
        } else {
            return null;
        }

    }

    /**
     * Trimmt und setzt einen String auf lower case
     *
     * @param string
     * @return
     */
    private String normalizeString(String string) {
        string = string.trim();
        return string.toLowerCase();
    }


    /**
     * Filtert aus einer Stringliste das am kuerzesten zurückliegende/ jüngste Datum
     *
     * @param stringL Stringliste
     * @return
     * @throws ParseException
     */
    private Long filterNewestDate(List<String> stringL) throws ParseException {
        VerdictDateFormatter verdictDateFormatter = new VerdictDateFormatter();
        // Empfängt eine Liste und gibt dabei das neueste Datum zurück.
        List<Long> dateVerdicts;
        dateVerdicts = verdictDateFormatter.formateStringDateToLongList(stringL);
        Optional<Long> optionalLong = dateVerdicts.stream().max(Long::compareTo);
        return optionalLong.orElse(0L);
    }

    private void setSenates() {
        //Zivilsenate
        senateMap.put("I", "1. Zivilsenat");
        senateMap.put("II", "2. Zivilsenat");
        senateMap.put("III", "3. Zivilsenat");
        senateMap.put("IV", "4. Zivilsenat");
        senateMap.put("V", "5. Zivilsenat");
        senateMap.put("VI", "6. Zivilsenat");
        senateMap.put("VII", "7. Zivilsenat");
        senateMap.put("VIII", "8. Zivilsenat");
        senateMap.put("IX", "9. Zivilsenat");
        senateMap.put("X", "10. Zivilsenat");
        senateMap.put("XI", "11. Zivilsenat");
        senateMap.put("XII", "12. Zivilsenat");
        // Strafsenate
        senateMap.put("1", "1. Strafsenat");
        senateMap.put("2", "2. Strafsenat");
        senateMap.put("3", "3. Strafsenat");
        senateMap.put("4", "4. Strafsenat");
        senateMap.put("5", "5. Strafsenat");

        //weitere Senate
        //TODO von Dirk die restlichen Bezeichnungen bekommen
        senateMap.put("", "");
    }

    private String getSenateFromDocketNumber(String string, List<String> docketNumList) {
        // Senat ermitteln
        Pattern civilpenalSenPat = Pattern.compile("([I+|IV|V|VI|VII|VIII|IX|X|XI|XII|1-6]+)\\s[A-Za-z()]{2,20}\\s\\d+/\\d\\d");
        Pattern otherSenPat = Pattern.compile("(VGS|RiZ\\s?s?(R)|KZR|VRG|RiZ|EnRB|StbSt\\s?(B)|AnwZ\\s?(Brfg)|RiSt|PatAnwSt\\s?(R)|AnwZ\\s?(B)|PatAnwZ|EnVZ|AnwSt\\s?(B)|NotSt\\s?(Brfg)|KVZ|KZB|AR\\s?(Ri)|NotZ\\s?(Brfg)|RiSt\\s?(B)|AnwZ\\s?(P)|EnZB|RiSt\\s?(R)|NotSt\\s?(B)|AnwSt|WpSt\\s?(R)|KVR|AR\\s?(Kart)|EnZR|StbSt\\s?(R)|WpSt\\s?(B)|KZA|AR\\s?(Enw)|AnwSt\\s?(R)|KRB|RiZ\\s?(B)|PatAnwSt\\s?(B)|EnVR|AnwZ|NotZ|EnZA|AR)\\s\\d+/\\d+");

        for (String docketstring : docketNumList) {
            Matcher m = civilpenalSenPat.matcher(docketstring);

            if (m.find()) {
                String senateElement = m.group(1);
                return senateMap.get(senateElement);
            } else if (Pattern.matches(String.valueOf(otherSenPat), docketstring)) {
                String senateElement = m.group(1);
                return senateMap.get(senateElement);

            } else {
                //TODO was passiert wenn nur nicht matcht
                return null;
            }
        }
        return null;
    }

}