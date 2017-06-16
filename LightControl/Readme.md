# LightControl
## Beschreibung
Bei diesem Smart Thing handelt es sich um eine von uns in verminderter Größe hergestellten Simulation der Beleuchtung in einem SmartHome. Durch diese Simulation können wir Lichter im Haus ansteuern und leuchten lassen. Die Farbe der LEDs lässt sich über die App einstellen. Auch durch den SmokeDeviceAlarm können die LEDs zum Leuchten gebracht werden. Innerhalb dieses Projekts war es das Ziel, die Beleuchtung zu planen, zu erstellen und dann die Steuerung zu programmieren. Auch war die Kommunikation der Steuerungmit der App und dem Netzwerk wichtig. 

![Image of LightControll](https://gilbert.informatik.uni-stuttgart.de/StuPro2016/SmartHomeMain/raw/6d7a7a8138fd6c82a5ef065184e784acefc8966a/LightControl/photo_2017-01-23_14-57-11.jpg)


## Funktionen
Die LightControl besitzt folgende Funktionen:
### Befehle an die LightControll
Die LightControl wartet fortlaufend auf Befehle, die über die Middleware bei ihr eintreffen und reagiert anschließend auf diese. Folge Befehle können an die Garage gesendet werden:
- Lights on: Lichter gehen an.
- Lights off: Lichter gehen aus.
- Lights alarm: Lichter gehen an, da der Rauchmelder Alarm geschlagen hat.


### Informationen von der LightControl
Die LightControl besitzt verschiedene Zustände, die abgefragt werden können:
- LEDInfoRequest
- LEDInfObject(*ID,isRGB,red,green,blue,brightness*)
- LEDInfoResponse(*LEDInfoList*)
- RoomInfoRequest
- RoomInfoReponse(*RoomInfoList*)
- addRoom(*roomID*)
- removeRoom(*roomID*)
- addLight(*lightID,roomID,slot*)
- addRGBLight(*lightID,roomID,slotRed,slotGreen,slotBlue*)
- removeLight(*lightID*)
- setBrightness(*lightID,brightness*)
- setcolor(*lightID,red,green,blue*)
- setRoomBrightness(*roomID,brightness*)
- setRoomColor(*roomID,red,green,blue*)

## Installation und Betrieb
Um die Lichtersteuerung zu benutzen, muss sie am Strom angeschlossen sein. Zudem muss der [Raspberry Pi](https://www.raspberrypi.org/) angeschlossen sein. Nun kann man die Lichter über die App steuern. [Anleitung](https://gilbert.informatik.uni-stuttgart.de/StuPro2016/SmartHomeMain/blob/dev/LightControl/readme.txt)


## Aufbau
Die LightControl besteht aus einem Raspberry Pi, LEDs und einer Verstärkerplatine.


### Hardware
Bei der in diesem Projekt verwendeten Raspberry Pi handelt es sich um einen [Raspberry Pi 3](https://www.raspberrypi.org/products/raspberry-pi-3-model-b/ "Website über den Raspberry Pi 3") EV3 des Herstellers [Raspberry](https://www.raspberrypi.org/ "Webseite des Raspberry"). 

### Kaa-Schnittstelle

#### Ausgehend

| Event | Syntax | Parameter | Beschreibung |
|-------|--------|-----------|--------------|
| LEDinfoResponse | InfoResponseEvent(LEDInfoList) | LEDInfoList(smart_things.light.schema.LEDInfoObjekt) |Dieses Event wird gesendet sobald die Middleware Kaa ein InfoRequesrEvent sendet. |
| RoomInfoResponseEvent |  RoomInfoResponseEvent(RoomInfoList) | RoomInfoList(array,items string) |Dieses Event wird gesendet sobald die Middleware Kaa ein RoomInfoRequestEvent sendet. |



#### Eingehend

| Event | Syntax | Parameter | Beschreibung |
|-------|--------|-----------|--------------|
| LEDInfoObjekt | LEDInfoObjekt(ID,isRGB,red,green,blue,brightness) | ID(string),isRGB(boolean),red(int),green(int),Blue(int),brightness(int) |Dieses Event wird empfangen sobald die Middleware Kaa die Lichter ansteuert. |
| RoomInfoRequest | -- | -- |Dieses Event wird empfangen sobald die Middleware Kaa Informationen über die Räume verlangt. |
| addRoom | addRoom(roomID)  | roomID(int)  |Dieses Event wird empfangen sobald über die App ein neuer Raum erstellt wird. |
| removeRoom | removeRoom(roomID)  | roomID(int) |Dieses Event wird empfangen sobald über die App ein Raum gelöscht wird. |
| addLight | addLight(lightID) | lightID(int),roomID(int),slot(int) | Dieses Event wird empfangen sobald über die App ein neues Licht eingerichtet wird. |
| addRGBLight | addRGBLight(lightID,roomID,slotRed,slotGreen,slotBlue) | lightID(int),roomID(int),slotRed(int),slotGreen(int),slotBlue(int) |Dieses Event wird empfangen sobald über die App ein neues farbiges LED eingerichtet wird. |
| removeLight | removeLight(lightID) | lightID(int) | Dieses Event wird empfangen sobald über die App ein bestimmtes Licht entfernt wird. |
| setBrightness | setBrightness(lightID,brightness) | lightID(int),brightness(int) |Dieses Event wird empfangen wenn über die App die Helligkeit der LEDs geändert wird. |
| setColor | setColor(lightID,red,green,blue) | lightID(int),red(int),blue(int) |Dieses Event wird empfangen wenn der Benutzer über die App die Farbe eines bestimmten Lichtes ändert. |
| setRoomBrightness | setRoomBrightness(roomID,brightness) | roomID(int),brightness(int) |Dieses Event wird empfangen wenn der Benutzer über die App die Helligkeit in einem ganzen Raum ändert. |
| setRoomColor | setRoomColor(roomID,red,green,blue) | roomID(int),red(int),green(int),blue(int) |Dieses Event wird empfangen sobald der Benutzer der App die Farbe der LEDs in einem kompletten Raum ändert. |

## Anmerkungen
Netzteil ist intern von einem Teammitglied und sollte durch eines der Universität ersetzt werden.