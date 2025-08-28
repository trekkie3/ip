import task.Deadline;
import task.Event;
import task.Task;
import task.Todo;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Iris {
    static final String SEPARATOR = "-------------------------------------";

    static void preamble() {
        System.out.println("Hello! I'm Iris.");
        System.out.println("What can I do for you?");
        System.out.println(SEPARATOR);
    }

    static void farewell() {
        System.out.println("Bye, see you soon!");
    }


    /**
     * Saves taskList into data.txt.
     *
     * @param filePath Path to save tasks
     * @param taskList List of tasks
     */
    static void save(String filePath, List<Task> taskList) {
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(filePath));
            for (Task task : taskList) {
                writer.println(task.serialize());
            }
            writer.close();
        } catch (IOException exception) {
            System.err.println("Error: Failed to write tasks to " + filePath + ".");
        }
    }


    /**
     * Loads taskList from the specified filePath.
     *
     * @param filePath Path to load tasks
     * @return loaded task list
     */
    static List<Task> load(String filePath) {
        List<Task> taskList = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(new File(filePath));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                Task task = Task.deserialize(line);
                if (task != null) {
                    taskList.add(task);
                    System.out.println("Loaded task: " + task);
                } else {
                    System.err.println("Error: Malformed task line, ignoring: " + line);
                }
            }
            scanner.close();
        } catch (FileNotFoundException exception) {
            System.out.println("Note: Tasks " + filePath + " not found. Starting from scratch...");
        }
        return taskList;
    }


    private static void printUsageHint(String command, String usage) {
        System.err.printf("Incorrect usage of the \"%s\" command.\n", command);
        System.err.printf("Usage: %s\n", usage);
    }


    public static void main(String[] args) {
        Scanner reader = new Scanner(System.in);
        String filePath = "data.txt";
        List<Task> taskList = load(filePath);

        preamble();

        MAIN:
        while (true) {
            Command command = new Command(reader.nextLine());

            switch (command.type) {
            case ADD_TODO -> {
                try {
                    Task task = Todo.generateTodo(command.maybeArgument);
                    taskList.add(task);
                    System.out.println("Added new task:");
                    System.out.println(task);
                } catch (Exception exception) {
                    printUsageHint("todo", "todo <description>");
                }
            }
            case ADD_EVENT -> {
                try {
                    Task task = Event.generateEvent(command.maybeArgument);
                    taskList.add(task);
                    System.out.println("Added new task:");
                    System.out.println(task);
                } catch (Exception exception) {
                    printUsageHint("event", "event <description> /from <date> /to <date>");
                }
            }
            case ADD_DEADLINE -> {
                try {
                    Task task = Deadline.generateDeadline(command.maybeArgument);
                    taskList.add(task);
                    System.out.println("Added new task:");
                    System.out.println(task);
                } catch (Exception exception) {
                    printUsageHint("deadline", "deadline <description> /by <date>");
                }
            }
            case FIND -> {
                String keyword = command.maybeArgument;
                System.out.println("Here are the matching tasks in your list:");
                for (int i = 0; i < taskList.size(); i++) {
                    Task task = taskList.get(i);
                    if (task.getDescription().contains(keyword)) {
                        System.out.printf("%d: %s\n", i + 1, task);
                    }
                }
            }
            case DELETE -> {
                try {
                    int listNumber = Integer.parseInt(command.maybeArgument);
                    Task removed = taskList.remove(listNumber - 1);
                    System.out.println("I've deleted this task:");
                    System.out.println(removed);
                    System.out.printf("You have %d tasks left.\n", taskList.size());
                } catch (Exception exception) {
                    printUsageHint("delete", "delete <item-number>");
                }
            }
            case LIST -> {
                System.out.println("Here are your tasks:");
                for (int i = 0; i < taskList.size(); i++) {
                    System.out.printf("%d: %s\n", i + 1, taskList.get(i));
                }
            }
            case MARK -> {
                try {
                    int listNumber = Integer.parseInt(command.maybeArgument);
                    taskList.get(listNumber - 1).setDone(true);
                    System.out.println("I've marked this task as done:");
                    System.out.println(taskList.get(listNumber - 1));
                } catch (Exception exception) {
                    printUsageHint("mark", "mark <item-number>");
                }
            }
            case UNMARK -> {
                try {
                    int listNumber = Integer.parseInt(command.maybeArgument);
                    taskList.get(listNumber - 1).setDone(false);
                    System.out.println("I've marked this task to be completed:");
                    System.out.println(taskList.get(listNumber - 1));
                } catch (Exception exception) {
                    printUsageHint("unmark", "unmark <item-number>");
                }
            }
            case BYE -> {
                break MAIN;
            }
            case INVALID -> System.err.println("Please input a valid command.");
            }

            System.out.println(SEPARATOR);
        }

        save(filePath, taskList);
        farewell();
    }
}
