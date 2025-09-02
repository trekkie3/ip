package iris.task;

/**
 * Custom exception class for iris.task-related errors.
 */
public class TaskException extends Exception {
    final TaskExceptionType exceptionType;

    TaskException(TaskExceptionType exceptionType) {
        this.exceptionType = exceptionType;
    }
}
