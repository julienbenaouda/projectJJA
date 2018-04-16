package taskman;

/**
 * This class represents a pair o objects.
 * @param <First> the type of the first object.
 * @param <Second> the type of the second object.
 */
public class Pair<First, Second> {

	/**
	 * Represents the first object.
	 */
	private First first;

	/**
	 * Represents the second object.
	 */
	private Second second;

	/**
	 * Create a pair of objects.
	 * @param first the first object.
	 * @param second the second object.
	 */
	public Pair(First first, Second second) {
		this.first = first;
		this.second = second;
	}

	/**
	 * Returns the first object.
	 * @return the first object.
	 */
	public First getFirst() {
		return first;
	}

	/**
	 * Returns the second object.
	 * @return the second object.
	 */
	public Second getSecond() {
		return second;
	}
}
