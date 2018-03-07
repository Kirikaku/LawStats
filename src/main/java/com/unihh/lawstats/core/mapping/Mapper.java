package com.unihh.lawstats.core.mapping;


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
 * <p>
 * Aktenzeichen:    "NOTFOUND" - wird später vom AnalyzingCoordinator behandelt
 * Richter:         übergibt leeres String Array
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
     * creates a verdict object of given jsonText
     *
     * @param jsonText - the json text in string
     * @return the verdict object from the json object, when a exce
     * @throws NoDocketnumberFoundException when no Docketnumber is in the jsonText
     */
    public Verdict mapJSONStringToVerdicObject(String jsonText) throws NoDocketnumberFoundException {

        JSONObject json = new JSONObject(jsonText);
        JSONArray jsonArray = json.getJSONArray("entities");

        // Listen für die einzelnen Entities
        List<String> docketnumberL = new ArrayList<>();
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
        // Creating new Verdict-object
        // When one attribute is not available, we dont set an empty value, we let them null
        Verdict verdict = new Verdict();

        //Docket Number (If not available, return null)
        if (!docketnumberL.isEmpty()) {
            verdict.setDocketNumber(mostCommon(docketnumberL));
        } else {
            throw new NoDocketnumberFoundException("no Docketnumber found for given json: " + jsonText);
        }

        //Senate
        if (!verdict.getDocketNumber().isEmpty()) {
            verdict.setSenate(getSenateFromDocketNumber(verdict.getDocketNumber(), docketnumberL));
        }

        //Judges
        if (!judgeL.isEmpty()) {
            String[] judgeList = judgeL.toArray(new String[judgeL.size()]);
            verdict.setJudgeList(judgeList);
        }

        //Date Verdict
        if (!dateverdictL.isEmpty()) {
            verdict.setDateVerdict(filterNewestDate(dateverdictL));
        }

        //Oberlandesgericht - Court
        if (!foreDecRACcL.isEmpty()) {
            verdict.setForeDecisionRACCourt(mostCommon(foreDecRACcL));
        }

        //Oberlandesgericht - Datum
        if (!foreDecRACdvL.isEmpty()) {
            verdict.setForeDecisionRACVerdictDate((filterNewestDate(foreDecRACdvL)));
        }

        //Landesgericht - Court
        if (!foreDecRCcL.isEmpty()) {
            verdict.setForeDecisionRCCourt(mostCommon(foreDecRCcL));
        }

        //Landesgericht - Date
        if (!foreDecRCdvL.isEmpty()) {
            verdict.setForeDecisionRCVerdictDate(filterNewestDate(foreDecRCdvL));
        }

        //Amtsgericht - Court
        if (!foreDecDCcL.isEmpty()) {
            verdict.setForeDecisionDCCourt(mostCommon(foreDecDCcL));
        }

        //Amtsgericht - Date
        if (!foreDecDCdvL.isEmpty()) {
            verdict.setForeDecisionDCVerdictDate(filterNewestDate((foreDecDCdvL)));
        }

        //Entscheidungssätze
        //verdict.setDecisionSentences(null); only set array when there is really a string, otherwise null
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
     * filtered the newest date
     *
     * @param stringL dates in string
     * @return the newest date-long
     */
    private Long filterNewestDate(List<String> stringL) {
        VerdictDateFormatter verdictDateFormatter = new VerdictDateFormatter();
        // Empfängt eine Liste und gibt dabei das neueste Datum zurück.
        List<Long> dateVerdicts;
        dateVerdicts = verdictDateFormatter.formateStringDateToLongList(stringL);
        Optional<Long> optionalLong = dateVerdicts.stream().max(Long::compareTo);
        return optionalLong.orElse(0L);
    }

    private void setSenates() {
        //Zivilsenate
        senateMap.put("I", "1. ");
        senateMap.put("II", "2. ");
        senateMap.put("III", "3. ");
        senateMap.put("IV", "4. ");
        senateMap.put("V", "5. ");
        senateMap.put("VI", "6. ");
        senateMap.put("VII", "7. ");
        senateMap.put("VIII", "8. ");
        senateMap.put("IX", "9. ");
        senateMap.put("X", "10. ");
        senateMap.put("XI", "11. ");
        senateMap.put("XII", "12. ");
        // Strafsenate
        senateMap.put("1", "1. ");
        senateMap.put("2", "2. ");
        senateMap.put("3", "3. ");
        senateMap.put("4", "4. ");
        senateMap.put("5", "5. ");
        //weitere Senate
        senateMap.put("AK", "3. Strafsenat");
        senateMap.put("AnwSt", "Senat für Anwaltssachen");
        senateMap.put("AnwSt (B)", "Senat für Anwaltssachen");
        senateMap.put("AnwSt (R)", "Senat für Anwaltssachen");
        senateMap.put("AnwZ", "Senat für Anwaltssachen");
        senateMap.put("AnwZ (B)", "Senat für Anwaltssachen");
        senateMap.put("AnwZ (Brfg)", "Senat für Anwaltssachen");
        senateMap.put("AnwZ (P)", "Senat für Anwaltssachen");
        senateMap.put("AR", "Allgemeine Register");
        senateMap.put("AR (Enw)", "Generalbundesanwalt");
        senateMap.put("AR (Kart)", "Generalbundesanwalt");
        senateMap.put("AR (Ri)", "Dienstgericht des Bundes");
        senateMap.put("AR (Vollz)", "5. Strafsenat");
        senateMap.put("AR (VS)", "5. Strafsenat");
        senateMap.put("AR (VZ)", "Zivilsenat");
        senateMap.put("ARP", "Generalbundesanwalt");
        senateMap.put("ARs", "Strafsenat");
        senateMap.put("ARZ", "Zivilsenat");
        senateMap.put("BAusl", "4. Strafsenat");
        senateMap.put("BGns", "Generalbundesanwalt");
        //bisher 20
        senateMap.put("BGs", "Ermittlungsrichter");
        senateMap.put("BJs", "Generalbundesanwalt");
        senateMap.put("BLw", "Senat für Landwirtschaftssachen");
        senateMap.put("EnRB", "Kartellsenat");
        senateMap.put("EnVR", "Kartellsenat");
        senateMap.put("EnVZ", "Kartellsenat");
        senateMap.put("EnZA", "Kartellsenat");
        senateMap.put("EnZB", "Kartellsenat");
        senateMap.put("EnZR", "Kartellsenat");
        senateMap.put("GSSt", "Großer Senat für Strafsachen");
        senateMap.put("GSZ", "Großer Senat für Zivilsachen");
        senateMap.put("KRB", "Kartellsenat");
        senateMap.put("KVR", "Kartellsenat");
        senateMap.put("KVZ", "Kartellsenat");
        senateMap.put("KZA", "Kartellsenat");
        senateMap.put("KZB", "Kartellsenat");
        senateMap.put("KZR", "Kartellsenat");
        senateMap.put("LwZA", "Senat für Landwirtschaftssachen");
        senateMap.put("LwZB", "Senat für Landwirtschaftssachen");
        senateMap.put("LwZR", "Senat für Landwirtschaftssachen");
        //bisher 40
        senateMap.put("NotSt(B)", "Senat für Notarsachen");
        senateMap.put("NotSt(Brfg)", "Senat für Notarsachen");
        senateMap.put("NotZ", "Senat für Notarsachen");
        senateMap.put("NotZ(Brfg)", "Senat für Notarsachen");
        senateMap.put("PatAnwSt(B)", "Senat für Patentanwaltssachen");
        senateMap.put("PatAnwSt(R)", "Senat für Patentanwaltssachen");
        senateMap.put("PatAnwZ", "Senat für Patentanwaltssachen");
        senateMap.put("RiSt", "Dienstgericht des Bundes");
        senateMap.put("RiSt(B)", "Dienstgericht des Bundes");
        senateMap.put("RiSt(R)", "Dienstgericht des Bundes");
        senateMap.put("RiZ", "Dienstgericht des Bundes");
        senateMap.put("RiZ(B)", "Dienstgericht des Bundes");
        senateMap.put("RiZ(R)", "Dienstgericht des Bundes");
        senateMap.put("StB", "3. Strafsenat");
        senateMap.put("StbSt(B)", "Senat für Steuerberater- und Steuerbevollmächtigtensachen");
        senateMap.put("StbSt(R)", "Senat für Steuerberater- und Steuerbevollmächtigtensachen");
        senateMap.put("StE", "Strafsenat");
        senateMap.put("StR", "Strafsenat");
        senateMap.put("VGS", "Vereinigte große Senate");
        senateMap.put("VRG", "1. Zivilsenat");
        //bisher 60
        senateMap.put("WpSt(B)", "Senat für Wirtschaftsprüfersachen");
        senateMap.put("WpSt(R)", "Senat für Wirtschaftsprüfersachen");
        senateMap.put("ZA", "Zivilsenat");
        senateMap.put("ZB", "Zivilsenat");
        senateMap.put("ZR", "Zivilsenat");
        senateMap.put("ZR(Ü)", "Zivilsenat");
        //66 Stück

        //senateMap.put("", "");
    }

    private String getSenateFromDocketNumber(String string, List<String> docketNumList) {
        // Senat ermitteln
        Pattern senPattern1 = Pattern.compile("([IXV|1-6]+)\\s([A-Za-z()]{2,20})\\s\\d+/\\d\\d");
        Pattern senPattern2 = Pattern.compile("([VGS|RiZ\\s?s?(R)|KZR|VRG|AK|RiZ|EnRB|StbSt\\s?(B)|AnwZ\\s?(Brfg)|RiSt|PatAnwSt\\s?(R)|AnwZ\\s?(B)|PatAnwZ|EnVZ|AnwSt\\s?(B)|NotSt\\s?(Brfg)|KVZ|KZB|AR\\s?(Ri)|NotZ\\s?(Brfg)|RiSt\\s?(B)|AnwZ\\s?(P)|EnZB|RiSt\\s?(R)|NotSt\\s?(B)|AnwSt|WpSt\\s?(R)|KVR|AR\\s?(Kart)|EnZR|StbSt\\s?(R)|WpSt\\s?(B)|KZA|AR\\s?(Enw)|AnwSt\\s?(R)|KRB|RiZ\\s?(B)|PatAnwSt\\s?(B)|EnVR|AnwZ|NotZ|EnZA|AR]{2,20})\\s\\d+/\\d\\d");


        for (String docketstring : docketNumList) {
            Matcher m = senPattern1.matcher(docketstring);
            Matcher m3 = senPattern2.matcher(docketstring);
            String senateElement1 = "";
            String senateElement2 = "";
            String finalElement = "";

            if (m.find()) {
                senateElement1 = m.group(1);
            }
            if (m3.find()) {
                senateElement2 = m3.group(1);
            }

            if (!senateElement1.isEmpty() && !senateElement2.isEmpty()) {
                finalElement = senateMap.get(senateElement1.trim()) + senateMap.get(senateElement2.trim());
            } else if (senateElement1.isEmpty() && !senateElement2.isEmpty()) {
                finalElement = senateMap.get(senateElement2.trim());
            } else if (!senateElement1.isEmpty() && senateElement2.isEmpty()) {
                finalElement = senateMap.get(senateElement1.trim());
            } else if (senateElement1.isEmpty() && senateElement2.isEmpty()) {
                //finalElement = null;
            }

            return finalElement;
        }
        return null;
//
//            if (m.find()) {
//                String senateElement1 = m.group(1);
//                String senateElement2 = m.group(2);
//                String finalElement = senateMap.get(senateElement1) + senateMap.get(senateElement2);
//                return finalElement;
//
//            } else if (m2.find()/*Pattern.matches(String.valueOf(otherSenPat), docketstring)*/) {
//                System.out.println(m2.matches());
//                String senateElement3 = m2.group(1);
//                System.out.println("wuff");
//                return senateMap.get(senateElement3);
//
//            } else {
//                return null;
//            }
    }

}