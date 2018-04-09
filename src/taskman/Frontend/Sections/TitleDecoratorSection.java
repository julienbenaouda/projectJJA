package taskman.Frontend.Sections;

/**
 * This class is responsible for decorating a section with a title.
 * @author Alexander Braekevelt
 */
public class TitleDecoratorSection extends Section {

    /**
     * Represents the title of the section.
     */
    private final String title;

    /**
     * Represents the decoration around the title of the sections.
     */
    private static final char decoration = '-';

    /**
     * Represents the length of the title.
     */
    private static final int length = 50;

    /**
     * Represents the section that is decorated.
     */
    private final Section section;

    /**
     * Constructs a title decorator for a section.
     * @param title the title of the section.
     * @throws NullPointerException if the title is null.
     */
    public TitleDecoratorSection(String title, Section section) throws NullPointerException {
        if (title == null || section == null) {
            throw new NullPointerException("Arguments cannot be null!");
        }
        this.title = title;
        this.section = section;
    }

    /**
     * Shows the section.
     * @throws Cancel when the user cancels the section.
     */
    @Override
    public void show() throws Cancel {
        int lineLength = length - this.title.length();
        int div = Math.floorDiv(lineLength, 2);
        int mod = Math.floorMod(lineLength, 2);
        println("\n" + repeat(decoration, div) + ' ' + this.title.toUpperCase() + ' ' + repeat(decoration, div + mod));
        this.section.show();
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

}
