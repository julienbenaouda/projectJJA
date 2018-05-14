package taskman.backend.resource;

import taskman.backend.time.AvailabilityPeriod;
import taskman.backend.time.TimeSpan;
import taskman.backend.user.Developer;
import taskman.backend.wrappers.ResourceTypeWrapper;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is responsible for storing and retrieving resource types of the system.
 * It has a name and list of resources. It also holds the information regarding the availability of its resources
 * @author Jeroen Van Der Donckt, Julien Benaouda
 */
public class ResourceType implements ResourceTypeWrapper {

    /**
     * Creates a new resource type with given name.
     *
     * @param name the name of the resource type
     * @throws IllegalArgumentException when the name is null
     */
    public ResourceType(String name){
    	if(name == null) {
    		throw new IllegalArgumentException("the name can't be null.");
    	}
        setName(name);
        availability = new HashMap<>();
        resources = new ArrayList<>();
        AvailabilityPeriod always = new AvailabilityPeriod(LocalTime.of(0, 0), LocalTime.of(23, 59));
        for(int i = 1; i < 8; i++) {
        	addAvailability(i, always);
        }
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
	@Override
	public List<Resource> getResources() {
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
	 * Returns if a resource with the given name exists.
	 * @param name the name of the resource.
	 * @return if a resource with the given name exists.
	 */
	public boolean hasResource(String name) throws IllegalArgumentException {
		for (Resource resource: this.resources) {
			if (resource.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns the resource for a given name.
	 * @param name the name of the resource.
	 * @return the resource with the given name.
	 * @throws IllegalArgumentException if no resource with the given name is found.
	 */
	public Resource getResource(String name) throws IllegalArgumentException {
		for (Resource resource: this.resources) {
			if (resource.getName().equals(name)) {
				return resource;
			}
		}
		throw new IllegalArgumentException("No resource with name " + name + " found!");
    }
	
	/**
	 * Adds a resource to the list of resources.
     *
	 * @param resource the resource to add
	 * @post the given resource is added to the list of resources
	 */
	private void addResource(Resource resource) {
		resources.add(resource);
	}
	
	/**
	 * creates a new resource with given name
	 * @param name the name of the resource
	 * @throws IllegalArgumentException when the name is null or already exists
	 */
	public void createResource(String name) {
		if(name == null || hasResource(name)) {
			throw new IllegalArgumentException("This resource already exists. Please try another name.");
		}
		Resource r = new Resource(name, this);
		addResource(r);
	}
	
	/**
	 * creates a resource from a developer
	 * @param name the name of the resource
	 * @param startBreak the start time of the break of the developer
	 * @param d the developer to link to the resource
	 */
	public void createResourceFromUser(String name, LocalTime startBreak, Developer d) {
		Resource r = new DeveloperResource(name, this, startBreak, d);
		addResource(r);
	}

	/**
	 * Removes a resource from the list of resources.
	 *
	 * @param resource the resource to remove
	 * @post the given resource is removes from the list of resources
	 * @throws IllegalStateException if the resource cannot be removed.
	 */
	public void removeResource(Resource resource) throws IllegalStateException {
		if (resource.canRemove()) {
			resources.remove(resource);
		} else {
			throw new IllegalStateException("The resource cannot be removed!");
		}
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
		if (numberAvailable >= amount) {
			return true;
		} else {
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
	 * Adds the availability for the given day .
	 *
	 * @param weekDay the day for which to add the availability
	 * @param availabilityPeriod the availability
	 * @post the given availability is added to the list
	 * @throws IllegalArgumentException when the weekday is less than 0 or greater than 6
	 */
	public void addAvailability(int weekDay, AvailabilityPeriod availabilityPeriod) throws IllegalArgumentException {
		if(weekDay < 1 || weekDay > 7) {
			throw new IllegalArgumentException("the number of the week day must be between 1 and 7");
		}
		availability.put(weekDay, availabilityPeriod);
	}
}
