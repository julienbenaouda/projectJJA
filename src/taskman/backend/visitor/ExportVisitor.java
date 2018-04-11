package taskman.backend.visitor;

import taskman.backend.importExport.ImportExportException;
import taskman.backend.importExport.XmlObject;
import taskman.backend.project.Project;
import taskman.backend.project.ProjectOrganizer;
import taskman.backend.task.Task;
import taskman.backend.time.AvailabilityPeriod;
import taskman.backend.time.Clock;
import taskman.backend.time.TimeSpan;
import taskman.backend.user.User;
import taskman.backend.user.UserManager;

public class ExportVisitor implements Visitor {


	/**
	 * Represents the root xml object.
	 */
	private XmlObject root;

	/**
	 * Creates an ExportVisitor for exporting objects.
	 */
	public ExportVisitor(String path) throws ImportExportException {
		this.root = new XmlObject();
	}

	@Override
	public void visitProjectOrganizer(ProjectOrganizer p) {

	}

	@Override
	public void visitProject(Project p) {

	}

	@Override
	public void visitTask(Task t) {

	}

	@Override
	public void visitClock(Clock c) {

	}

	@Override
	public void visitTimeSpan(TimeSpan t) {

	}

	@Override
	public void visitUserManager(UserManager u) {

	}

	@Override
	public void visitUser(User u) {

	}

	@Override
	public void visitAvailabilityPeriod(AvailabilityPeriod availabilityPeriod) {

	}
}
