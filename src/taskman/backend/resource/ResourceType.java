package taskman.backend.resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import taskman.backend.time.AvailabilityPeriod;

/**
 * This class is responsible for storing and retrieving resource types of the system.
 *
 * @author Jeroen Van Der Donckt
 */
public class ResourceType {

    /**
     * Creates a new resource type with given name.
     *
     * @param name the name of the resource type
     */
    public  ResourceType(String name){
        setName(name);
        availability = new HashMap<>();
        resources = new ArrayList<>();
    }

    /**
     * Represents the name of a resource type.
     */
    private String name;

    /**
     * Returns the name of the resource type
     *
     * @return the name of the resource type
     */
    public String getName(){
        return name;
    }

    /**
     * Sets the name of the resource type to the given name.
     *
     * @param name the name of the resource type
     * @post the name of the resource type is set to the given name
     */
    private void setName(String name){
        this.name = name;
    }

    /**
     * Returns the hashcode of the name of the resource type
     *
     * @return the hashcode of the name of the resource type
     */
    @Override
    public int hashCode() {
        return getName().hashCode();
    }
	/**
	 * return the availability of this resource
	 */
	private Map<Integer, AvailabilityPeriod> getAvailability() {
		return availability;
	}
	
	/**
	 * represents the availability for every week day
	 */
	private HashMap<Integer, AvailabilityPeriod> availability;
	
	/**
	 * @return the list of resources
	 */
	private List<Resource> getResources() {
		return resources;
	}
	
	/**
	 * adds a resource to the list of resources
	 * @param resource the resource to add
	 */
	public void addResource(Resource resource) {
		resources.add(resource);
	}
	
	/**
	 * represents the list of resources
	 */
	private ArrayList<Resource> resources;
}
