# Brewery Explorer

**Software-Qualitätssicherung SoSe 2024**

# Einführung und Ziele

Das Ziel der Brewery Explorer Webanwendung ist es, Brauereien in einer bestimmten Stadt sowie deren Informationen wie beispielsweise Adresse, Telefonnummer oder Link zur Webseite abzurufen. Um die Effizienz der Anfragen zu verbessern, wird eine gezielte Caching-Strategie angewendet. Dafür setze ich Quarkus als Backend-Framework, Angular für das Frontend und PostgreSQL als Datenbank ein. Die benötigten Brauereiinformationen werden von der *Open Brewery DB* API (https://openbrewerydb.org) abgerufen und in der Datenbank, die hierbei als Cache dient, für erneute Anfragen abgespeichert. Die Suche nach Brauereien erfolgt durch die Eingabe des Stadtnamens mithilfe einer benutzerfreundlichen Weboberfläche.

## Aufgabenstellung

[Assignment Details SQS 2024](https://github.com/Okanx68/sqs-project/blob/main/doc/images/Assignment%20Details%20SQS2024.png)

## Qualitätsziele

| Qualitätskriterium | Beschreibung | Ziele | Maßnahmen |
|---------------------|---------------|-------|-----------|
| **1. Reliability - Zuverlässigkeit** | Die Fähigkeit der Anwendung, stabil und fehlerfrei zu laufen, selbst bei unerwarteten Eingaben und hoher Last. | - Resiliente Verarbeitung von Benutzereingaben <br>- Hohe Stabilität bei starker Nutzung | - Umfangreiche Testabdeckung<br>- Lasttests mit Artillery|
| **2. Portability - Übertragbarkeit** | Flexibilität der Anwendung in Bezug auf die Laufzeitumgebung. |- Externe Abhängigkeiten verringern<br>- Ressourcen effizient nutzen | - Containerisierung mithilfe von Docker zur Isolierung der Laufzeitumgebungen sowie Sicherstellung der Plattformunabhängigkeit |
| **3. Usability - Benutzerfreundlichkeit** | Einfache Interaktion mit der Benutzeroberfläche. | - Simple Bedienung<br>- Schnelle Ladezeiten | - End-to-End-Tests mit Playwright<br>- Frontendtests<br>- Integrationstest |

## Stakeholder

| Rolle        | Kontakt        | Erwartungshaltung |
|--------------|----------------|-------------------|
| Dozent | Beneken, Gerd (gerd.beneken@th-rosenheim.de) | "Will ein funktionierendes und gutes Projekt sehen!"  |
| Dozent | Reimer, Mario-Leander (mario-leander.reimer@th-rosenheim.de) | "Will ein funktionierendes und sehr gutes Projekt sehen!"  |

# Randbedingungen

# Kontextabgrenzung

## Fachlicher Kontext
Das System interagiert mit der Open Brewery DB API, um Brauereiinformationen im Quarkus-Backend abzurufen. Diese Daten werden in der PostgreSQL-Datenbank zwischengespeichert und Benutzern über die Angular-Webanwendung zugänglich gemacht.

Die Schnittstelle im Backend bietet die einfache Möglichkeit, eine Liste von Brauereien mit Informationen wie beispielsweise Adresse, Telefonnummer oder Link zur Webseite für eine bestimmte Stadt zurückzugeben.

* GET /api/v1/breweries/{cityName}: Dies ist der Endpunkt, um die Liste der Brauereien abzurufen. Der Pfadparameter {cityName} wird dabei durch den Stadtnamen ersetzt.

Parameter:

* cityName*: Der Name der Stadt, welche für die Suche der Brauereien benötigt wird. Dabei handelt es sich um einen Pfadparameter vom Typ String. Zum Beispiel: "Austin".
  
* count*: Die maximale Anzahl an Brauereien, die abgerufen und zwischengespeichert werden sollen. Dabei handelt es sich um einen Queryparameter vom Typ Integer. Von der Angular-Webanwendung aus werden standardmäßig maximal 20 Brauereien abgerufen.

Responses:

* 200 Success: Erfolgreiche Antwort, die eine Liste der Brauereien in einer jeweiligen Stadt zurückgibt.

* 204 No Content: Es wurden keine Brauereien gefunden.

* 400 Bad Request: Invalide Parameter.

* 404 Not Found: Die angeforderte Ressource wurde nicht gefunden.

* 500 Internal Server Error: Interner Serverfehler.

* Media Type: Die Antwort ist im JSON-Format ('application/json').

Die API-Dokumentation wird mittels Swagger UI bereitgestellt: http://localhost:8080/api/v1/q/swagger-ui

Die OpenAPI-Spezifikation des Backends kann mit folgendem Link im JSON-Format abgerufen werden: http://localhost:8080/api/v1/q/openapi?format=json
### Externe Schnittstelle Open Brewery DB

Die externe Schnittstelle ermöglicht das Abrufen einer Liste von Brauereiinformationen für eine bestimmte Stadt.

* GET https://api.openbrewerydb.org/v1/breweries: Dies ist der externe Endpunkt, über den die Liste der Brauereien abgerufen werden, wenn kein Eintrag im Cache zwischengespeichert ist.

Parameter:

* by_city: Der Name der Stadt als Queryparamter, für die eine Liste an Brauereien im JSON-Format zurückgegeben werden sollen.

* per_page: Die maximale Anzahl an Brauereien als Queryparameter.

API-Dokumentation des externen Endpunkts: https://openbrewerydb.org/documentation#list-breweries

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
