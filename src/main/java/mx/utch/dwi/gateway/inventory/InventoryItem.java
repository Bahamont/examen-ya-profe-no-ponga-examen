package mx.utch.dwi.gateway.inventory;

public record InventoryItem(
        String productId,
        int availableUnits,
        String warehouse,
        String location,
        int estimatedDeliveryDays,
        String stockStatus
) {
}
