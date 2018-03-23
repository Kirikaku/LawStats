package com.unihh.lawstats.core.mapping;


import com.unihh.lawstats.core.model.Verdict;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Important:
 * If there are no Entities coming from Watson for a category, we handle this as follows:
 * <p>
 * Aktenzeichen:    gives an error
 * Richter:         returns empty String Array
 * Datum:           return "emtpy" Date (Date is 0 -> 1970)
 * Gericht:         returns an empty string
 * (Gerichtsdatum): will not be returned for now
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

        // List for the entities
        List<String> docketnumberL = new ArrayList<>();
        Set<String> judgeL = new HashSet<>();
        List<String> dateVerdictList = new ArrayList<>();
        List<String> foreDecRACcL = new ArrayList<>();
        List<String> foreDecRCcL = new ArrayList<>();
        List<String> foreDecDCcL = new ArrayList<>();

        // Iterating through the JSON Array
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObjectEntity = jsonArray.getJSONObject(i);
            String type = jsonObjectEntity.getString("type");

            //Entity Query | Sorting into the assigned list
            switch (type) {
                case "Aktenzeichen":
                    docketnumberL.add(jsonObjectEntity.getString("text"));
                    break;
                case "Richter":
                    judgeL.add(jsonObjectEntity.getString("text"));
                    break;
                case "Datum":
                    dateVerdictList.add(jsonObjectEntity.getString("text"));
                    break;
                case "Gericht":
                    // Checkes every upcoming case of foredecisions
                    // e.g. Oberlandsgericht, Oberlandesgericht, Oberlandesgerichts, OLG
                    String[] words = jsonObjectEntity.getString("text").toLowerCase().split("\\s");
                    if ((words[0].contains("oberland") &&
                            (words[0].endsWith("gericht") ||
                                    words[0].endsWith("gerichts")))) {
                        foreDecRACcL.add("oberlandesgericht " + words[1]);
                    } else if (words[0].contains("olg")) {
                        foreDecRACcL.add("oberlandesgericht " + words[1]);

                    } else if ((words[0].contains("land") &&
                            (words[0].endsWith("gericht") ||
                                    words[0].endsWith("gerichts")))) {
                        foreDecRCcL.add("landesgericht " + words[1]);
                    } else if (words[0].contains("lg")) {
                        foreDecRACcL.add("landesgericht " + words[1]);
                    } else if ((words[0].contains("amt") &&
                            (words[0].endsWith("gericht") ||
                                    words[0].endsWith("gerichts")))) {
                        foreDecRCcL.add("amtsgericht " + words[1]);
                    }
                    else if(words[0].contains("ag")) {
                        foreDecRCcL.add("amtsgericht " + words[1]);
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
        if (!dateVerdictList.isEmpty()) {
            verdict.setDateVerdict(filterNewestDate(dateVerdictList));
        }

        //Oberlandesgericht - Court
        if (!foreDecRACcL.isEmpty()) {
            verdict.setForeDecisionRACCourt(mostCommon(foreDecRACcL));
        }

        //Landesgericht - Court
        if (!foreDecRCcL.isEmpty()) {
            verdict.setForeDecisionRCCourt(mostCommon(foreDecRCcL));
        }

        //Amtsgericht - Court
        if (!foreDecDCcL.isEmpty()) {
            verdict.setForeDecisionDCCourt(mostCommon(foreDecDCcL));
        }

        //Entscheidungssätze
        //verdict.setDecisionSentences(null); only set array when there is really a string, otherwise null
        return verdict;
    }


    /**
     * Helper: searches the Element in a List of Strings, which occurs the most
     * Case of same cardinality: selects the Last Element
     *
     * @param typelist - Liste von Strings e.g. JudgeList
     * @return - the most common Element from the List
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
     * Trims a string and sets it to lower case
     *
     * @param string The string that should be normalized
     * @return The normalized string
     */
    private String normalizeString(String string) {
        string = string.trim();
        return string.toLowerCase();
    }


    /**
     * filter the newest date (nearest date to today)
     *
     * @param stringL dates in string
     * @return the newest date-long
     */
    private Long filterNewestDate(List<String> stringL) {
        VerdictDateFormatter verdictDateFormatter = new VerdictDateFormatter();
        // Empfängt eine Liste und gibt dabei das neueste Datum zurück.
        List<Long> dateVerdicts;
        dateVerdicts = verdictDateFormatter.formateStringDateToLongList(stringL).stream().filter(Objects::nonNull).collect(Collectors.toList());
        if (dateVerdicts.isEmpty()) {
            return null;
        }
        Optional<Long> optionalLong = dateVerdicts.stream().max(Long::compareTo);
        return optionalLong.orElse(0L);

    }

    /**
     * Sets the relationship from DocketNumbers to Senates,
     * because we get the Senate from our DocketNumber
     */
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

    /**
     * Extracts the Senate Name out of the DocketNumber
     *
     * @param string        - The chosen Docketnumber of the Verdict
     * @param docketNumList - The List of all Docketnumbers in the Verdict
     * @return if method found a matching senate -> Senate | else: empty string
     */
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
            }

            return finalElement;
        }
        return "";
    }

    /**
     * This method set the minimum date in the given text for the last verdict foreDecisionCourt which is available
     *
     * @param text    the string where should be the date
     * @param verdict the verdict which the text is related
     */
    public void setMinimumDateForLastForeDecision(String text, Verdict verdict) {
        if (text == null || verdict == null) {
            return; // we dont want Nullpointerexceptions
        }

        VerdictDateFormatter verdictDateFormatter = new VerdictDateFormatter();
        AtomicLong minDate = new AtomicLong();
        Arrays.stream(text.split(" ")).filter(s -> !s.isEmpty()) //Drop all empty strings
                .map(verdictDateFormatter::formatStringToLong) // map all strings to an date
                .filter(Objects::nonNull) //drop all nulls (no date)
                .min(Long::compareTo) //get minimum date
                .ifPresent(minDate::set); //set date if present

        if (verdict.getForeDecisionRACCourt() != null) {
            verdict.setForeDecisionRACVerdictDate(minDate.get());
        } else if (verdict.getForeDecisionRCCourt() != null) {
            verdict.setForeDecisionRCVerdictDate(minDate.get());
        } else if (verdict.getForeDecisionDCCourt() != null) {
            verdict.setForeDecisionRCVerdictDate(minDate.get());
        }

    }

}