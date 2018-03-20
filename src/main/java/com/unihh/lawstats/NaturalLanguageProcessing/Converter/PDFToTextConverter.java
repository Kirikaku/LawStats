package com.unihh.lawstats.NaturalLanguageProcessing.Converter;

import java.io.IOException;

public class PDFToTextConverter {

    public void convertPDFToText(String path, boolean isDeployMode) {
        try {
            // PDF in Text umwandeln

            if(!isDeployMode) {
                ProcessBuilder pb = new ProcessBuilder("src/main/resources/preprocessing/pdftotext", "-enc", "UTF-8", path);

                Process p = pb.start();
                p.waitFor();
            } else {
                ProcessBuilder pb = new ProcessBuilder("lawstats/preprocessing/pdftotext", "-enc", "UTF-8", path);

                Process p = pb.start();
                p.waitFor();
            }



        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
