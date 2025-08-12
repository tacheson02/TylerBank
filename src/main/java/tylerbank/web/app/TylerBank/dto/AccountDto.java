package tylerbank.web.app.TylerBank.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) for Account
 * Allows API to use Account data without affecting the underlying Account model
 * @since 2.1
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto {
    private String code;
    private String label;
    private char symbol;
}
