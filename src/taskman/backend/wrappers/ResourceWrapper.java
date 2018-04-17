package taskman.backend.wrappers;

public interface ResourceWrapper {


	/**
	 * Returns the name of the resource.
	 * @return the name of the resource.
	 */
	String getName();

	/**
	 * Returns the type of the resource.
	 * @return the type of the resource.
	 */
	ResourceTypeWrapper getType();

}
