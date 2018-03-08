package com.unihh.lawstats.core.mapping;

import com.unihh.lawstats.core.model.Verdict;

import java.io.File;

public class MapperTest {

    public static void main(String[] args) {
        String jsonString = "{\n" +
                "  \"language\": \"de\",\n" +
                "  \"entities\": [\n" +
                "    {\n" +
                "      \"type\": \"Aktenzeichen\",\n" +
                "      \"count\": 1,\n" +
                "      \"text\": \"VIII ZB 666/13\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"type\": \"Richter\",\n" +
                "      \"count\": 1,\n" +
                "      \"text\": \"Eve Jobs\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"type\": \"Richter\",\n" +
                "      \"count\": 1,\n" +
                "      \"text\": \"Prof. Dr. Gurkensalat\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"type\": \"Datum\",\n" +
                "      \"count\": 1,\n" +
                "      \"text\": \"14-6-2006\"\n" +
                "    }\n" +
                "  ]\n" +
                "}\n";

        try {
            Mapper mapper = new Mapper();
            Verdict verdict = mapper.mapJSONStringToVerdicObject(jsonString);
            System.out.println("Buh");
        }
        catch (NoDocketnumberFoundException ex) {
            ex.printStackTrace();
        }
    }
}
