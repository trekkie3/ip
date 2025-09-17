package iris.task;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Represents a task that should be done after a specific date.
 */
public class DoAfter extends Task {
    private final LocalDate after;

    /**
     * Constructs a DoAfter task.
     *
     * @param description Description of the task
     * @param after       The date after which the task should be done
     */
    public DoAfter(String description, LocalDate after) {
        super(description);
        this.after = after;
    }

    @Override
    public String toString() {
        return String.format(
                "[A]%s (after: %s)",
                super.toString(),
                after.format(DATE_STORAGE)
        );
    }

    /**
     * Generates a DoAfter task from the given argument string.
     *
     * @param argument string containing details
     * @return DoAfter task
     * @throws TaskException on invalid argument
     */
    public static DoAfter generateDoAfter(String argument) throws TaskException {
        if (argument == null) {
            throw new TaskException(TaskExceptionType.NO_ARGUMENTS_PROVIDED);
        }
        String description = null;
        String afterStr = null;
        String[] split = argument.split(
                makeSplitRegex(new String[]{FLAG_AFTER})
        );
        String capturing = "description";
        for (String item : split) {
            item = item.trim();
            if (capturing != null) {
                if (capturing.equals("description")) {
                    description = item;
                } else if (capturing.equals("after")) {
                    afterStr = item;
                }
                capturing = null;
            } else {
                if (item.equals(FLAG_AFTER)) {
                    capturing = "after";
                } else {
                    throw new TaskException(TaskExceptionType.UNRECOGNIZED_ARGUMENT);
                }
            }
        }
        if (description == null || afterStr == null) {
            throw new TaskException(TaskExceptionType.ARGUMENTS_MISSING);
        }
        try {
            LocalDate afterDate = LocalDate.parse(afterStr, DATE_INPUT);
            return new DoAfter(description, afterDate);
        } catch (DateTimeParseException exception) {
            throw new TaskException(TaskExceptionType.INVALID_DATE_FORMAT);
        }
    }

    /**
     * Serializes the DoAfter task into a string for storage.
     *
     * @return Serialized string
     */
    @Override
    public String serialize() {
        return String.format("DOAFTER|%b|%s|%s", isDone, description, after.format(DATE_STORAGE));
    }
}
