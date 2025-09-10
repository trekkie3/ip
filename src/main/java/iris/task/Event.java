package iris.task;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Represents an Event task with a description, start date, and end date.
 */
public class Event extends Task {
    private final LocalDate from;
    private final LocalDate to;

    /**
     * Constructs an Event iris.task.
     *
     * @param description Description of the event
     * @param from        Start date of the event
     * @param to          End date of the event
     */
    public Event(String description, LocalDate from, LocalDate to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns a string representation of the Event iris.task.
     *
     * @return String representation
     */
    public String toString() {
        return String.format(
                "[E]%s (from: %s to: %s)",
                super.toString(),
                from.format(DATE_STORAGE),
                to.format(DATE_STORAGE)
        );
    }

    /**
     * Generates an Event iris.task from the given argument string.
     *
     * @param argument string containing details
     * @return Event iris.task
     * @throws TaskException on invalid argument
     */
    public static Event generateEvent(String argument) throws TaskException {
        if (argument == null) {
            throw new TaskException(TaskExceptionType.NO_ARGUMENTS_PROVIDED);
        }
        String description = null;
        String from = null;
        String to = null;
        String[] split = argument.split(
                makeSplitRegex(new String[]{FLAG_FROM, FLAG_TO})
        );
        String capturing = "description";
        for (String item : split) {
            item = item.trim();
            if (capturing != null) {
                if (capturing.equals("description")) {
                    description = item;
                } else if (capturing.equals("from")) {
                    from = item;
                } else if (capturing.equals("to")) {
                    to = item;
                }
                capturing = null;
            } else {
                if (item.equals(FLAG_FROM)) {
                    capturing = "from";
                } else if (item.equals(FLAG_TO)) {
                    capturing = "to";
                } else {
                    throw new TaskException(TaskExceptionType.UNRECOGNIZED_ARGUMENT);
                }
            }
        }
        if (description == null || from == null || to == null) {
            throw new TaskException(TaskExceptionType.ARGUMENTS_MISSING);
        }
        try {
            LocalDate fromDate = LocalDate.parse(from, DATE_INPUT);
            LocalDate toDate = LocalDate.parse(to, DATE_INPUT);
            return new Event(description, fromDate, toDate);
        } catch (DateTimeParseException exception) {
            throw new TaskException(TaskExceptionType.INVALID_DATE_FORMAT);
        }
    }

    /**
     * Serializes the Event iris.task into a string for storage.
     *
     * @return Serialized string
     */
    @Override
    public String serialize() {
        return String.format(
                "EVENT|%b|%s|%s|%s",
                isDone,
                description,
                from.format(DATE_STORAGE),
                to.format(DATE_STORAGE)
        );
    }
}
