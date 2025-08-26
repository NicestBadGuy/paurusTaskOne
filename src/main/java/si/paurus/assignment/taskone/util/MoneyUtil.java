package si.paurus.assignment.taskone.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MoneyUtil {

    public static BigDecimal calculateWinnings(BigDecimal playedAmount, BigDecimal odd) {
        return playedAmount.multiply(odd).subtract(playedAmount).setScale(2, RoundingMode.HALF_UP);
    }

    public static BigDecimal calculateRateTax(BigDecimal winnings, BigDecimal rate) {
        return winnings.multiply(rate).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }
}
