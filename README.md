# Brewery Explorer

**Software-Qualitätssicherung SoSe 2024** 

# Einführung und Ziele

Das Ziel der Brewery Explorer Webanwendung ist es, Brauereien in einer bestimmten Stadt sowie deren Informationen wie beispielsweise Adresse, Telefonnummer oder Link zur Webseite abzurufen. Um die Effizienz der Anfragen zu verbessern, wird eine gezielte Caching-Strategie angewendet. Dafür setze ich Quarkus als Backend-Framework, Angular für das Frontend und PostgreSQL als Datenbank ein. Die benötigten Brauereiinformationen werden von der *Open Brewery DB* API (https://openbrewerydb.org) abgerufen und in der Datenbank, die hierbei als Cache dient, für erneute Anfragen abgespeichert. Die Suche nach Brauereien erfolgt durch die Eingabe des Stadtnamens mithilfe einer benutzerfreundlichen Weboberfläche.

## Aufgabenstellung

[Assignment Details SQS 2024](https://github.com/Okanx68/sqs-project/blob/main/doc/images/Assignment%20Details%20SQS2024.png)

## Qualitätsziele

| Qualitätskriterium | Beschreibung | Ziele | Maßnahmen |
|---------------------|---------------|-------|-----------|
| **1. Reliability - Zuverlässigkeit** | Die Fähigkeit der Anwendung, stabil und fehlerfrei zu laufen, selbst bei unerwarteten Eingaben und hoher Last. | - Resiliente Verarbeitung von Benutzereingaben <br>- Hohe Stabilität bei starker Nutzung | - Umfangreiche Testabdeckung<br>- Lasttests mit Artillery<br>- Integrationstest|
| **2. Portability - Übertragbarkeit** | Flexibilität der Anwendung in Bezug auf die Laufzeitumgebung. |- Externe Abhängigkeiten verringern<br>- Ressourcen effizient nutzen | - Containerisierung mithilfe von Docker zur Isolierung der Laufzeitumgebungen sowie Sicherstellung der Plattformunabhängigkeit<br>- Docker-Compose-Datei zum Starten der Services|
| **3. Usability - Benutzerfreundlichkeit** | Einfache Interaktion mit der Benutzeroberfläche. | - Simple Bedienung<br>- Schnelle Ladezeiten | - End-to-End-Tests mit Playwright<br>- UI-Tests |

## Stakeholder

| Rolle        | Kontakt        | Erwartungshaltung |
|--------------|----------------|-------------------|
| Dozent | Beneken, Gerd (gerd.beneken@th-rosenheim.de) | "Will ein funktionierendes und gutes Projekt sehen!"  |
| Dozent | Reimer, Mario-Leander (mario-leander.reimer@th-rosenheim.de) | "Will ein funktionierendes und sehr gutes Projekt sehen!"  |
| Entwickler | Karaoglan, Okan (okan.karaoglan@stud.th-rosenheim.de) | "Will ein funktionierendes und sehr gutes Projekt entwickeln!" |

# Randbedingungen

Folgende Randbedingungen wurden im Rahmen dieses Projektes festgelegt:

* Das Projekt besteht aus einem Frontend, einem Backend, einer Datenbank und einer externen API
* Zur Datenbeschaffung wird eine externe API verwendet
* Die Datenbank dient als Cache bzw. Zwischenspeicher für die Daten
* Das Backend speichert die Daten der externen API in der Datenbank
* Im Frontend kann der Benutzer einen Suchbegriff eingeben
* Nach Eingabe eines Suchbegriffs im Frontend wird zunächst in der Datenbank nach einem Eintrag gesucht
* Die externe API wird nur angesprochen, wenn kein Eintrag in der Datenbank existiert
* Die gefundenen Daten werden im Frontend visuell dargestellt
* Der eigens geschriebene Code wird umfassend getestet, einschließlich Last-, Integrations-, Unit-, UI- sowie End-to-End-Tests
* Eine CI/CD-Pipeline wird implementiert, die den Code nach jedem Push automatisch baut, testet und bereitstellt
* Der Code wird zusätzlich durch statische Code-Analyse in der Pipeline geprüft
* Alle Services werden als Docker-Container bereitgestellt
* Die Docker-Container werden nach jedem Push in der Pipeline neu gebaut und in der Registry abgelegt
* Über eine Docker-Compose-Datei können die Docker-Container gestartet werden

# Kontextabgrenzung

## Fachlicher Kontext

![Kontextdiagramm](https://github.com/Okanx68/sqs-project/blob/main/doc/images/Kontextdiagramm.drawio.png)

| Kommunikationsbeziehung            | Eingabe                                 | Ausgabe                                |
|------------------------------------|-----------------------------------------|----------------------------------------|
| User -> Brewery Explorer           | Eingabe des Stadtnamens                 | Aktualisierung der Benutzeroberfläche  |
| Brewery Explorer -> User           | -                                       | Anzeige der Brauereiinformationen      |
| Brewery Explorer -> Open Brewery DB| Suchanfrage mit Stadtnamen              | -                                      |
| Open Brewery DB -> Brewery Explorer| -                                       | Rückgabe der Brauereidaten             |

## Technischer Kontext
Das System interagiert mit der externen Open Brewery DB API, um Brauereiinformationen im Quarkus-Backend abzurufen. Diese Daten werden in der PostgreSQL-Datenbank zwischengespeichert und Benutzern über die Angular-Webanwendung zugänglich gemacht.
### UML Deployment Diagramm

![UML Deployment Diagramm](https://github.com/Okanx68/sqs-project/blob/main/doc/images/UML_Deployment_Diagram.png)

| Technischer Kanal                  | Eingabe                                      | Ausgabe                                |
|------------------------------------|----------------------------------------------|----------------------------------------|
| User -> Angular Application       | Benutzeraktionen (z.B. Stadtnamen)           | Aktualisierte Benutzeroberfläche       |
| Angular Frontend -> BreweryDataResource | HTTP Request (Stadtnamen)              | HTTP Response (Brauereidaten)  |
| BreweryDataResource -> BreweryDataController | API Call                              | Ergebnis der Datenabfrage                  |
| BreweryDataController -> BreweryDBService | API Request (Stadtnamen)               | API Response (Brauereidaten)           |
| BreweryDBService -> Open Brewery DB | API Request (Stadtnamen)                   | Liste der Brauereien                   |
| Open Brewery DB -> BreweryDBService | -                                           | Brauereiinformationen                  |
| BreweryDataController -> BreweryData | Datenabfrage                                | Daten speichern/abrufen                |
| BreweryData -> PostgreSQL          | SQL Query                                   | SQL Response                           |
| BreweryDataController -> BreweryDataDTO | Datenkonvertierung                       | DTO                                    |
| BreweryDataResource -> Angular Application | HTTP Response                          | Aktualisierte Benutzeroberfläche       |


### Schnittstelle zum Backend

Die '*BreweryDataResource*' Schnittstelle im Backend bietet die einfache Möglichkeit, eine Liste von Brauereien mit Informationen wie beispielsweise Adresse, Telefonnummer oder Link zur Webseite für eine bestimmte Stadt zurückzugeben.

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

### Externe technische Schnittstelle - Open Brewery DB
Externe REST-API, die Brauereiinformationen basierend auf Stadtnamen zurückgibt. Der '*BreweryDBService*' kommuniziert direkt mit dieser API, um die Daten abzurufen und an den '*BreweryDataController*' weiterzugeben.

* GET https://api.openbrewerydb.org/v1/breweries: Dies ist der externe Endpunkt, über den die Liste der Brauereien abgerufen werden, wenn kein Eintrag im Cache zwischengespeichert ist.

Parameter:

* by_city: Der Name der Stadt als Queryparamter, für die eine Liste an Brauereien im JSON-Format zurückgegeben werden sollen.

* per_page: Die maximale Anzahl an Brauereien als Queryparameter.

API-Dokumentation des externen Endpunkts: https://openbrewerydb.org/documentation#list-breweries

### Mapping fachlicher auf technische Schnittstellen

- **Fachliche Eingabe: Stadtnamen** -> **Technische Schnittstelle: HTTP Request von der Angular Application an BreweryDataResource**
- **Fachliche Ausgabe: Brauereidaten** -> **Technische Schnittstelle: HTTP Response von BreweryDataResource an die Angular Application**

# Lösungsstrategie

Die zentrale Entwurfsstrategie dieses Projekts basiert auf mehreren wichtigen Technologieentscheidungen und Systementwürfen, die auf die spezifischen Aufgabenstellungen, Qualitätsziele und Randbedingungen abgestimmt sind.

## Technologieentscheidungen
Für das Backend wurde Quarkus als Framework ausgewählt. Quarkus bietet schnelle Startzeiten, geringe Speicherauslastung und native Unterstützung für GraalVM, was den Anforderungen an Performance und Skalierbarkeit gerecht wird. Angular wurde als Frontend-Framework gewählt, da es eine robuste Plattform für die Entwicklung dynamischer Single-Page-Anwendungen bietet und eine intuitive Benutzeroberfläche ermöglicht. PostgreSQL dient als Datenbank und wurde aufgrund seiner Zuverlässigkeit, Leistungsfähigkeit und Erweiterbarkeit ausgewählt. Die Datenbank wird als Cache für die Daten der externen API genutzt. Zur Beschaffung von Brauereiinformationen wird die Open Brewery DB API verwendet, da sie eine umfassende Datenquelle für die benötigten Informationen bietet.

## Top-Level-Zerlegung des Systems
Das System folgt einer Microservices-Architektur, bei der mehrere lose gekoppelte Dienste jeweils spezifische Funktionalitäten bereitstellen. Diese Architektur ermöglicht eine einfachere Wartung, Skalierung und Weiterentwicklung der einzelnen Komponenten. Zudem werden alle Services als Docker-Container bereitgestellt, was für Konsistenz zwischen Entwicklungs- und Produktionsumgebungen sorgt und die Bereitstellung und Skalierung mithilfe einer Docker-Compose-Datei erleichtert.

## Qualitätsanforderungen
Ein zentraler Aspekt der Entwurfsstrategie ist die Sicherstellung der **Zuverlässigkeit (Reliability)** der Anwendung. Diese umfasst die Fähigkeit der Anwendung, stabil und fehlerfrei zu laufen, selbst bei unerwarteten Eingaben und hoher Last. Um diese Ziele zu erreichen, wird eine resiliente Verarbeitung von Benutzereingaben und eine hohe Stabilität bei starker Nutzung angestrebt. Maßnahmen zur Erreichung dieser Ziele umfassen eine umfangreiche Testabdeckung, Lasttests mit Artillery sowie Integrationstests.

Ein weiteres wichtiges Qualitätsziel ist die **Übertragbarkeit (Portability)** der Anwendung. Dies bezieht sich auf die Flexibilität der Anwendung in Bezug auf die Laufzeitumgebung. Um dies zu gewährleisten, sollen externe Abhängigkeiten verringert und die Ressourcennutzung effizient gestaltet werden. Hierfür wird die Containerisierung mithilfe von Docker eingesetzt, um isolierte Laufzeitumgebungen zu schaffen und die Plattformunabhängigkeit sicherzustellen. Eine Docker-Compose-Datei wird bereitgestellt, um die Verwaltung und Orchestrierung der verschiedenen Docker-Container zu erleichtern.

Darüber hinaus spielt die **Benutzerfreundlichkeit (Usability)** eine entscheidende Rolle. Eine einfache Interaktion mit der Benutzeroberfläche ist essentiell. Die Ziele hierbei sind eine simple Bedienung und schnelle Ladezeiten. Maßnahmen zur Erreichung dieser Ziele umfassen End-to-End-Tests mit Playwright und UI-Tests, um eine optimale Benutzererfahrung zu gewährleisten.

## Organisatorische Entscheidungen
Ein wesentlicher Bestandteil der organisatorischen Entscheidungen in diesem Projekt ist die Implementierung einer CI/CD-Pipeline mithilfe von GitHub Actions um für eine hohe Qualität und Zuverlässigkeit des Codes zu sorgen. Diese Pipeline automatisiert den gesamten Prozess vom Code-Commit bis zur Bereitstellung und gewährleistet eine kontinuierliche Integration und Auslieferung neuer Funktionen und Verbesserungen. Bei jedem Push oder Pull-Request wird der Code automatisch gebaut und durch eine Reihe automatisierter Tests, einschließlich Unit-, Integrations- und End-to-End-Tests, geprüft. Zusätzlich werden Frontend-Tests durchgeführt, um sicherzustellen, dass die Benutzeroberfläche den Erwartungen entspricht. SonarCloud ergänzt als statisches Code-Analyse-Tool die Tests, um sicherzustellen, dass der Code den Qualitätsstandards entspricht. Nach erfolgreichem Bestehen aller Tests werden Docker-Container erstellt und in einer Registry gespeichert. Diese Container können dann in die verschiedenen Umgebungen mithilfe der Docker-Compose-Datei bereitgestellt werden. Um die Leistungsfähigkeit und Stabilität der Anwendung unter hoher Last zu gewährleisten, werden zudem automatisierte Lasttests mit Artillery durchgeführt.
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
