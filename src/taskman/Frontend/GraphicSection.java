package taskman.Frontend;

import java.util.Collection;
import java.util.Scanner;

/**
 * This class is responsible for providing a uniform interface for showing graphic sections.
 * @author Alexander Braekevelt
 */
public abstract class GraphicSection {

    /**
     * Represents the title of the section.
     */
    private final String title;

    /**
     * Represents the decoration around the title of the sections.
     */
    private static final String decoration = "----------";

    /**
     * Construct a graphic section.
     * @param title the titel of the section.
     */
    public GraphicSection(String title) {
        this.title = title;
    }

    /**
     * Shows the graphic section.
     */
    public final void show() {
        printTitle(this.title);
        showContent();
    }

    /**
     * Show the content of the graphic section.
     */
    protected abstract void showContent();

    /**
     * Prints the given text to the console.
     * @param text the text to print
     */
    protected final void print(String text) {
        System.out.print(text);
    }

    /**
     * Prints the given text to the console and starts a new line.
     * @param text the text to print
     */
    protected final void println(String text) {
        print(text + "\n");
    }

    /**
     * Prints the given text to the console as a title.
     * @param text the text to print
     */
    private final void printTitle(String text) {
        println(decoration + String.format(" %1$10s ", text.toUpperCase()) + decoration);
    }

    /**
     * Reads a string from the user input.
     * @return the string the user entered.
     */
    protected String inputString() {
        try (Scanner sc = new Scanner(System.in)) {
            return sc.nextLine();
        }
    }

    /**
     * Reads a string from the user input.
     * @return the string the user entered.
     */
    protected String inputString(String question) {
        print(question + ' ');
        return inputString();
    }

    /**
     * Only lets the user enter a valid answer.
     * @param question the question to answer.
     * @param answers the valid answers.
     * @return a valid answer.
     */
    protected String inputValidAnswer(String question, Collection<String> answers) {
        String answer;
        while (true) {
            answer = inputString(question);
            if (answers.contains(answer)) {
                return answer;
            } else {
                println("Invalid answer! Allowed: " + String.join(", ", answers));
            }
        }
    }

}
