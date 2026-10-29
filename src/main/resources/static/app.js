const form = document.getElementById("search-form");
const productIdInput = document.getElementById("product-id");
const productCard = document.getElementById("product-card");

function pretty(value) {
  return JSON.stringify(value, null, 2);
}

async function loadJson(url) {
  const response = await fetch(url);
  const body = await response.json().catch(() => ({ error: "INVALID_JSON" }));
  return { ok: response.ok, status: response.status, body };
}

form.addEventListener("submit", async (event) => {
  event.preventDefault();
  const id = productIdInput.value;

  const [catalog, inventory, fx, composite] = await Promise.all([
    loadJson(`/microservices/products/${id}`),
    loadJson(`/microservices/inventory/${id}`),
    loadJson("https://api.frankfurter.app/latest?from=USD&to=MXN"),
    loadJson(`/api/v1/product-summary/${id}`)
  ]);

  document.getElementById("catalog-json").textContent = pretty(catalog.body);
  document.getElementById("inventory-json").textContent = pretty(inventory.body);
  document.getElementById("fx-json").textContent = pretty(fx.body);
  document.getElementById("composite-json").textContent = pretty(composite.body);

  const data = composite.body;
  productCard.classList.remove("empty");
  productCard.innerHTML = `
    <p class="muted">${data.product_id ?? id}</p>
    <h3>${data.title ?? "Sin título"}</h3>
    <p class="price">USD ${data.price_usd ?? "—"} · MXN ${data.price_mxn ?? "—"}</p>
    <p>Stock: ${data.stock_status ?? "N/D"} (${data.available_units ?? "n/d"} unidades)</p>
    <p class="status">${data.status ?? composite.status}</p>
    <p class="muted">${data.currency_conversion_source ?? ""}</p>
  `;
});
