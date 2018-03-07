# LawStats

###Inhalt
Diese Applikation dient zur **Suche und Analyse** einer Bibiliothek von Revisionen
des Bundesgerichtshofs nach gewählten **Attributen und Schlagworten**, sowie zur
automatischen einer Untersuchung selbst hochgeladenen Revisision als pdf-Dokument.

Zudem kann der **Revisionsausgang** der gesuchten oder hochgeladenen Urteile
ausgegeben werden.

###Applikation aufrufen
Die Applikation kann sowohllokal als auch online auf "" aufgerufen werden.
Online steht bereits eine komplette Bibliothek von ca. 50.000 Revisionen zur Verfügung.

Zur lokalen Nutzung in der IDE muss das Projekt als *Existing Maven Project* importiert
werden und dann als *Spring Boot App* gestartet werden. Die Applikation steht dann auf 
`localhost:8080` oder auf dem gewählten Port der `application.properties` zur Verfügung.

Zusätzlich kann dann ein Solr Server als eigene Bibliothek eingebunden werden...


##Benutzung (nicht in Readme)

####Filter
Zum Filtern und Durchsuchen der Bibliothek kann über die Eingabefelder zunächst ein
**Kriterium** und dann nach einem passendem **Schlagwort** bzw. **Datumsintervall**
angegeben werden.

Dabei kann nach **Aktenzeichen**, **Senat**, **Richter** der Revision sowie sowohl
**Oberlandes**-, **Landes** und **Amtsgericht** der Vorentscheidung gefiltert werden.

Sollte ein Datum ausgewählt worden sein, kann sowohl nach der **Entscheidungsdatum** der
Revision, als auch nach den Zeitpunkten den genannten Vorausgennanten Entscheidungen
gesucht werden. Durch _Hinzufügen_ kann auch eine Kombination von Filterkriterien
gewählt werden, oder auch kein konkretes Schlagwort angegeben werden, um eine Liste aller
in der gewählten Kategorie enthaltenen Objekte anzuzeigen.

Über _Submit_ wird nun sowohl eine absolute, als auch eine relative Angebe über die
Revisionausgänge der gewählten Filterkriterien angezeigt. Es können weiterhin weitere 
Attribute zur Suche hinzugefügt oder über _Reset_ alle gewählten Kriterien zurückgesetzt
werden.