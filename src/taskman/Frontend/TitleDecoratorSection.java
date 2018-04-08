package taskman.Frontend;

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
    private static final String decoration = "----------";

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
        println("\n" + decoration + ' ' + this.title.toUpperCase() + ' ' + decoration);
        this.section.show();
    }

}
