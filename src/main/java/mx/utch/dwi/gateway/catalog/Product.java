package mx.utch.dwi.gateway.catalog;

public record Product(
        String id,
        String title,
        String description,
        String category,
        double priceUsd
) {
}
