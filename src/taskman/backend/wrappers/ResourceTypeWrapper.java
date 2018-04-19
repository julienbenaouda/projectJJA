package taskman.backend.wrappers;

import java.util.List;

public interface ResourceTypeWrapper {

	/**
	 * Returns the name of the resource type.
	 * @return the name of the resource type.
	 */
	String getName();

	/**
	 * Returns the resources of the resource type.
	 * @return the list of resources.
	 */
	List<? extends ResourceWrapper> getResources();
}
