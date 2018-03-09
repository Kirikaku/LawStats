# LawStats

### Inhalt
Diese Applikation dient zur **Suche und Analyse** einer Bibiliothek von Revisionen
des Bundesgerichtshofs nach gewählten **Attributen und Schlagworten**, sowie zur
automatischen Untersuchung von selbst hochgeladenen Revisision als pdf-Dokument.

Zudem kann der **Revisionsausgang** der gesuchten oder hochgeladenen Urteile
ausgegeben werden.

### Applikation aufrufen
Die Applikation kann sowohllokal als auch online auf "" aufgerufen werden.
Online steht bereits eine komplette Bibliothek von ca. 50.000 Revisionen zur Verfügung.

Zur lokalen Nutzung in der IDE muss das Projekt als *Existing Maven Project* importiert
werden und dann als *Spring Boot App* gestartet werden. Die Applikation steht dann auf 
`localhost:8080` oder auf dem gewählten Port der `application.properties` zur Verfügung.

Zusätzlich kann dann ein Solr Server als eigene Bibliothek eingebunden werden...