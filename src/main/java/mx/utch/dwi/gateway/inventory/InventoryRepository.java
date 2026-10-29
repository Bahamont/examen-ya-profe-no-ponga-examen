package mx.utch.dwi.gateway.inventory;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class InventoryRepository {

    private final Map<String, InventoryItem> inventory = new ConcurrentHashMap<>();

    public InventoryRepository() {
        inventory.put("P-101", new InventoryItem(
                "P-101", 15, "Almacén Norte — Chihuahua", "Pasillo B, Rack 12", 3, "IN_STOCK"
        ));
        inventory.put("P-102", new InventoryItem(
                "P-102", 0, "Almacén Sur — Juárez", "Pasillo A, Rack 04", 10, "OUT_OF_STOCK"
        ));
        inventory.put("P-103", new InventoryItem(
                "P-103", 4, "Almacén Centro — Chihuahua", "Pasillo C, Rack 02", 5, "LOW_STOCK"
        ));
    }

    public Optional<InventoryItem> findByProductId(String productId) {
        return Optional.ofNullable(inventory.get(productId));
    }
}
