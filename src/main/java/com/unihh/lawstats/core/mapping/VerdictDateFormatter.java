package com.unihh.lawstats.core.mapping;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

public class VerdictDateFormatter {


    //TODO siehe Mapper -> main und statics löschen! -> Ticket in Trello
    private static DateFormat df_ttd = new SimpleDateFormat("dd-MM-yyyy", Locale.GERMAN);
    private static DateFormat df_ood = new SimpleDateFormat("d-M-yyyy", Locale.GERMAN);
    private static DateFormat df_otd = new SimpleDateFormat("d-MM-yyyy", Locale.GERMAN);
    private static DateFormat df_tod = new SimpleDateFormat("dd-M-yyyy", Locale.GERMAN);


    public static Date formatDateVerdict(String string) throws ParseException {

        Date date;
        if (Pattern.matches("(\\d{2})-(\\d{2})-(\\d{4})", string)) {
            date = df_ttd.parse(string);
        } else if (Pattern.matches("(\\d{1})-(\\d{1})-(\\d{4})", string)) {
            date = df_ood.parse(string);
        } else if (Pattern.matches("(\\d{2})-(\\d{1})-(\\d{4})", string)) {
            date = df_otd.parse(string);
        } else if (Pattern.matches("(\\d{1})-(\\d{2})-(\\d{4})", string)) {
            date = df_tod.parse(string);
        }
        else {
            date = new Date(0000, 00, 00);
        }
        return date;
    }




    public static List<Date> formatDateVerdictList(List<String> stringL) throws ParseException {
        // Empfängt eine Liste und gibt dabei das neueste Datum zurück.
        List<Date> dateVerdictList = new ArrayList<>();
        for (String string : stringL) {
            Date date;
            date = formatDateVerdict(string);
            dateVerdictList.add(date);
        }
        return dateVerdictList;

    }
}
