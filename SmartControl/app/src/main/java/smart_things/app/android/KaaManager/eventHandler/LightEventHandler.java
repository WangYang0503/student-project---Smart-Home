package smart_things.app.android.KaaManager.eventHandler;

import android.util.Log;

import org.kaaproject.kaa.client.KaaClient;
import org.kaaproject.kaa.client.event.EventFamilyFactory;
import org.kaaproject.kaa.client.event.FindEventListenersCallback;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

import smart_things.light.schema.AddLightEvent;
import smart_things.light.schema.AddRGBLightEvent;
import smart_things.light.schema.AddRoomEvent;
import smart_things.light.schema.LEDInfoObject;
import smart_things.light.schema.LEDInfoRequestEvent;
import smart_things.light.schema.LEDInfoResponseEvent;
import smart_things.light.schema.LightEventClassFamily;
import smart_things.light.schema.RemoveLightEvent;
import smart_things.light.schema.RemoveRoomEvent;
import smart_things.light.schema.RoomInfoResponseEvent;
import smart_things.light.schema.SetBrightnessEvent;
import smart_things.light.schema.SetColorEvent;
import smart_things.light.schema.SetEmergencyMode;
import smart_things.light.schema.SetRoomBrightnessEvent;
import smart_things.light.schema.SetRoomColorEvent;

//import smart_things.light.schema.SetEmergencyMode;

/**
 * Created by Mario Maser on 07.01.17.
 */

public class LightEventHandler extends Observable {

    private KaaClient kaaClient;
    private LightEventClassFamily tecf;
    private List<String> roomItems = new ArrayList<>();
    private List<LEDInfoObject> responseObject;
    private LEDInfoObject responseName;

    public LightEventHandler(KaaClient kaaClient) {
        this.kaaClient = kaaClient;
        EventFamilyFactory eventFamilyFactory = kaaClient.getEventFamilyFactory();
        final List<String> FQNs = new LinkedList<>();
        FQNs.add(LightEventHandler.class.getName());
        tecf = eventFamilyFactory.getLightEventClassFamily();

        tecf.addListener(new LightEventClassFamily.Listener() {

            @Override
            public void onEvent(LEDInfoResponseEvent ledInfoResponse, String s) {
                responseObject = ledInfoResponse.getLEDInfoList();
                if (!roomItems.isEmpty()) {
                    roomItems.clear();
                    for (LEDInfoObject l : responseObject) {
                        roomItems.add(l.getRoomID());

                    }
                } else {
                    for (LEDInfoObject l : responseObject) {
                        roomItems.add(l.getRoomID());
                    }
                }
                setChanged();
                notifyObservers();
            }

            @Override
            public void onEvent(RoomInfoResponseEvent roomInfoResponse, String s) {

            }
        });

        kaaClient.findEventListeners(FQNs, new FindEventListenersCallback() {
            @Override
            public void onEventListenersReceived(List<String> list) {

            }

            @Override
            public void onRequestFailed() {

            }
        });

    }


    public List<String> getRoomItems() {

        return roomItems;
    }

    public void receiveResponse() {

        Log.d("receiveResponse()", "LEDInfoResponse");
    }

    public void sendBrightnessEvent(String lightID, int brightness) {
        SetBrightnessEvent b = new SetBrightnessEvent();
        b.setLightID(lightID);
        b.setBrightness(brightness);
        tecf.sendEventToAll(b);
    }

    public void initialize() {
        LEDInfoRequestEvent request = new LEDInfoRequestEvent();
        tecf.sendEventToAll(request);
    }

    public void sendSetRoomBrightness(String roomID, int brightness) {
        SetRoomBrightnessEvent b = new SetRoomBrightnessEvent();
        b.setRoomID(roomID);
        b.setBrightness(brightness);
        tecf.sendEventToAll(b);
    }

    public void sendSetRoomColor(String roomID, int red, int green, int blue) {
        SetRoomColorEvent c = new SetRoomColorEvent();
        c.setRoomID(roomID);
        c.setRed(red);
        c.setGreen(green);
        c.setBlue(blue);
        tecf.sendEventToAll(c);
    }

    public void sendColorEvent(String lightID, int red, int green, int blue) {
        SetColorEvent c = new SetColorEvent();
        c.setLightID(lightID);
        c.setRed(red);
        c.setGreen(green);
        c.setBlue(blue);
        tecf.sendEventToAll(c);
    }

    public void sendAddRoomEvent(String roomID) {
        AddRoomEvent room = new AddRoomEvent();
        room.setRoomID(roomID);
        tecf.sendEventToAll(room);
    }

    public void sendRemoveRoomEvent(String roomID) {
        RemoveRoomEvent room = new RemoveRoomEvent();
        room.setRoomID(roomID);
        tecf.sendEventToAll(room);
    }

    public void sendAddLightEvent(String lightID, String roomID, int slot) {
        AddLightEvent light = new AddLightEvent();
        light.setLightID(lightID);
        light.setRoomID(roomID);
        light.setSlot(slot);
        tecf.sendEventToAll(light);
    }

    public void sendAddRGBLightEvent(String lightID, String roomID, int slotRed, int slotGreen, int slotBlue) {
        AddRGBLightEvent light = new AddRGBLightEvent();
        light.setLightID(lightID);
        light.setRoomID(roomID);
        light.setSlotRed(slotRed);
        light.setSlotGreen(slotGreen);
        light.setSlotBlue(slotBlue);
        tecf.sendEventToAll(light);
    }

    public void sendRemoveLightEvent(String lightID) {
        RemoveLightEvent light = new RemoveLightEvent();
        light.setLightID(lightID);
        tecf.sendEventToAll(light);
    }

    public void sendEmergencyModeCloseEvent() {

        tecf.sendEventToAll(new SetEmergencyMode(false));
    }

    public List<LEDInfoObject> getResponseObject() {
        return responseObject;
    }
}