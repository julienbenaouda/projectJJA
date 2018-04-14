package taskman.frontend.sections;

/**
 * This class is responsible for showing a piece of text to the user.
 * @author Alexander Braekevelt
 */
public class TextSection extends Section {

    /**
     * Represents the text to show.
     */
    private final String text;

    /**
     * Represents if the section should block after showing the text.
     */
    private final boolean isBlocking;

    /**
     * Constructs a text section.
     * @param text the text to show.
     * @param isBlocking if the section should block after showing the text.
     * @throws NullPointerException if the text is null.
     */
    public TextSection(String text, boolean isBlocking) throws NullPointerException {
        if (text == null) {
            throw new NullPointerException("Text cannot be null!");
        }
        this.text = text;
        this.isBlocking = isBlocking;
    }

    /**
     * Shows the text section.
     */
    @Override
    public void show() {
        println(this.text);
        if (this.isBlocking) inputAnswer("Press enter to continue...");
    }
}
