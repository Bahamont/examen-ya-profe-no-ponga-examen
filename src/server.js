import express from "express";
import path from "node:path";
import { fileURLToPath } from "node:url";
import { PORT } from "./config.js";
import productsRouter from "./routes/products.js";
import inventoryRouter from "./routes/inventory.js";
import compositeRouter from "./routes/composite.js";

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

const app = express();

app.use(express.json());
app.use(express.static(path.join(__dirname, "..", "public")));
app.use(productsRouter);
app.use(inventoryRouter);
app.use(compositeRouter);

app.get("/health", (_req, res) => {
  res.json({
    service: "ecommerce-gateway",
    status: "UP",
    timestamp: new Date().toISOString()
  });
});

app.use((err, _req, res, _next) => {
  res.status(500).json({
    status: "ERROR",
    message: err.message || "Error interno del servidor"
  });
});

app.listen(PORT, () => {
  console.log(`E-Commerce Gateway escuchando en http://localhost:${PORT}`);
});

export default app;
