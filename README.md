# E-Commerce Gateway & Dashboard — Composite API

Práctica de **Desarrollo Web Integral (Unidad 3)** — Universidad Tecnológica de Chihuahua (UTCH).

Stack: **Java 17 + Spring Boot 3**.

La rama **`main` no se modifica**. El desarrollo se integra en **`dev`** con ramas `feature/*` (Gitflow).

## Requisitos

- JDK 17+
- Maven 3.9+

## Ejecución

```bash
./mvnw spring-boot:run
```

En Windows: `mvnw.cmd spring-boot:run`

- Dashboard: http://localhost:8080
- Health: http://localhost:8080/health

## Endpoints

| Recurso | Método y ruta |
|---------|----------------|
| Catálogo | `GET /microservices/products/{id}` |
| Inventario | `GET /microservices/inventory/{productId}` |
| Composite | `GET /api/v1/product-summary/{id}` |
| Composite (alias) | `GET /api/v1/products/{id}/summary` |

IDs de ejemplo: `P-101`, `P-102`, `P-103`.

La agregadora usa **CompletableFuture** para llamar en paralelo a los dos microservicios propios y a Frankfurter (`https://api.frankfurter.app/latest?from=USD&to=MXN`).

Reporte técnico: `docs/REPORTE_TECNICO.md`.

## Flujo de ramas

| Rama | Contenido |
|------|-----------|
| `main` | Protegida / no se toca |
| `dev` | Integración |
| `feature/catalogo-productos` | Microservicio de catálogo |
| `feature/inventario` | Microservicio de inventario |
| `feature/composite-api` | API agregadora + Frankfurter |
| `feature/dashboard` | Interfaz web |
| `docs/reporte-tecnico` | Evidencia y reporte |
