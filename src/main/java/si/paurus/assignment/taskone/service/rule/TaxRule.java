package si.paurus.assignment.taskone.service.rule;

import org.springframework.stereotype.Component;
import si.paurus.assignment.taskone.config.TaxationRuleConfiguration;
import si.paurus.assignment.taskone.model.TaxationModeE;
import si.paurus.assignment.taskone.model.TaxationTypeE;

import java.math.BigDecimal;
import java.util.Map;

@Component
public class TaxRule {

    private final TaxationRuleConfiguration props;

    public TaxRule(TaxationRuleConfiguration props) {
        this.props = props;
    }

    public Map<TaxationModeE, BigDecimal> findParameters(long traderId, TaxationTypeE type) {
        return
                props.getRules()
                     .getOrDefault(traderId, Map.of())
                     .getOrDefault(type, Map.of());
    }
}
