package si.paurus.assignment.taskone.service.mapping;

import org.springframework.stereotype.Component;
import si.paurus.assignment.taskone.model.NewBetBO;
import si.paurus.assignment.taskone.model.PossibleBetResultBO;
import si.paurus.assignment.taskone.model.TaxationModeE;
import si.paurus.assignment.taskone.model.TaxationTypeE;
import si.paurus.assignment.taskone.service.rule.TaxRule;

import java.math.BigDecimal;
import java.util.Map;

import static si.paurus.assignment.taskone.util.MoneyUtil.calculateRateTax;
import static si.paurus.assignment.taskone.util.MoneyUtil.calculateWinnings;

@Component
public class BetMapper {
    private final TaxRule taxRule;


    public BetMapper(TaxRule taxRule) {
        this.taxRule = taxRule;
    }

    /**
     * This is where the crucial magic happens. Tax rate and amount for both GENERAL and WINNINGS are determined by properties and are read to config
     * file. If there was a link to task/confluence/source of knowledge that determines this implementation it would be put <a href="...">here</a>.
     *
     * @param newBetBO
     * @param taxationType
     * @return
     */
    public PossibleBetResultBO map(NewBetBO newBetBO, TaxationTypeE taxationType) {
        var taxationParameters = taxRule.findParameters(newBetBO.getTraderId(), taxationType);

        var result = new PossibleBetResultBO();
        // TODO: as stated in readme I can't deduce difference between possibleReturnAmount and possibleReturnAmountBefTax
        result.setPossibleReturnAmount(calcPossibleReturnAmount(newBetBO.getPlayedAmount(), newBetBO.getOdd()));
        result.setPossibleReturnAmountBefTax(calcPossibleReturnAmountBefTax(newBetBO.getPlayedAmount(), newBetBO.getOdd()));
        result.setPossibleReturnAmountAfterTax(calcPossibleReturnAmountAfterTax(
                newBetBO.getPlayedAmount(),
                newBetBO.getOdd(),
                taxationType,
                taxationParameters));
        result.setTaxRate(taxationParameters.get(TaxationModeE.RATE));
        result.setTaxAmount(taxationParameters.get(TaxationModeE.AMOUNT));
        return result;
    }

    private BigDecimal calcPossibleReturnAmount(BigDecimal playedAmount, BigDecimal odd) {
        return playedAmount.add(calculateWinnings(playedAmount, odd));
    }

    private BigDecimal calcPossibleReturnAmountBefTax(BigDecimal playedAmount, BigDecimal odd) {
        return playedAmount.add(calculateWinnings(playedAmount, odd));

    }

    private BigDecimal calcPossibleReturnAmountAfterTax(
            BigDecimal playedAmount,
            BigDecimal odd,
            TaxationTypeE taxationType,
            Map<TaxationModeE, BigDecimal> taxationParameters) {
        var taxationBase = switch (taxationType) {
            case GENERAL -> playedAmount.add(calculateWinnings(playedAmount, odd));
            case WINNINGS -> calculateWinnings(playedAmount, odd);
        };

        var rate = taxationParameters.get(TaxationModeE.RATE);
        var amount = taxationParameters.get(TaxationModeE.AMOUNT);

        var rateTax = calculateRateTax(taxationBase, rate);

        // TODO: I'm gonna assume that bigger taxation must be chosen.
        var usedTax = rateTax.compareTo(amount) > 0 ? rateTax : amount;
        var finalCalculation = taxationBase.subtract(usedTax);
        return TaxationTypeE.WINNINGS.equals(taxationType) ? playedAmount.add(finalCalculation) : finalCalculation;
    }
}
