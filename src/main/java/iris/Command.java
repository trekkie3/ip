package iris;

/**
 * Represents a command given by the user.
 */
public class Command {
    final CommandType type;
    private String maybeArgument = null;

    /**
     * Creates a iris.Command object from a line of input.
     *
     * @param line input line
     */
    public Command(String line) {
        switch (line) {
        case "list" -> this.type = CommandType.LIST;
        case "bye" -> this.type = CommandType.BYE;
        default -> {
            String[] split = line.split(" ", 2);
            String command = split[0];
            this.maybeArgument = split.length > 1 ? split[1] : null;
            switch (command) {
            case "find" -> this.type = CommandType.FIND;
            case "delete" -> this.type = CommandType.DELETE;
            case "mark" -> this.type = CommandType.MARK;
            case "unmark" -> this.type = CommandType.UNMARK;
            case "todo" -> this.type = CommandType.ADD_TODO;
            case "deadline" -> this.type = CommandType.ADD_DEADLINE;
            case "event" -> this.type = CommandType.ADD_EVENT;
            default -> this.type = CommandType.INVALID;
            }
        }
        }
    }

    public String getMaybeArgument() {
        return maybeArgument;
    }
}
