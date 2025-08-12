package tylerbank.web.app.TylerBank.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) for Cards
 * Allows API to use Transfer objects instead of database entities
 * @since 2.4
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CardDto {
    private double amount;
}
