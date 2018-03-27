# LawStats

### Inhalt
Diese Applikation dient zur **Suche und Analyse** einer Bibiliothek von Revisionen
des Bundesgerichtshofs nach gewählten **Attributen und Schlagworten**, sowie zur
automatischen Untersuchung von selbst hochgeladenen Revisision als pdf-Dokument.

Zudem kann der **Revisionsausgang** der gesuchten oder hochgeladenen Urteile
ausgegeben werden.

### How to start the Application
You have to pull the entire Software and on top of it you have to download following zip - file
and insert the file into `resources/data/features` (This file is to big for github). then the
software ist able to start.

#### How to build
When you want to build your software for getting an executable jar, then you have to comment
all mains, but not the `Application` - class, so that you have one main class, which is important.

#### How to deploy
You will get 2 DOCKERFILE - Files with 2 Readme's, follow the Readme for building and starting
both Containers and you will get a solr with schema and the application which holds a connection
to localhost:8983, where the solr should be


### Natural Language Processing Utils
Für einige Funktionen des Programms, wie zum Beispiel das Herunterladen von Bundesgerichtshofs Urteilen oder das Trainieren des Modells, gibt es kein User- oder Commandline Interface. Zur Nutzung dieser Funktionalitäten können die entsprechenden Main-Klassen im Package Natural Language Processing genutzt werden. 
