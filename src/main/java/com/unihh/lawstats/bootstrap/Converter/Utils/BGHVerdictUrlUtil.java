package com.unihh.lawstats.bootstrap.Converter.Utils;

import java.net.URL;

public class BGHVerdictUrlUtil {

    public String retrieveBGHVerdictUrl(String bghVerdictNumber){
        String url = "http://juris.bundesgerichtshof.de/cgi-bin/rechtsprechung/document.py?Gericht=bgh&Art=en&Datum=Aktuell&nr=" + bghVerdictNumber + "&Frame=4&.pdf";

        return url;
    }


    public String retrieveBGHVerdictUrl(int bghVerdictNumber){
        String url = retrieveBGHVerdictUrl(String.valueOf(bghVerdictNumber));

        return url;
    }


}
