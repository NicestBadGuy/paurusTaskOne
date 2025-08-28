package si.paurus.assignment.taskone.util;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoneyUtilTest {

    @ParameterizedTest
    @CsvSource({
            // played, odd, expectedWinnings
            "5.00, 3.2, 11.00",
            "10.00, 1.0, 0.00",
            "10.00, 1.333, 3.33",
            // This doesn't make sense in rl, who would play and pay that?
            "1000000.00, 1.07, 70000.00"
    })
    void testWinningsCases(String played, String odd, String expected) {
        var res = MoneyUtil.calculateWinnings(new BigDecimal(played), new BigDecimal(odd));
        assertEquals(new BigDecimal(expected), res);
    }

    @ParameterizedTest
    @CsvSource({
            // base, rate%, expectedTax
            "11.00, 10, 1.10",
            "123.45, 0, 0.00",
            "9.99, 100, 9.99",
            "1.00, 2.5, 0.03",
            "7.777, 12, 0.93"
    })
    void testRateTaxCases(String base, String rate, String expected) {
        var res = MoneyUtil.calculateRateTax(new BigDecimal(base), new BigDecimal(rate));
        assertEquals(new BigDecimal(expected), res);
    }
}
