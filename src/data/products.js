export const products = {
  "P-101": {
    id: "P-101",
    title: "Laptop Gamer XYZ",
    description: "Laptop para gaming con GPU dedicada, 16 GB RAM y SSD 1 TB.",
    category: "Electrónica",
    price_usd: 1200.0
  },
  "P-102": {
    id: "P-102",
    title: "Audífonos Inalámbricos Nova",
    description: "Cancelación activa de ruido y 30 horas de batería.",
    category: "Audio",
    price_usd: 149.99
  },
  "P-103": {
    id: "P-103",
    title: "Monitor Ultrawide 34\"",
    description: "Panel IPS 144 Hz, resolución 3440x1440.",
    category: "Periféricos",
    price_usd: 499.0
  }
};

export function findProductById(id) {
  return products[id] ?? null;
}
