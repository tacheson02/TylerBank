package tylerbank.web.app.TylerBank.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) for currency conversion.
 * @since v2.3
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConversionDto {
    private String fromCurrency;
    private String toCurrency;
    private double amount;
}
