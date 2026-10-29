package mx.utch.dwi.gateway.web;

import java.time.Instant;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/health")
    public Map<String, Object> health() {
        return Map.of(
                "service", "ecommerce-gateway",
                "status", "UP",
                "stack", "Spring Boot",
                "timestamp", Instant.now().toString()
        );
    }
}
