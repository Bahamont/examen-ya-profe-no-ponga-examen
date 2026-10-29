package mx.utch.dwi.gateway.composite;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProductSummary(
        String productId,
        String title,
        Double priceUsd,
        Double priceMxn,
        String stockStatus,
        Integer availableUnits,
        String currencyConversionSource,
        String status,
        List<ServiceError> errors
) {
}
