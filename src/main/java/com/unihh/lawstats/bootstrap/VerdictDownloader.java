package com.unihh.lawstats.bootstrap;

import com.unihh.lawstats.bootstrap.Watson.DownloadManager;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class VerdictDownloader implements Runnable {

    DownloadManager _downloadManager;
    int _endIndex;
    String _filePath;



    public VerdictDownloader(DownloadManager downloadManager, int endIndex, String filePath) {
        _downloadManager = downloadManager;
        _endIndex = endIndex;
        _filePath = filePath;
    }

    public void run() {

        int counter = _downloadManager.getAndIncrementCounter();
        while(counter <= _endIndex) {

            downloadVerdicts(counter, "ein pfad");
            counter = _downloadManager.getAndIncrementCounter();
        }


    }

    public void downloadVerdicts(int counter, String filePath) {


        String urlString = "http://juris.bundesgerichtshof.de/cgi-bin/rechtsprechung/document.py?Gericht=bgh&Art=en&Datum=Aktuell&nr=" + counter + "&Frame=4&.pdf";
        filePath = "C:\\Users\\Phillip\\Documents\\verdicts\\verdict" + counter + ".pdf";//"C:\\Users\\Phillip\\Documents\\verdicts\\verdict"+i+".pdf";

        try {
            URL url = new URL(urlString);
            BufferedInputStream in = new BufferedInputStream(url.openStream());
            Files.copy(in, Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
            in.close();
        }catch(Exception e) {
            e.printStackTrace();
        }

    }

}





