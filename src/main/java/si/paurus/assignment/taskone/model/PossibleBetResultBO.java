package si.paurus.assignment.taskone.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PossibleBetResultBO {
    private BigDecimal possibleReturnAmount;
    private BigDecimal possibleReturnAmountBefTax;
    private BigDecimal possibleReturnAmountAfterTax;
    private BigDecimal taxRate;
    private BigDecimal taxAmount;
}
