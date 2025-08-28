package task;

public class TaskException extends Exception {
    final TaskExceptionType exceptionType;

    TaskException(TaskExceptionType exceptionType) {
        this.exceptionType = exceptionType;
    }
}
