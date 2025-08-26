package si.paurus.assignment.taskone.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class NewBetBO {
    @Schema(description = "Trader ID", example = "1")

    @NotNull(message = "Trader ID is required")
    private Long traderId;

    @NotNull(message = "Played amount must not be null")
    @Positive(message = "Played amount must be greater than 0")
    private BigDecimal playedAmount;
    /**
     * TODO: I find this odd (pun intended), that incoming request would define odds. Would think that odds are read and set by something else.
     */
    @NotNull(message = "Odd must not be null")
    @DecimalMin(value = "1", inclusive = false, message = "Odd must be greater than 1")
    private BigDecimal odd;
}
