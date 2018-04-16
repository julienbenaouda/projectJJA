package taskman.backend.resource;

import java.util.*;

import taskman.backend.time.AvailabilityPeriod;
import taskman.backend.time.TimeSpan;

/**
 * This class is responsible for storing and retrieving resource types of the system.
 *
 * @author Jeroen Van Der Donckt, Julien Benaouda
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
     * Returns the name of the resource type.
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
	 * Represents the availability for every week day.
	 */
	private HashMap<Integer, AvailabilityPeriod> availability;

    /**
     * Returns the hashcode of the name of the resource type.
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
     * Returns the availability period of the given day.
     *
     * @param day the day to get the availability period from
     * @return the availability period of the given day
     */
	public AvailabilityPeriod getAvailabilityPeriod(int day){
	    return availability.get(day);
    }


	/**
	 * Represents the list of resources.
	 */
	private ArrayList<Resource> resources;

	/**
	 * Returns the resources of the resource type.
	 *
	 * @return the list of resources
	 */
	private List<Resource> getResources() {
		return resources;
	}

    /**
     * Returns the number of resources of the resource type.
     *
     * @return the number of resources of the resource type
     */
	public int getNbOfResources(){
	    return resources.size();
    }
	
	/**
	 * Adds a resource to the list of resources.
     *
	 * @param resource the resource to add
	 * @post the given resource is added to the list of resources
	 */
	public void addResource(Resource resource) {
		resources.add(resource);
	}

	/**
	 * Returns if there are enough resources  of the resource type available in the given time span.
	 *
	 * @param timeSpan the time span to check the number of resources for
	 * @param amount the amount of resources that needs to be available
	 * @return true if there are enough resources of the resource type available in the given time span, otherwise false
	 */
	public boolean hasAvailableResources(TimeSpan timeSpan, int amount){
		int numberAvailable = 0;
		for (Resource resource : getResources()){
			if (resource.isAvailable(timeSpan)){
				numberAvailable += 1;
			}
		}
		if (numberAvailable >= amount){
			return true;
		}
		else{
			return false;
		}
	}

	/**
	 * Returns the available resources of the resource type in the given time span.
	 *
	 * @param timeSpan the time span to get the available resources from
	 * @return the available resources of the resource type in the given time span
	 */
	public List<Resource> getAvailableResources(TimeSpan timeSpan){
		boolean validTimeSpan = false;
		for(Integer key : getAvailability().keySet()){
			if (getAvailability().get(key).overlaps(timeSpan)){
				validTimeSpan = true;
			}
		}

        List<Resource> resources = new ArrayList<>();
		if (validTimeSpan) {
			for (Resource resource : getResources()) {
				if (resource.isAvailable(timeSpan)) {
					resources.add(resource);
				}
			}
		}
		return resources;
	}

	/**
	 * Adds the availability for the given day 
	 * @param weekDay the day for which to add the availability
	 * @param availabilityPeriod the availability
	 * @post the given availability is added to the list
	 * @throws IllegalArgumentException when the weekday is less than 0 or greater than 6
	 */
	public void addAvailability(int weekDay, AvailabilityPeriod availabilityPeriod) throws IllegalArgumentException {
		if(weekDay < 0 || weekDay > 6) {
			throw new IllegalArgumentException("the number of the week day must be between 0 and 6");
		}
		availability.put(weekDay, availabilityPeriod);
	}
}
