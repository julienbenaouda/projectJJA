package taskman.Frontend;

import sun.plugin.dom.exception.InvalidStateException;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for letting the user choose between a number of options.
 * @author Alexander Braekevelt
 */
public class SelectionSection extends Section {

	/**
	 * Represents the option to cancel between each answer.
	 */
	private final Boolean withCancel;
	private final String cancelText;

	/**
	 * Represents the options the user can choose.
	 */
	private final List<String> options;

	/**
	 * Represents the answer of the user.
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
	 * @param cancelText the name of the option to exit.
	 * @throws NullPointerException if an argument is null.
	 */
	public SelectionSection(Boolean withCancel, String cancelText) throws NullPointerException {
		if (withCancel == null || cancelText == null) {
			throw new NullPointerException("Argument of constructor cannot be null!");
		}
		this.withCancel = withCancel;
		this.cancelText = cancelText;
		this.options = new ArrayList<>();
	}

	/**
	 * Adds an option to the selection.
	 * @param option the name of the option.
	 * @throws NullPointerException if an argument is null.
	 */
	public void addOption(String option) throws NullPointerException {
		if (option == null) {
			throw new NullPointerException("Option cannot be null!");
		}
		this.options.add(option);
	}

	/**
	 * Shows the selection.
	 * @throws Cancel when the user cancels the section.
	 */
	@Override
	public void show() throws Cancel {
		resetAnswer();

		println("Options:");
		ArrayList<String> validSelections = new ArrayList<>();

		// Add options
		for (Integer i = 1; i <= this.options.size(); i++) {
			println(String.format("%2d - %s", i, this.options.get(i - 1)));
			validSelections.add(i.toString());
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
			this.answer = selection;
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
	 * @throws InvalidStateException if the selection does not contain an answer.
	 */
	public String getAnswer() throws InvalidStateException {
		if (!hasAnswer()) {
			throw new InvalidStateException("Selection does not contain an answer.");
		}
		return this.options.get(this.answer);
	}

	/**
	 * Returns the answer of the user.
	 * @return an Integer.
	 * @throws InvalidStateException if the selection does not contain an answer.
	 */
	public Integer getAnswerNumber() throws InvalidStateException {
		if (!hasAnswer()) {
			throw new InvalidStateException("Selection does not contain an answer.");
		}
		return this.answer;
	}

	/**
	 * Reset the answer.
	 */
	private void resetAnswer() {
		this.answer = null;
	}

}
