# SmartCar
## Beschreibung
Bei diesem Smart Thing handelt es sich um einen Mindstorm Ev3, welcher mit einem Mindstormmotor und mehreren Sensoren ausgestattet ist. 
Er ist in der Lage, einer Linie zu folgen, sowie auch Gegenständen auszuweichen, die ihm im Weg stehen.
Innerhalb dieses Projekts war es das Ziel, den Mindstorm so autonom wie möglich fahren zu lassen. 
Während das SmartCar in der Lage sein soll, die Farbe einer Linie zu erkennen und dieser mit deren Spezifikationen zu folgen, 
soll es auch über eine App gesteuert werden können.

![Image of the SmartCar](https://gilbert.informatik.uni-stuttgart.de/StuPro2016/SmartHomeMain/raw/2c2d12170347c95bc704cc26440776f55ed2473d/SmartCar/photo_2017-01-11_10-17-18.jpg)


## Funktionen
Das SmartCar besitzt folgende Funktionen:
### Befehle an den Mindstorm
Das Auto wartet fortlaufend auf Befehle, die über die Middleware bei ihm eintreffen und reagiert anschließend auf diese. Folge Befehle können an SmartCar gesendet werden:
-LostWayEvent
-ToogleStandbyEvent
-ParkingPossibleEvent
-AvoidedDeathEvent
-SetSpeedEvent
-StartedDrivingEvent
-StoppedDrivingEvent
-StartedParkingEvent
-DodgeEvent
-StartedParking
-StopDrivingEvent
-StartDrivingEvent
-InfoResponseEvent
-InfoObject
-InfoRequestEvent


### Informationen des Mindstorm
Der Mindstorm hat folgende Informationen :
 -Mindstorm:
 - InfoRequestEvent
 - InfoObject (*Speed,Driving,Color1,Color2,Distance,Touched,Battery*)
 - InfoResponseEvent
(* InfoObjekt*)
 - StartedDrivingEvent
 - StoppedDrivingEvent
 - StartedParkingEvent
 - DodgeEvent
 - StartDivingEvent
 - StopDrivingEvent
 - ParkingPossibleEvent
 - SetSpeedEvent
(*Speed*)
 - AvoidedDeathEvent
 - ToogleStandbyeEvent
 - LostWayEvent


## Installation und Betrieb
Um den Mindstorm in dem Smart-Home-Netzwerk in Betrieb nehmen zu können, ist es notwedig zu überprüfen, ob der Mindstorm aufgeladen ist. Ist dies nicht der Fall, muss er zunächst am Netzteil befestigt und geladen werden.
Auch ist es wichtig vor der Inbetriebnahme zu kontrollieren, ob alle Sensoren und der Motor am für sie vorgesehenen Anschluss befestigt sind. Nach einer kleinen Wartezeit meldet dann der Mindstorm, dass er bereit ist. Nun kann man ihn über die App ansteuern. Im Anschluss daran kann man den Mindstorm über die App starten.



## Aufbau
Das SmartCat besteht aus einem Mindstorm, der mit zwei Motoren betrieben wird. 
Weiterhin besitzt er zwei Farbsensoren sowie einen Ultraschallsensor.



### Hardware
Bei der verwendeten Mindstorm handelt es sich um einen [Mindstorm](https://www.lego.com/de-de/mindstorms/about-ev3 "Website über den EV3 Mindstorm") EV3 des Herstellers [Lego](https://www.lego.com/de-de/ "Webseite von Lego"). 

### Kaa-Schnittstelle


#### Ausgehend

| Event | Syntax | Parameter | Beschreibung |
|-------|--------|-----------|--------------|
| InfoResponseObjekt | InfoResponseObjekt(*InfoObjekt*) | InfoObjekt des Autos | Wenn über die Middleware ein InfoRequestEvent gesendet wird sendet das Smart Car ein InfoResponseObjekt. |
| StartedDrivingEvent | StartDivingEvent | -- | Event wird gesendet wenn die Middleware Kaa das StartDrivingEvent ausführt. |
| StoppedDrivingEvent | StoppedDrivingEvent | -- |Event wird gesendet wenn das Smart Car fertig mit Parken ist. |
| StartedParkingEvent | StartedParkingEvent | -- | Event wird gesendet wenn die Farbsensoren die blaue Linie erkennen. |
| ParkingPossibleEvent | ParkingPossibleEvent | -- | Dieses Event wird gesendet wenn der Mindstorm es für möglich erachtet zu parken.|
| LostWayEvent | LostWayEvent | -- | Wenn die Farbsensoren keine Farbe mehr erkennen wird dieses Event getriggert. |
| DodgeEvent | DodgeEvent | -- | Dieses Event wird gesendet sobald der Mindstorm einem Objekt auf dem Demoboard ausweicht.|
| AvoidedDeathEvent | AvoidedDeathEvent | -- | Dieses Event wird gesendet wenn der Mindstorm gerade so dem Tod entkommen ist.|
| ToogleStandbyEvent | ToogleStandbyEvent | -- | Dieses Event wird gesendet sobald er Mindstorm in den Standbye Modus geht.|
#### Eingehend

| Event | Syntax | Parameter | Beschreibung |
|-------|--------|-----------|--------------|
| InfoObject | InfoObject(*Speed,Driving,Color1,Color2,Distance,Touched,Battery*)|Speed(int),Driving(boolean),Color1(string),Color2(string),Distance(double),Touched(boolean),Battery(int) | Dieses Event wird empfangen sobald der Mindstorm gestartet wird. |
| InfoRequestEvent | InfoRequestEvent | -- | Dieses Event wird empfangen wenn der Benutzer die App startet und in den Auto Tab wechselt. |
| StartDivingEvent| StartDivingEvent|--|Dieses Event wird empfangen sobald der Benutzer in der App das Auto startet.|
| StopDrivingEvent | StopDrivingEvent | -- | Dieses Event wird empfangen wenn der Benutzer das Auto über die App parken lässt.|
| SetSpeedEvent | SetSpeedEvent(*Speed*)|Speed(*int*)|Dieses Event wird empfangen sobald der Nutzer in der App die Geschwindigkeit des Autos ändert oder einstellt.|

## Anmerkungen
Es ist wichtig den Mindstorm regelmäßig aufzuladen da der Akku gerne leer geht.