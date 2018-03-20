package com.unihh.lawstats.bootstrap.Downloader;

import com.unihh.lawstats.PropertyManager;

public class DownloadManager {
    int _counter;


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
