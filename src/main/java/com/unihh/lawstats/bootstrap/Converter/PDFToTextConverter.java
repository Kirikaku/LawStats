package com.unihh.lawstats.bootstrap.Converter;

import java.io.IOException;

public class PDFToTextConverter {

    public void convertPDFToText(String path) {
        try {
            // PDF in Text umwandeln
            // ProcessBuilder pb = new ProcessBuilder(System.getProperty("user.dir") + "\\helper\\pdftotext.exe", "-enc", "UTF-8", path);
            ProcessBuilder pb = new ProcessBuilder("src/main/resources/preprocessing/pdftotext", "-enc", "UTF-8", path);

            Process p = pb.start();
            p.waitFor();

            String textPath = path.replace(".pdf", ".txt");
            String cleanPath = textPath.replace(".txt", "_clean.txt");

            // Text in cleanText umwandeln
            //pb = new ProcessBuilder("java", "-jar", System.getProperty("user.dir")+"\\helper\\converter.jar", textPath, cleanPath, System.getProperty("user.dir")+"\\helper\\shortcuts.txt");
            //p = pb.start();
            //p.waitFor();


        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
