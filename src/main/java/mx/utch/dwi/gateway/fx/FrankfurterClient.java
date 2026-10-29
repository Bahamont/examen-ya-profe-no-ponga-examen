package mx.utch.dwi.gateway.fx;

import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class FrankfurterClient {

    private final RestClient restClient;
    private final String frankfurterUrl;

    public FrankfurterClient(
            RestClient restClient,
            @Value("${app.frankfurter-url}") String frankfurterUrl
    ) {
        this.restClient = restClient;
        this.frankfurterUrl = frankfurterUrl;
    }

    public FrankfurterRate fetchUsdToMxn() {
        try {
            Map<String, Object> body = restClient.get()
                    .uri(frankfurterUrl)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});

            if (body == null || body.get("rates") == null) {
                return FrankfurterRate.failure("FX_EMPTY_BODY");
            }

            @SuppressWarnings("unchecked")
            Map<String, Object> rates = (Map<String, Object>) body.get("rates");
            Object mxn = rates.get("MXN");
            if (mxn == null) {
                return FrankfurterRate.failure("MXN_RATE_MISSING");
            }

            double rate = Double.parseDouble(mxn.toString());
            String date = body.get("date") != null ? body.get("date").toString() : null;
            return FrankfurterRate.success(rate, date);
        } catch (RestClientException ex) {
            return FrankfurterRate.failure(ex.getMessage());
        } catch (Exception ex) {
            return FrankfurterRate.failure(ex.getMessage());
        }
    }
}
