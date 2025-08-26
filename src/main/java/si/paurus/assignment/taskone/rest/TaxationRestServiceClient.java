package si.paurus.assignment.taskone.rest;

import io.swagger.v3.oas.annotations.Operation;
import si.paurus.assignment.taskone.model.NewBetBO;
import si.paurus.assignment.taskone.model.PossibleBetResultBO;
import si.paurus.assignment.taskone.model.TaxationTypeE;

public interface TaxationRestServiceClient {

    @Operation(summary = "Calculate possible return", description = "Applies trader-specific tax rules")
    PossibleBetResultBO createBet(NewBetBO newBetBO, TaxationTypeE taxationType);

}
