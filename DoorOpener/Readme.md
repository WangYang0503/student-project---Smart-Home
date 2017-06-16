# DoorControll
## Beschreibung
Bei diesem Smart Thing handelt es sich um eine von uns in verminderter Größe hergestellte Garage, die wir mit einem Mindstormmotor und mehreren Sensoren ausgestattet haben. Sie ist in der Lage, sowohl das Garagentor über die App zu steuern, als auch das Garagentor über die Kommunikation mit dem Mindstormauto zu öffnen. Innerhalb dieses Projekts war es das Ziel, die Garage zu planen, zu erstellen und dann die Steuerung zu programmieren. Auch war es wichtig, dass die Garage mit dem Mindstormauto kommunizieren kann. 
Auch die manuelle Öffnung durch einen Tastsensor wurde ermöglicht, da die Garage in einem Brandfall auch von innen geöffnet werden muss.

![Image of garage](https://gilbert.informatik.uni-stuttgart.de/StuPro2016/SmartHomeMain/raw/40980a81551f0df67249766e85ab4cc42f616bd9/DoorOpener/photo_2017-01-11_10-13-30.jpg)
![Image of sensor](https://gilbert.informatik.uni-stuttgart.de/StuPro2016/SmartHomeMain/raw/40980a81551f0df67249766e85ab4cc42f616bd9/DoorOpener/photo_2017-01-11_10-17-23.jpg)

## Funktionen
Die Garage besitzt folgende Funktionen, welche über die App, manuell und durch das Auto abgerufen werden können.
### Befehle an die Garage
Die Garage wartet fortlaufend auf Befehle, die über die Middleware bei ihr eintreffen und reagiert anschließend auf diese. Folgende Befehle können an die Garage gesendet werden:
- Door close: Das Schließen der Garage kann über das Netzwerk eingeleitet werden.
- Door open: Das Öffnen der Garage kann durch das Netzwerk eingeleitet werden.


### Informationen von der Garage
Die Garage besitzt verschiedene Zustände die abgefragt werden können:
- InfoObject(DoorOpened,DoorMoveState,GarageOpened,GarageMoveState)
- InfoRequestEvent
- InfoResponseEvent(InfoObject)
- DoorClosedEvent
- DoorOpenedEvent
- GarageClosedEvent
- GarageOpenedEvent
- OpenDoorEent
- CloseDoorEvent
- OpenGarageEvent
- CloseGarageEvent

## Installation und Betrieb
Um die Garage in dem Smart-Home-Netzwerk in Betrieb nehmen zu können, ist es notwendig zu überprüfen, ob der Mindstorm aufgeladen ist. Ist dies nicht der Fall, muss er zunächst am Netzteil befestigt und geladen werden. 
Auch ist es wichtig vor der Inbetriebnahme zu kontrollieren, ob alle Sensoren und der Motor am dafür vorgesehenen Anschluss befestigt sind. Nach einer kleinen Wartezeit meldet dann der Mindstorm, dass er bereit ist. Dann kann man ihn über die App ansteuern. Nun kann man die Garage öffnen und schließen.

Neben der Steuerung über das Netzwerk kann die Garage auch manuell mithilfe des Touchsensors im Inneren der Garage zu jeder Zeit geöffnet oder geschlossen werden.

## Aufbau
Die Garage besteht aus Holz, einem Mindstorm, einem Mindstormmotor und vier Sensoren.
Die Haustüre besteht aus dem Mindstorm der Garage und einem Motor zum Öffnen der Türe.


### Hardware
Bei der in diesem Projekt verwendeten Mindstorm handelt es sich um einen [Mindstorm](https://www.lego.com/de-de/mindstorms/about-ev3 "Website über den EV3 Mindstorm") EV3 des Herstellers [Lego](https://www.lego.com/de-de/ "Webseite von Lego"). 

### Kaa-Schnittstelle

#### Ausgehend

| Event | Parameter | Beschreibung |
|-------|--------|-----------|--------------|
| InfoRequestEvent |InfoObjekt mit den aktuellen Werten der Garage. | Dieses Event sendet an die Middleware Kaa die aktuellen Werte und Zustände der Garage. |
| InfoResponseEvent |InfoObjekt mit den aktuellen Werten der Garage | Dieses Event reagiert auf eine Abfrage der Middleware Kaa und sendet dann als Antwort das InfoObjekt mit allen aktuellen Werten und Zuständen. |
| DoorClosed | -- | -- | Dieses Event wird ausgelöst sobald die Türe geschlossen ist. |
| DoorOpenedEvent | -- | -- |Dieses Event wird ausgelöst sobald die Türe geöffnet wurde. |

#### Eingehend

| Event | Syntax | Parameter | Beschreibung |
|-------|--------|-----------|--------------|
| InfoObjekt | InfoObjekt(DoorOpened,DoorMoveState,GarageOpened,GarageMoveState) | DoorOpened(boolean),DoorMoveState(smart_thing.door.schema.MoveStates),GarageOpened(boolean),GarageMoveState(smart_thing.door.schema.MoveStates) | Diess Event bekommt der Mindstorm von der Middleware Kaa beim Start |
| OpenDoorEvent | -- | -- |Dieses Event wird gesendet sobald die Türe geöffnet werden soll. |
| CloseDoorEvent | -- | -- |Dieses Event wird gesendet sobald die Türe geschlossen werden soll. |
| OpenGarageEvent | -- | -- |Dieses Event wird gesendet sobald die Garage geöffnet werden soll. |
| CloseGarageEvent | -- | -- |Dieses Event wird gesendet sobald die Garage geschlossen werden soll. |

## Anmerkungen
Regeln lassen sich dynamisch im Backend einstellen.