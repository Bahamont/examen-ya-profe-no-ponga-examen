import { Router } from "express";
import { findProductById } from "../data/products.js";

const router = Router();

router.get("/microservices/products/:id", (req, res) => {
  const product = findProductById(req.params.id);

  if (!product) {
    return res.status(404).json({
      error: "PRODUCT_NOT_FOUND",
      message: `No existe el producto ${req.params.id}`,
      product_id: req.params.id
    });
  }

  return res.json(product);
});

export default router;
