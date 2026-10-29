import { Router } from "express";
import { findInventoryByProductId } from "../data/inventory.js";

const router = Router();

router.get("/microservices/inventory/:productId", (req, res) => {
  const item = findInventoryByProductId(req.params.productId);

  if (!item) {
    return res.status(404).json({
      error: "INVENTORY_NOT_FOUND",
      message: `No hay registro de inventario para ${req.params.productId}`,
      product_id: req.params.productId
    });
  }

  return res.json(item);
});

export default router;
