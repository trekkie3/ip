package iris;

import java.util.List;
import java.util.Map;

import iris.task.Deadline;
import iris.task.DoAfter;
import iris.task.Event;
import iris.task.Task;
import iris.task.Todo;

/**
 * Represents a command given by the user.
 */
public class Command {
    // Map of command keywords to their corresponding types to avoid magic strings
    private static final Map<String, CommandType> COMMANDS = Map.of(
            "list", CommandType.LIST,
            "bye", CommandType.BYE,
            "find", CommandType.FIND,
            "delete", CommandType.DELETE,
            "mark", CommandType.MARK,
            "unmark", CommandType.UNMARK,
            "todo", CommandType.ADD_TODO,
            "deadline", CommandType.ADD_DEADLINE,
            "event", CommandType.ADD_EVENT,
            "doafter", CommandType.ADD_DO_AFTER
    );

    private final CommandType type;
    private final String maybeArgument;

    /**
     * Creates a iris.Command object from a line of input.
     *
     * @param line input line
     */
    public Command(String line) {
        String[] split = line.split(" ", 2);
        String command = split[0];
        this.maybeArgument = split.length > 1 ? split[1] : null;
        this.type = COMMANDS.getOrDefault(command, CommandType.INVALID);
    }

    public String getMaybeArgument() {
        return maybeArgument;
    }

    /**
     * Executes the command on the given Iris instance.
     *
     * @param iris Iris instance
     * @return Result of command execution
     */
    public String execute(Iris iris) {
        String arg = getMaybeArgument();
        List<Task> taskList = iris.getTaskList();
        return switch (type) {
        case ADD_TODO -> handleAddTodo(taskList, arg);
        case ADD_EVENT -> handleAddEvent(taskList, arg);
        case ADD_DEADLINE -> handleAddDeadline(taskList, arg);
        case ADD_DO_AFTER -> handleAddDoAfter(taskList, arg);
        case FIND -> handleFind(taskList, arg);
        case DELETE -> handleDelete(taskList, arg);
        case LIST -> handleList(taskList);
        case MARK -> handleMark(taskList, arg);
        case UNMARK -> handleUnmark(taskList, arg);
        case BYE -> "Bye, see you soon!";
        case INVALID -> "Please input a valid command.";
        default -> "An unknown error occurred.";
        };
    }

    private static String getUsageHint(String command, String usage) {
        return String.format("Incorrect usage of the \"%s\" command.\n", command)
                + String.format("Usage: %s\n", usage);
    }

    private String handleAddTodo(List<Task> taskList, String arg) {
        try {
            Task task = Todo.generateTodo(arg);
            taskList.add(task);
            return "Added new iris.task:\n" + task;
        } catch (Exception exception) {
            return getUsageHint("todo", "todo <description>");
        }
    }

    private String handleAddEvent(List<Task> taskList, String arg) {
        try {
            Task task = Event.generateEvent(arg);
            taskList.add(task);
            return "Added new iris.task:\n" + task;
        } catch (Exception exception) {
            return getUsageHint("event", "event <description> /from <date> /to <date>");
        }
    }

    private String handleAddDeadline(List<Task> taskList, String arg) {
        try {
            Task task = Deadline.generateDeadline(arg);
            taskList.add(task);
            return "Added new iris.task:\n" + task;
        } catch (Exception exception) {
            return getUsageHint("deadline", "deadline <description> /by <date>");
        }
    }

    private String handleAddDoAfter(List<Task> taskList, String arg) {
        try {
            Task task = DoAfter.generateDoAfter(arg);
            taskList.add(task);
            return "Added new iris.task:\n" + task;
        } catch (Exception exception) {
            return getUsageHint("doafter", "doafter <description> /after <date>");
        }
    }

    private String handleFind(List<Task> taskList, String keyword) {
        StringBuilder result = new StringBuilder();
        result.append("Here are the matching tasks in your list:\n");
        for (int i = 0; i < taskList.size(); i++) {
            Task task = taskList.get(i);
            if (task.getDescription().contains(keyword)) {
                result.append(String.format("%d: %s\n", i + 1, task));
                result.append("\n");
            }
        }
        return result.toString();
    }

    private String handleDelete(List<Task> taskList, String arg) {
        try {
            int listNumber = Integer.parseInt(arg);
            Task removed = taskList.remove(listNumber - 1);
            return "I've deleted this iris.task:\n" + removed
                    + String.format("\nYou have %d tasks left.\n", taskList.size());
        } catch (Exception exception) {
            return getUsageHint("delete", "delete <item-number>");
        }
    }

    private String handleList(List<Task> taskList) {
        StringBuilder result = new StringBuilder();
        result.append("Here are your tasks:\n");
        for (int i = 0; i < taskList.size(); i++) {
            result.append(String.format("%d: %s\n", i + 1, taskList.get(i))).append("\n");
        }
        return result.toString();
    }

    private String handleMark(List<Task> taskList, String arg) {
        try {
            int listNumber = Integer.parseInt(arg);
            taskList.get(listNumber - 1).setDone(true);
            return "I've marked this iris.task as done:\n" + taskList.get(listNumber - 1);
        } catch (Exception exception) {
            return getUsageHint("mark", "mark <item-number>");
        }
    }

    private String handleUnmark(List<Task> taskList, String arg) {
        try {
            int listNumber = Integer.parseInt(arg);
            taskList.get(listNumber - 1).setDone(false);
            return "I've marked this iris.task to be completed:\n" + taskList.get(listNumber - 1);
        } catch (Exception exception) {
            return getUsageHint("unmark", "unmark <item-number>");
        }
    }
}
