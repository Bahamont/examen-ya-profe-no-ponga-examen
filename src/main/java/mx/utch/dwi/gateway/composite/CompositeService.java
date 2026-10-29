package mx.utch.dwi.gateway.composite;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import mx.utch.dwi.gateway.fx.FrankfurterClient;
import mx.utch.dwi.gateway.fx.FrankfurterRate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

@Service
public class CompositeService {

    private final RestClient restClient;
    private final FrankfurterClient frankfurterClient;
    private final String internalBaseUrl;
    private final Executor executor;

    public CompositeService(
            RestClient restClient,
            FrankfurterClient frankfurterClient,
            @Value("${app.internal-base-url}") String internalBaseUrl,
            Executor compositeExecutor
    ) {
        this.restClient = restClient;
        this.frankfurterClient = frankfurterClient;
        this.internalBaseUrl = internalBaseUrl;
        this.executor = compositeExecutor;
    }

    public ResponseEntity<ProductSummary> getSummary(String productId) {
        CompletableFuture<Map<String, Object>> productFuture = CompletableFuture.supplyAsync(
                () -> getJson(internalBaseUrl + "/microservices/products/" + productId),
                executor
        );
        CompletableFuture<Map<String, Object>> inventoryFuture = CompletableFuture.supplyAsync(
                () -> getJson(internalBaseUrl + "/microservices/inventory/" + productId),
                executor
        );
        CompletableFuture<FrankfurterRate> fxFuture = CompletableFuture.supplyAsync(
                frankfurterClient::fetchUsdToMxn,
                executor
        );

        CompletableFuture.allOf(productFuture, inventoryFuture, fxFuture).join();

        Map<String, Object> product = productFuture.join();
        Map<String, Object> inventory = inventoryFuture.join();
        FrankfurterRate fx = fxFuture.join();

        List<ServiceError> errors = new ArrayList<>();

        if (product == null || product.get("id") == null) {
            errors.add(new ServiceError("catalog", messageFrom(product, "PRODUCT_UNAVAILABLE")));
            ProductSummary errorPayload = new ProductSummary(
                    productId,
                    null,
                    null,
                    null,
                    null,
                    null,
                    "Frankfurter External API",
                    "ERROR",
                    errors
            );
            boolean notFound = product != null && "PRODUCT_NOT_FOUND".equals(product.get("error"));
            return ResponseEntity.status(notFound ? HttpStatus.NOT_FOUND : HttpStatus.BAD_GATEWAY)
                    .body(errorPayload);
        }

        String title = String.valueOf(product.get("title"));
        double priceUsd = Double.parseDouble(product.get("price_usd").toString());

        String stockStatus = null;
        Integer availableUnits = null;
        if (inventory != null && inventory.get("product_id") != null) {
            stockStatus = String.valueOf(inventory.get("stock_status"));
            availableUnits = Integer.parseInt(inventory.get("available_units").toString());
        } else {
            errors.add(new ServiceError("inventory", messageFrom(inventory, "INVENTORY_UNAVAILABLE")));
        }

        Double priceMxn = null;
        if (fx.ok() && fx.rate() != null) {
            priceMxn = Math.round(priceUsd * fx.rate() * 100.0) / 100.0;
        } else {
            errors.add(new ServiceError("frankfurter", fx.error() != null ? fx.error() : "FX_UNAVAILABLE"));
        }

        String status = errors.isEmpty() ? "SUCCESS" : "PARTIAL";
        ProductSummary summary = new ProductSummary(
                String.valueOf(product.get("id")),
                title,
                priceUsd,
                priceMxn,
                stockStatus,
                availableUnits,
                "Frankfurter External API",
                status,
                errors.isEmpty() ? null : errors
        );
        return ResponseEntity.ok(summary);
    }

    private Map<String, Object> getJson(String url) {
        try {
            return restClient.get()
                    .uri(url)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});
        } catch (RestClientResponseException ex) {
            try {
                return ex.getResponseBodyAs(new ParameterizedTypeReference<>() {});
            } catch (Exception ignored) {
                return Map.of("error", "HTTP_" + ex.getStatusCode().value(), "message", ex.getStatusText());
            }
        } catch (Exception ex) {
            return Map.of("error", "UNAVAILABLE", "message", ex.getMessage());
        }
    }

    private String messageFrom(Map<String, Object> body, String fallback) {
        if (body == null) {
            return fallback;
        }
        Object error = body.get("error");
        Object message = body.get("message");
        if (message != null) {
            return message.toString();
        }
        if (error != null) {
            return error.toString();
        }
        return fallback;
    }
}
