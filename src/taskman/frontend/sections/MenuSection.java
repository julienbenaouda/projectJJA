package taskman.frontend.sections;

import taskman.frontend.MethodCall;

/**
 * This class is responsible for showing a menu to the user and activating the appropriate method.
 * @author Alexander Braekevelt
 */
public class MenuSection extends Section {

	/**
	 * Represents the selection section of the menu.
	 */
	private SelectionSection<MethodCall> selectionSection;

	/**
	 * Constructs a menu.
	 */
	public MenuSection() {
		this("exit");
	}

	/**
	 * Constructs a menu.
	 * @param cancelText the name of the exit option.
	 */
	public MenuSection(String cancelText) {
		this.selectionSection = new SelectionSection<>(true, cancelText);
	}

	/**
	 * Adds an option to the menu.
	 * @param name the name of the option.
	 * @param action the action of the option.
	 * @throws NullPointerException if an argument is null.
	 */
	public void addOption(String name, MethodCall action) throws NullPointerException {
		this.selectionSection.addOption(name, action);
	}

	/**
	 * Shows the menu.
	 * @throws Cancel when the user cancels the section.
	 */
	@Override
	public void show() throws Cancel {
		this.selectionSection.resetAnswer();
		this.selectionSection.show();
		try {
			this.selectionSection.getAnswerObject().call();
		} catch (Cancel e) {
			println("Cancelled!");
		} catch (Exception e) {
			println("An error occurred: " + e.getMessage());
		}
	}
}