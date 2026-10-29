package mx.utch.dwi.gateway.inventory;

import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InventoryController {

    private final InventoryRepository inventoryRepository;

    public InventoryController(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @GetMapping("/microservices/inventory/{productId}")
    public ResponseEntity<?> getInventory(@PathVariable String productId) {
        return inventoryRepository.findByProductId(productId)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "error", "INVENTORY_NOT_FOUND",
                        "message", "No hay registro de inventario para " + productId,
                        "product_id", productId
                )));
    }
}
