package com.unihh.lawstats.core.mapping;


import com.unihh.lawstats.core.model.ForeDecision;
import com.unihh.lawstats.core.model.Verdict;
import org.apache.commons.io.IOUtils;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.io.*;
import java.util.*;

public class Mapper {


    public static void main(String[] args) throws JSONException, IOException {

        File testfile = new File("src/main/resources/testData/JsonMappingTestData.json");
        if (testfile.exists()) {
            InputStream is = new FileInputStream(testfile);
            String jsonTxt = IOUtils.toString(is, "UTF-8");
            JSONObject json = new JSONObject(jsonTxt);
            JSONArray jsonArray = json.getJSONArray("entities");

            // Listen für die einzelnen Entities
            List<String> docketnumberL = new ArrayList<>();
            // TODO List Revision Success
            List<String> senateL = new ArrayList<>();
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
                    case "DocketNumber":
                        docketnumberL.add(jsonObjectEntity.toString());
                        break;
                    //TODO Revision Success Case
                    case "Senate":
                        senateL.add(jsonObjectEntity.toString());
                        break;
                    case "Judges":
                        //judgeL.toArray(new String[judgeL.size()]);
                        judgeL.add(jsonObjectEntity.toString());
                        break;
                    case "DateVerdict":
                        dateverdictL.add(jsonObjectEntity.toString());
                        break;
                    case "ForeDecisionRACCourt":
                        foreDecRACcL.add(jsonObjectEntity.toString());
                        break;
                    case "ForeDecisionRACDateVerdict":
                        foreDecRACdvL.add(jsonObjectEntity.toString());
                        break;
                    case "ForeDecisionRCCourt":
                        foreDecRCcL.add(jsonObjectEntity.toString());
                        break;
                    case "ForeDecisionRCDateVerdict":
                        foreDecRCdvL.add(jsonObjectEntity.toString());
                        break;
                    case "ForeDecisionDCCourt":
                        foreDecDCcL.add(jsonObjectEntity.toString());
                        break;
                    case "ForeDecisionDCDateVerdict":
                        foreDecDCdvL.add(jsonObjectEntity.toString());
                        break;
                }
            }
            // Creating a new Verdict Object
            // TODO Date Format klären für die Verdict Dates (commented) und s. u.
            Verdict verdict = new Verdict();
            verdict.setDocketNumber(mostCommon(docketnumberL));
            // TODO revisionSuccess
            verdict.setSenate(mostCommon(senateL));
            verdict.setJudgeSet(judgeL);
            //verdict.setDateVerdict(newestDate(dateverdictL));

            // New ForeDecision Object for foreDecisionRAC - Oberlandesgericht
            ForeDecision rac = new ForeDecision();
            rac.setCourt(mostCommon(foreDecRACcL));
            //rac.setDateVerdict(); - benötigt Date Type
            verdict.setForeDecisionRAC(rac);

            // New Fore Decision Object for foreDecisionRC - Landesgericht
            ForeDecision rc = new ForeDecision();
            rc.setCourt(mostCommon(foreDecRCcL));
            //rc.setDateVerdict();
            verdict.setForeDecisionRC(rc);

            // New Fore Decision Object for foreDecisionRC - Amtsgericht
            ForeDecision dc = new ForeDecision();
            dc.setCourt(mostCommon(foreDecDCcL));
            //dc.setDateVerdict();
            verdict.setForeDecisionDC(dc);
            System.out.println("");
        }
    }

    //TODO benötigt finalen Datentyp Date oder Localedate
    private static Date newestDate(List<String> dateverdict) {
        return null;
    }


    private static String mostCommon(List<String> typelist) {
        Map<String, Integer> map = new HashMap<>();
        for (String i : typelist) {
            Integer val = map.get(i);
            map.put(i, val == null ? 0 : val + 1);
        }
        Map.Entry<String, Integer> max = null;

        for (Map.Entry<String, Integer> e : map.entrySet()) {
            if (max == null || e.getValue() > max.getValue()) {
                max = e;
            }
        }
        return max.getKey();
    }


}



