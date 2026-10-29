package mx.utch.dwi.gateway.fx;

import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

@RestController
public class FxController {

    private final RestClient restClient;
    private final String frankfurterUrl;

    public FxController(RestClient restClient, @Value("${app.frankfurter-url}") String frankfurterUrl) {
        this.restClient = restClient;
        this.frankfurterUrl = frankfurterUrl;
    }

    @GetMapping("/microservices/fx/usd-mxn")
    public ResponseEntity<?> latestUsdMxn() {
        try {
            Map<String, Object> body = restClient.get()
                    .uri(frankfurterUrl)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});
            return ResponseEntity.ok(body);
        } catch (Exception ex) {
            return ResponseEntity.status(502).body(Map.of(
                    "error", "FX_UNAVAILABLE",
                    "source", "Frankfurter External API",
                    "message", ex.getMessage()
            ));
        }
    }
}
