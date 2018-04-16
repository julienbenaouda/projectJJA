package taskman.frontend.sections;

/**
 * This class is responsible for showing a title.
 * @author Alexander Braekevelt
 */
public class TitleSection extends Section {

    /**
     * Represents the section that shows the text of the title.
     */
    private final TextSection textSection;

    /**
     * Constructs a title decorator for a section.
     * @param title the title of the section.
     * @throws NullPointerException if the title is null.
     */
    public TitleSection(String title) throws NullPointerException {
        if (title == null) {
            throw new NullPointerException("Arguments cannot be null!");
        }
        char decoration = '-';
        int length = 50;
        int lineLength = length - title.length();
        int div = Math.floorDiv(lineLength, 2);
        int mod = Math.floorMod(lineLength, 2);
        String text = "\n" + repeat(decoration, div) + ' ' + title.toUpperCase() + ' ' + repeat(decoration, div + mod);
        this.textSection = new TextSection(text, false);
    }

    /**
     * Repeat a char c n times.
     * @param c the char
     * @param n the number of times
     * @return a String with n time char c.
     */
    private String repeat(char c, int n) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < n; i++) result.append(c);
        return result.toString();
    }

    /**
     * Shows the section.
     */
    @Override
    public void show() {
        this.textSection.show();
    }

}
