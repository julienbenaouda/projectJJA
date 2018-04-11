package taskman.frontend.sections;

/**
 * This class is responsible for showing a piece of information to the user.
 * @author Alexander Braekevelt
 */
public class InfoSection extends Section {

    /**
     * Represents the query to execute.
     */
    private final String text;

    /**
     * Represents if the section should halt.
     */
    private final boolean halt;

    /**
     * Constructs an info section.
     * @throws NullPointerException if the text is null.
     */
    public InfoSection(String text, boolean halt) throws NullPointerException {
        if (text == null) {
            throw new NullPointerException("Text cannot be null!");
        }
        this.text = text;
        this.halt = halt;
    }

    /**
     * Shows the info section.
     */
    @Override
    public void show() {
        println(this.text);
        if (this.halt) inputAnswer("Press enter to continue...");
    }
}
