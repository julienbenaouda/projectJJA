package taskman.Frontend;

import sun.plugin.dom.exception.InvalidStateException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class is responsible for presenting a form to the user and storing the answers.
 * @author Alexander Braekevelt
 */
public class FormSection extends Section {

    /**
     * Represents the option to cancel between each answer.
     */
    private final Boolean withCancel;

    /**
     * Represents the questions the user must answer.
     */
    private final List<String> questions;

    /**
     * Represents the answers of the user.
     * @invar if this list is not null, it's size is equal to the number of questions.
     */
    private List<String> answers;

    /**
     * Constructs a form.
     * @param withCancel if the user can cancel between each answer.
     * @param questions the questions of the form.
     * @throws NullPointerException if the argument is null.
     */
    public FormSection(Boolean withCancel, String... questions) throws NullPointerException {
        if (withCancel == null || questions == null) {
            throw new NullPointerException("Arguments of form constructor cannot be null!");
        }
        this.withCancel = withCancel;
        this.questions = Arrays.asList(questions);
    }

    /**
     * Shows the form.
     * @throws Cancel when the user cancels the section.
     */
    @Override
    public void show() throws Cancel {
        resetAnswers();
        List<String> answers = new ArrayList<>();
        for (String question: questions) {
            answers.add(inputAnswer(question));
            if (this.withCancel && inputValidAnswer("Cancel? [Y/N]", Arrays.asList("Y", "N")).equals("Y")) {
                throw new Cancel();
            }
        }
        this.answers = answers;
    }

    /**
     * Returns if the form contains the answers (only true if form is shown and not cancelled).
     * @return a Boolean.
     */
    public Boolean hasAnswers() {
        return this.answers != null;
    }

    /**
     * Returns an answer of the user.
     * @param nr the number of the answer.
     * @return a String.
     * @throws IllegalStateException if the form does not contain answers.
     * @throws IndexOutOfBoundsException if no answer corresponds to this number.
     */
    public String getAnswer(int nr) throws IllegalStateException, IndexOutOfBoundsException {
        if (!hasAnswers()) {
            throw new IllegalStateException("Form does not contain answers.");
        }
        return this.answers.get(nr);
    }

    /**
     * Resets the answers of the form.
     */
    private void resetAnswers() {
        this.answers = null;
    }

}
