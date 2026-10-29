package mx.utch.dwi.gateway.composite;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CompositeController {

    private final CompositeService compositeService;

    public CompositeController(CompositeService compositeService) {
        this.compositeService = compositeService;
    }

    @GetMapping({"/api/v1/product-summary/{id}", "/api/v1/products/{id}/summary"})
    public ResponseEntity<ProductSummary> summary(@PathVariable String id) {
        try {
            return compositeService.getSummary(id);
        } catch (Exception ex) {
            ProductSummary fallback = new ProductSummary(
                    id,
                    null,
                    null,
                    null,
                    null,
                    null,
                    "Frankfurter External API",
                    "ERROR",
                    List.of(new ServiceError("composite", ex.getMessage()))
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(fallback);
        }
    }
}
