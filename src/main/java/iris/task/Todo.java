package iris.task;

/**
 * Represents a Todo task with a description.
 */
public class Todo extends Task {
    public Todo(String description) {
        super(description);
    }

    public String toString() {
        return String.format("[T]%s", super.toString());
    }

    /**
     * Generates a Todo iris.task from the given argument string.
     *
     * @param argument string containing details
     * @return Todo iris.task
     * @throws TaskException on invalid argument
     */
    public static Todo generateTodo(String argument) throws TaskException {
        if (argument == null) {
            throw new TaskException(TaskExceptionType.NO_ARGUMENTS_PROVIDED);
        }
        String desc = argument.trim();
        if (desc.isEmpty()) {
            throw new TaskException(TaskExceptionType.ARGUMENTS_MISSING);
        }
        return new Todo(desc);
    }

    /**
     * Serializes the Todo iris.task into a string for storage.
     *
     * @return Serialized string
     */
    @Override
    public String serialize() {
        return String.format("TODO|%b|%s", isDone, description);
    }
}
