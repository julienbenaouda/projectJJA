package taskman;

public class Main {

	public static void main(String[] args) {
		Project p = new Project("test", "testsdesc", "22/02/2012 12:12", "22/03/2012 12:12");
		Task t = new Task("test", "5", "5");
		p.addTask(t);
		int id = t.getID();
		p.getTask(id);
		Task tnew = new Task("test 2", "5", "10");
		p.addTask(tnew);
		p.getTask(tnew.getID());
	}

}
