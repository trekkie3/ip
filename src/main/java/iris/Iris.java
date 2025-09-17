package iris;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import iris.task.Task;

/**
 * Main class of the Iris application.
 * Handles user interaction, command parsing, and task management.
 */
public class Iris {
    private final List<Task> taskList;

    /**
     * Constructor for Iris class.
     */
    public Iris() {
        this.taskList = new ArrayList<>();
    }

    /**
     * Expose tasks to Command for execution.
     */
    public List<Task> getTaskList() {
        return taskList;
    }

    /**
     * Returns the preamble message for the application.
     *
     * @return Preamble message string
     */
    public String getPreamble() {
        return "Hello! I'm Iris. What can I do for you?";
    }

    /**
     * Saves taskList into data.txt.
     *
     * @param filePath Path to save tasks
     */
    public void save(String filePath) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            taskList.stream()
                    .map(Task::serialize)
                    .forEach(writer::println);
        } catch (IOException exception) {
            System.err.println("Error: Failed to write tasks to " + filePath + ".");
        }
    }


    /**
     * Loads taskList from the specified filePath.
     *
     * @param filePath Path to load tasks
     */
    public String load(String filePath) {
        StringBuilder result = new StringBuilder();
        try {
            Scanner scanner = new Scanner(new File(filePath));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                Task task = Task.deserialize(line);
                if (task != null) {
                    this.taskList.add(task);
                    result.append("Loaded iris.task: ").append(task).append("\n");
                } else {
                    result.append("Error: Malformed iris.task line, ignoring: ").append(line).append("\n");
                }
            }
            scanner.close();
        } catch (FileNotFoundException exception) {
            result.append("Note: Tasks ").append(filePath).append(" not found. Starting from scratch...");
        } catch (Exception exception) {
            assert false : "Unexpected error when loading tasks from " + filePath + ".";
        }
        return result.toString();
    }


    /**
     * Processes a command string and updates the task list accordingly.
     *
     * @param commandString The command string input by the user.
     * @return A response message indicating the result of the command.
     */
    public String processCommand(String commandString) {
        return new Command(commandString).execute(this);
    }
}
