package taskman.backend.resource;

import java.util.HashSet;
import java.util.Set;

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
}
