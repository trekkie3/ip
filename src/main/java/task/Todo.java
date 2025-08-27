package task;

public class Todo extends Task {
    public Todo(String description) {
        super(description);
    }

    public String toString() {
        return String.format("[T]%s", super.toString());
    }

    public static Todo generateTodo(String argument) throws TaskException {
        if (argument == null) {
            throw new TaskException(TaskExceptionType.NO_ARGUMENTS_PROVIDED);
        }
        return new Todo(argument);
    }

    @Override
    public String serialize() {
        return String.format("TODO|%b|%s", isDone, description);
    }
}
