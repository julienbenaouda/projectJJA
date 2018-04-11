package taskman.backend.resource;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Class representing a resource item.
 *
 * @author Jeroen Van Der Donckt, Julien Benaouda
 */
public class ResourceItem implements Resource {

    /**
     * Creates a new resource item with the given resource type.
     *
     * @param type the resource type of the resource item
     * @post a new resource item is created with given resource type
     */
    public ResourceItem(ResourceType type){
        setType(type);
    }

	@Override
	public LocalDateTime firstAvailableTime(LocalDateTime time, ArrayList<Resource> resources) {
		// TODO Auto-generated method stub
		return null;
	}

    /**
     * Represents the resource type of the resource item.
     */
	private ResourceType type;
	
    @Override
    public ResourceType getType() {
        return type;
    }

    /**
     * Sets the resource type of the resource item to the given type.
     *
     * @param type the resource type of the resource type
     * @post the resource type of the resource item is set to the given type
     */
	private void setType(ResourceType type) {
	    this.type = type;
    }
}
