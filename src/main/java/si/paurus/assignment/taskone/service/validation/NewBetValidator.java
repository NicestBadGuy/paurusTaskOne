package si.paurus.assignment.taskone.service.validation;

import org.springframework.stereotype.Component;
import si.paurus.assignment.taskone.exception.ValidationException;
import si.paurus.assignment.taskone.model.NewBetBO;

import java.math.BigDecimal;
import java.util.Optional;

@Component
public class NewBetValidator {
    public Optional<ValidationException> validate(NewBetBO newBetBO) {
        if (newBetBO.getPlayedAmount().scale() > 2) {
            return Optional.of(ValidationExceptionReason.PRECISION_TOO_HIGH.toException());
        }
        if (newBetBO.getTraderId() == null) {
            return Optional.of(ValidationExceptionReason.TRADER_VALIDATION_FAILED.toException());
        }
        if (!(newBetBO.getOdd().compareTo(BigDecimal.ONE) > 0)) {
            return Optional.of(ValidationExceptionReason.INVALID_ODD.toException());
        }
        return Optional.empty();
    }


}
