package si.paurus.assignment.taskone.rest;

import org.springframework.web.bind.annotation.*;
import si.paurus.assignment.taskone.model.NewBetBO;
import si.paurus.assignment.taskone.model.PossibleBetResultBO;
import si.paurus.assignment.taskone.model.TaxationTypeE;
import si.paurus.assignment.taskone.service.TaxationService;

@RestController
@RequestMapping("/api/v1/taxation")
public class TaxationRestService extends ThrowableRestService implements TaxationRestServiceClient {

    private final TaxationService taxationService;

    public TaxationRestService(TaxationService taxationService) {
        this.taxationService = taxationService;
    }

    @Override
    @PostMapping("/bet")
    public PossibleBetResultBO createBet(
            /*@Valid*/ @RequestBody NewBetBO newBetBO,
            @RequestParam TaxationTypeE taxationType) {
        return taxationService.createBet(newBetBO, taxationType);
    }
}
