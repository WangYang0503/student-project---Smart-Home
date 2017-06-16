package smart_things.backend.server.kaa;

import smart_things.car.schema.CarEventClassFamily;
import smart_things.coffee.schema.CoffeeEventClassFamily;
import smart_things.door.schema.DoorEventClassFamily;
import smart_things.light.schema.LightEventClassFamily;
import smart_things.smoke.schema.SmokeEventClassFamily;

/**
 * Collection of all event class families (tecfs) for each smart thing. For easier handling of all class families
 * together they are collected in this model class and can be used by getters and setters.
 * <p>
 * Created by Jan on 16.01.2017.
 */
public class ECFCollection {
    //Declaration of all used event class families
    private CoffeeEventClassFamily tecf_coffee = null;
    private CarEventClassFamily tecf_car = null;
    private LightEventClassFamily tecf_light = null;
    private DoorEventClassFamily tecf_door = null;
    private SmokeEventClassFamily tecf_smoke = null;

    //Getters and setters for the declared event class families
    public CoffeeEventClassFamily getTecf_coffee() {
        return tecf_coffee;
    }

    public void setTecf_coffee(CoffeeEventClassFamily tecf_coffee) {
        this.tecf_coffee = tecf_coffee;
    }

    public CarEventClassFamily getTecf_car() {
        return tecf_car;
    }

    public void setTecf_car(CarEventClassFamily tecf_car) {
        this.tecf_car = tecf_car;
    }

    public LightEventClassFamily getTecf_light() {
        return tecf_light;
    }

    public void setTecf_light(LightEventClassFamily tecf_light) {
        this.tecf_light = tecf_light;
    }

    public DoorEventClassFamily getTecf_door() {
        return tecf_door;
    }

    public void setTecf_door(DoorEventClassFamily tecf_door) {
        this.tecf_door = tecf_door;
    }

    public SmokeEventClassFamily getTecf_smoke() {
        return tecf_smoke;
    }

    public void setTecf_smoke(SmokeEventClassFamily tecf_smoke) {
        this.tecf_smoke = tecf_smoke;
    }
}
