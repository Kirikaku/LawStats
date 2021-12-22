package com.unihh.lawstats.naturalLawLanguageProcessing.MainMethods;

import com.unihh.lawstats.naturalLawLanguageProcessing.Downloader.DownloadManager;

/**
 * @author Phillip
 */
public class DownloaderMain {

    /**
     * Method to download BGH verdicts.
     */
    public static void main(String[] args){
        int numberOfThreads = 1;
        int startIndex = 20000;
        int endIndex = 30000;

        DownloadManager downloadManager = new DownloadManager();
        downloadManager.downloadMultithread(numberOfThreads,startIndex, endIndex);
    }

}
