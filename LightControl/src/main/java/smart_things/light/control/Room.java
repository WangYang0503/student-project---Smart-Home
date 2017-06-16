package smart_things.light.control;
import java.util.LinkedList;

/**
 * This class represents a room in the house. The room contains a list of lights.
 * @author Studienprojekt, Mario Maser
 *
 */
public class Room {

	private String id = "";
	private LinkedList<Light> lights;
	
	public Room(String id){
		this.id = id;
		lights = new LinkedList<>();
	}

	/**
	 * @param light Instance that implements Light
	 */
	public void addLight(Light light){
		lights.add(light);
	}
	
	/**
	 * Removes the first occurrence of a light with the specified id
	 * @param id Distinct id of the light
	 */
	public void removeLight(String id){
		for(int i = 0; i < lights.size(); i++){
			if(lights.get(i).getId().equals(id)){
				lights.remove(i);
				break;
			}
		}
	}

	/**
	 * 
	 * @param id Distinct id of the light
	 * @return first occurrence of a light with the specified id. Null if no light is found
	 */
	public Light getLight(String id){
		Light light = null;
		for(int i = 0; i < lights.size(); i++){
			if(lights.get(i).getId().equals(id)){
				light = lights.get(i);
			}
		}
		return light;
	}
	
	/**
	 * Removes all instances of Light from the list and releases their GPIO Pins
	 */
	public void removeAllLights(){
		for(int i = 0; i < lights.size(); i++){
			lights.get(i).releasePin();
			lights.remove(i);
		}
	}
	
	public LinkedList<Light> getLights() {
		return lights;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	
	
}
