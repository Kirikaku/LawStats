package com.unihh.lawstats.bootstrap.Watson;

import com.unihh.lawstats.bootstrap.Converter.Formatter;
import com.unihh.lawstats.bootstrap.Converter.PDFToTextConverter;
import com.unihh.lawstats.core.mapping.Mapper;
import com.unihh.lawstats.core.model.Verdict;


import java.io.File;
import java.text.ParseException;
import java.util.Date;

public class AnalyzingCoordinator {


    public Verdict analyzeDocument(File fileToAnalyze) {
        Verdict verdict = new Verdict();
        verdict.setForeDecisionDCCourt("Landgericht Erfurt");
        verdict.setDocketNumber("12345");
        verdict.setForeDecisionRCCourt("Oberlandesgericht Gibtsnicht");
        verdict.setDateVerdict(new Date(2015, 04, 12).getTime());

        PDFToTextConverter pdfToTextConverter = new PDFToTextConverter();
        LawEntityExtractor lawEntityExtractor = new LawEntityExtractor();
        Mapper verdictMapper = new Mapper();


        String documentText = null;
        String jsonNLUResponse = null;

        String path = fileToAnalyze.getPath();
        String pathTxt = path.replace(".pdf", ".txt");


        pdfToTextConverter.convertPDFToText(path);
        documentText = Formatter.formatText(pathTxt);
        /*
        documentText = "BUNDESGERICHTSHOF XI ZR 392/12 BESCHLUSS vom 9-12-2014 in dem Rechtsstreit Der XI_ Zivilsenat des Bundesgerichtshofs hat am 9-12-2014 durch den Richter Dr_ Ellenberger als Vorsitzenden, die Richter Dr_ Grüneberg und Maihold sowie die Richterinnen Dr_ Menges und Dr_ Derstadt beschlossen:\n" +
                "Auf die Nichtzulassungsbeschwerde des Klägers wird der Beschluss des 6_ Zivilsenats des Oberlandesgerichts Frankfurt am Main vom 11-9-2012 aufgehoben. Die Sache wird zur neuen Verhandlung und Entscheidung, auch über die Kosten des Beschwerdeverfahrens, an das Berufungsgericht zurückverwiesen. Streitwert: 191000 €.\n" +
                "\n" +
                "Gründe:\n" +
                "\n" +
                " I_ \n" +
                "\n" +
                "1 Der Kläger wendet sich mit seiner Klage gegen die von der Beklagten betriebene Zwangsvollstreckung aus einem notariell beurkundeten Schuldanerkenntnis.\n" +
                "\n" +
                "2 Im Juli 1999 schlossen der Kläger und die Beklagte mit der B_ \n" +
                "\n" +
                "AG (im Folgenden: Bank) einen Darlehensvertrag über 700.000 DM netto. Zur Sicherung des Darlehensrückzahlungsanspruchs der Bank wurde eine an einem Grundstück der Beklagten bestehende Eigentümerbriefgrundschuld in Höhe von 2,5 Mio. DM an die Bank abgetreten.\n" +
                "\n" +
                "3 Als in der Folgezeit das Darlehen nicht mehr bedient werden konnte, erwirkte die jetzige Beklagte gegen den jetzigen Kläger im Januar 2002 ein\n" +
                "\n" +
                "- rechtskräftig gewordenes - Urteil des LG Gießen, nach dem der Kläger der Beklagten den Schaden zu ersetzen hat, der daraus entsteht, dass noch nach dem 30-4-2000 (a) am Grundstück der Beklagten eine Grundschuld über 2,5 Mio. DM zugunsten der Bank eingetragen ist, und (b) die Beklagte gegenüber der Bank aus dem Darlehensvertrag von 1999 persönlich haftet.\n" +
                "\n" +
                "4 Am 22-10-2004 begab sich der Kläger mit der Beklagten und deren Rechtsanwalt zu einem Notar und gab dort ein abstraktes Schuldanerkenntnis mit Zwangsvollstreckungsunterwerfung ab. Der Kläger erkannte in Ziffer II_ 1.\n" +
                "\n" +
                "an, der Beklagten einen Betrag in Höhe von 375000 € nebst gesetzlichen Zinsen nach § 288 Abs_ 1 BGB ab dem 1-11-2004 zu schulden. Ihm wurde nachgelassen, an die Beklagte monatlich 2000 € zu zahlen. Der Kläger verpflichtete sich zudem, die monatlich anfallenden Zins- und Tilgungsleistungen in Höhe von 2013,21 € an die Bank zu zahlen; die Tilgungsleistungen sollten den in Ziffer II_ 1_ bezeichneten Schadensersatzanspruch reduzieren.\n" +
                "\n" +
                "5 Der Kläger zahlte in der Folgezeit monatlich 2000 € an die Beklagte und 2013,21 € an die Bank. Durch die Zahlungen an die Bank wurde das Darlehen in der Zeit bis zum 1-8-2010 in Höhe von 21474,26 € getilgt. Die Zahlungen an die Beklagte belaufen sich auf insgesamt 191000 €.\n" +
                "\n" +
                "6 Das Landgericht hat der Klage zu einem geringen Teil stattgegeben und die Zwangsvollstreckung aus der notariellen Urkunde für unzulässig erklärt, soweit wegen mehr als 353525,74 € vollstreckt wird, weil der Anspruch aus dem Schuldanerkenntnis um die Tilgung des Darlehens in Höhe von 21474,26 € zu reduzieren sei. Im Übrigen hat das Landgericht die Klage abgewiesen, da dem Kläger weitere Einwendungen nicht zustünden. Insbesondere sei die Beklagte nicht um das Schuldanerkenntnis ungerechtfertigt bereichert.\n" +
                "\n" +
                "7 Mit seiner Berufung hat der Kläger beantragt, die Zwangsvollstreckung aus dem notariell beurkundeten Schuldanerkenntnis für unzulässig zu erklären,\n" +
                "\n" +
                "soweit wegen eines höheren Betrages als 162525,74 € vollstreckt wird. Das Berufungsgericht hat die Berufung nach Erteilung eines Hinweises durch einstimmigen Beschluss gemäß § 522 Abs_ 2 ZPO zurückgewiesen. Zur Begründung hat es im Wesentlichen ausgeführt:\n" +
                "\n" +
                "8 Der Kläger könne das Schuldanerkenntnis nicht gemäß § 812 Abs_ 1 Satz 1 Fall 1 in Verbindung mit Abs_ 2 BGB kondizieren. Aus dem Urteil des LG Gießen von 2002 ergebe sich, dass im Verhältnis der Parteien zueinander der Kläger allein verpflichtet sein sollte, die Verbindlichkeiten aus dem Darlehensvertrag von 1999 zu begleichen. Dieses Innenverhältnis sei Gegenstand des abstrakten Schuldanerkenntnisses. Solange die Darlehensschuld nicht vollständig beglichen sei, bestünden diese Rechtsbeziehungen im Innenverhältnis und bildeten den Rechtsgrund für das abstrakte Schuldanerkenntnis. Bei diesem handele es sich um einen Vergleich im Sinne von § 779 BGB, durch den der Streit zwischen den Parteien über die Art und Weise der Erfüllung der in dem rechtskräftigen Urteil des LG Gießen festgestellten Verpflichtung des Klägers beseitigt werden sollte und in dem der Kläger durch die Verzinsungspflicht keine überobligationsmäßige Verbindlichkeit eingegangen sei.\n" +
                "\n" +
                "9 Auch die vom Kläger erklärte Hilfsaufrechnung mit Zahlungen, die er in Erfüllung des abstrakten Schuldanerkenntnisses in Höhe von insgesamt 191000 € gezahlt habe, verhelfe der Berufung nicht (teilweise) zum Erfolg. Eine solche Gegenforderung könnte nur dann bestehen, wenn der Kläger diese Zahlungen gemäß § 812 Abs_ 1 Satz 1 Fall 1 BGB ohne Rechtsgrund geleistet hätte. Der Rechtsgrund liege jedoch in dem wirksamen abstrakten Schuldanerkenntnis. Dieses habe der Kläger nicht gemäß § 123 BGB wegen arglistiger Täuschung anfechten können, da er nicht getäuscht worden sei.\n" +
                "\n" +
                "II_\n" +
                "\n" +
                "10 Die Nichtzulassungsbeschwerde des Klägers ist statthaft und auch im Übrigen zulässig. Die Revision ist nach § 543 Abs_ 2 Satz 1 Nr_ 2 Fall 2 ZPO zur Sicherung einer einheitlichen Rechtsprechung zuzulassen, da der angegriffene Beschluss den Anspruch des Klägers auf rechtliches Gehör aus Art_ 103 Abs_ 1 GG verletzt (vgl_ Senatsbeschlüsse vom 11-5-2004 - XI ZB 39/03,\n" +
                "\n" +
                "BGHZ 159, 135, 139 f_ und vom 9-2-2010 - XI ZR 140/09, BKR 2010,\n" +
                "\n" +
                "515, 516). Aus demselben Grund ist dieser Beschluss gemäß § 544 Abs_ 7 ZPO aufzuheben und die Sache zur neuen Verhandlung und Entscheidung an das Berufungsgericht zurückzuverweisen.\n" +
                "\n" +
                "11 1. Art_ 103 Abs_ 1 GG verpflichtet das Gericht, den Vortrag der Parteien zur Kenntnis zu nehmen und bei seiner Entscheidung in Erwägung zu ziehen\n" +
                "\n" +
                "(BVerfGE 65, 293, 295; 83, 24, 35; 96, 205, 216; BVerfG, NJW-RR 2001, 1006,\n" +
                "\n" +
                "1007). Grundsätzlich ist davon auszugehen, dass das Gericht das von ihm entgegengenommene Vorbringen eines Beteiligten auch zur Kenntnis genommen und in Erwägung gezogen hat, zumal es nach Art_ 103 Abs_ 1 GG nicht verpflichtet ist, sich mit jedem Vorbringen in der Begründung seiner Entscheidung ausdrücklich zu befassen. Ein Verstoß gegen Art_ 103 Abs_ 1 GG setzt eine gewisse Evidenz der Gehörsverletzung voraus. Im Einzelfall müssen besondere Umstände vorliegen, die deutlich ergeben, dass das Vorbringen entweder überhaupt nicht zur Kenntnis genommen oder bei der Entscheidung ersichtlich nicht erwogen worden ist (BVerfGE 65, 293, 295 f_; 86, 133, 146; 96, 205, 216 f_;\n" +
                "\n" +
                "BVerfG, NJW 2000, 131; Senatsbeschluss vom 20-1-2009 - XI ZR 510/07, WM 2009, 405 Rn_ 8).\n" +
                "\n" +
                "\n" +
                "\n" +
                "12 2. Nach diesen Maßgaben ist Art_ 103 Abs_ 1 GG hier verletzt. Das Berufungsgericht hat den Kerngehalt des Vortrags des Klägers übergangen, indem es nicht geprüft hat, ob dem Kläger aufgrund der unstreitigen Zahlungen an die Beklagte der Einwand der Erfüllung gemäß § 362 Abs_ 1 BGB zusteht, obwohl der Kläger - insbesondere in seiner Stellungnahme zum Hinweisbeschluss des Berufungsgerichts vom 9-7-2012 - die Berücksichtigung dieser Zahlungen eingefordert und damit auch den Erfüllungseinwand geltend gemacht hat.\n" +
                "\n" +
                "13 3. Die Entscheidung des Berufungsgerichts beruht auf dieser Gehörsverletzung. Diese Voraussetzung ist schon dann erfüllt, wenn nicht ausgeschlossen werden kann, dass das Berufungsgericht bei Berücksichtigung des übergangenen Vorbringens anders entschieden hätte (vgl_ BVerfGE 7, 95, 99; 60,\n" +
                "\n" +
                "247, 250; 62, 392, 396; 65, 305, 308; 89, 381, 392 f_). Dies ist hier der Fall, da die titulierte Forderung durch die unstreitigen Zahlungen des Klägers an die Beklagte zumindest teilweise durch Erfüllung gemäß § 362 Abs_ 1 BGB erloschen sein könnte. Entsprechend der Forderungsberechnung in der Anlage zur Klageschrift kommt jedenfalls eine Erfüllung in Höhe von 63533,16 € in Betracht. Eine weitergehende Erfüllung könnte eingetreten sein, wenn sich durch Auslegung des Schuldanerkenntnisses oder aus § 138 BGB ergäbe, dass die in Ziffer II_ 1_ anerkannten Verzugszinsen nicht unabhängig davon geschuldet sind, ob der Beklagten, die nach dem nicht bestrittenen Klägervortrag keine Zahlungen an die Bank geleistet hat, durch ihre Haftung für das Darlehen oder die Grundschuld auf ihrem Grundstück tatsächlich ein konkreter Schaden entstanden ist.\n" +
                "\n" +
                "Ellenberger Menges Grüneberg Derstadt Maihold Vorinstanzen: LG Gießen, Entscheidung vom 21-09-2011 - 2 O 501/10 OLG Frankfurt am Main, Entscheidung vom 11-09-2012 - 6 U 192/11 -\n" +
                "\n";
                */
        jsonNLUResponse = lawEntityExtractor.extractEntities("10:864de4a5-5bab-495e-8080-2f1185d1b38d", documentText); //TODO model id von config holen

        try {
            verdict = verdictMapper.mapJSONStringToVerdicObject(jsonNLUResponse);
        } catch (ParseException pE) {
            System.out.println("Der JSON String konnte nicht auf ein Verdict Objekt gemappt werden.");
            //TODO hier sollte vernünftig geloggt werden
            pE.printStackTrace();
        }


        //TODO hier kommt es drauf an ob Watson oder TU Darm. Classifier benutzt wird
        verdict.setRevisionSuccess(1);

        String jsonClassifierResponse = "";

        //


        return verdict;
    }

}
