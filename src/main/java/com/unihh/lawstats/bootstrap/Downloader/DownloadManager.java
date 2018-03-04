package com.unihh.lawstats.bootstrap.Downloader;

public class DownloadManager {
    int _counter;



    public void downloadMultithread(int numberOfThreads, int startIndex, int endIndex){
        _counter = startIndex;

        for(int i = 1; i <= numberOfThreads; i++){
            new VerdictDownloader(this, endIndex, "ein pfad").run();

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
