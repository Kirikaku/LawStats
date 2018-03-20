package com.unihh.lawstats.bootstrap.Downloader;

import com.unihh.lawstats.PropertyManager;

public class DownloadManager {
    int _counter;

    public static void main(String args[]){
        DownloadManager downloadManager = new DownloadManager();  //TODO delete this
        downloadManager.downloadMultithread(1,20000, 30000);
    }

    public void downloadMultithread(int numberOfThreads, int startIndex, int endIndex){
        _counter = startIndex;

        for(int i = 1; i <= numberOfThreads; i++){
            new VerdictDownloader(this, endIndex, PropertyManager.getLawProperty(PropertyManager.DOWNLOADTARGETDIRECTION)).run();
        }


    }


    public synchronized int getCounter(){
        return _counter;
    }

    public synchronized void setCounter(int newCounter){
        _counter = newCounter;
    }

    public synchronized  int getAndIncrementCounter(){
        _counter++;

        return _counter;
    }
}
