package iris;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import iris.task.Deadline;
import iris.task.Event;
import iris.task.Task;
import iris.task.Todo;

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


    private static String getUsageHint(String command, String usage) {
        return String.format("Incorrect usage of the \"%s\" command.\n", command) + String.format("Usage: %s\n", usage);
    }


    /**
     * Processes a command string and updates the task list accordingly.
     *
     * @param commandString The command string input by the user.
     * @return A response message indicating the result of the command.
     */
    @SuppressWarnings("checkstyle:Indentation")
    public String processCommand(String commandString) {
        Command command = new Command(commandString);
        String maybeArgument = command.getMaybeArgument();

        switch (command.type) {
        case ADD_TODO -> {
            try {
                Task task = Todo.generateTodo(maybeArgument);
                taskList.add(task);
                return ("Added new iris.task:\n" + task);
            } catch (Exception exception) {
                return getUsageHint("todo", "todo <description>");
            }
        }
        case ADD_EVENT -> {
            try {
                Task task = Event.generateEvent(maybeArgument);
                taskList.add(task);
                return ("Added new iris.task:\n" + task);
            } catch (Exception exception) {
                return getUsageHint("event", "event <description> /from <date> /to <date>");
            }
        }
        case ADD_DEADLINE -> {
            try {
                Task task = Deadline.generateDeadline(maybeArgument);
                taskList.add(task);
                return ("Added new iris.task:\n" + task);
            } catch (Exception exception) {
                return getUsageHint("deadline", "deadline <description> /by <date>");
            }
        }
        case FIND -> {
            StringBuilder result = new StringBuilder();
            result.append("Here are the matching tasks in your list:\n");
            for (int i = 0; i < taskList.size(); i++) {
                Task task = taskList.get(i);
                if (task.getDescription().contains(maybeArgument)) {
                    result.append(String.format("%d: %s\n", i + 1, task));
                    result.append("\n");
                }
            }
            return result.toString();
        }
        case DELETE -> {
            try {
                int listNumber = Integer.parseInt(maybeArgument);
                Task removed = taskList.remove(listNumber - 1);

                return (
                        "I've deleted this iris.task:\n"
                                + removed
                                + String.format("\nYou have %d tasks left.\n", taskList.size())
                    );
            } catch (Exception exception) {
                return getUsageHint("delete", "delete <item-number>");
            }
        }
        case LIST -> {
            StringBuilder result = new StringBuilder();
            result.append("Here are your tasks:\n");
            for (int i = 0; i < taskList.size(); i++) {
                result.append(String.format("%d: %s\n", i + 1, taskList.get(i))).append("\n");
            }
            return result.toString();
        }
        case MARK -> {
            try {
                int listNumber = Integer.parseInt(maybeArgument);
                taskList.get(listNumber - 1).setDone(true);

                return ("I've marked this iris.task as done:\n" + taskList.get(listNumber - 1));
            } catch (Exception exception) {
                return getUsageHint("mark", "mark <item-number>");
            }
        }
        case UNMARK -> {
            try {
                int listNumber = Integer.parseInt(maybeArgument);
                taskList.get(listNumber - 1).setDone(false);

                return ("I've marked this iris.task to be completed:\n" + taskList.get(listNumber - 1));
            } catch (Exception exception) {
                return getUsageHint("unmark", "unmark <item-number>");
            }
        }
        case BYE -> {
            return "Bye, see you soon!";
        }
        case INVALID -> {
            return ("Please input a valid command.");
        }
        default -> {
            return ("An unknown error occurred.");
        }
        }
    }

}
