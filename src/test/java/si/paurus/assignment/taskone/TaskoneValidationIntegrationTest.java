package si.paurus.assignment.taskone;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import si.paurus.assignment.taskone.model.TaxationTypeE;

import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests focused on validation flows:
 * - NewBetValidator (scale, nulls, odd > 1)
 * - TraderValidator (missing rules)
 * - PossibleBetResultValidator (negative results -> calculation failed)
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        // Trader 1: normal rules (so business logic can pass if input is valid)
        "tax.rules.1.GENERAL.RATE=10", "tax.rules.1.GENERAL.AMOUNT=2.00", "tax.rules.1.WINNINGS.RATE=10", "tax.rules.1.WINNINGS.AMOUNT=1.00",

        // Trader 2: WINNINGS amount set huge to force negative after-tax base for a tiny bet
        "tax.rules.2.WINNINGS.AMOUNT=999.00", "tax.rules.2.WINNINGS.RATE=0", "tax.rules.2.GENERAL.RATE=0", "tax.rules.2.GENERAL.AMOUNT=0.00"})
class TaskoneValidationIntegrationTest {

    @Autowired
    MockMvc mvc;

    @ParameterizedTest
    @CsvSource({"GENERAL", "WINNINGS"})
    @DisplayName("odd <= 1 -> 400 (INVALID_ODD)")
    void oddMustBeGreaterThanOne(TaxationTypeE type) throws Exception {
        String body = """
                  { "traderId": 1, "playedAmount": 5.00, "odd": 1.0 }
                """;

        mvc.perform(post("/api/v1/taxation/bet").contentType(MediaType.APPLICATION_JSON).content(body).param("taxationType", type.toString()))
           .andExpect(status().isBadRequest())
           .andExpect(content().string(anyOf(containsString("INVALID_ODD"), containsString("Odd"), containsString("greater than 1"))));
    }

    @ParameterizedTest
    @CsvSource({"GENERAL", "WINNINGS"})
    @DisplayName("playedAmount scale > 2 -> 400 (PRECISION_TOO_HIGH)")
    void playedAmountScaleTooHigh(TaxationTypeE type) throws Exception {
        String body = """
                  { "traderId": 1, "playedAmount": 5.001, "odd": 3.2 }
                """;

        mvc.perform(post("/api/v1/taxation/bet").contentType(MediaType.APPLICATION_JSON).content(body).param("taxationType", type.toString()))
           .andExpect(status().isBadRequest())
           .andExpect(content().string("VE-0001 - Precision should be max two."));
    }

    @ParameterizedTest
    @CsvSource({"GENERAL", "WINNINGS"})
    @DisplayName("missing traderId -> 400 (TRADER_VALIDATION_FAILED)")
    void missingTraderId(TaxationTypeE type) throws Exception {
        String body = """
                  { "playedAmount": 5.00, "odd": 3.2 }
                """;

        mvc.perform(post("/api/v1/taxation/bet").contentType(MediaType.APPLICATION_JSON).content(body).param("taxationType", type.toString()))
           .andExpect(status().isBadRequest())
           .andExpect(content().string(anyOf(containsString("TRADER_VALIDATION_FAILED"), containsString("Trader"))));
    }

    @ParameterizedTest
    @CsvSource({"GENERAL", "WINNINGS"})
    @DisplayName("unknown trader rules -> 400 (TRADER_VALIDATION_FAILED)")
    void unknownTraderRules(TaxationTypeE type) throws Exception {
        String body = """
                  { "traderId": 999, "playedAmount": 5.00, "odd": 3.2 }
                """;

        mvc.perform(post("/api/v1/taxation/bet").contentType(MediaType.APPLICATION_JSON).content(body).param("taxationType", type.toString()))
           .andExpect(status().isBadRequest())
           .andExpect(content().string("VE-0002 - Trader validation failed."));
    }

    @ParameterizedTest
    @CsvSource({"GENERAL", "WINNINGS"})
    @DisplayName("negative after-tax -> 400 (BET_CALCULATION_FAILED)")
    void negativeAfterTaxTriggersCalculationFailed(TaxationTypeE type) throws Exception {
        String body = """
                  { "traderId": 1, "playedAmount": 0.50, "odd": 1.10 }
                """;

        mvc.perform(post("/api/v1/taxation/bet?taxationType=GENERAL") // if you pass type via query/header adjust accordingly
                                                                      .contentType(MediaType.APPLICATION_JSON)
                                                                      .content(body)
                                                                      .param("taxationType", type.toString()))
           .andExpect(status().isBadRequest())
           .andExpect(content().string("VE-0003 - Failed to calculate bet."));
    }
}
