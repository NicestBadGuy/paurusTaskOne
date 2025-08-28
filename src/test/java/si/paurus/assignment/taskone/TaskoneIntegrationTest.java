package si.paurus.assignment.taskone;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import si.paurus.assignment.taskone.model.PossibleBetResultBO;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Happy-path integration tests (no validation errors).
 * <p>
 * Assumptions based on BetMapper:
 * - possibleReturnAmount = played + winnings = played * odd
 * - possibleReturnAmountBefTax = same as above
 * - After-tax:
 * GENERAL: possible - max(rateTax(base=possible), amount)
 * WINNINGS: played + (winnings - max(rateTax(base=winnings), amount)) = possible - usedTax
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        // Trader 1 rules
        "tax.rules.1.GENERAL.RATE=10",
        "tax.rules.1.GENERAL.AMOUNT=2.00",
        "tax.rules.1.WINNINGS.RATE=10",
        "tax.rules.1.WINNINGS.AMOUNT=1.00"
})
class TaskoneIntegrationTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    @Test
    @DisplayName("GENERAL: returns 200 with correct amounts")
    void general_ok() throws Exception {
        String body = """
                  { "traderId": 1, "playedAmount": 5.00, "odd": 3.2 }
                """;

        var json = mvc.perform(post("/api/v1/taxation/bet")
                                       .param("taxationType", "GENERAL")
                                       .contentType(MediaType.APPLICATION_JSON)
                                       .content(body))
                      .andExpect(status().isOk())
                      .andReturn()
                      .getResponse()
                      .getContentAsString();

        var result = mapper.readValue(json, PossibleBetResultBO.class);

        // Now you can assert on BigDecimal values directly
        assertEquals(new BigDecimal("16.00"), result.getPossibleReturnAmount());
        assertEquals(new BigDecimal("16.00"), result.getPossibleReturnAmountBefTax());
        assertEquals(new BigDecimal("14.00"), result.getPossibleReturnAmountAfterTax());
        assertEquals(new BigDecimal("10"), result.getTaxRate());
        assertEquals(new BigDecimal("2.00"), result.getTaxAmount());
    }

    @Test
    @DisplayName("WINNINGS: returns 200 with correct amounts")
    void winnings_ok() throws Exception {
        String body = """
                  { "traderId": 1, "playedAmount": 5.00, "odd": 3.2 }
                """;

        var json = mvc.perform(post("/api/v1/taxation/bet")
                                       .param("taxationType", "WINNINGS")
                                       .contentType(MediaType.APPLICATION_JSON)
                                       .content(body))
                      .andExpect(status().isOk())
                      .andReturn()
                      .getResponse()
                      .getContentAsString();

        PossibleBetResultBO result = mapper.readValue(json, PossibleBetResultBO.class);

        // Now you can assert on BigDecimal values directly
        assertEquals(new BigDecimal("16.00"), result.getPossibleReturnAmount());
        assertEquals(new BigDecimal("16.00"), result.getPossibleReturnAmountBefTax());
        assertEquals(new BigDecimal("14.90"), result.getPossibleReturnAmountAfterTax());
        assertEquals(new BigDecimal("10"), result.getTaxRate());
        assertEquals(new BigDecimal("1.00"), result.getTaxAmount());
    }
}
