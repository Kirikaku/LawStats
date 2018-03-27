package com.unihh.lawstats.NaturalLawLanguageProcessing.Downloader;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;


/**
 * @author Phillip
 */
public class VerdictDownloader implements Runnable {

    DownloadManager _downloadManager;
    int _endIndex;
    String _folderPath;



    public VerdictDownloader(DownloadManager downloadManager, int endIndex, String folderPath) {
        _downloadManager = downloadManager;
        _endIndex = endIndex;
        _folderPath = folderPath;
    }

    /**
     * Method to enable multithrad downloading.
     */
    public void run() {

        int counter = _downloadManager.getAndIncrementCounter();
        while(counter <= _endIndex) {

            downloadVerdicts(counter, _folderPath);
            counter = _downloadManager.getAndIncrementCounter();
        }

    }

    /**
     * Downloads and stores a verdict from the BGH website.
     * @param counter Number to specify which verdict shall be downloaded.
     * @param targetFolderPath The path where the verdict shall be stored.
     */
    public void downloadVerdicts(int counter, String targetFolderPath) {
        String urlString = "http://juris.bundesgerichtshof.de/cgi-bin/rechtsprechung/document.py?Gericht=bgh&Art=en&Datum=Aktuell&nr=" + counter + "&Frame=4&.pdf";
        String targetPath = targetFolderPath + "verdict" + counter + ".pdf";

        try {
            URL url = new URL(urlString);
            BufferedInputStream in = new BufferedInputStream(url.openStream());
            Files.copy(in, Paths.get(targetPath), StandardCopyOption.REPLACE_EXISTING);
            in.close();
        }catch(Exception e) {
            e.printStackTrace();
        }

    }

}





