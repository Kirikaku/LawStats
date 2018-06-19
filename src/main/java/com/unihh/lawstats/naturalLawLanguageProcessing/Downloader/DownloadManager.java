package com.unihh.lawstats.naturalLawLanguageProcessing.Downloader;

import com.unihh.lawstats.PropertyManager;

/**
 * @author Phillip
 *
 * Enables multithread downloading.
 */
public class DownloadManager {
    int _counter;


    /**
     * Starts multiple download threads.
     * @param numberOfThreads Number of threads to be started.
     * @param startIndex Specifies the first verdict to be downloaded. Depends on the index convention of the BGH website.
     * @param endIndex Specifies the last verdict to be downloaded. Depends on the index convention of the BGH website.
     */
    public void downloadMultithread(int numberOfThreads, int startIndex, int endIndex){
        _counter = startIndex;

        for(int i = 1; i <= numberOfThreads; i++){
            new VerdictDownloader(this, endIndex, PropertyManager.getLawProperty(PropertyManager.DOWNLOADTARGETDIRECTION)).run();
        }


    }




    public synchronized  int getAndIncrementCounter(){
        _counter++;

        return _counter;
    }
}
