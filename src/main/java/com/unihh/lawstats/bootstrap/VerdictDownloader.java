package com.unihh.lawstats.bootstrap;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class VerdictDownloader {

    public void downloadVerdicts(int startIndex, int endIndex, String filePath) throws Exception{




        for(int i = startIndex; i <= endIndex; i++) {

            String urlString = "http://juris.bundesgerichtshof.de/cgi-bin/rechtsprechung/document.py?Gericht=bgh&Art=en&Datum=Aktuell&nr="+i+"&Frame=4&.pdf";
            //String filePath = "C:\\Users\\Phillip\\Documents\\Studium\\Praktikum Sprachtechnologie\\VerdictPresentation\\verdict"+i+".pdf";//"C:\\Users\\Phillip\\Documents\\verdicts\\verdict"+i+".pdf";

            URL url = new URL(urlString);
            BufferedInputStream in = new BufferedInputStream(url.openStream());
            Files.copy(in, Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
            in.close();
        }
    }



}
