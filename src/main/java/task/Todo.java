package task;

public class Todo extends Task {
    public Todo(String description) {
        super(description);
    }

    public String toString() {
        return String.format("[T]%s", super.toString());
    }

    /**
     * Generates a Todo task from the given argument string.
     *
     * @param argument string containing details
     * @return Todo task
     * @throws TaskException on invalid argument
     */
    public static Todo generateTodo(String argument) throws TaskException {
        if (argument == null) {
            throw new TaskException(TaskExceptionType.NO_ARGUMENTS_PROVIDED);
        }
        return new Todo(argument);
    }

    /**
     * Serializes the Todo task into a string for storage.
     *
     * @return Serialized string
     */
    @Override
    public String serialize() {
        return String.format("TODO|%b|%s", isDone, description);
    }
}
