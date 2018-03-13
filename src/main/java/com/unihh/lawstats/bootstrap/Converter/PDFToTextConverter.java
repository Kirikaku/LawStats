package com.unihh.lawstats.bootstrap.Converter;

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

            /*
            String textPath = path.replace(".pdf", ".txt");
            String cleanPath = textPath.replace(".txt", "_clean.txt");
               */

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
