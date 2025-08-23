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
        for(String keyword : keywords) {
            result.append(String.format("(?<=%s)|(?=%s)", keyword, keyword)).append("|");
        }
        result.deleteCharAt(result.length() - 1);
        return result.toString();
    }

}

class Todo extends Task {
    public Todo(String description) {
        super(description);
    }

    public String toString() {
        return String.format("[T]%s", super.toString());
    }

    public static Todo generateTodo(String argument) throws Exception {
        if(argument == null) {
            throw new IrisException(IrisExceptionType.NO_ARGUMENTS_PROVIDED);
        }
        return new Todo(argument);
    }
}

class Deadline extends Task {
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

    static Deadline generateDeadline(String argument) throws Exception {
        if(argument == null) {
            throw new IrisException(IrisExceptionType.NO_ARGUMENTS_PROVIDED);
        }
        String description = null;
        String by = null;
        String[] split = argument.split(
                makeSplitRegex(new String[]{"/by"})
        );
        String capturing = "description";
        for(String items : split) {
            items = items.trim();
            if(capturing != null) {
                if(capturing.equals("description")) {
                    description = items;
                } else if(capturing.equals("by")) {
                    by = items;
                }
                capturing = null;
            } else {
                if(items.equals("/by")) {
                    capturing = "by";
                } else {
                    throw new IrisException(IrisExceptionType.UNRECOGNIZED_ARGUMENT);
                }
            }
        }
        if(description == null || by == null) {
            throw new IrisException(IrisExceptionType.ARGUMENTS_MISSING);
        }
        return new Deadline(description, by);
    }
}

class Event extends Task {
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

    static Event generateEvent(String argument) throws Exception {
        if(argument == null) {
            throw new IrisException(IrisExceptionType.NO_ARGUMENTS_PROVIDED);
        }
        String description = null;
        String from = null;
        String to = null;
        String[] split = argument.split(
            makeSplitRegex(new String[]{"/from", "/to"})
        );
        String capturing = "description";
        for(String item : split) {
            item = item.trim();
            if(capturing != null) {
                if(capturing.equals("description")) {
                    description = item;
                } else if(capturing.equals("from")) {
                    from = item;
                } else if(capturing.equals("to")) {
                    to = item;
                }
                capturing = null;
            } else {
                if(item.equals("/from")) {
                    capturing = "from";
                } else if(item.equals("/to")) {
                    capturing = "to";
                } else {
                    throw new IrisException(IrisExceptionType.UNRECOGNIZED_ARGUMENT);
                }
            }
        }
        if(description == null || from == null || to == null) {
            throw new IrisException(IrisExceptionType.ARGUMENTS_MISSING);
        }
        return new Event(description, from, to);
    }
}
