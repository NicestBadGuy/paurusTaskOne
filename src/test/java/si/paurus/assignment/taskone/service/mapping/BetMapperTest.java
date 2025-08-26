package si.paurus.assignment.taskone.service.mapping;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import si.paurus.assignment.taskone.model.NewBetBO;
import si.paurus.assignment.taskone.model.PossibleBetResultBO;
import si.paurus.assignment.taskone.model.TaxationModeE;
import si.paurus.assignment.taskone.model.TaxationTypeE;
import si.paurus.assignment.taskone.service.rule.TaxRule;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class BetMapperTest {


    private TaxRule taxRule;
    private BetMapper mapper;

    @BeforeEach
    void setUp() {
        taxRule = Mockito.mock(TaxRule.class);
        mapper = new BetMapper(taxRule);
    }

    @Test
    void testCalculationCaseTaxationTypeGeneral() {
        var bet = bet(1L, "5.00", "3.2");

        Map<TaxationModeE, BigDecimal> params = new EnumMap<>(TaxationModeE.class);
        params.put(TaxationModeE.RATE, new BigDecimal("10"));   // percent
        params.put(TaxationModeE.AMOUNT, new BigDecimal("1.00")); // flat EUR

        when(taxRule.findParameters(eq(1L), eq(TaxationTypeE.GENERAL))).thenReturn(params);

        PossibleBetResultBO res = mapper.map(bet, TaxationTypeE.GENERAL);

        assertBigDecimal("16.00", res.getPossibleReturnAmount());
        assertBigDecimal("16.00", res.getPossibleReturnAmountBefTax());
        assertBigDecimal("14.40", res.getPossibleReturnAmountAfterTax()); // 16.00 - 1.60

        assertBigDecimal("10", res.getTaxRate());
        assertBigDecimal("1.00", res.getTaxAmount());
    }

    @Test
    void testCalculationTaxationTypeWinnings() {
        var bet = bet(2L, "5.00", "3.2");

        Map<TaxationModeE, BigDecimal> params = new EnumMap<>(TaxationModeE.class);
        params.put(TaxationModeE.RATE, new BigDecimal("5"));
        params.put(TaxationModeE.AMOUNT, new BigDecimal("1.50"));

        when(taxRule.findParameters(eq(2L), eq(TaxationTypeE.WINNINGS))).thenReturn(params);

        PossibleBetResultBO res = mapper.map(bet, TaxationTypeE.WINNINGS);

        assertBigDecimal("16.00", res.getPossibleReturnAmount());
        assertBigDecimal("16.00", res.getPossibleReturnAmountBefTax());
        assertBigDecimal("14.50", res.getPossibleReturnAmountAfterTax());

        // Winnnings are 11. 5% of 11 is 0.55, 1.5 is used since its bigger. 5 + 9.5

        assertBigDecimal("5", res.getTaxRate());
        assertBigDecimal("1.50", res.getTaxAmount());
    }

    @Test
    void testCalculationCaseTaxationTypeGeneralUseRate() {
        var bet = bet(3L, "5.00", "3.2");

        Map<TaxationModeE, BigDecimal> params = new EnumMap<>(TaxationModeE.class);
        params.put(TaxationModeE.RATE, new BigDecimal("50"));
        params.put(TaxationModeE.AMOUNT, new BigDecimal("1.40"));

        when(taxRule.findParameters(eq(3L), eq(TaxationTypeE.GENERAL))).thenReturn(params);

        var res = mapper.map(bet, TaxationTypeE.GENERAL);

        assertBigDecimal("16.00", res.getPossibleReturnAmount());
        assertBigDecimal("8.00", res.getPossibleReturnAmountAfterTax()); // 50%
    }

    @Test
    void testCalculationCaseTaxationTypeWinningsUseRate() {
        var bet = bet(4L, "5.00", "3.2");
        Map<TaxationModeE, BigDecimal> params = new EnumMap<>(TaxationModeE.class);
        params.put(TaxationModeE.RATE, new BigDecimal("10"));  // 10% of 11.00 = 1.10
        params.put(TaxationModeE.AMOUNT, new BigDecimal("0.60"));

        when(taxRule.findParameters(eq(4L), eq(TaxationTypeE.WINNINGS))).thenReturn(params);

        var res = mapper.map(bet, TaxationTypeE.WINNINGS);

        // 9.90 + 5
        assertBigDecimal("14.90", res.getPossibleReturnAmountAfterTax());
    }

    // Helpers bellow

    private static NewBetBO bet(long traderId, String played, String odd) {
        // Minimal stub assuming simple POJO with setters; adapt if you have a builder/record
        NewBetBO bo = new NewBetBO();
        bo.setTraderId(traderId);
        bo.setPlayedAmount(new BigDecimal(played));
        bo.setOdd(new BigDecimal(odd));
        return bo;
    }

    private static void assertBigDecimal(String expected, BigDecimal actual) {
        assertEquals(new BigDecimal(expected), actual, "Expected " + expected + " but was " + actual);
    }

}
