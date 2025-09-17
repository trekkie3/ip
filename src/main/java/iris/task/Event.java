package iris.task;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Represents an Event task with a description, start date, and end date.
 */
public class Event extends Task {
    private final LocalDate from;
    private final LocalDate to;

    // Helper to improve readability of parsing state
    private enum Capturing {
        DESCRIPTION, FROM, TO
    }

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
        String[] split = splitByFlags(argument);
        ParsedArgs args = parseArgs(split);
        LocalDate fromDate = parseDate(args.from);
        LocalDate toDate = parseDate(args.to);
        return new Event(args.description, fromDate, toDate);
    }

    // --- Helpers (SLAP) ---

    private static String[] splitByFlags(String argument) {
        return argument.split(makeSplitRegex(new String[]{FLAG_FROM, FLAG_TO}));
    }

    private static ParsedArgs parseArgs(String[] split) throws TaskException {
        String description = null;
        String from = null;
        String to = null;
        Capturing capturing = Capturing.DESCRIPTION;
        for (String raw : split) {
            String item = raw.trim();
            if (capturing != null) {
                switch (capturing) {
                case DESCRIPTION -> description = item;
                case FROM -> from = item;
                case TO -> to = item;
                default -> throw new TaskException(TaskExceptionType.UNRECOGNIZED_ARGUMENT);
                }
                capturing = null;
            } else {
                if (item.equals(FLAG_FROM)) {
                    capturing = Capturing.FROM;
                } else if (item.equals(FLAG_TO)) {
                    capturing = Capturing.TO;
                } else {
                    throw new TaskException(TaskExceptionType.UNRECOGNIZED_ARGUMENT);
                }
            }
        }
        if (description == null || from == null || to == null) {
            throw new TaskException(TaskExceptionType.ARGUMENTS_MISSING);
        }
        return new ParsedArgs(description, from, to);
    }

    private static LocalDate parseDate(String value) throws TaskException {
        try {
            return LocalDate.parse(value, DATE_INPUT);
        } catch (DateTimeParseException ex) {
            throw new TaskException(TaskExceptionType.INVALID_DATE_FORMAT);
        }
    }

    private record ParsedArgs(String description, String from, String to) {
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
