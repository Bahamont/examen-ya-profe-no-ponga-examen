package mx.utch.dwi.gateway.catalog;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepository {

    private final Map<String, Product> products = new ConcurrentHashMap<>();

    public ProductRepository() {
        products.put("P-101", new Product(
                "P-101",
                "Laptop Gamer XYZ",
                "Laptop para gaming con GPU dedicada, 16 GB RAM y SSD 1 TB.",
                "Electrónica",
                1200.00
        ));
        products.put("P-102", new Product(
                "P-102",
                "Audífonos Inalámbricos Nova",
                "Cancelación activa de ruido y 30 horas de batería.",
                "Audio",
                149.99
        ));
        products.put("P-103", new Product(
                "P-103",
                "Monitor Ultrawide 34\"",
                "Panel IPS 144 Hz, resolución 3440x1440.",
                "Periféricos",
                499.00
        ));
    }

    public Optional<Product> findById(String id) {
        return Optional.ofNullable(products.get(id));
    }
}
