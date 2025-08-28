
package task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Deadline extends Task {
    final LocalDate deadline;
    private static final DateTimeFormatter PARSER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MMM d yyyy");

    public Deadline(String description, LocalDate deadline) {
        super(description);
        this.deadline = deadline;
    }

    public String toString() {
        return String.format(
                "[D]%s (by: %s)",
                super.toString(),
                deadline.format(FORMATTER)
        );
    }

    /**
     * Generates a Deadline task from the given argument string.
     *
     * @param argument string containing details
     * @return Deadline task
     * @throws TaskException on invalid argument
     */
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
        try {
            LocalDate deadlineDate = LocalDate.parse(by, PARSER);
            return new Deadline(description, deadlineDate);
        } catch (Exception exception) {
            throw new TaskException(TaskExceptionType.INVALID_DATE_FORMAT);
        }
    }

    /**
     * Serializes the  task into a string for storage.
     *
     * @return Serialized string
     */
    @Override
    public String serialize() {
        return String.format("DEADLINE|%b|%s|%s", isDone, description, deadline.format(FORMATTER));
    }
}
