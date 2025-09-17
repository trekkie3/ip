package iris.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

/**
 * Represents a Task with a description and completion status.
 */
public abstract class Task {
    // Shared date formatters
    public static final DateTimeFormatter DATE_INPUT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter DATE_STORAGE = DateTimeFormatter.ofPattern("MMM d yyyy");

    // Centralized flag tokens
    public static final String FLAG_BY = "/by";
    public static final String FLAG_FROM = "/from";
    public static final String FLAG_TO = "/to";
    public static final String FLAG_AFTER = "/after";

    protected final String description;
    protected boolean isDone;


    /**
     * Creates a new Task with the given description.
     *
     * @param description the description of the task
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDone(boolean isDone) {
        this.isDone = isDone;
    }

    public String toString() {
        return String.format("[%c] %s", this.isDone ? 'X' : ' ', this.description);
    }

    static String makeSplitRegex(String[] keywords) {
        if (keywords == null || keywords.length == 0) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        for (String keyword : keywords) {
            String quoted = Pattern.quote(keyword);
            result.append("(?<=").append(quoted).append(")|(?=").append(quoted).append(")").append("|");
        }
        if (!result.isEmpty()) {
            result.deleteCharAt(result.length() - 1);
        }
        return result.toString();
    }

    /**
     * Serializes the Task object to a string.
     *
     * @return the serialized string
     */
    public abstract String serialize();

    /**
     * Deserializes a string to a Task object.
     *
     * @param line the serialized string
     * @return the deserialized Task object, or null if deserialization fails
     */
    public static Task deserialize(String line) {
        if (line == null || line.isEmpty()) {
            return null;
        }
        String[] parts = line.split("\\|");
        if (parts.length < 3) {
            return null;
        }
        String type = parts[0];
        boolean isDone = Boolean.parseBoolean(parts[1]);
        String description = parts[2];
        return switch (type) {
        case "TODO" -> deserializeTodo(isDone, description);
        case "DEADLINE" -> deserializeDeadline(parts, isDone, description);
        case "EVENT" -> deserializeEvent(parts, isDone, description);
        case "DOAFTER" -> deserializeDoAfter(parts, isDone, description);
        default -> null;
        };
    }

    // --- Helpers (SLAP) ---

    private static Task deserializeTodo(boolean isDone, String description) {
        Todo todo = new Todo(description);
        todo.setDone(isDone);
        return todo;
    }

    private static Task deserializeDeadline(String[] parts, boolean isDone, String description) {
        if (parts.length < 4) {
            return null;
        }
        LocalDate by = parseStorageDate(parts[3]);
        if (by == null) {
            return null;
        }
        Deadline deadline = new Deadline(description, by);
        deadline.setDone(isDone);
        return deadline;
    }

    private static Task deserializeEvent(String[] parts, boolean isDone, String description) {
        if (parts.length < 5) {
            return null;
        }
        LocalDate from = parseStorageDate(parts[3]);
        LocalDate to = parseStorageDate(parts[4]);
        if (from == null || to == null) {
            return null;
        }
        Event event = new Event(description, from, to);
        event.setDone(isDone);
        return event;
    }

    private static Task deserializeDoAfter(String[] parts, boolean isDone, String description) {
        if (parts.length < 4) {
            return null;
        }
        LocalDate after = parseStorageDate(parts[3]);
        if (after == null) {
            return null;
        }
        DoAfter doAfter = new DoAfter(description, after);
        doAfter.setDone(isDone);
        return doAfter;
    }

    private static LocalDate parseStorageDate(String value) {
        try {
            return LocalDate.parse(value, DATE_STORAGE);
        } catch (Exception ex) {
            return null;
        }
    }
}

