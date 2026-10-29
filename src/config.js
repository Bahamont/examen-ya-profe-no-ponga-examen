export const PORT = Number(process.env.PORT || 3000);
export const INTERNAL_BASE = process.env.INTERNAL_BASE || `http://127.0.0.1:${PORT}`;
export const FRANKFURTER_URL = "https://api.frankfurter.app/latest?from=USD&to=MXN";
export const REQUEST_TIMEOUT_MS = Number(process.env.REQUEST_TIMEOUT_MS || 5000);
