# Kaffeemaschine
## Beschreibung
Bei diesem Smart Thing handelt es sich um eine reale Kaffeemaschine, die bereits werksseitig mit einem WLAN-Modul und einer vorgefertigten App zur Bedienung über ein WLAN-Netzwerk ausgestattet ist. Sie ist in der Lage, sowohl mit Hilfe von bereits fertig gemahlenem Kaffeepulver Kaffee aufzubrühen, als auch Kaffeebohnen frisch zu mahlen und aus diesen Kaffee zu kochen. Innerhalb dieses Projekts war es das Ziel, die Kommunikation zwischen der Kaffeemaschine und der mitgelieferten App zu entschlüsseln, die daraus resultierenden Erkenntnisse, wie die Kaffeemaschine über WiFi gesteuert und kontrolliert werden kann in einer API zu implementieren und diese zu verwenden, um die Kaffeemaschine in das Smart Home zu integrieren. Dabei sollte es möglich sein, die Kaffeemaschine über das Netzwerk bezüglich der Menge an Kaffee, die gekocht werden soll, der gewünschten Stärke des Kaffees und der Art des Kochens (Pulver verwenden oder Bohnen mahlen) nach eigenen Wünschen zu konfigurieren, das Kaffeekochen über das Netzwerk zu starten und Informationen wie die zuletzt getroffenen Einstellungen und den aktuellen Status der Kaffeemaschine (*Kocht sie gerade Kaffee oder ist sie bereits fertig? Hat sie ausreichend Wasser?*) abzufragen. Dies alles sollte dazu dienen, um die Steuerung der Kaffeemaschine über die anderen Smart Things und die Android-App für den Benutzer so einfach wie möglich zu machen.

## Funktionen
Die Kaffeemaschine innerhalb des Smart-Home-Netzwerkes verfügt über die folgenden Funktionen, aufgeteilt in Befehle, die aus dem Netzwerk über die Middleware an die Kaffeemaschine gesendet werden können und den Statusmeldungen und Informationen, die die Kaffeemaschine über das Netzwerk zurückmelden kann.
### Befehle an die Kaffeemaschine
Die Kaffeemaschine wartet fortlaufend auf Befehle, die über die Middleware bei ihr eintreffen und reagiert anschließend auf diese. Folge Befehle können an die Kaffeemaschine gesendet werden:
- Kaffee kochen: Der Kochvorgang der Kaffeemaschine kann über das Netzwerk gestartet werden.
- Kochvorgang abbrechen: Ein bereits gestarteter Kochvorgang der Kaffeemaschine kann über das Netzwerk abgebrochen werden.
- Brühstärke einstellen: Die gewünschte Brühstärke (*Schwach, Medium oder Stark*), die der Kaffee am Ende des Kochvorgangs haben soll, kann über das Netzwerk in den drei verschiedenen Stufen eingestellt werden.
- Tassenanzahl einstellen: Die gewünschte Anzahl an Tassen (*1 bis 12 Tassen*), für die Kaffee gekocht werden soll, kann über das Netzwerk eingestellt werden.
- Warmhaltezeit der Warmhalteplatte einstellen: Die Kaffeemaschine verfügt über eine Warmhalteplatte, auf der die Kanne steht, die den darin enthaltenen Kaffee für eine benutzerdefinierte Zeit warmhalten kann. Diese Zeit (*von 5 bis zu 40 Minuten*) kann über das Netzwerk eingestellt werden.
- Statusabfrage: Die Kaffeemaschine kann über das Netzwerk dazu veranlasst werden, ihren aktuellen Status und Informationen über vorgenommene Einstellungen an die Middleware zurückzumelden.

### Informationen von der Kaffeemaschine
Die Kaffeemaschine sendet jede Sekunde den aktuellen Status und die vorgenommenen Einstellungen an die in dem Projekt entwickelte Software ("Polling"). Diese ist dann in der Lage, etwaige Statusänderungen der Middleware mitzuteilen. Das umfasst die folgenden Informationen:
- Antwort auf Statusabfrage: Wird die Kaffeemaschine über das Netzwerk durch den Statusabfragen-Befehl dazu aufgefordert, ihren aktuellen Status und weitere Informationen zurückzumelden, sendet sie die folgenden Informationen an die Middleware:
 - Aktueller Status (*Bereit, Kochend, Mahlend*)
 - Das Vorhandensein einer Karaffe im Kaffeeausgabebereich (*Ja, Nein*)
 - Die eingestellte Art der Kaffeezubereitung (*Filter, Mahlwerk*)
 - Den aktuellen Wasserstand (*Leer, Wenig, Halbvoll, Voll*)
 - Die eingestellte Kaffeestärke (*Schwach, Medium, Stark*)
 - Die eingestellte Tassenanzahl für die zu kochende Kaffeemenge (*1 bis 12*)
- Erfolgreicher Verbindungsaufbau: Wenn die Software der Kaffeemaschine selbst erfolgreich eine Verbindung zur Kaffeemaschine aufbauen konnte, wird diese Mitteilung über das Netzwerk an die Middleware gesendet.
- Verbindungsverlust: Wenn die Software der Kaffeemaschine selbst die Verbindung zur Kaffemaschine verloren hat, wird diese Mitteilung über das Netzwerk an die Middleware gesendet.
- Technischer Fehler: Sollte ein weiterer, unbekannter technischer Fehler auftreten, der die Software der Kaffeemaschine oder die Verbindung der Software zur Kaffeemaschine betrifft, wird dies der Middleware über das Netzwerk mitgeteilt. Dabei wird ein Text mitversendet, der den aufgetretenen Fehler beschreibt.
- Wenig Wasser: Wird versucht, mit der Kaffeemaschine Kaffee zu kochen, obwohl zu wenig Wasser für die eingestellte Tassenanzahl vorhanden ist, aber dennoch ausreichend Wasser, um überhaupt etwas Kaffee kochen zu können, so wird der Middleware über das Netzwerk mitgeteilt, das momentan wenig Wasser in dem Wasserbehälter der Kaffeemaschine vorhanden ist. Anschließend kocht die Kaffeemaschine noch so viel Wasser, wie es mit dem aktuellen Wasserfüllstand möglich ist.
- Kein Wasser: Wird versucht, mit der Kaffeemaschine Kaffee zu kochen, obwohl kein Wasser im Wasserbehälter vorhanden ist, so wird dies der Middleware über das Netzwerk mitgeteilt. Es wird im Anschluss kein Kaffee gekocht werden.
- Keine Karaffe: Wird versucht, mit der Kaffeemaschine Kaffee zu kochen, obwohl im dafür vorgesehenen Kaffeeausgabebereich der Maschine keine Karaffe/Kaffeekanne vorhanden ist, so wird dies der Middleware über das Netzwerk mitgeteilt. Es wird im Anschluss kein Kaffee gekocht werden.
- Kaffeekochen bereits gestartet: Wurd versucht, mit der Kaffeemaschine Kaffee zu kochen, obwohl der Kochvorgang bereits gestartet und noch nicht beendet worden ist, so wird dies der Middleware über das Netzwerk mitgeteilt. Es wird im Anschluss an die Beendigung des aktuellen Kochvorgangs kein weiterer Kochvorgang gestartet.
- Kaffeekochen abgeschlossen: Wurde der Kochvorgang der Kaffeemaschine gestartet, so wird an dessen Ende die Middleware über das Netzwerk über die Beendigung des Kochvorgangs informiert.

## Installation und Betrieb
Um die Kaffeeemaschine in dem Smart-Home-Netzwerk in Betrieb nehmen zu können, ist es notwendig, zunächst die Kaffeemaschine (als Hardware) an das Stromnetz anzuschließen und ihr WLAN-Modul nach der kurzen Hochfahrdauer mit dem lokalen WiFi-Netzwerk, das als Netzwerk für das Smart Home eingesetzt wird, unter Verwendung der werksseitig mitgelieferten [Android-App](https://play.google.com/store/apps/details?id=am.smarter.smarterandroid&hl=de "Link zur Android-App von Smarter im Google Play Store") zu verbinden. Anschließend ist es erforderlich, den [CoffeeServer](/CoffeeMachine/CoffeeServer "Verweis auf das CoffeeServer-Verzeichnis") als ausführbare JAR-Datei, wie sie im Verlauf des Maven-Install-Prozesses generiert wird, auf einem Rechner auszuführen. Nach einer kurzen Startzeit baut dieser dann automatisch Verbindungen mit der im selben Netzwerk befindlichen Kaffeemaschine und der Middleware auf. Daraufhin ist die Kaffeemaschine einsatzbereit und sendet ihren aktuellen Status sowie weitere Informationen bei Anforderung und nach etwaigen Zustandsänderungen automatisch an die Middleware, während sie gleichzeitig Befehle entgegennimmt und ausführt.

Neben der Steuerung über das Netzwerk kann sie auch weiterhin manuell aus der Nähe er Knopfdruck bedient werden. Etwaige dadurch verursache Zustands- oder Statusänderungen werden ebenfalls an die Middleware übermittelt.

## Aufbau
Neben der Kaffeemaschine und der mitgelieferten [Android-App](https://play.google.com/store/apps/details?id=am.smarter.smarterandroid&hl=de "Link zur Android-App von Smarter im Google Play Store") besteht die Kaffeemaschine zum einen aus der [SmartCoffee-API](/CoffeeMachine/SmartCoffeeAPI "Verweis auf das SmartCoffee-API-Verzeichnis"), die für die Ansteuerung und Abfrage der Kaffeemaschine über das WiFi-Netzwerk zuständig ist und aus dem [CoffeeServer](/CoffeeMachine/CoffeeServer "Verweis auf das CoffeeServer-Verzeichnis"), der diese API implementiert und die Verbindung mit der Middleware aufbaut, um an sie Nachrichten zu senden und Befehle von ihr zu empfangen, die dann wiederum vom CoffeeServer direkt an die Kaffeemaschine zur Ausführung weitergegeben werden können.

### Software
[TODO: Beschreibung der Sofwarekomponenten dieses Smart Things.]

### Hardware
Bei der in diesem Projekt verwendeten Kaffeemaschine mit WLAN-Modul handelt es sich um die [Smarter Coffee](http://store.smarter.am/collections/frontpage/products/smarter-coffee-machine-eu-plug?variant=5737210309 "Webseite von Smarter Coffee (EU-Stecker)")-Kaffeemaschine des Herstellers [Smarter](http://store.smarter.am/ "Webseite von Smarter"). 

### Kaa-Schnittstelle
[TODO: Beschreibung der Schnittstellen der Event Class Family im KAA, detailierte Auflistung der Events in Form einer Tabelle, Verweis auf die zugehörige JSON-Datei]

#### Ausgehend

| Event | Syntax | Parameter | Beschreibung |
|-------|--------|-----------|--------------|
| Name des Events | Name(Para1, Para2, Para3) | Beschreibung der Parameter | Beschreibung des Events: Zweck, Aufgabe, Sinn |

#### Eingehend

| Event | Syntax | Parameter | Beschreibung |
|-------|--------|-----------|--------------|
| Name des Events | Name(Para1, Para2, Para3) | Beschreibung der Parameter | Beschreibung des Events: Zweck, Aufgabe, Sinn |

## Anmerkungen
[TODO: Besondere Anmerkungen zu diesem Smart Thing, sofern vorhanden]