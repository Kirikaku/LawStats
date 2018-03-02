package com.unihh.lawstats.bootstrap.Converter;

import java.io.IOException;

public class PDFToTextConverter {

    public void convertPDFToText(String path) {
        try {
            // PDF in Text umwandeln

            ProcessBuilder pb = new ProcessBuilder("src/main/resources/preprocessing/pdftotext", "-enc", "UTF-8", path);

            Process p = pb.start();
            p.waitFor();

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
