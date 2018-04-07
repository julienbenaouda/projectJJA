package taskman.Frontend;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.stream.IntStream;

public class Menu extends GraphicSection {

    /**
     * Represents the options of the menu.
     * @invar the number of names is always equal to the number of actions.
     */
    private ArrayList<String> names;
    private ArrayList<GraphicSection> actions;

    /**
     * Construct a graphic section.
     * @param title the titel of the section.
     */
    public Menu(String title) {
        super(title);
        this.names = new ArrayList<>();
        this.actions = new ArrayList<>();
    }

    /**
     * Add an option to the menu.
     * @param name the name of the option.
     * @param action the action of the option.
     * @throws NullPointerException if an argument is null.
     */
    public void addOption(String name, GraphicSection action) throws NullPointerException {
        if (name == null || action == null) {
            throw new NullPointerException("Arguments of option cannot be null!");
        }
        this.names.add(name);
        this.actions.add(action);
    }

    /**
     * Show the content of the menu.
     */
    @Override
    protected void showContent() {
        println("Options:");
        long[] ints = IntStream.range(1, this.names.size() + 1).asLongStream().toArray();
        for (long i: ints) {
            println(String.format("%2d - %s", i, this.names.get(i - 1)));
        }
        return inputValidAnswer("Choose option:", ) - 1;
    }
}
