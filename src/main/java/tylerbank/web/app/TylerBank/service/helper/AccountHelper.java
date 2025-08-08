package tylerbank.web.app.TylerBank.service.helper;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tylerbank.web.app.TylerBank.repository.AccountRepository;

import java.util.Map;

/**
 * Helper class for managing accounts and currency conversion.
 * @since v2.1
 */
@Component
@RequiredArgsConstructor
@Getter
public class AccountHelper {

    private final AccountRepository accountRepository;

    private final Map<String, String> CURRENCIES = Map.of(
            "USD", "United States Dollar",
            "EUR", "Euro",
            "GBP", "British Pound",
            "JPY", "Japanese Yen",
            "INR", "Indian Rupee",
            "CAD", "Canadian Dollar",
            "CNY", "Chinese Yuan"
    );
}
