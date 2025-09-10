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
        try {
            String type = parts[0];
            boolean isDone = Boolean.parseBoolean(parts[1]);
            String description = parts[2];
            switch (type) {
            case "TODO":
                Todo todo = new Todo(description);
                todo.setDone(isDone);
                return todo;
            case "DEADLINE":
                if (parts.length < 4) {
                    return null;
                }
                Deadline deadline = new Deadline(description, LocalDate.parse(parts[3], DATE_STORAGE));
                deadline.setDone(isDone);
                return deadline;
            case "EVENT":
                if (parts.length < 5) {
                    return null;
                }
                Event event = new Event(description,
                        java.time.LocalDate.parse(parts[3], DATE_STORAGE),
                        java.time.LocalDate.parse(parts[4], DATE_STORAGE)
                );
                event.setDone(isDone);
                return event;
            case "DOAFTER":
                if (parts.length < 4) {
                    return null;
                }
                DoAfter doAfter = new DoAfter(description, LocalDate.parse(parts[3], DATE_STORAGE));
                doAfter.setDone(isDone);
                return doAfter;
            default:
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }
}

