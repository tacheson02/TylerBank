package tylerbank.web.app.TylerBank.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) for Transfers
 * Allows API to use Transfer objects instead of database entities
 * @since 2.2
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransferDto {
    private long recipientAccountNumber;
    private double amount;
    private String code;
}
