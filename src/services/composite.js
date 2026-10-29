import { INTERNAL_BASE } from "../config.js";
import { fetchJson } from "./httpClient.js";
import { getUsdToMxnRate } from "./frankfurter.js";

function roundCurrency(value) {
  return Math.round(value * 100) / 100;
}

export async function buildProductSummary(productId) {
  const errors = [];

  const [productResult, inventoryResult, fxResult] = await Promise.all([
    fetchJson(`${INTERNAL_BASE}/microservices/products/${encodeURIComponent(productId)}`),
    fetchJson(`${INTERNAL_BASE}/microservices/inventory/${encodeURIComponent(productId)}`),
    getUsdToMxnRate()
  ]);

  if (!productResult.ok || !productResult.body?.id) {
    errors.push({
      service: "catalog",
      detail: productResult.error || productResult.body?.error || "PRODUCT_UNAVAILABLE"
    });

    return {
      httpStatus: productResult.status === 404 ? 404 : 502,
      payload: {
        product_id: productId,
        title: null,
        price_usd: null,
        price_mxn: null,
        stock_status: null,
        available_units: null,
        currency_conversion_source: "Frankfurter External API",
        status: "ERROR",
        errors
      }
    };
  }

  const product = productResult.body;
  let stock_status = null;
  let available_units = null;

  if (inventoryResult.ok && inventoryResult.body?.product_id) {
    stock_status = inventoryResult.body.stock_status;
    available_units = inventoryResult.body.available_units;
  } else {
    errors.push({
      service: "inventory",
      detail: inventoryResult.error || inventoryResult.body?.error || "INVENTORY_UNAVAILABLE"
    });
  }

  let price_mxn = null;
  if (fxResult.ok) {
    price_mxn = roundCurrency(Number(product.price_usd) * fxResult.rate);
  } else {
    errors.push({
      service: "frankfurter",
      detail: fxResult.error || "FX_UNAVAILABLE"
    });
  }

  const status = errors.length === 0 ? "SUCCESS" : "PARTIAL";

  return {
    httpStatus: 200,
    payload: {
      product_id: product.id,
      title: product.title,
      price_usd: product.price_usd,
      price_mxn,
      stock_status,
      available_units,
      currency_conversion_source: "Frankfurter External API",
      status,
      ...(errors.length ? { errors } : {})
    }
  };
}
