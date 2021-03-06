package taskman.frontend.sections;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;

/**
 * This class is responsible for providing a uniform interface for showing sections.
 * @author Alexander Braekevelt
 */
public abstract class Section {

    /**
     * Shows the section.
     * @throws Cancel if the user cancels the section.
     */
    public abstract void show() throws Cancel;

    /**
     * Prints the given text to the console.
     * @param text the text to print
     */
    final void print(String text) {
        System.out.print(text);
    }

    /**
     * Prints the given text to the console and starts a new line.
     * @param text the text to print
     */
    final void println(String text) {
        print(text + "\n");
    }

    /**
     * Reads a string from the user input.
     * @return the string the user entered.
     */
    String inputString() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            return br.readLine();
        } catch (IOException ignored) {
            return "";
        }
    }

    /**
     * Reads a string from the user input.
     * @param question the question to answer
     * @return the answer the user entered.
     */
    String inputAnswer(String question) {
        print(question + ' ');
        return inputString();
    }

    /**
     * Lets the user enter a valid answer.
     * @param question the question to answer.
     * @param answers a collection of valid answers.
     * @return an answer from the collection.
     */
    String inputValidAnswer(String question, Collection<String> answers) {
        String answer;
        while (true) {
            answer = inputAnswer(question);
            if (answers.contains(answer)) {
                return answer;
            } else {
                println("Invalid answer! Allowed: " + String.join(", ", answers));
            }
        }
    }

}
