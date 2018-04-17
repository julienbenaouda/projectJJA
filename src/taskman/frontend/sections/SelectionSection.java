package taskman.frontend.sections;

import java.util.*;

/**
 * This class is responsible for letting the user choose between a number of optionNames.
 * @author Alexander Braekevelt
 */
public class SelectionSection<Type> extends Section {

	/**
	 * Represents the option to cancel between each answer.
	 */
	private final Boolean withCancel;
	private final String cancelText;

	/**
	 * Represents the options the user can choose.
	 */
	private final List<String> optionNames;
	private final List<Type> optionObjects;

	/**
	 * Represents the choice of the user.
	 */
	private Integer answer;

	/**
	 * Constructs a selection section.
	 * @param withCancel if the user can cancel.
	 * @throws NullPointerException if the argument is null.
	 */
	public SelectionSection(Boolean withCancel) throws NullPointerException {
		this(withCancel, "cancel");
	}

	/**
	 * Constructs a selection section.
	 * @param withCancel if the user can cancel.
	 * @param cancelText the name of the exit option.
	 * @throws NullPointerException if an argument is null.
	 */
	public SelectionSection(Boolean withCancel, String cancelText) throws NullPointerException {
		if (withCancel == null || cancelText == null) {
			throw new NullPointerException("Argument of constructor cannot be null!");
		}
		this.withCancel = withCancel;
		this.cancelText = cancelText;
		this.optionNames = new ArrayList<>();
		this.optionObjects = new ArrayList<>();
	}

	/**
	 * Adds an option to the selection.
	 * @param name the name of the option.
	 * @throws NullPointerException if the name is null.
	 */
	public void addOption(String name) throws NullPointerException {
		addOption(name, null);
	}

	/**
	 * Adds an option to the selection.
	 * @param name the name of the option.
	 * @param object the object of the option.
	 * @throws NullPointerException if an argument is null.
	 */
	public void addOption(String name, Type object) throws NullPointerException {
		if (name == null) {
			throw new NullPointerException("Option cannot be null!");
		}
		this.optionNames.add(name);
		this.optionObjects.add(object);
	}

	/**
	 * Add a collection of options to the selection.
	 * @param options the collection of options.
	 * @throws NullPointerException if an argument is null.
	 */
	public void addOptions(Collection<String> options) {
		if (options == null) {
			throw new NullPointerException("Options cannot be null!");
		}
		for (String option: options) {
			addOption(option);
		}
	}

	/**
	 * Add a map of options to the selection.
	 * @param options the map of options.
	 * @throws NullPointerException if an argument is null.
	 */
	public void addOptions(Map<String, Type> options) {
		if (options == null) {
			throw new NullPointerException("Options cannot be null!");
		}
		for (Map.Entry<String, Type> option: options.entrySet()) {
			addOption(option.getKey(), option.getValue());
		}
	}

	/**
	 * Add a list of options to the selection, one for the names and one for the objects.
	 * @param optionNames the names of options.
	 * @param optionObjects the objects of options.
	 * @throws NullPointerException if an argument is null.
	 */
	public void addOptions(List<String> optionNames, List<Type> optionObjects) {
		if (optionNames == null || optionObjects == null) {
			throw new NullPointerException("Options cannot be null!");
		}
		for (int i = 0; i < Math.min(optionNames.size(), optionObjects.size()); i++) {
			addOption(optionNames.get(i), optionObjects.get(i));
		}
	}

	/**
	 * Shows the selection.
	 * @throws Cancel if the user cancels the section.
	 */
	@Override
	public void show() throws Cancel {
		resetAnswer();

		println("Options:");
		ArrayList<String> validSelections = new ArrayList<>();

		// Add optionNames
		Integer i = 1;
		for (String option: this.optionNames) {
			println(String.format("%2d - %s", i, option));
			validSelections.add(i.toString());
			i++;
		}

		// Add cancel
		if (withCancel) {
			println(String.format("%2d - %s", 0, this.cancelText));
			validSelections.add("0");
		}

		Integer selection = Integer.parseInt(inputValidAnswer("Choose option:", validSelections));
		if (selection == 0) {
			throw new Cancel();
		} else {
			this.answer = selection - 1;
		}
	}

	/**
	 * Returns if the selection contains an answer (only true if shown and not cancelled).
	 * @return a Boolean.
	 */
	public Boolean hasAnswer() {
		return this.answer != null;
	}

	/**
	 * Returns the answer of the user.
	 * @return a String.
	 * @throws IllegalStateException if the selection does not contain an answer.
	 */
	public String getAnswer() throws IllegalStateException {
		if (!hasAnswer()) {
			throw new IllegalStateException("Selection does not contain an answer.");
		}
		return this.optionNames.get(this.answer);
	}

	/**
	 * Returns the answer of the user.
	 * @return a Type.
	 * @throws IllegalStateException if the selection does not contain an answer.
	 */
	public Type getAnswerObject() throws IllegalStateException {
		if (!hasAnswer()) {
			throw new IllegalStateException("Selection does not contain an answer.");
		}
		return this.optionObjects.get(this.answer);
	}

	/**
	 * Reset the answer.
	 */
	public void resetAnswer() {
		this.answer = null;
	}

}
