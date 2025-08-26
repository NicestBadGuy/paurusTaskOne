package si.paurus.assignment.taskone.service.validation;

import si.paurus.assignment.taskone.exception.ValidationException;

public enum ValidationExceptionReason {
    PRECISION_TOO_HIGH("VE-0001", "Precision should be max two."),
    TRADER_VALIDATION_FAILED("VE-0002", "Trader validation failed."),
    BET_CALCULATION_FAILED("VE-0003", "Failed to calculate bet."),
    INVALID_ODD("VE-0004", "Odds have to be bigger than one."),
    ;

    private final String code;
    private final String message;

    ValidationExceptionReason(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public ValidationException toException() {
        return new ValidationException(code, message);
    }
}
