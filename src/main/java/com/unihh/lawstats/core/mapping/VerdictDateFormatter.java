package com.unihh.lawstats.core.mapping;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class VerdictDateFormatter {
    private DateFormat df_ttd = new SimpleDateFormat("dd-MM-yyyy", Locale.GERMAN);
    private DateFormat df_ood = new SimpleDateFormat("d-M-yyyy", Locale.GERMAN);
    private DateFormat df_otd = new SimpleDateFormat("d-MM-yyyy", Locale.GERMAN);
    private DateFormat df_tod = new SimpleDateFormat("dd-M-yyyy", Locale.GERMAN);


    public Long formateStringToLong(String string) {

        Date date;
        try {
            if (Pattern.matches("(\\d{2})-(\\d{2})-(\\d{4})", string)) {
                date = df_ttd.parse(string);
            } else if (Pattern.matches("(\\d{1})-(\\d{1})-(\\d{4})", string)) {
                date = df_ood.parse(string);
            } else if (Pattern.matches("(\\d{2})-(\\d{1})-(\\d{4})", string)) {
                date = df_otd.parse(string);
            } else if (Pattern.matches("(\\d{1})-(\\d{2})-(\\d{4})", string)) {
                date = df_tod.parse(string);
            } else {
                date = new Date(0000, 00, 00);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date().getTime();
        }
        return date.getTime();
    }

    public List<Long> formateStringDateToLongList(List<String> stringL) {
        // Empfängt eine Liste und gibt dabei das neueste Datum zurück.
        List<Long> dateVerdictList = new ArrayList<>();
        for (String string : stringL) {
            Long date = formateStringToLong(string);
            dateVerdictList.add(date);
        }
        return dateVerdictList;
    }

    /**
     * This method format the given date in a string
     * date format: dd:MM:yyyy
     *
     * @param verdictDate the date which we want to format
     * @return the formattet date in a string
     */
    public String formatVerdictDateToString(final Long verdictDate) {
        if(verdictDate != null) {
            DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            return formatter.format(verdictDate);
        }
        return "";
    }
}
