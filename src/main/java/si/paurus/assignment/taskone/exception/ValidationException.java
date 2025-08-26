package si.paurus.assignment.taskone.exception;

public class ValidationException extends RuntimeException {
    private final String code;
    private final String message;

    public ValidationException(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String toString() {
        return code + " - " + message;
    }
}
