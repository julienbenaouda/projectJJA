/**
 * 
 */
package taskman;

/**
 * This interface represents a visitor
 * @author Julien Benaouda
 *
 */
public interface Visitor {
	/**
	 * visits a project
	 * @param p the project to visit
	 */
	public void visitProject(Project p);
	/**
	 * visits a task
	 * @param t the task to visit
	 */
	public void visitTask(Task t);
	/**
	 * visits a timeSpan
	 * @param t the time span to visit
	 */
	public void visitTimeSpan(TimeSpan t);
	/**
	 * visits the system clock
	 * @param c the system clock
	 */
	public void visitClock(Clock c);
	/**
	 * visits a user
	 * @param u the user to visit
	 */
	public void visitUser(User u);
}
