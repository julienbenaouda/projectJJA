package taskman.Frontend.Sections;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is responsible for showing a menu to the user and activating the appropriate method.
 * @author Alexander Braekevelt
 */
public class MenuSection extends Section {

    /**
     * Represents the options of the menu.
     */
    private Map<String, MethodCall> actions;

    /**
     * Represents the selection section of the menu.
     */
    private SelectionSection selectionSection;

    /**
     * Constructs a menu.
     */
    public MenuSection() {
        this.actions = new HashMap<>();
        this.selectionSection = new SelectionSection(true, "exit");
    }

    /**
     * Adds an option to the menu.
     * @param name the name of the option.
     * @param action the action of the option.
     * @throws NullPointerException if an argument is null.
     */
    public void addOption(String name, MethodCall action) throws NullPointerException {
        if (name == null || action == null) {
            throw new NullPointerException("Arguments of option cannot be null!");
        }
        this.selectionSection.addOption(name);
        this.actions.put(name, action);
    }

    /**
     * Shows the menu.
     * @throws Cancel when the user cancels the section.
     */
    @Override
    public void show() throws Cancel {
        this.selectionSection.show();
        try {
            this.actions.get(this.selectionSection.getAnswer()).call();
        } catch (Cancel e) {
            println("Cancelled!");
        } catch (Exception e) {
            println("An error occurred: " + e.getMessage());
        }
    }
}
