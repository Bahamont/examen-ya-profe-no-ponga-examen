import { FRANKFURTER_URL } from "../config.js";
import { fetchJson } from "./httpClient.js";

export async function getUsdToMxnRate() {
  const result = await fetchJson(FRANKFURTER_URL);

  if (!result.ok || !result.body?.rates?.MXN) {
    return {
      ok: false,
      error: result.error || "FX_UNAVAILABLE",
      source: "Frankfurter External API"
    };
  }

  return {
    ok: true,
    rate: Number(result.body.rates.MXN),
    date: result.body.date ?? null,
    source: "Frankfurter External API"
  };
}
