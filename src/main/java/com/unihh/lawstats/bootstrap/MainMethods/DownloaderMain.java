package com.unihh.lawstats.bootstrap.MainMethods;

import com.unihh.lawstats.bootstrap.Downloader.DownloadManager;

public class DownloaderMain {

    public static void main(String[] args){
        int numberOfThreads = 1;
        int startIndex = 20000;
        int endIndex = 30000;

        DownloadManager downloadManager = new DownloadManager();
        downloadManager.downloadMultithread(numberOfThreads,startIndex, endIndex);
    }

}
