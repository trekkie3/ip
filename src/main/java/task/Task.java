package task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public abstract class Task {
    protected String description;
    protected boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public void setDone(boolean isDone) {
        this.isDone = isDone;
    }

    public String toString() {
        return String.format("[%c] %s", this.isDone ? 'X' : ' ', this.description);
    }

    static String makeSplitRegex(String[] keywords) {
        StringBuilder result = new StringBuilder();
        for (String keyword : keywords) {
            result.append(String.format("(?<=%s)|(?=%s)", keyword, keyword)).append("|");
        }
        result.deleteCharAt(result.length() - 1);
        return result.toString();
    }

    /**
     * Serializes this task.Task to a string for saving.
     */
    public String serialize() {
        // Should be overridden by subclasses
        return String.format("TASK|%b|%s", isDone, description);
    }

    /**
     * Deserializes a string to a task.Task object.
     * Returns null if the line is malformed.
     */
    public static Task deserialize(String line) {
        if (line == null || line.isEmpty()) return null;
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
                if (parts.length < 4) return null;
                // Use the same formatter as Deadline
                DateTimeFormatter deadlineFormatter = DateTimeFormatter.ofPattern("MMM d yyyy");
                Deadline deadline = new Deadline(description, LocalDate.parse(parts[3], deadlineFormatter));
                deadline.setDone(isDone);
                return deadline;
            case "EVENT":
                if (parts.length < 5) return null;
                // Use the same formatter as Event
                DateTimeFormatter eventFormatter = DateTimeFormatter.ofPattern("MMM d yyyy");
                Event event = new Event(description,
                        java.time.LocalDate.parse(parts[3], eventFormatter),
                        java.time.LocalDate.parse(parts[4], eventFormatter)
                );
                event.setDone(isDone);
                return event;
            default:
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }
}

