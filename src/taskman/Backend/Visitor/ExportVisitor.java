package taskman.Backend.Visitor;

import taskman.Backend.ImportExport.ImportExportException;
import taskman.Backend.ImportExport.XmlObject;
import taskman.Backend.Project.Project;
import taskman.Backend.Project.ProjectOrganizer;
import taskman.Backend.Task.Task;
import taskman.Backend.Time.Clock;
import taskman.Backend.Time.TimeSpan;
import taskman.Backend.User.User;
import taskman.Backend.User.UserManager;

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
}
