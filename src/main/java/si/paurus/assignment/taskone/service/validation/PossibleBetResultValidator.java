package si.paurus.assignment.taskone.service.validation;

import org.springframework.stereotype.Component;
import si.paurus.assignment.taskone.exception.ValidationException;
import si.paurus.assignment.taskone.model.PossibleBetResultBO;

import java.math.BigDecimal;
import java.util.Optional;

@Component
public class PossibleBetResultValidator {
    public Optional<ValidationException> validate(PossibleBetResultBO possibleBetResultBO) {
        if (possibleBetResultBO.getPossibleReturnAmount().compareTo(BigDecimal.ZERO) < 0) {
            return Optional.of(ValidationExceptionReason.BET_CALCULATION_FAILED.toException());
        }
        if (possibleBetResultBO.getPossibleReturnAmountAfterTax().compareTo(BigDecimal.ZERO) < 0) {
            return Optional.of(ValidationExceptionReason.BET_CALCULATION_FAILED.toException());
        }
        return Optional.empty();
    }
}
