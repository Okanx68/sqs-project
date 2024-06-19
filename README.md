# Brewery Explorer

**Software-Qualitätssicherung SoSe 2024** 

# Einführung und Ziele

Das Ziel der Brewery Explorer Webanwendung ist es, Brauereien in einer bestimmten Stadt sowie deren Informationen wie beispielsweise Adresse, Telefonnummer oder Link zur Webseite abzurufen. Um die Effizienz der Anfragen zu verbessern, wird eine gezielte Caching-Strategie angewendet. Dafür setze ich Quarkus als Backend-Framework, Angular für das Frontend und PostgreSQL als Datenbank ein. Die benötigten Brauereiinformationen werden von der *Open Brewery DB* API (https://openbrewerydb.org) abgerufen und in der Datenbank, die hierbei als Cache dient, für erneute Anfragen abgespeichert. Die Suche nach Brauereien erfolgt durch die Eingabe des Stadtnamens mithilfe einer benutzerfreundlichen Weboberfläche.

## Aufgabenstellung

[Assignment Details SQS 2024](https://github.com/Okanx68/sqs-project/blob/main/doc/images/Assignment%20Details%20SQS2024.png)

## Qualitätsziele

| Qualitätskriterium | Beschreibung | Ziele | Maßnahmen |
|---------------------|---------------|-------|-----------|
| **1. Reliability - Zuverlässigkeit** | Die Fähigkeit der Anwendung, stabil und fehlerfrei zu laufen, selbst bei unerwarteten Eingaben und hoher Last. | - Resiliente Verarbeitung von Benutzereingaben <br>- Hohe Stabilität bei starker Nutzung | - Umfangreiche Testabdeckung mit Unit-Tests<br>- Lasttests mit Artillery<br>- Integrationstest|
| **2. Portability - Übertragbarkeit** | Flexibilität der Anwendung in Bezug auf die Laufzeitumgebung. |- Externe Abhängigkeiten verringern<br>- Ressourcen effizient nutzen<br>-Browserunabhängige Nutzbarkeit | - Containerisierung mithilfe von Docker zur Isolierung der Laufzeitumgebungen sowie Sicherstellung der Plattformunabhängigkeit<br>- Docker-Compose-Datei zum Starten der Services<br>- End-to-End-Tests mit Playwright |
| **3. Usability - Benutzerfreundlichkeit** | Einfache Interaktion mit der Benutzeroberfläche. | - Simple Bedienung<br>- Übersichtliche Weboberfläche | - End-to-End-Tests mit Playwright<br>- UI-Tests |

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

* GET `/api/v1/breweries/{cityName}`: Dies ist der Endpunkt, um die Liste der Brauereien abzurufen. Der Pfadparameter {cityName} wird dabei durch den Stadtnamen ersetzt.

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

* per_page: Die maximale Anzahl an Brauereien als Queryparameter (standardmäßig maximal 20 Brauereien).

API-Dokumentation des externen Endpunkts: https://openbrewerydb.org/documentation#list-breweries

### Mapping fachlicher auf technische Schnittstellen

- **Fachliche Eingabe: Stadtnamen** -> **Technische Schnittstelle: HTTP Request von der Angular Application an BreweryDataResource**
- **Fachliche Ausgabe: Brauereidaten** -> **Technische Schnittstelle: HTTP Response von BreweryDataResource an die Angular Application**

# Lösungsstrategie

Die zentrale Entwurfsstrategie dieses Projekts basiert auf mehreren wichtigen Technologieentscheidungen und Systementwürfen, die auf die spezifischen Aufgabenstellungen, Qualitätsziele und Randbedingungen abgestimmt sind.

## Technologieentscheidungen
Für das Backend wurde Quarkus als Framework ausgewählt. Quarkus bietet schnelle Startzeiten, eine nahtlose Integration mit Container-Technologien wie Docker und ist besonders gut für Microservice-Architekturen geeignet, was den Anforderungen an Performance und Skalierbarkeit gerecht wird. Angular wurde als Frontend-Framework gewählt, da es eine robuste Plattform für die Entwicklung dynamischer Single-Page-Anwendungen bietet und eine intuitive Benutzeroberfläche ermöglicht. PostgreSQL dient als Datenbank und wurde aufgrund seiner Zuverlässigkeit, Leistungsfähigkeit und Erweiterbarkeit ausgewählt. Die Datenbank wird als Cache für die Daten der externen API genutzt. Zur Beschaffung von Brauereiinformationen wird die Open Brewery DB API verwendet, da sie eine umfassende Datenquelle für die benötigten Informationen bietet.

## Top-Level-Zerlegung des Systems
Das System folgt einer Microservices-Architektur, bei der mehrere lose gekoppelte Dienste jeweils spezifische Funktionalitäten bereitstellen. Diese Architektur ermöglicht eine einfachere Wartung, Skalierung und Weiterentwicklung der einzelnen Komponenten. Zudem werden alle Services als Docker-Container bereitgestellt, was für Konsistenz zwischen Entwicklungs- und Produktionsumgebungen sorgt und die Bereitstellung und Skalierung mithilfe einer Docker-Compose-Datei erleichtert.

## Qualitätsanforderungen
Ein zentraler Aspekt der Entwurfsstrategie ist die Sicherstellung der **Zuverlässigkeit (Reliability)** der Anwendung. Diese umfasst die Fähigkeit der Anwendung, stabil und fehlerfrei zu laufen, selbst bei unerwarteten Eingaben und hoher Last. Um diese Ziele zu erreichen, wird eine resiliente Verarbeitung von Benutzereingaben und eine hohe Stabilität bei starker Nutzung angestrebt. Maßnahmen zur Erreichung dieser Ziele umfassen eine umfangreiche Testabdeckung mit Unit-Tests, Lasttests mit Artillery sowie Integrationstests.

Ein weiteres wichtiges Qualitätsziel ist die **Übertragbarkeit (Portability)** der Anwendung. Dies bezieht sich auf die Flexibilität der Anwendung in Bezug auf die Laufzeitumgebung. Um dies zu gewährleisten, sollen externe Abhängigkeiten verringert und die Ressourcennutzung effizient gestaltet werden. Hierfür wird die Containerisierung mithilfe von Docker eingesetzt, um isolierte Laufzeitumgebungen zu schaffen und die Plattformunabhängigkeit sicherzustellen. Eine Docker-Compose-Datei wird bereitgestellt, um die Verwaltung und Orchestrierung der verschiedenen Docker-Container zu erleichtern. End-to-End-Tests mit Playwright gewährleisten, dass die Anwendung auf verschiedenen Browsern einwandfrei funktioniert, was die browserunabhängige Nutzbarkeit sicherstellt.

Darüber hinaus spielt die **Benutzerfreundlichkeit (Usability)** eine entscheidende Rolle. Eine einfache Interaktion mit der Benutzeroberfläche ist essentiell. Die Ziele hierbei sind eine simple Bedienung und eine übersichtliche Weboberfläche. Maßnahmen zur Erreichung dieser Ziele umfassen End-to-End-Tests mit Playwright und UI-Tests, um eine optimale Benutzererfahrung zu gewährleisten.

## Organisatorische Entscheidungen
Ein wesentlicher Bestandteil der organisatorischen Entscheidungen in diesem Projekt ist die Implementierung einer CI/CD-Pipeline mithilfe von GitHub Actions um für eine hohe Qualität und Zuverlässigkeit des Codes zu sorgen. Diese Pipeline automatisiert den gesamten Prozess vom Code-Commit bis zur Bereitstellung und gewährleistet eine kontinuierliche Integration und Auslieferung neuer Funktionen und Verbesserungen. Bei jedem Push oder Pull-Request wird der Code automatisch gebaut und durch eine Reihe automatisierter Tests, einschließlich Unit-, Integrations- und End-to-End-Tests, geprüft. Zusätzlich werden Frontend-Tests durchgeführt, um sicherzustellen, dass die Benutzeroberfläche den Erwartungen entspricht. ArchUnit-Tests werden im Backend verwendet, um die Einhaltung von Architekturregeln zu überprüfen. Die Dockerfiles werden in der Pipeline gelintet, um sicherzustellen, dass sie syntaktisch korrekt und effizient geschrieben sind. SonarCloud ergänzt als statisches Code-Analyse-Tool die Tests, um sicherzustellen, dass der Code den Qualitätsstandards entspricht. Nach erfolgreichem Bestehen aller Tests werden Docker-Container erstellt und in einer Registry gespeichert. Diese Container können dann in die verschiedenen Umgebungen mithilfe der Docker-Compose-Datei bereitgestellt werden. Um die Leistungsfähigkeit und Stabilität der Anwendung unter hoher Last zu gewährleisten, werden zudem automatisierte Lasttests mit Artillery durchgeführt.

# Bausteinsicht

## Whitebox Gesamtsystem

**Übersichtsdiagramm**

![Whitebox Gesamtsystem](https://github.com/Okanx68/sqs-project/blob/main/doc/images/Whitebox_Gesamtsystem.drawio.png)

**Begründung**
Die Zerlegung des Gesamtsystems in Bausteine folgt den Prinzipien der Modularität und der Verantwortlichkeitstrennung. Dadurch wird die Wartbarkeit und Erweiterbarkeit des Systems verbessert.

**Enthaltene Bausteine**

| Name               | Verantwortung                                  |
|--------------------|----------------------------------------------|
| Angular-Frontend    | Bereitstellungg der Benutzeroberfläche        |
| Quarkus-Backend | Verarbeitung der Geschäftslogik und Bereitstellung der API-Endpunkte            |
| PostgeSQL-Datenbank | Speicherung und Verwaltung der Daten           |
| Open Brewery DB API | Externe Quelle für Brauereiinformationen           |


**Wichtige Schnittstellen**  

| Schnittstelle      | Beschreibung                                  |
|--------------------|----------------------------------------------|
| Frontend-Backend   | Schnittstelle für die Kommunikation zwischen Angular-Frontend und Quarkus-Backend       |
| Backend-Datenbank | Schnittstelle für die Kommunikation zwischen Quarkus-Backend und PostgreSQL-Datenbank            |
| Backend-Externe-API | Schnittstelle für die Kommunikation zwischen Quarkus-Backend und Open Brewery DB API          |

### Angular-Frontend

**Zweck/Verantwortung**
Das Angular-Frontend ist verantwortlich für die Bereitstellung der Benutzeroberfläche, über die Benutzer nach Brauereien suchen und Informationen anzeigen können.

**Schnittstelle(n)**
* HTTP GET `api/v1/breweries/{cityName}`: Schnittstelle zur Abfrage von Brauereiinformationen basierend auf dem Stadtnamen.

### Quarkus-Backend

**Zweck/Verantwortung**
Das Quarkus-Backend verarbeitet die Geschäftslogik und stellt API-Endpunkte zur Verfügung, über die das Frontend und andere externe Systeme auf die Anwendung zugreifen können.

**Schnittstelle(n)**
* HTTP GET `api/v1/breweries/{cityName}`: Endpunkt zur Abfrage von Brauereiinformationen.
* JDBC: Schnittstelle zur Kommunikation mit der PostgreSQL-Datenbank.
  
### PostgreSQL-Datenbank

**Zweck/Verantwortung**
Die PostgreSQL-Datenbank speichert und verwaltet alle Daten, die von der Anwendung benötigt werden, einschließlich der gecachten Brauereiinformationen.

**Schnittstelle(n)**
* JDBC: Schnittstelle zur Kommunikation mit dem Quarkus Backend.

### Open Brewery DB API

**Zweck/Verantwortung**
Die Open Brewery DB API dient als externe Quelle für Brauereiinformationen und wird vom Backend verwendet, um Daten abzurufen, die nicht im lokalen Cache verfügbar sind.

**Schnittstelle(n)
* HTTP GET `https://api.openbrewerydb.org/v1/breweries?by_city={cityName}&per_page={count}`: Endpunkt zur Abfrage von Brauereiinformationen basierend auf dem Stadtnamen und der maximalen Anzahl an Brauerein.

## Ebene 2

### Whitebox Quarkus-Backend

**Übersichtsdiagramm**

![Whitebox Quarkus Backend](https://github.com/Okanx68/sqs-project/blob/main/doc/images/Quarkus_Backend_Ebene_modified.drawio.png)

**Enthaltene Bausteine**

| Name               | Verantwortung                                  |
|--------------------|----------------------------------------------|
| BreweryDataResource    | Bereitstellung der API-Endpunkte zur Abfrage von Brauereidaten       |
| BreweryDataController | Geschäftslogik zur Verarbeitung von Brauereianfragen          |
| BreweryDBService | Kommunikation mit der Open Brewery DB API           |
| BreweryData | Datenmodell für Brauereidaten sowie Kommunikation mit der Datenbank          |
| BreweryDataDTO | Datenübertragungsobjekt für Brauereiinformationen         |

### Whitebox Angular-Frontend

**Übersichtsdiagramm**

![Whitebox Angular Frontend](https://github.com/Okanx68/sqs-project/blob/main/doc/images/Angular_Frontend_Ebene_new.drawio.png)

**Enthaltene Bausteine**

| Name               | Verantwortung                                  |
|--------------------|----------------------------------------------|
| AppComponent    | Bereitstellung der Weboberfläche (Suchfunktion sowie Anzeige der Liste von Brauerein)      |
| BreweryService | Kommunikation mit dem Backend zur Abfrage von Brauerdaten        |
| BreweryModel | Datenmodell für Brauereidaten           |

# Laufzeitsicht

## Szenario 1: Abruf von Brauereiinformationen (Cache-Hit)

### Ablaufbeschreibung

1. **Benutzeranfrage im Frontend**: Ein Benutzer navigiert auf der Angular-basierten Webanwendung und gibt den Namen einer Stadt ein, um Informationen zu Brauereien abzurufen.
2. **Anfrage an das Backend**: Das Frontend sendet eine HTTP GET-Anfrage an das Quarkus-Backend. Der Endpunkt ist `/api/v1/breweries/{cityName}`.
3. **Überprüfung des Caches im Backend**: Das Backend prüft, ob die Brauereiinformationen für die angefragte Stadt bereits im Cache (PostgreSQL-Datenbank) vorhanden sind.
4. **Cache-Hit**: Wenn die Informationen im Cache vorhanden sind, werden diese aus dem Cache an das Backend gesendet.
5. **Antwort an das Frontend**: Das Backend sendet die Brauereiinformationen an das Frontend.
6. **Anzeige der Daten**: Das Frontend zeigt die erhaltenen Baustelleninformationen dem Benutzer an.

### Laufzeitdiagramm

![Laufzeitdiagramm_Szenario1](https://github.com/Okanx68/sqs-project/blob/main/doc/images/Laufzeitdiagramm_Szenario1_cache_hit_modified.drawio.png)

## Szenario 2: Abruf von Brauereiinformationen (Cache-Miss)

### Ablaufbeschreibung

1. **Benutzeranfrage im Frontend**: Ein Benutzer navigiert auf der Angular-basierten Webanwendung und gibt den Namen einer Stadt ein, um Informationen zu Brauereien abzurufen.
2. **Anfrage an das Backend**: Das Frontend sendet eine HTTP GET-Anfrage an das Quarkus-Backend. Der Endpunkt ist `/api/v1/breweries/{cityName}`.
3. **Überprüfung des Caches im Backend**: Das Backend prüft, ob die Brauereiinformationen für die angefragte Stadt bereits im Cache (PostgreSQL-Datenbank) vorhanden sind.
4. **Cache-Miss**: Wenn die Informationen nicht im Cache vorhanden sind, wird eine Anfrage an die externe API der Open Brewery DB gesendet.
5. **Anfrage and die API der Open Brewery DB**: Das Backend sendet eine HTTP GET-Anfrage an die API der Open Brewery DB, um die Brauereiinformationen für die angefragte Stadt abzurufen.
6. **Empfang und Speicherung der Daten**: Die API der Open Brewery DB liefert die Daten im JSON-Format zurück. Das Backend speichert diese Daten im Cache (PostgreSQL-Datenbank).
7. **Antwort an das Frontend**: Das Backend sendet die Brauereiinformationen an das Frontend.
8. **Anzeige der Daten**: Das Frontend zeigt die erhaltenen Baustelleninformationen dem Benutzer an.

### Laufzeitdiagramm

![Laufzeitdiagramm_Szenario2](https://github.com/Okanx68/sqs-project/blob/main/doc/images/Laufzeitdiagramm_Szenario2_cache_miss.drawio.png)

## Szenario 3: Fehlgeschlagener Abruf von Brauereiinformationen (falscher Stadtnamen)

### Ablaufbeschreibung

1. **Benutzeranfrage im Frontend**: Ein Benutzer navigiert auf der Angular-basierten Webanwendung und gibt den Namen einer Stadt ein, um Informationen zu Brauereien abzurufen.
2. **Anfrage an das Backend**: Das Frontend sendet eine HTTP GET-Anfrage an das Quarkus-Backend. Der Endpunkt ist `/api/v1/breweries/{cityName}`.
3. **Überprüfung des Caches im Backend**: Das Backend prüft, ob die Brauereiinformationen für die angefragte Stadt bereits im Cache (PostgreSQL-Datenbank) vorhanden sind.
4. **Cache-Miss**: Wenn die Informationen nicht im Cache vorhanden sind, wird eine Anfrage an die externe API der Open Brewery DB gesendet.
5. **Anfrage and die API der Open Brewery DB**: Das Backend sendet eine HTTP GET-Anfrage an die API der Open Brewery DB, um die Brauereiinformationen für die angefragte Stadt abzurufen.
6. **Fehlerantwort von der API der Open Brewery DB**: Die API der Open Brewery DB liefert keine Brauereiinformationen zurück.
7. **Antwort an das Frontend**: Das Backend sendet eine Fehlerantwort an das Frontend, um den Benutzer über den Fehler zu informieren.
8. **Anzeige der Daten**: Das Frontend zeigt eine entsprechende Fehlermeldung dem Benutzer an ("City not found. Try again.").

### Laufzeitdiagramm

![Laufzeitdiagramm_Szenario2](https://github.com/Okanx68/sqs-project/blob/main/doc/images/Laufzeitdiagramm_Szenario3.drawio.png)

# Verteilungssicht

## Infrastruktur 

![Verteilungssicht](https://github.com/Okanx68/sqs-project/blob/main/doc/images/Verteilungssicht_modified.drawio.png)

**Begründung**
Das System wird in einer Container-Umgebung mittels einer Docker-Compose-Datei betrieben, um eine einfache Skalierbarkeit, Portabilität und Konsistenz zu gewährleisten. Docker-Container werden verwendet, um die einzelnen Komponenten des Systems zu isolieren und zu verwalten.

**Qualitäts- und/oder Leistungsmerkmale**
* **Skalierbarkeit**: Durch den Einsatz von Docker-Containern kann das System leicht horizontal skaliert werden, indem zusätzliche Container bei Bedarf gestartet werden.
* **Portabilität**: Docker gewährleistet, dass die Anwendung in verschiedenen Umgebungen gleich läuft, was die Portabilität zwischen Entwicklungs-, Test- und Produktionsumgebungen verbessert.
* **Isolation**: Jeder Dienst läuft in seinem eigenen Container, was die Isolation und damit die Stabilität und Sicherheit des Systems erhöht.
* **Wiederholbarkeit**: Die Verwendung von Docker-Compose ermöglicht eine einfache und wiederholbare Bereitstellung der gesamten Systemumgebung.

**Zuordnung von Bausteinen zu Infrastruktur**

| Baustein               | Infrastruktur                                  |
|--------------------|----------------------------------------------|
| Frontend-Container    | Docker-Container "sqs_frontend" wird bereitgestellt auf Nginx-Container "nginx:alpine" |
| Backend-Container | Docker-Container "sqs_backend" wird bereitsgestellt auf JVM-Container "registry.access.redhat.com/ubi8/openjdk-17:1.18" |
| PostgeSQL-Datenbank | Docker-Container "sqs_pg_compose" wird bereitgestellt auf Datenbank-Container "postgres:14" |
| Open Brewery DB API | Externer Service |

**Docker-Compose-Datei**

In diesem Projekt genutzte Docker-Compose-Datei: [*\<Docker-Compose-File\>*](https://github.com/Okanx68/sqs-project/blob/main/docker-compose.yml)

Die Images für das Front- und Backend werden aus der GitHub-Registry des Projektes gezogen.

    +-------------------------+           +---------------------------+           +----------------------------+  
    |                         |           |                           |           |                            |  
    |           db            |---------->|         backend           |---------->|         frontend           |  
    |   postgres:14           |           |  sqs_backend              |           |  sqs_frontend              |  
    |   Port: 5432            |           |  Ports: 8080              |           |  Port: 4200                |  
    |   Volumes:              |           |  Depends on: db           |           |  Depends on: backend       |  
    |   postgres_data         |           |                           |           |                            |  
    |                         |           |                           |           |                            |  
    +-------------------------+           +---------------------------+           +----------------------------+

# Querschnittliche Konzepte

## Backend UML-Klassendiagramm 

![UML-Klassendiagramm](https://github.com/Okanx68/sqs-project/blob/main/doc/images/UML_Klassendiagramm_Backend.png)

## GitHub Actions

In diesem Projekt verwendete Pipeline: [*\<Workflow-File\>*](https://github.com/Okanx68/sqs-project/blob/main/docker-compose.yml)

```
+---------------------+         +---------------------------+
| lint_dockerfiles    |-------->| build_analyze_push        |
| - Lint Backend      |         | - Build backend           |
|   Dockerfile        |         | - Build frontend          |
| - Lint Frontend     |         | - Run SonarCloud Analysis |
|   Dockerfile        |         | - Push Docker images      |
+---------------------+         +-----------+---------------+
                                           |
                                           v
                              +------------+-------------+
                              |                          |
                +-----------------------+     +------------------------+
                | artillery_test        |     | end-to-end-tests       |
                | - Start Docker Compose|     | - Start Docker Compose |
                | - Run Artillery Tests |     | - Run Playwright Tests |
                +-----------------------+     +------------------------+
```

## Artillery & Playwright 

Lasttest-Skript: [*\<Artillery-Template\>*](https://github.com/Okanx68/sqs-project/blob/main/artillery-tests/artillery.yml)

End-to-End-Tests: [*\<Playwright-Tests\>*](https://github.com/Okanx68/sqs-project/blob/main/playwright/tests/homepage.spec.js)

## SonarCloud

Statische Code-Analyse für Front- und Backend: [*\<SonarCloud-Analyse\>*](https://sonarcloud.io/organizations/sqs-project/projects)

# Architekturentscheidungen

Die Architekturentscheidungen für dieses Projekt wurden sorgfältig getroffen, um eine robuste, skalierbare und wartbare Anwendung zu gewährleisten.

## Technologiewahl

**Quarkus**: Für das Backend wurde Quarkus ausgewählt, ein Framework, das sich durch schnelle Startzeiten und geringe Ressourcenanforderungen auszeichnet. Es ist besonders geeignet für Cloud-native und Microservice-Architekturen und ermöglicht eine nahtlose Integration mit Container-Technologien wie Docker.

**Angular**: Angular dient als Frontend-Framework. Es bietet eine leistungsstarke Plattform für die Entwicklung dynamischer Single-Page-Anwendungen und unterstützt eine intuitive Benutzeroberfläche.

**PostgreSQL**: Als Datenbank wurde PostgreSQL gewählt, um von seiner Zuverlässigkeit, Leistungsfähigkeit und Erweiterbarkeit zu profitieren. Die Datenbank wird als Cache für die Daten der externen API verwendet.

**Open Brewery DB API**: Zur Beschaffung von Brauereiinformationen wird die Open Brewery DB API genutzt, die eine umfassende und zuverlässige Datenquelle bietet.

**Docker**: Die Anwendung wurde mit Docker containerisiert, um eine konsistente Umgebung für Entwicklung, Test und Produktion zu gewährleisten.

**Docker Compose**: Zur Verwaltung der Container wurde Docker Compose eingesetzt, um die Container-Orchestrierung zu erleichtern.

**GitHub Actions**: Es wurden automatisierte CI/CD-Pipelines mit GitHub Actions eingerichtet, die das Bauen, Testen und Bereitstellen der Anwendung automatisieren. Dazu gehören auch statische Code-Analysen mit SonarCloud und das Bereitstellen der Docker-Container in einer Registry.

**Playwright**: End-to-End-Tests wurden mit Playwright durchgeführt, um die Funktionalität der gesamten Anwendung sicherzustellen.

**Artillery**: Für Lasttests wurde Artillery verwendet, um die Leistungsfähigkeit und Stabilität der Anwendung unter hoher Last zu überprüfen.

**JUnit**: JUnit wurde für Unit- und Integrationstests genutzt, um die Qualität und Korrektheit des Codes sicherzustellen.

**SonarCloud**: SonarCloud wurde zur statischen Code-Analyse verwendet, um die Codequalität zu überwachen und potenzielle Sicherheitslücken und Bugs frühzeitig zu identifizieren.

**ArchUnit**: ArchUnit wurde genutzt, um Architektur- und Design-Prinzipien im Backend zu überprüfen, sicherzustellen und durchzusetzen. Dies unterstützt die langfristige Wartbarkeit und Konsistenz des Codes.

**Hadolint**: Hadolint wurde verwendet, um die Dockerfiles zu linten und sicherzustellen, dass sie syntaktisch korrekt und nach bewährten Methoden geschrieben sind.

## Schichtenmodell

Das System wurde in mehreren Schichten organisiert, um eine klare Trennung von Präsentation, Geschäftslogik und Datenzugriff zu gewährleisten. Diese Struktur erhöht die Wartbarkeit und Skalierbarkeit der Anwendung. Im Detail besteht die Architektur aus folgenden Schichten:

* **Präsentationsschicht**: Diese Schicht umfasst das Angular-Frontend, das für die Benutzeroberfläche zuständig ist. Sie ermöglicht eine reaktive und benutzerfreundliche Interaktion mit dem System.

* **Geschäftslogikschicht**: Diese Schicht wird durch das Quarkus-Backend repräsentiert. Hier wird die Anwendungslogik implementiert, die die Geschäftsprozesse steuert und verarbeitet. Diese Schicht nimmt Anfragen vom Frontend entgegen, bearbeitet sie und kommuniziert mit der Datenzugriffsschicht, um die erforderlichen Daten zu erhalten oder zu speichern. ArchUnit-Tests werden hier verwendet, um sicherzustellen, dass die Architekturregeln in dieser Schicht eingehalten werden.

* **Datenzugriffsschicht**: Diese Schicht beinhaltet die Interaktionen mit der PostgreSQL-Datenbank. Sie kümmert sich um das Speichern, Abrufen und Verwalten der Daten. Die Datenbank dient auch als Cache für die Daten der externen API (Open Brewery DB).

## Entwicklungsprozess
Die Entwicklung des Projekts begann mit einem Backend-First Ansatz, um eine stabile Grundlage zu schaffen. Dieser Ansatz stellte sicher, dass die Geschäftslogik und Datenverwaltung solide implementiert wurden, bevor die Benutzeroberfläche entwickelt wurde. Nach der Fertigstellung und gründlichen Testung des Backends, das API- und Datenbankinteraktionen sicherstellt, wurde das Angular-Frontend entwickelt und nahtlos integriert.

Um eine hohe Qualität zu gewährleisten, wurden umfassende Tests durchgeführt, darunter Unit-Tests, Integrationstests, End-to-End-Tests mit Playwright sowie Lasttests mit Artillery. Diese Teststrategien stellten sicher, dass alle Systemkomponenten zuverlässig und performant zusammenarbeiten. ArchUnit-Tests wurden im Backend eingesetzt, um zusätzlich die Einhaltung von Architektur- und Designprinzipien zu überprüfen und sicherzustellen.

Die CI/CD-Pipeline, implementiert mit GitHub Actions, automatisierte den gesamten Prozess vom Code-Commit bis zur Bereitstellung. Diese Pipeline baute, testete und stellte die Anwendung bereit, wodurch kontinuierliche Integration und Auslieferung neuer Funktionen und Verbesserungen gewährleistet wurden. Zudem wurde eine statische Code-Analyse mit SonarCloud in der Pipeline durchgeführt, um die Codequalität zu überwachen und potenzielle Sicherheitslücken frühzeitig zu erkennen.

## Technologische Eigenschaften des Projekts

### Frontend

| Technologie  | Version       |
|----------------|-----------------|
| Angular    | 17.3.11 |
| Node | 20.13.0 |
| Bootstrap | 5.3.3 |

### Backend

| Technologie  | Version       |
|----------------|-----------------|
| Quarkus    | 3.9.1 |
| Maven | 3.8.6 |
| Java | 17 |

### Database

| Technologie  | Version       |
|----------------|-----------------|
| PostgreSQL    | 14 |

### Testing

| Technologie  | Version       |
|----------------|-----------------|
| JUnit      | 5 |
| ArchUnit | 1.3.0 |
| Artillery    | latest |
| Playwright | latest |

# Qualitätsanforderungen

Ein zentraler Aspekt der Entwurfsstrategie ist die Sicherstellung der **Zuverlässigkeit (Reliability)** der Anwendung. Diese umfasst die Fähigkeit der Anwendung, stabil und fehlerfrei zu laufen, selbst bei unerwarteten Eingaben und hoher Last. Um diese Ziele zu erreichen, wird eine resiliente Verarbeitung von Benutzereingaben und eine hohe Stabilität bei starker Nutzung angestrebt. Maßnahmen zur Erreichung dieser Ziele umfassen eine umfangreiche Testabdeckung mit Unit-Tests, Lasttests mit Artillery sowie Integrationstests.

Ein weiteres wichtiges Qualitätsziel ist die **Übertragbarkeit (Portability)** der Anwendung. Dies bezieht sich auf die Flexibilität der Anwendung in Bezug auf die Laufzeitumgebung. Um dies zu gewährleisten, sollen externe Abhängigkeiten verringert und die Ressourcennutzung effizient gestaltet werden. Hierfür wird die Containerisierung mithilfe von Docker eingesetzt, um isolierte Laufzeitumgebungen zu schaffen und die Plattformunabhängigkeit sicherzustellen. Eine Docker-Compose-Datei wird bereitgestellt, um die Verwaltung und Orchestrierung der verschiedenen Docker-Container zu erleichtern. End-to-End-Tests mit Playwright gewährleisten, dass die Anwendung auf verschiedenen Browsern einwandfrei funktioniert, was die browserunabhängige Nutzbarkeit sicherstellt.

Darüber hinaus spielt die **Benutzerfreundlichkeit (Usability)** eine entscheidende Rolle. Eine einfache Interaktion mit der Benutzeroberfläche ist essentiell. Die Ziele hierbei sind eine simple Bedienung und eine übersichtliche Weboberfläche. Maßnahmen zur Erreichung dieser Ziele umfassen End-to-End-Tests mit Playwright und UI-Tests, um eine optimale Benutzererfahrung zu gewährleisten.

## Qualitätsbaum

![Qualitätsbaum](https://github.com/Okanx68/sqs-project/blob/main/doc/images/Quality_Tree_modified.png)

## Qualitätsszenarien

| Attribut                           | Szenario                                                                                   | Maßnahme                                                                                             |
|------------------------------------|--------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------|
| Reliability - Zuverlässigkeit      | Nutzungsszenario: Um die Zuverlässigkeit zu gewährleisten, wird das System umfassend getestet. | Unit-Tests, Integrationstests, End-to-End-Tests, Lasttests                                       |
|                                    | Änderungsszenario: Regelmäßige Aktualisierung des Testskonzepts bei einer Änderung im Code. | Umfassende Testabdeckung                                                    |
|                                    | Nutzungsszenario: Das System bleibt unter hoher Last stabil und reagiert effizient.         | Lasttests mit Artillery                                                      |
|                                    | Änderungsszenario: Neue Anforderungen erfordern Anpassungen und erneute Tests der Systemstabilität. | Anpassung der Tests                                                         |
| Portability - Übertragbarkeit      | Nutzungsszenario: Das System läuft in verschiedenen Docker-Containern auf unterschiedlichen Plattformen. | Containerisierung mit Docker, Nutzung von Docker Compose zur Orchestrierung                           |
|                                    | Änderungsszenario: Neue Container-Umgebungen werden unterstützt und getestet.               | Unabhängige Laufzeitumgebung                                              |
|                                    | Nutzungsszenario: Das System funktioniert in unterschiedlichen Browsern ohne Probleme.      | End-to-End-Tests mit Playwright                                 |
|                                    | Änderungsszenario: Zukünftige Browser-Versionen werden unterstützt und getestet.               | Regelmäßige Updates und Tests mit Playwright                                         |
| Usability - Benutzerfreundlichkeit | Nutzungsszenario: Die Benutzer können intuitiv nach Brauereien suchen und die Ergebnisse anzeigen lassen. | Benutzerfreundliches UI-Design, einfache Navigation. End-to-End-Tests mit Playwright  |
|                                    | Änderungsszenario: Verbesserungen in der Benutzeroberfläche werden kontinuierlich umgesetzt. | Benutzerfeedback einholen und umsetzen, regelmäßige Usability-Tests                                   |
|                                    | Nutzungsszenario: Das System zeigt relevante Informationen zu Brauereien klar und verständlich an. | Übersichtliches Layout, konsistente Darstellung der Daten                                           |
|                                    | Änderungsszenario: Neue Nutzeranforderungen and das Design werden kontinuierlich integriert.  | Regelmäßige Überprüfung und Anpassung an Nutzeranforderungen                     |


# Risiken und technische Schulden

| Risiko/Technische Schuld             | Beschreibung                                                                                               | Maßnahme zur Risikovermeidung/Risikominimierung/Abbau der technischen Schuld                   | Priorität |
|--------------------------------------|-----------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------|-----------|
| Sicherheitslücken                    | Potenzielle Sicherheitslücken in Backend und Frontend können die Anwendung gefährden.                          | Implementierung von Sicherheitsmechanismen wie OAuth2 oder Inputvalidierung und -sanitierung, regelmäßige Sicherheitsüberprüfungen       | Hoch      |
| Fehlende Testabdeckung               | Unzureichende Tests, besonders im Bereich End-to-End-Tests und Lasttests, können zu Fehlern im System führen. | Ausbau einer umfassenden Teststrategie mit Unit-, Integrations-, Last- und End-to-End-Tests              | Hoch      |
| Mangelnde Skalierbarkeit             | Das Quarkus-Backend könnte bei hoher Last an seine Grenzen stoßen.                                         | Skalierungstechniken anwenden, horizontales Scaling unterstützen, Lasttests regelmäßig durchführen | Hoch      |
| Performance-Probleme                 | Performanceprobleme bei hoher Last und großen Datenmengen könnten die Benutzererfahrung beeinträchtigen.   | Optimierung von Caching-Mechanismen und Datenbankabfragen, Einsatz von Load Balancing | Mittel  |
| Docker-Komplexität                   | Der gleichzeitige Betrieb von mehreren Docker-Containern (Backend, Frontend, Datenbank) kann komplex sein.  | Automatisierte Einrichtung und Orchestrierung der Docker-Container, ausführliche Dokumentation     | Mittel    |
| Abhängigkeit von externer API        | Verfügbarkeit und Zuverlässigkeit der Open Brewery DB API könnte die Systemstabilität beeinflussen.         | Implementierung von Fallback-Mechanismen, Caching der API-Daten, regelmäßige Überprüfung der API-Verfügbarkeit | Mittel    |
| Komplexität der API-Kommunikation    | Die Kommunikation zwischen Frontend und Backend via REST kann komplex und fehleranfällig sein.          | Detaillierte API-Dokumentation, Einsatz von API-Gateways, umfassendes Testen der API-Endpunkte    | Mittel    |
| Wartungsaufwand                      | Hoher Wartungsaufwand durch regelmäßige Updates und Anpassungen der Abhängigkeiten und Dockerfiles.        | Automatisierung der Wartungsaufgaben, regelmäßige Überprüfung und Aktualisierung der Abhängigkeiten | Mittel    |
| Dateninkonsistenz                    | Inkonsistenzen bei der Synchronisation der Daten zwischen der PostgreSQL-Datenbank und der externen API.    | Implementierung von Datenintegritätsprüfungen, regelmäßige Datenabgleich- und Synchronisationsmechanismen | Mittel    |


# Glossar

| Begriff        | Definition        |
|----------------|-------------------|
| *\<Begriff-1>* | *\<Definition-1>* |
| *\<Begriff-2*  | *\<Definition-2>* |
