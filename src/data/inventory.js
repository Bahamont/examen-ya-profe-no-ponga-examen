export const inventory = {
  "P-101": {
    product_id: "P-101",
    available_units: 15,
    warehouse: "Almacén Norte — Chihuahua",
    location: "Pasillo B, Rack 12",
    estimated_delivery_days: 3,
    stock_status: "IN_STOCK"
  },
  "P-102": {
    product_id: "P-102",
    available_units: 0,
    warehouse: "Almacén Sur — Juárez",
    location: "Pasillo A, Rack 04",
    estimated_delivery_days: 10,
    stock_status: "OUT_OF_STOCK"
  },
  "P-103": {
    product_id: "P-103",
    available_units: 4,
    warehouse: "Almacén Centro — Chihuahua",
    location: "Pasillo C, Rack 02",
    estimated_delivery_days: 5,
    stock_status: "LOW_STOCK"
  }
};

export function findInventoryByProductId(productId) {
  return inventory[productId] ?? null;
}
