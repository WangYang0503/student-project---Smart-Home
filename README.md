# student-project---Smart-Home
Studienprojekt 16/17: Smart Things

Studienprojekt vom Sommersemester 2016 bis zum Wintersemester 2016/2017 an der Abteilung für Software Engineering des Instituts für Softwaretechnologie an der Universität Stuttgart.


Einführung

Dieses Studienprojekt beschäftigt sich mit der Erstellung eines sogenannten Smart Homes, in dem verschiedene "smarte" Alltagsgegenstände miteinander vernetzt sind und über eine gemeinsame Middleware miteinander kommunizieren. Der Fokus des Projekts liegt dabei auf der Anbindung bestehender Geräte an ein Netzwerk, der Entwicklung weiterer solcher Komponenten und der Implementierung diverser "Wenn-Dann"-Regeln, die das Smart Homes auf seine Umgebung reagieren lassen und es damit überhaupt erst lebendig werden lassen. Viele Bestandteile eines "echten" intelligenten Hauses überschreiten jedoch den Umfang eines Studienprojekts in erheblichem Maße, weshalb an dieser Stelle beispielsweise weder eine Lichtsteuerung am 230V-Stromnetz, noch eine realitätsnahe Tür- oder Garagensteuerung realisiert werden kann. Auch die Aufrüstung eines echten Autos zu einem sogenannten "Smart Car", das den Ausgangspunkt vieler Ideen für ein intelligentes Gerätenetzwerk darstellt, sprengt selbstverständlich den Rahmen. Aus diesem Grund beschränkt sich dieses Studienprojekt bei der Entwicklung der verschiedenen Komponente im Allgemeinen auf Miniatur-Modelle und zweckmäßige Nachbauten. Diese genügen trotzdem, um die notwendigen Schnittstellen für die Geräte zu schaffen, eine grundlegende Implementierung zur Umsetzung der "Wenn-Dann"-Regeln vorzunehmen und dem äußeren Betrachter einen Eindruck über die bestehenden Möglichkeiten in dem Smart Home zu verschaffen.

Das Studienprojekt umfasst die folgenden "Smart Things":


Eine Kaffeemaschine
Ein Smart Car
Eine Haus- und eine Garagentür
Die Innen- und Außenbeleuchtung eines Hauses
Einen Rauchmelder


All diese Gegenstände lassen sich zusätzlich über eine Android-App steuern und kontrollieren. Um diese Geräte in Form von "Wenn-Dann"-Regeln auch tatsächlich auch verknüpfen zu können, kommt ein dafür entwickeltes Backend mit Datenbank und leicht zu bedienender Weboberfläche zum Einsatz.

Bei der Kaffeemaschine handelt es sich im Gegensatz zu den anderen Smart Things nicht um ein Modell, sondern um eine richtige Kaffeemaschine in Lebensgröße, die über eine WLAN-Schnittstelle verfügt und somit auch in das Netzwerk eingebunden werden kann. Um die Interaktion der beiden elektronischen Komponenten im Smart Home besser demonstrieren zu können, wurden die Steuerung der Innen- und Außenbeleuchtung und der Haus- und Garagentür  in ein kleines Holzmodell eines Wohnhauses integriert. Bei dem Smart Car handelt es sich um einen Lego Mindstorms-Roboter, der - als Modell eines autonomen, oder zumindest intelligenten Autos - selbstständig fahren und mit anderen Komponenten des Smart Homes kommunizieren kann. Aufgrund der nur aufwändig herzustellenden realen Testbedingungen zur Auslösung des Rauchmelders wird dieser vollständig über eine Android-App auf einem Smartphone simuliert.

Ein weiterer Schwerpunkt dieses Studienprojekts liegt auf der Durchführung von  Aktivitäten des Safety Engineerings, um sicherzustellen, dass bei einer realen Umsetzung des Modells in Lebensgröße so wenig Gefährdungen wie möglich für die Umwelt auftreten. So wurden in den Entwicklungsprozess des Entwickler-Teams verschiedene safety-spezifische Aktivitäten integriert, die die fortwährende Analyse des Gesamtsystems hinsichtlich Safety-Risiken und deren Beseitigung sicherstellen.

Im Folgenden wird ein Überblick über das Projekt gegeben und auf die Hauptverzeichnisse der einzelnen Smart Things verwiesen, in denen sich dann nähere Informationen zum Aufbau, der Installation, zum Betrieb und den implementierten Schnittstellen und Features befinden.


Übersicht der Komponenten

In dem folgenden Schaubild ist der grundlegende Aufbau des durch das Studienprojekt realisierten Smart-Home-Netzwerks dargestellt:



Middleware

Im Zentrum des Netzwerks steht die Middleware. Sie ist grundlegend für das Verwalten der einzelnen Endgeräte, den Smart Things, zuständig. Sie besteht aus einem Server, auf dem die Kaa IoT Development Platform installiert ist und ausgeführt wird. Sie ist in der Lage Software Development Kits (SDKs) in verschiedenen Programmiersprachen zu generieren, darunter auch SDKs für Java, nachdem die entsprechenden Smart-Things auf der dazugehörigen Weboberfläche des Kaa-Servers spezifiziert worden sind. Diese SDKs enthalten die notwendigen Schnitstellen in der jeweiligen Sprache, um dann in der Software der Endgeräte mit der Kaa-Middleware kommunizieren zu können. Die Endgeräte verbinden sich nach dem Start ihrer Software unter Verwendung der zuvor generierten SKDs automatisch mit der Middleware und können bei Bedarf Nachrichten in Form von Events an diese übermitteln. Der Kaa-Server nimmt Nachrichten von den Endgeräten entgegen und verteilt sie an die momentan verfügbaren Endgeräte, die auf Events dieser Art warten (Listener-Prinzip). Diese Art der Kommunikation greift das Backend auf, um an dieser Stelle die Umsetzung der zuvor durch den Anwender festgelegten "Wenn-Dann-Regeln" sicherzustellen.

--> Verzeichnis im Repository


Backend

Um das Smart-Home nun tatsächlich auch “smart” machen zu können, sodass es in bestimmten Situationen von alleine vordefinierte Aktionen auslöst, genügt die direkte Kommunikation der Smart-Things über die Middleware nicht. In diesem Fall müssten Verknüpfungen zur Kommunikation der Smart-Things untereinander umständlich direkt in dem Quellcode der einzelnen Komponenten implementiert werden und wären folglich ohne Neukompilierung des jeweiligen Projekts nicht durch den Endanwender anpassbar. Da dies natürlich eine sehr unkomfortable Art der Konfiguration des Smart Homes darstellt, übernimmt das Backend diese Aufgabe, in welchem der Benutzer über eine Weboberfläche die Regeln, die für sein Smart Home greifen sollen, einstellen und verwalten kann. Bei diesen Regeln handelt es sich dabei um die bereits angesprochenen “Wenn-Dann-Regeln”: Eine Aktion wird vom Smart-Home dann ausgelöst oder ausgeführt, wenn zuvor ein bestimmtes Ereignis oder eine bestimmte Situation aufgetreten ist.

Das Grundgerüst des Backends besteht aus einem Java-Programm, welches selbst auch ein KAA-SDK besitzt und darüber Zugriff auf die Schnittstellen aller Smart-Things erhält. Es ist somit über entsprechende Listener in der Lage, auf die ausgesendeten Events aller Smart-Things zu reagieren und selbst Events an diese Smart-Things auszusenden, die dann dementsprechend Aktionen auslösen können. Ausgehend von dieser Basis besitzt das Backend eine SQLite-Datenbank, in der die Regeln gespeichert werden können. Eine sogenannte “RuleWorker”-Klasse übernimmt auf dem Server die Aufgabe, auf Eingabe eines Eventnamens aus dieser Datenbank alle passenden Regeln herauszusuchen, die diesen Eventnamen als Trigger-Event gespeichert haben, die dazugehörige Aktionsmethode abzufragen auszulösen.

Das Backend besteht weiter aus einer Webserver-Komponente, die sowohl statische Webseiten ausgeben, als auch Ajax-Requests entgegennehmen, verarbeiten und beantworten kann. Für diese Webserver-Komponente gibt es eine Weboberfläche, die die Möglichkeit bietet, über ein Formular neue Regeln anzulegen, diese in einer Tabelle anzuzeigen, zu editieren, zu löschen, zu aktivieren, zu deaktivieren und sie auch direkt über die Weboberfläche auszuführen, ohne dass das zugehörige Triggerevent dafür ausgelöst werden muss. Ferner steht neben einem persistenten Log, das alle Aktionen des Smart-Homes, sowie alle eingehenden und ausgehenden Events und die jeweils ausgelösten Regeln aufzeichnet, auch ein sogenanntes “Live Log” zu Verfügung, das dem Benutzer in Echtzeit auf der Weboberfläche Informationen über die ausgeführten Regeln bietet.

Kurzum bietet das Backend dem Benutzer des Smart Homes über eine Weboberfläche eine sehr komfortable Möglichkeit, “Wenn-Dann-Regeln” für das Smart-Home nach eigenen Wünschen individuell zu verwalten, die die Art und Weise festlegen, auf der die einzelnen innerhalb dieses Projekts entwickelten Smart-Things miteinander interagieren können.

--> Verzeichnis im Repository


Kaffeemaschine

Die Kaffeemaschine, die in diesem Projekt zum Einsatz kommt, ist bereits werksseitig mit einem WLAN-Modul ausgestattet. Damit dieses genutzt werden kann, ist zunächst die Konfiguration der Verbindung mit dem lokalen WiFi-Netzwerk notwendig, welche über die bereits existierende Android-App des Kaffeemaschinenherstellers erfolgen muss. Im Rahmen dieses Projekts wurde die Kommunikation dieser bereits vorhandenen App mit der Kaffeemaschine über das Netzwerk beobachtet und analysiert, sodass es gelungen ist, die Kommunikation zu großen Teilen zu entschlüsseln und die Hersteller-App damit durch eine eigens entwickelte Software zu ersetzen. Da die Kaffeemaschine aufgrund fehlender Möglichkeiten, diese zu programmieren oder weiter zu konfigureren nicht in der Lage ist, selbstständig mit der Middleware zu kommunizieren, wurde als Teil des Studienprojekts ein Java-Programm, der sogenannte "CoffeeServer", entwickelt, das zum einen die Kommunikation mit der Middleware übernimmt und zum anderen Befehle an die Kaffeemaschine schickt und Statusmeldungen von ihr empfängt, um sie mit dem Smart Home verknüpfen zu können. Dabei wurden sämtliche zuvor entschlüsselte Kommunikationsvorschriften für die Kaffeemaschine in eine Java-Bibliothek ausgelagert ("SmartCoffeeAPI"), die vom CoffeeServer verwendet wird und es erlaubt, über WiFi die Verbindung mit dem WLAN-Modul der Kaffeemaschine aufzubauen, die Maschine anzusteuern und Statusmeldungen von ihr zu empfangen.

Die Kaffeemaschine ist so in der Lage, Statusmeldungen über den aktuellen Zustand (beispielsweise Bereit, Kochend, Mahlend) an die Middleware und damit an andere Smart Things zu senden und gleichtig von diesen Befehle zu empfangen, beispielsweise Kaffee kochen, Brühstärke ändern und Tassenzahl ändern.

--> Verzeichnis im Repository


Smart Car

Das sogenannte "Smart Car" wird in diesem Studienprojekt modellhaft über einen Lego-Mindstorm (EV3) realisiert, der ganz grob die ungefähre Bauform eines Fahrzeugs aufweist. Auf dem "programmieren intelligenten Stein" (Mindstorm-Brick) des Mindstorms, der das logische Herz des Lego-Roboters darstellt, ist das javafähige Betriebssystem Lejos installiert. Damit ist es möglich, Java-Programme auf dem Mindstorm auszuführen. Die EV3-Serie erlaubt zudem den Anschluss eines wifi-fähigen USB-Sticks, über den zusammen mit der entsprechenden Implementierung dann die Kommunikation mit dem Kaa-Server erfolgen kann. Wie bei den meisten anderen der zu dem Studienprojekt gehörenden Smart-Things kann somit das vom Kaa-Server für den Mindstorm generierte SDK in ein Java-Programm eingebettet werden und über das Netzwerk mit der Middleware kommunizieren.

Das Smart Car ist beispielsweise in der Lage selbstständig über Lichtsensorik einer farbigen Linie zu folgen, wenn dies beispielsweise von der App veranlasst wird, auf Befehl anzuhalten, anhand der Linienfarbe zu erkennen, wo sich das Auto gerade aufhält (zum Beispiel, dass es sich in der Nähe der Garage befindet), unter Verwendung  eines drehbaren Ultraschallsensors Hindernissen auszuweichen, aktuelle Statussignale an die Middleware zurückzumelden und unter Verwendung von Bodenmarkierungen in die Garage einzuparken.

--> Verzeichnis im Repository


Haus- und Garagentüren

Die hier zum Einsatz kommenden Türen - jeweils eine Haustüre und eine Garagentüre - sind Teil des Holzmodells eines Hauses, das zur Veranschaulichung dieser Komponente und der Lichtsteuerung angefertigt wurde. Ähnlich wie bei dem Smart Car wird auch hier die Steuerung der beiden Türen über einen Lego-Roboter (EV3) realisiert, dessen Mindstorm-Brick mit einem WiFi-USB-Stick ausgestattet ist und das javafähige Betriebssystem Lejos installiert hat. Die Türen werden über Lego-Motren, die zu diesem Roboter gehören, angesteuert: Eine Konstruktion aus einem Getriebe mit mehreren Zahnrädern erlaubt es dem Motor, die Haustür vorsichtig zu öffnen und wieder zu schließen. Eine Art Flaschenzug mit einer Seilwinde, die an einem Motor auf dem Dach der Garage befestigt ist, erlaubt es einem anderen Motor, der mit demselben Mindstorm verknüpft ist, das Garagentor zu öffnen und zu schließen. Zusätzlich wird der Status des Garagentors (Offen oder Geschlossen) durch einen Ultraschallsensor erfasst.

Die Haus- und Türsteuerung kann so den aktuellen Status der Türen an die Middleware melden und gleichzeitig von anderen Smart Things Befehle empfangen und ausführen, wie das Öffnen und Schließen der Türen.

--> Verzeichnis im Repository


Innen- und Außenbeleuchtung

Neben der Türsteuerung wird das Hausmodell auch mit verschiedenen Lichtern versorgt, die sowohl außen vom Haus, als auch innen in Form von RGB-LEDs angebracht sind. Diese werden über eine Verstärkerplatine, auf der Transistorschaltungen realisiert sind, um die LEDs mit ausreichend Strom zu versorgen, geschaltet. Diese Platine ist wiederum mit einem Raspberry Pi 3 verknüpft, das Raspbian als Betriebssystem verwendet und somit in der Lage ist, über ein darauf ausgeführtes Java-Programm die LEDs über die Verstärkerplatine durch das Ansteuerun der GPIO-Ports an- und auszuschalten. Das Raspberry Pi besitzt bereits werkseitig ein integriertes WLAN-Modul und kann somit direkt mit dem Smart-Home-Netzwerk verknüpft werden und ebenso ein Kaa-SDK verwenden. Die im Rahmen des Studienprojekts erstellte Software auf dem Raspberry Pi unterstützt die Zuordnung der LEDs zu Räumen, sodass verschiedene LED-Gruppen in einem Raum zusammengefasst und dann von anderen Smart Things gemeinsam als Gruppe angesteuert werden können. Die Konfiguration und Aufteilung der LEDs an den verschiedenen Pins in Räume erfolgt dabei über eine XML-Datei, die von der Software auf dem Raspberry zu Beginn eingelesen wird. Da es sich bei den LEDs um RGB-Leds handelt, kann auch die Farbe der Beleuchtung durch die softwareseitige Ansteuerung verändert werden.

Die Lichtsteuerung auf dem Raspberry kann so den aktuellen Status der Beleuchtung (zum Beispiel An, Aus, Farbe, Helligkeit) an die Middleware senden und im Gegenzug Befehle anderer Smart Things entgegennehmen, wie das Dimmen der LED-Helligkeit, das Verändern der Farben und das Ein- und Ausschalten einzelner LEDs oder ganzer zu Räumen zusammengefasster LED-Gruppen.

--> Verzeichnis im Repository


Rauchmelder

Auch ein Rauchmelder soll als Smart Thing in das Smart-Home-Netzwerk integriert werden. Allerdings wurde aus Gründen des Aufwands und der nur schwer herzustellenden notwendigen Testbedingungen darauf verzichtet, einen echten Rauchmelder, wie man ihn aus den Baumärkten kennt, umzurüsten. Stattdessen wird der Rauchmelder über eine Android-App simuliert, die auf einem Smartphone oder Tablet ausgeführt wird. Innerhalb dieser App kann so mit einem Knopfdruck ein Feueralarm ausgelöst werden, dere von einem schrillen Ton begeleitet wird. Der Alarm hält so lange an, bis er durch einen erneuten Knopfdruck in der App deaktiviert wird. Diese App integriert ebenfalls ein Kaa-SDK, das jedoch im Gegensatz zu den bisher verwendeten Java-SDKs speziell auf Android-Geräte ausgerichtet ist. Damit ist auch hier eine direkte Kommunikation über die Middleware zu anderen Smart Things möglich.

Wird bei der Rauchmeldersimulation der Feueralarm ausgelöst, sendet die App ein entsprechendes Event an die Middleware, welches diese wiederum an andere Geräte verteilt, die dann entsprechend auf den Alarm reagieren können, beispielsweise durch Aktivierung einer Notbeleuchtung. Endet der Alarm, wird erneut die Middleware verständigt, um etwaige ergriffene Maßnahmen anderer Smart Things wieder abzubrechen, sofern nötig.

--> Verzeichnis im Repository


Android-App

Als zentrale Benutzerschnittstelle des Smart-Home-Netzwerks wird im Rahmen des Studienprojekts eine Android-App entwickelt, über die alle Smart-Things eingesehen, abgefragt und kontrolliert werden können sollen. Ähnlich wie der Rauchmelder implementiert auch die App ein speziell auf Android ausgelegtes Kaa-SDK, dass es dem Nutzer ermöglicht, über das Netzwerk mit der Middleware zu kommunizieren.

Die App empfängt nahezu alle Kaa-Events der mit dem Netzwerk verknüpften Smart-Things und kann so zu jeder Komponente Statusmeldungen einsehen und Benachrichtigungen dieser reagieren, beispielsweise wenn das Kochen des Kaffees abgeschlossen ist. Darüberhinaus kann über die App auch manuell auf die Smart Things Einfuss genommen werden, um beispielsweise des Starten oder Stoppen des Smart Cars zu veranlassen, Kaffeeeinstellungen zu konfigurieren, Lichter ein-, aus- und umzuschalten oder die Türen zu öffnen oder zu schließen. Darüberhinaus erlaubt es die App, auch Timer für das Kaffeekochen festzulegen, sodass der Benutzer bereits im Voraus konfigurieren kann, zu welcher Uhr- oder Tageszeit automatisch Kaffee gekocht werden soll, sofern natürlich ausreichend Ressourcen wie Kaffee und Wasser vorhanden sind.

--> Verzeichnis im Repository


Integrierte Regeln

Neben der Steuerung der Smart Things über die Android-App ist es eines der Hauptziele dieses Studienprojekts, die Interaktion der intelligenten Geräte untereinander über die Middleware zu realisieren. So sollen nach dem Prinzip der "Wenn-Dann-Regeln" zuvor definierte Aktionen ausgelöst werden, wenn das Smart Home eine bestimmte Situation registriert. Diese Art der Regeldefinition erfolgt von Seiten des Benutzers auf der Weboberfläche des Backends, welches im Anschluss dann in der Lage ist, auch diese Regeln anzuwenden. "Wenn das Smart Car am Smart Home ankommt, soll die Kaffeemaschine Kaffee kochen, damit der Besitzer diesen trinken kann, wenn er das Haus betritt." ist ein typisches Beispiel für eine solche Regel.

Unabhängig von der Android-App wurden die folgenden Interaktionen ihm Rahmen dieses Projekts implementiert:




Wenn-Dann-Regel
Auslösende Komponente
Reagierende Komponente
Anmerkungen




Wenn das Auto am Smart Home ankommt, soll Kaffee gekocht werden.
Smart Car
Kaffeemaschine
Der Grundgedanke bei dieser Regel ist, dass sich der Besitzer des Smart Homes nach einem anstrengenden Arbeitstag bei einer Tasse frischem Kaffee entspannen möchte, sobald er zu Hause angekommen ist.


Wenn das Auto an der Garage ankommt, soll sich das Garagentor öffnen.
Smart Car
Türsteuerung
Wenn das Auto die Garage erreicht, ist davon auszugehen, dass es auch in die Garage eingeparkt werden soll. Deshalb öffnet sich das Tor automatisch, um dem Besitzer lästiges manuelles Öffnen zu ersparen.


Wenn ein Feueralarm ausgelöst wird, soll die Hausbeleuchtung eingeschaltet werden und rot leuchten.
Rauchmelder
Lichtsteuerung
Durch das Einschalten der Beleuchtung soll dem Besitzer des Smart Homes zum einen das Haus ausgeleuchtet werden, damit er sich auch nachts sofort in der Notsituation zurecht finden kann und nicht erst den Lichtschalter suchen muss; zum anderen soll ihm mit dem roten Leuchten, wie man es auch aus Science-Fiction-Filmen kennt, zusätzlich der Ernst der Lage deutlichgemacht werden.


Wenn ein Feueralarm ausgelöst wird, sollen die Haus- und Garagentüre geöffnet werden.
Rauchmelder
Türsteuerung
Um dem Besitzer des Smart Homes im Falle eines Feueralarms frühzeitig mögliche Fluchthindernisse aus dem Weg zu räumen, sollen die Haus- und die Garagentüre automatisch geöffnet werden.




Die "Auslösende Komponente" bezeichnet das Smart Thing, das die "Wenn"-Aufgabe der Regel übernimmt und die Situation erkennt, auf die reagiert werden soll. Die "Reagierende Komponente" bezeichnet dagegen das Smart Thing, das den "Dann"-Teil der Regel übernimmt und auf die eingetretene Situation mit einer zuvor definierten Aktion reagiert.


Technische Übersicht

In Laufe des Projekts wurden einige verschiedene Techniken und Entwicklungswerkzeuge eingesetzt. An dieser Stelle soll darüber eine Übersicht gewährt werden.


Java

Der allergrößte Teil der Software dieses Projekts ist in Java 8 geschrieben. Dies betrifft vor allem die Software der Kaffeemaschine, bestehend aus dem CoffeeServer und der SmartCoffeeAPI, und die Lichtsteuerung, welche in "reinem" Java, also ohne weitere plattformabhängige Bibliotheken und Erweiterungen, verfasst sind. Die Software der Kaffeemaschine kann damit als ausführbare JAR-Datei auf einem nahezu beliebigen Rechner ausgeführt werden. Im Rahmen des Studienprojekts wurde speziell ein Raspberry PI 3 (Model B) mit installierter JRE als Zielplattform für den CoffeeServer vorgesehen, auf dem dann auch die Lichtsteuerung ausgeführt werden soll, die die GPIO-Pins des Raspberrys verwendet, um die LEDs, die die Beleuchtung des Smart Homes repräsentieren, über eine Verstärkerplatine anzusteuern. Als Entwicklungsumgebungen wurden hierfür IntelliJ IDEA von JetBrains und Eclipse eingesetzt.


Android

Die App zur Gesamtsteuerung und der Rauchmelder-Simulator sind Android-Apps, die in Java 8 geschrieben worden sind. Zur Ausführung auf Android-Geräten setzen diese ein Minimum-Android-SDK-Level von 16 ("Jelly Bean") voraus, Ziel-SDK-Level ist 24 ("Nougat"). Zur Entwicklung wurde ausschließlich Android Studio von JetBrains eingesetzt, die Apps werden in diesem mit Gradle gebuildet.


LeJOS

Das Smart Car und die Türsteuerung für die Haus- und Garagentüre sind jeweils über einen Lego-Mindstorm EV3 realisiert, über deren "intelligenten Stein" die Kommunikation mit der Middleware umgesetzt wird und die die dazugehörenden Lego-Sensoren und Lego-Motoren einsetzen, um die Umgebung zu erfassen und zu beeinflussen. Da der Kaa-Server auf der Middleware nur für bestimmte Programmiersprachen SDKs generieren kann, die für die Software der Smart Things zwingend notwendig sind, um mit der Middleware interagieren zu können, war es erforderlich, das Standardbetriebssystem der Mindstorms, das nur C-ähnliche Programme ausführen kann, zu entfernen und stattdessen das Betriebssystem LeJOS (LeJOS EV3 0.9.1-beta) über eine SD-Karte aufzuspielen, das die Ausführung von Java-Programmen als ausführbare JAR-Dateien ermöglicht, die mit einer entsprechenden API für EV3-Mindstorms ausgestattet worden sind. Als Entwicklungsumgebungen wurden hierfür IntelliJ IDEA von JetBrains und Eclipse eingesetzt.


Maven

Als Build-Management-Tool wird in diesem Studienprojekt Apache Maven eingesetzt, das zahlreiche zentrale Aufgaben übernimmt und dessen Zyklus jeweils für jedes Smart Thing in ein eigenes Submodul aufgeteilt ist. Sämtliche Kaa-SDKs werden bei einem Aufruf von Mavens Goal "clean install" zunächst aus dem Sammelordner für die SKDs in das lokale Maven-Repository installiert und stehen dann den anderen Komponenten, die die entsprechenden SDKs als Abhängigkeit eingetragen haben, zum Kompilieren zur Verfügung. Nach der Installation dieser SDKs buildet der Maven-Prozess die Software und erstellt aus dem CoffeeServer, welcher abhängig von der SmartCoffeeAPI ist, der Lichtsteuerung, der Software für das SmartCar und der Software für die Türsteuerung jeweils ausführbare JAR-Dateien, welche in einen build-Ordner im Hauptverzeichnis kopiert werden. Dabei werden bestehende softwaretechnische Abhängigkeiten und zusätzlich benötigte APIs, wie beispielsweise LeJOS "ev3classes" für die Programmierung der EV3-Mindstorms in Java und die zahlreichen Kaa-SDKs, berücksichtigt und mit in die JAR-Dateien gepackt ("jar with dependencies"). Auch die beiden Android-Apps (App zur Hauptsteuerung und Rauchmelder) werden zu APK-Dateien gebuildet; das geschieht allerdings über das in Android-Studio eingebettete Gradle, dessen Build-Prozess jeweils von Maven mitgestartet wird. Abhängigkeiten der Android-Apps sind daher nicht in Maven direkt integriert, sondern in den dazugehörigen Gradle-Build-Dateien in Android Studio. Die generierten APK-Dateien werden nach Abschluss der Kompilierung ebenfalls in den zentralen Build-Ordner im Hauptverzeichnis kopiert.

Bevor die einzelnen Software-Komponenten gebuildet werden, führt Maven jeweils etwaige angelegte Unit-Tests und statische Codeanalysen mittels Checkstyle und FindBugs durch und reklamiert etwaige Testfehlverschläge, Verstöße gegen den als Checkstyle-XML-Datei definierten Styleguide oder gefundene Bug-Muster.

Bei den Smart Things, die auf einem Lego Mindstorms ausgeführt werden - also dem Smart Car und der Türsteuerung - ist es notwendig, die generierten ausführbaren JAR-Dateien auf den im selben Netzwerk befindlichen Lego-Mindstorm mittels SCP zu kopieren. Auch dies kann Maven auf Wunsch übernehmen: Durch einen Aufruf des Goals "clean intall antrun:run" im Unterverzeichnis des jeweiligen SmartThings wird die JAR-Datei generiert und automatisch auf den "intelligenten Stein" kopiert, wo sie dann manuell über den LeJOS-Explorer auf dem Mindstorm ausgeführt werden kann.

Weitere Infos zu Maven und dem Build-Management befinden sich im Dokumentationsordner über das Build-Management im Repository in Form einer Präsentation.


Git

Git ist die Software zu Versionsverwaltung, die in dem Studienprojekt zum Einsatz kommt. Als Webanwendung für das zentrale Git-Repository auf dem Uniserver kommt GitLab zum Einsatz, welches mit JIRA von Atlassian, der von den Projektteilnehmern verwendeten Webanwendung zum Bugtracking und Projektmanagement, direkt verschaltet ist. Auf diese Weise kann des Bugtracking und Umsetzen von User Stories und Tasks anhand der Commit-Messages analog zu den Commits im zentralen Git-Repository vorgenommen werden.

Die zentrale ".gitignore"-Datei befindet sich im Hauptverzeichnis des Repositories.


Jenkins

Um das gesamte Projekt fortlaufend nach jedem Commit anhand des Maven-Zyklusses automatisiert bauen und so frühzeitig Inkompatiblitäten und Softwarefehler erkennen zu können, kommt bei diesem Studienprojekt Jenkins als Continuous-Integration-Server zum Einsatz. Dieser Uni-Server hat direkten Zugriff auf das zentrale Git-Repository und kann so den Build-Prozess direkt nach jedem Commit anhand der vom Repository bezogenen Dateien ausführen.

Im Falle eines Build-Fehlschlags aufgrund von im Maven-Zyklus aufgetretenen Fehlern werden die Teilnehmer des Studienprojekts umgehend per E-Mail über die bestehenden Proleme unterrichtet, damit diese möglichst zeitnah behoben werden können.
