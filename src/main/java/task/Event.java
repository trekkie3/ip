package task;

public class Event extends Task {
    final String from;
    final String to;

    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    public String toString() {
        return String.format(
                "[E]%s (from: %s to: %s)",
                super.toString(),
                this.from,
                this.to
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
        return new Event(description, from, to);
    }

    @Override
    public String serialize() {
        return String.format("EVENT|%b|%s|%s|%s", isDone, description, from, to);
    }
}
