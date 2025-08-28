package task;

/**
 * Custom exception class for task-related errors.
 */
public class TaskException extends Exception {
    final TaskExceptionType exceptionType;

    TaskException(TaskExceptionType exceptionType) {
        this.exceptionType = exceptionType;
    }
}
