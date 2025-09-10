package iris;

import java.util.Map;

/**
 * Represents a command given by the user.
 */
public class Command {
    final CommandType type;
    private String maybeArgument = null;

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
        "event", CommandType.ADD_EVENT
    );

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
}
