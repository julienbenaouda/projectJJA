package taskman.backend.visitor;

import taskman.backend.project.Project;
import taskman.backend.task.Task;
import taskman.backend.time.Clock;
import taskman.backend.time.TimeSpan;
import taskman.backend.user.User;

public class ExportVisitor implements Visitor {



	/**
	 * visits a project
	 * @param p the project to visit
	 */
	@Override
	public void visitProject(Project p) {

	}

	/**
	 * visits a task
	 * @param t the task to visit
	 */
	@Override
	public void visitTask(Task t) {

	}

	/**
	 * visits a timeSpan
	 * @param t the time span to visit
	 */
	@Override
	public void visitTimeSpan(TimeSpan t) {

	}

	/**
	 * visits the system clock
	 * @param c the system clock
	 */
	@Override
	public void visitClock(Clock c) {

	}

	/**
	 * visits a user
	 * @param u the user to visit
	 */
	@Override
	public void visitUser(User u) {

	}

}
