package si.paurus.assignment.taskone.service;

import org.springframework.stereotype.Service;
import si.paurus.assignment.taskone.model.NewBetBO;
import si.paurus.assignment.taskone.model.PossibleBetResultBO;
import si.paurus.assignment.taskone.model.TaxationTypeE;
import si.paurus.assignment.taskone.service.mapping.BetMapper;
import si.paurus.assignment.taskone.service.validation.NewBetValidator;
import si.paurus.assignment.taskone.service.validation.PossibleBetResultValidator;
import si.paurus.assignment.taskone.service.validation.TraderValidator;

@Service
public class TaxationService {
    // Mappers
    private final BetMapper betMapper;
    // Validators
    private final NewBetValidator newBetValidator;
    private final PossibleBetResultValidator possibleBetResultValidator;
    private final TraderValidator traderValidator;

    public TaxationService(
            BetMapper betMapper,
            NewBetValidator newBetValidator,
            PossibleBetResultValidator possibleBetResultValidator,
            TraderValidator traderValidator) {
        this.betMapper = betMapper;
        this.newBetValidator = newBetValidator;
        this.possibleBetResultValidator = possibleBetResultValidator;
        this.traderValidator = traderValidator;
    }

    public PossibleBetResultBO createBet(NewBetBO newBetBO, TaxationTypeE taxationType) {
        newBetValidator.validate(newBetBO).ifPresent(ex -> {
            throw ex;
        });

        traderValidator.validate(newBetBO.getTraderId(), taxationType).ifPresent(ex -> {
            throw ex;
        });

        var result = betMapper.map(newBetBO, taxationType);

        possibleBetResultValidator.validate(result).ifPresent(ex -> {
            throw ex;
        });

        return result;
    }
}
