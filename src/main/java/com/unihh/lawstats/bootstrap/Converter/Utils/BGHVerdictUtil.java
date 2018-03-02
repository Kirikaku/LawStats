package com.unihh.lawstats.bootstrap.Converter.Utils;

import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BGHVerdictUtil {

    public String retrieveBGHVerdictUrl(String bghVerdictNumber){
        String url = "http://juris.bundesgerichtshof.de/cgi-bin/rechtsprechung/document.py?Gericht=bgh&Art=en&Datum=Aktuell&nr=" + bghVerdictNumber + "&Frame=4&.pdf";

        return url;
    }


    public String retrieveBGHVerdictUrl(int bghVerdictNumber){
        String url = retrieveBGHVerdictUrl(String.valueOf(bghVerdictNumber));

        return url;
    }


    public String retrieveBGHVerdictNumberForFileName(String filename){

        String bghVerdictNumber = null;

        String pattern = "verdict(\\d{4})^\\s";

        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(filename);

        if(m.find()) {

            bghVerdictNumber = m.group(1);

        }

        return bghVerdictNumber;
    }


}