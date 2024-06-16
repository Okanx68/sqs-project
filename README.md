# Einführung und Ziele

Das Ziel dieses Projekts ist es, die Effizienz bei der Abfrage von Brauereiinformationen durch eine Caching-Strategie zu verbessern. Dafür setze ich Quarkus als Backend-Framework, Angular für das Frontend und PostgreSQL als Datenbank ein. Die benötigten Brauereiinformationen werden von der *Open Brewery DB* API (https://openbrewerydb.org) abgerufen und in der Datenbank, die als Cache dient, für erneute Anfragen gespeichert.

Ein besonderer Schwerpunkt liegt auf der Abfrage von Brauereien in spezifischen Städten wie Austin oder San Diego. Die Suche nach Brauereien erfolgt durch die Eingabe des Stadtnamens. Zu diesem Zweck wird eine Webseite bereitgestellt, die es Benutzern ermöglicht, Städtenamen einzugeben und die entsprechenden Brauereien in dieser Stadt anzuzeigen.

## Aufgabenstellung

## Qualitätsziele

## Stakeholder

| Rolle        | Kontakt        | Erwartungshaltung |
|--------------|----------------|-------------------|
| Dozent | Beneken, Gerd (gerd.beneken@th-rosenheim.de) | "Will ein funktionierendes und gutes Projekt sehen!"  |
| Dozent | Reimer, Mario-Leander (mario-leander.reimer@th-rosenheim.de) | "Will ein funktionierendes und sehr gutes Projekt sehen!"  |

# Randbedingungen

# Kontextabgrenzung

## Fachlicher Kontext
Das System interagiert mit der Open Brewery DB API, um Brauereiinformationen im Quarkus-Backend abzurufen. Diese Daten werden in der PostgreSQL-Datenbank zwischengespeichert und Benutzern über eine Angular-Webanwendung zugänglich gemacht.

Die Schnittstelle im Backend bietet eine einfache Möglichkeit, eine Liste von Brauereien für eine bestimmte Stadt abzurufen.

* GET /api/v1/breweries/{cityName}: Dies ist der Endpunkt, um die Liste der Brauereien abzurufen. Der Pfadparameter {cityName} wird durch den Stadtnamen ersetzt, für die die Brauerein abgerufen werden sollen.

Parameter:

* cityName*: Der Name der Stadt, für die die Brauereien abgerufen werden sollen. Dabei handelt es sich um einen Pfadparameter vom Typ String. Zum Beispiel: "Austin".
  
* count*: Die maximale Anzahl an Brauereien, die abgerufen werden sollen. Dabei handelt es sich um einen Queryparameter vom Typ Integer. Standardmäßig werden vom Frontend maximal 20 Brauereien abgerufen.

Responses:

* 200 Success: Erfolgreiche Antwort, die eine Liste der aktuellen Baustellen im JSON-Format zurückgibt.

* 204 Not Found: Es wurden keine Baustellen gefunden.

* 400 Bad Request: Invalide Parameter.

* 404 Not Found: Die angeforderte Ressource wurde nicht gefunden.

* 500 Internal Server Error: Interner Serverfehler.

* Media Type: Die Antwort ist im JSON-Format ('application/json').

Die API-Dokumentation wird mittels Swagger UI bereitgestellt: http://localhost:8080/api/v1/q/swagger-ui

### Externe Schnittstelle Open Brewery DB

## Technischer Kontext

**\<Diagramm oder Tabelle>**

**\<optional: Erläuterung der externen technischen Schnittstellen>**

**\<Mapping fachliche auf technische Schnittstellen>**

# Lösungsstrategie

# Bausteinsicht

## Whitebox Gesamtsystem

***\<Übersichtsdiagramm>***

Begründung  
*\<Erläuternder Text>*

Enthaltene Bausteine  
*\<Beschreibung der enthaltenen Bausteine (Blackboxen)>*

Wichtige Schnittstellen  
*\<Beschreibung wichtiger Schnittstellen>*

### \<Name Blackbox 1>

*\<Zweck/Verantwortung>*

*\<Schnittstelle(n)>*

*\<(Optional) Qualitäts-/Leistungsmerkmale>*

*\<(Optional) Ablageort/Datei(en)>*

*\<(Optional) Erfüllte Anforderungen>*

*\<(optional) Offene Punkte/Probleme/Risiken>*

### \<Name Blackbox 2>

*\<Blackbox-Template>*

### \<Name Blackbox n>

*\<Blackbox-Template>*

### \<Name Schnittstelle 1>

…

### \<Name Schnittstelle m>

## Ebene 2

### Whitebox *\<Baustein 1>*

*\<Whitebox-Template>*

### Whitebox *\<Baustein 2>*

*\<Whitebox-Template>*

…

### Whitebox *\<Baustein m>*

*\<Whitebox-Template>*

## Ebene 3

### Whitebox \<\_Baustein x.1\_\>

*\<Whitebox-Template>*

### Whitebox \<\_Baustein x.2\_\>

*\<Whitebox-Template>*

### Whitebox \<\_Baustein y.1\_\>

*\<Whitebox-Template>*

# Laufzeitsicht

## *\<Bezeichnung Laufzeitszenario 1>*

-   \<hier Laufzeitdiagramm oder Ablaufbeschreibung einfügen>

-   \<hier Besonderheiten bei dem Zusammenspiel der Bausteine in diesem
    Szenario erläutern>

## *\<Bezeichnung Laufzeitszenario 2>*

…

## *\<Bezeichnung Laufzeitszenario n>*

…

# Verteilungssicht

## Infrastruktur Ebene 1

***\<Übersichtsdiagramm>***

Begründung  
*\<Erläuternder Text>*

Qualitäts- und/oder Leistungsmerkmale  
*\<Erläuternder Text>*

Zuordnung von Bausteinen zu Infrastruktur  
*\<Beschreibung der Zuordnung>*

## Infrastruktur Ebene 2

### *\<Infrastrukturelement 1>*

*\<Diagramm + Erläuterungen>*

### *\<Infrastrukturelement 2>*

*\<Diagramm + Erläuterungen>*

…

### *\<Infrastrukturelement n>*

*\<Diagramm + Erläuterungen>*

# Querschnittliche Konzepte

## *\<Konzept 1>*

*\<Erklärung>*

## *\<Konzept 2>*

*\<Erklärung>*

…

## *\<Konzept n>*

*\<Erklärung>*

# Architekturentscheidungen

# Qualitätsanforderungen

<div class="formalpara-title">

**Weiterführende Informationen**

</div>

Siehe [Qualitätsanforderungen](https://docs.arc42.org/section-10/) in
der online-Dokumentation (auf Englisch!).

## Qualitätsbaum

## Qualitätsszenarien

# Risiken und technische Schulden

# Glossar

| Begriff        | Definition        |
|----------------|-------------------|
| *\<Begriff-1>* | *\<Definition-1>* |
| *\<Begriff-2*  | *\<Definition-2>* |
