package smart_things.backend.server.kaa;

import smart_things.car.schema.CarEventClassFamily;
import smart_things.coffee.schema.CoffeeEventClassFamily;
import smart_things.door.schema.DoorEventClassFamily;
import smart_things.light.schema.LightEventClassFamily;
import smart_things.smoke.schema.SmokeEventClassFamily;

/**
 * Collection of all event listeners for each smart thing. For easier handling of all listeners together they
 * are collected in this model class and can be used by getters and setters.
 * <p>
 * Created by Jan on 16.01.2017.
 */
public class ListenerCollection {
    //Declaration of all used listeners
    private CoffeeEventClassFamily.Listener listeners_coffee;
    private CarEventClassFamily.Listener listeners_car;
    private LightEventClassFamily.Listener listeners_light;
    private DoorEventClassFamily.Listener listeners_door;
    private SmokeEventClassFamily.Listener listeners_smoke;

    /**
     * Public constructor with which a collection of existing event listeners can be created.
     *
     * @param listeners_coffee Listeners for the coffee machine events
     * @param listeners_car    Listeners for the smart car events
     * @param listeners_light  Listeners for the light control events
     * @param listeners_door   Listeners for the door control events
     * @param listeners_smoke  Listeners for the smoke detector device events
     */
    public ListenerCollection(CoffeeEventClassFamily.Listener listeners_coffee, CarEventClassFamily.Listener listeners_car, LightEventClassFamily.Listener listeners_light, DoorEventClassFamily.Listener listeners_door, SmokeEventClassFamily.Listener listeners_smoke) {
        this.listeners_coffee = listeners_coffee;
        this.listeners_car = listeners_car;
        this.listeners_light = listeners_light;
        this.listeners_door = listeners_door;
        this.listeners_smoke = listeners_smoke;
    }

    //Getters and setters for the declared event listeners
    public CoffeeEventClassFamily.Listener getListeners_coffee() {
        return listeners_coffee;
    }

    public void setListeners_coffee(CoffeeEventClassFamily.Listener listeners_coffee) {
        this.listeners_coffee = listeners_coffee;
    }

    public CarEventClassFamily.Listener getListeners_car() {
        return listeners_car;
    }

    public void setListeners_car(CarEventClassFamily.Listener listeners_car) {
        this.listeners_car = listeners_car;
    }

    public LightEventClassFamily.Listener getListeners_light() {
        return listeners_light;
    }

    public void setListeners_light(LightEventClassFamily.Listener listeners_light) {
        this.listeners_light = listeners_light;
    }

    public DoorEventClassFamily.Listener getListeners_door() {
        return listeners_door;
    }

    public void setListeners_door(DoorEventClassFamily.Listener listeners_door) {
        this.listeners_door = listeners_door;
    }

    public SmokeEventClassFamily.Listener getListeners_smoke() {
        return listeners_smoke;
    }

    public void setListeners_smoke(SmokeEventClassFamily.Listener listeners_smoke) {
        this.listeners_smoke = listeners_smoke;
    }
}
