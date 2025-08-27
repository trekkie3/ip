
package task;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Event extends Task {
    final LocalDate from;
    final LocalDate to;
    private static final DateTimeFormatter PARSER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MMM d yyyy");

    public Event(String description, LocalDate from, LocalDate to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    public String toString() {
        return String.format(
                "[E]%s (from: %s to: %s)",
                super.toString(),
                from.format(FORMATTER),
                to.format(FORMATTER)
        );
    }

    public static Event generateEvent(String argument) throws TaskException {
        if (argument == null) {
            throw new TaskException(TaskExceptionType.NO_ARGUMENTS_PROVIDED);
        }
        String description = null;
        String from = null;
        String to = null;
        String[] split = argument.split(
                makeSplitRegex(new String[]{"/from", "/to"})
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
                if (item.equals("/from")) {
                    capturing = "from";
                } else if (item.equals("/to")) {
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
            LocalDate fromDate = LocalDate.parse(from, PARSER);
            LocalDate toDate = LocalDate.parse(to, PARSER);
            return new Event(description, fromDate, toDate);
        } catch (Exception exception) {
            throw new TaskException(TaskExceptionType.INVALID_DATE_FORMAT);
        }
    }

    @Override
    public String serialize() {
        return String.format("EVENT|%b|%s|%s|%s", isDone, description, from.format(FORMATTER), to.format(FORMATTER));
    }
}
