package com.unihh.lawstats.bootstrap.Downloader;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class VerdictDownloader implements Runnable {

    DownloadManager _downloadManager;
    int _endIndex;
    String _folderPath;



    public VerdictDownloader(DownloadManager downloadManager, int endIndex, String folderPath) {
        _downloadManager = downloadManager;
        _endIndex = endIndex;
        _folderPath = folderPath;
    }

    public void run() {

        int counter = _downloadManager.getAndIncrementCounter();
        while(counter <= _endIndex) {

            downloadVerdicts(counter, _folderPath);
            counter = _downloadManager.getAndIncrementCounter();
        }

    }


    public void downloadVerdicts(int counter, String targetFolderPath) {



        String urlString = "http://juris.bundesgerichtshof.de/cgi-bin/rechtsprechung/document.py?Gericht=bgh&Art=en&Datum=Aktuell&nr=" + counter + "&Frame=4&.pdf";
        String targetPath = targetFolderPath + "verdict" + counter + ".pdf"; //TODO properties DONE

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





