package com.unihh.lawstats.bootstrap.Converter.Formatting;

public class FormattingManager {

    int _counter;



    public void formatMultithread(int numberOfThreads, int startIndex, int endIndex, String sourceBasePath, String targetBasePath){
        _counter = startIndex;

        for(int i = 1; i <= numberOfThreads; i++){
            new RunnableFormatter(this, endIndex, sourceBasePath, targetBasePath).run();
        }


    }



    public synchronized  int getAndIncrementCounter(){
        _counter++;

        return _counter;
    }

}
