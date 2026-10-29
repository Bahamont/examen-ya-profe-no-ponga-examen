import { REQUEST_TIMEOUT_MS } from "../config.js";

export async function fetchJson(url, { timeoutMs = REQUEST_TIMEOUT_MS } = {}) {
  const controller = new AbortController();
  const timer = setTimeout(() => controller.abort(), timeoutMs);

  try {
    const response = await fetch(url, {
      method: "GET",
      headers: { Accept: "application/json" },
      signal: controller.signal
    });

    const body = await response.json().catch(() => ({}));

    return {
      ok: response.ok,
      status: response.status,
      body
    };
  } catch (error) {
    return {
      ok: false,
      status: 0,
      body: null,
      error: error.name === "AbortError" ? "TIMEOUT" : error.message
    };
  } finally {
    clearTimeout(timer);
  }
}
