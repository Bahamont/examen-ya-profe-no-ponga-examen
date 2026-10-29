import { Router } from "express";
import { buildProductSummary } from "../services/composite.js";

const router = Router();

async function handleSummary(req, res) {
  try {
    const { payload, httpStatus } = await buildProductSummary(req.params.id);
    return res.status(httpStatus).json(payload);
  } catch (error) {
    return res.status(500).json({
      product_id: req.params.id,
      status: "ERROR",
      currency_conversion_source: "Frankfurter External API",
      errors: [{ service: "composite", detail: error.message }]
    });
  }
}

router.get("/api/v1/product-summary/:id", handleSummary);
router.get("/api/v1/products/:id/summary", handleSummary);

export default router;
