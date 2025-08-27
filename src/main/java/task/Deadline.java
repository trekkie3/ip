package task;

public class Deadline extends Task {
    final String deadline;

    public Deadline(String description, String deadline) {
        super(description);
        this.deadline = deadline;
    }

    public String toString() {
        return String.format(
                "[D]%s (by: %s)",
                super.toString(),
                this.deadline
        );
    }

    public static Deadline generateDeadline(String argument) throws TaskException {
        if (argument == null) {
            throw new TaskException(TaskExceptionType.NO_ARGUMENTS_PROVIDED);
        }
        String description = null;
        String by = null;
        String[] split = argument.split(
                makeSplitRegex(new String[]{"/by"})
        );
        String capturing = "description";
        for (String items : split) {
            items = items.trim();
            if (capturing != null) {
                if (capturing.equals("description")) {
                    description = items;
                } else if (capturing.equals("by")) {
                    by = items;
                }
                capturing = null;
            } else {
                if (items.equals("/by")) {
                    capturing = "by";
                } else {
                    throw new TaskException(TaskExceptionType.UNRECOGNIZED_ARGUMENT);
                }
            }
        }
        if (description == null || by == null) {
            throw new TaskException(TaskExceptionType.ARGUMENTS_MISSING);
        }
        return new Deadline(description, by);
    }

    @Override
    public String serialize() {
        return String.format("DEADLINE|%b|%s|%s", isDone, description, deadline);
    }
}
