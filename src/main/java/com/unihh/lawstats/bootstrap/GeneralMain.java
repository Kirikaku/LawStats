package com.unihh.lawstats.bootstrap;

import com.unihh.lawstats.bootstrap.Converter.Formatting.FormattingManager;
import com.unihh.lawstats.bootstrap.Converter.RandomDocumentSelector;
import com.unihh.lawstats.bootstrap.Downloader.DownloadManager;

public class GeneralMain {


    public static void main(String[] args){

        String methodName = args[0];

        if(methodName=="FORMAT"){
            int numberOfThreads = Integer.valueOf(args[1]);
            int startIndex = Integer.valueOf(args[2]);
            int endIndex = Integer.valueOf(args[3]);
            String sourceBasePath = args[4];
            String targetBasePath =  args[5];

            FormattingManager formattingManager = new FormattingManager();
            formattingManager.formatMultithread(numberOfThreads, startIndex, endIndex, sourceBasePath, targetBasePath);
        }
        else if(methodName=="SELECT_RANDOM"){
            int numberOfDocuments = Integer.valueOf(args[1]);
            int startIndex = Integer.valueOf(args[2]);
            int endIndex = Integer.valueOf(args[3]);

            RandomDocumentSelector randomDocumentSelector = new RandomDocumentSelector();
            randomDocumentSelector.selectRandomDocuments(numberOfDocuments, startIndex, endIndex);
        }
        else if(methodName=="DOWNLOAD_VERDICTS"){
            int numberOfThreads = Integer.valueOf(args[1]);
            int startIndex = Integer.valueOf(args[2]);
            int endIndex = Integer.valueOf(args[3]);

            DownloadManager downloadManager = new DownloadManager();
            downloadManager.downloadMultithread(1,20000, 30000);
        }
        else if(methodName == "CREATE_DATA_BASEFILES"){
            System.out.println("Hallo ein Test");
        }
        else if(methodName == "CREATE_DATA_FROM_BASEFILE"){

        }

    }

}
