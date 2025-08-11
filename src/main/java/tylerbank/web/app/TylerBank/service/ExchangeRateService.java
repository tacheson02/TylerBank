package tylerbank.web.app.TylerBank.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Class to fetch and manage exchange rate data from the Currency API
 * @since v2.3
 */
@Component
@RequiredArgsConstructor
@Getter
public class ExchangeRateService {

    private final RestTemplate restTemplate;

    private Map<String, Double> rates = new HashMap<>();

    private final Set<String> CURRENCIES = Set.of(
            "USD",
            "EUR",
            "GBP",
            "JPY",
            "INR",
            "CAD",
            "CNY"
    ) ;

    @Value("${currencyApi.apiKey}")
    private String apiKey;

    /**
     * Fetch and store the current exchange rate for the supported currencies
     * @since v2.3
     */
    public void getExchangeRate() {
        // Construct URL to fetch exchange rate data from Currency API
        String CURRENCY_API = "https://api.currencyapi.com/v3/latest?apikey=";
        var response = restTemplate.getForEntity(CURRENCY_API + apiKey, JsonNode.class);
        var data = Objects.requireNonNull(response.getBody()).get("data");

        // Iterate through currencies this app supports and store matching rate in the rates map
        for (var currency : CURRENCIES) {
            rates.put(currency, data.get(currency).get("value").doubleValue());
        }
    }
}
