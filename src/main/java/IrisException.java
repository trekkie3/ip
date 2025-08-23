enum IrisExceptionType {
    NO_ARGUMENTS_PROVIDED,
    ARGUMENTS_MISSING,
    UNRECOGNIZED_ARGUMENT,
}

public class IrisException extends Exception {
    final IrisExceptionType exceptionType;
    IrisException(IrisExceptionType exceptionType) {
        this.exceptionType = exceptionType;
    }
}
