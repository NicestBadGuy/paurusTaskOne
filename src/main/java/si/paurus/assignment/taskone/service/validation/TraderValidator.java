package si.paurus.assignment.taskone.service.validation;

import org.springframework.stereotype.Component;
import si.paurus.assignment.taskone.exception.ValidationException;
import si.paurus.assignment.taskone.model.TaxationTypeE;
import si.paurus.assignment.taskone.service.rule.TaxRule;

import java.util.Optional;

@Component
public class TraderValidator {

    private final TaxRule taxRule;

    public TraderValidator(TaxRule taxRule) {
        this.taxRule = taxRule;
    }

    public Optional<ValidationException> validate(Long traderId, TaxationTypeE taxationType) {
        var parameters = taxRule.findParameters(traderId, taxationType);
        return parameters.isEmpty() ? Optional.of(ValidationExceptionReason.TRADER_VALIDATION_FAILED.toException()) : Optional.empty();
    }

}
