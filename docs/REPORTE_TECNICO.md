# Reporte técnico — Implementación de Composite API y Consumo de Microservicios

## 1. Portada institucional

- **Universidad:** Universidad Tecnológica de Chihuahua (UTCH)
- **Programa educativo:** Desarrollo y Gestión de Software
- **Asignatura:** Desarrollo Web Integral — Unidad 3
- **Nombre de la práctica:** Implementación de Composite API y Consumo de Microservicios
- **Alumno / repositorio:** Bahamont (`examen-ya-profe-no-ponga-examen`)
- **Grupo:** Por completar
- **Fecha de entrega:** 29/10/2026
- **Stack:** Java 17, Spring Boot 3.3, Maven

## 2. Introducción y caso de estudio

El detalle de un producto en un e-commerce suele provocar **Chatty I/O**: el frontend dispara varias peticiones HTTP (catálogo, inventario y tipo de cambio). Eso aumenta latencia, consumo de batería en móviles y complejidad de manejo de errores en el cliente.

El patrón **Composite API / API Aggregator** concentra esas llamadas en el backend. El navegador consume un solo contrato (`GET /api/v1/product-summary/{id}`). El gateway consulta los microservicios **en paralelo** con `CompletableFuture` y entrega JSON consolidado.

## 3. Especificación técnica de las APIs

| Microservicio / API | Tipo | Protocolo / Formato | Endpoint / URL base | Descripción y función |
| --- | --- | --- | --- | --- |
| 1. Catálogo | Propia | REST / JSON | `/microservices/products/{id}` | Información general del producto y precio base USD |
| 2. Inventario | Propia | REST / JSON | `/microservices/inventory/{id}` | Stock disponible, almacén y estatus |
| 3. Divisas | Terceros (Frankfurter) | REST / JSON | `https://api.frankfurter.app/` | Tipo de cambio USD/MXN en tiempo real |
| 4. Composite API | Propia (agregadora) | REST / JSON | `/api/v1/product-summary/{id}` | Orquesta las 3 peticiones y unifica el payload |

Alias del composite: `GET /api/v1/products/{id}/summary`.

Productos de prueba: `P-101`, `P-102`, `P-103`.

## 4. Detalles de implementación, código y orquestación

### Estrategia de consumo

Las tres dependencias se disparan juntas con `CompletableFuture.supplyAsync` y se espera el conjunto con `CompletableFuture.allOf(...).join()`. No es un consumo secuencial: catálogo, inventario y Frankfurter corren en un pool de hilos propio (`compositeExecutor`).

### Consumo de la API externa (Frankfurter)

Cliente HTTP de Spring (`RestClient`) configurado en `HttpClientConfig` con timeout de conexión y lectura. `FrankfurterClient` consulta:

`https://api.frankfurter.app/latest?from=USD&to=MXN`

Mapeo: `rates.MXN` × `price_usd` → `price_mxn` (dos decimales).  
`currency_conversion_source` queda fijo como `"Frankfurter External API"`.

### Tolerancia a fallos

- `try/catch` en `FrankfurterClient` y en `CompositeController`.
- Si el catálogo no existe: HTTP 404 y `status: ERROR`.
- Si inventario o tipo de cambio fallan pero el producto sí existe: HTTP 200 y `status: PARTIAL` con arreglo `errors`.
- Timeouts en el `RestClient` evitan que un servicio lento bloquee indefinidamente al cliente.

## 5. Evidencias de funcionamiento

Con la aplicación en `http://localhost:8080`:

1. Catálogo: `GET /microservices/products/P-101`
2. Inventario: `GET /microservices/inventory/P-101`
3. Externa: `GET https://api.frankfurter.app/latest?from=USD&to=MXN`
4. Composite: `GET /api/v1/product-summary/P-101`

El dashboard web dispara las cuatro consultas y muestra el JSON unificado. Respuestas de ejemplo en `docs/evidencias/`.

## 6. Conclusiones y lecciones aprendidas

Integrar una API de terceros obliga a no tratarla como si siempre estuviera disponible: timeouts, cuerpo incompleto y `PARTIAL` son parte del contrato. La API agregadora reduce el Chatty I/O del frontend, centraliza la conversión de moneda y deja un único punto para evidencias (Postman o el dashboard).

## 7. Enlace al repositorio

Rama de integración: `dev` (no se trabaja sobre `main`).
