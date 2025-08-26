package si.paurus.assignment.taskone.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import si.paurus.assignment.taskone.model.TaxationModeE;
import si.paurus.assignment.taskone.model.TaxationTypeE;

import java.math.BigDecimal;
import java.util.Map;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "tax")
public class TaxationRuleConfiguration {
    /**
     * Structure:
     * rules -> traderId (Long) -> type (GENERAL/WINNINGS) -> mode (RATE/AMOUNT) -> value (BigDecimal)
     */
    private Map<Long, Map<TaxationTypeE, Map<TaxationModeE, BigDecimal>>> rules;
}
