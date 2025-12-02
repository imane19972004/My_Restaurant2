package fr.unice.polytech.services.order.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import fr.unice.polytech.api.dto.OrderDTO;
import fr.unice.polytech.services.shared.DataApiClient;
import fr.unice.polytech.utils.ETagGenerator;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class OrderHandler implements HttpHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final DataApiClient dataApi = new DataApiClient("http://localhost:8090");

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        try {
            if ("POST".equals(method)) {
                handleCreateOrder(exchange);
            }
            else if ("GET".equals(method) && path.matches("/api/orders/\\d+")) {
                handleGetOrder(exchange);
            }
            else if ("PUT".equals(method) && path.matches("/api/orders/\\d+")) {
                handleUpdateOrder(exchange);
            }
            else {
                sendResponse(exchange, 405, "{\"error\":\"Method not allowed\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendResponse(exchange, 500, "{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    private void handleCreateOrder(HttpExchange exchange) throws Exception {
        InputStream bodyStream = exchange.getRequestBody();
        String body = new String(bodyStream.readAllBytes(), StandardCharsets.UTF_8);

        OrderDTO order = objectMapper.readValue(body, OrderDTO.class);

        if (order.getRestaurantId() == null || order.getRestaurantId() == 0) {
            sendResponse(exchange, 400, "{\"error\":\"restaurantId is required\"}");
            return;
        }

        if (order.getDishes() == null || order.getDishes().isEmpty()) {
            sendResponse(exchange, 400, "{\"error\":\"dishes array is required\"}");
            return;
        }

        if (order.getDeliveryLocation() == null || order.getDeliveryLocation().trim().isEmpty()) {
            sendResponse(exchange, 400, "{\"error\":\"deliveryLocation is required\"}");
            return;
        }

        if (order.getTotalAmount() == 0.0) {
            double total = order.getDishes().stream()
                    .mapToDouble(dish -> dish.getPrice())
                    .sum();
            order.setTotalAmount(total);
        }

        OrderDTO created = dataApi.post("/data/orders", order, OrderDTO.class);

        String json = objectMapper.writeValueAsString(created);
        sendResponse(exchange, 201, json);
    }

    private void handleGetOrder(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        long id = Long.parseLong(path.substring(path.lastIndexOf('/') + 1));

        try {
            OrderDTO dto = dataApi.get("/data/orders/" + id, OrderDTO.class);

            String json = objectMapper.writeValueAsString(dto);
            String etag = ETagGenerator.generateETag(json);

            String ifNoneMatch = exchange.getRequestHeaders().getFirst("If-None-Match");

            if (etag.equals(ifNoneMatch)) {
                exchange.getResponseHeaders().set("ETag", etag);
                exchange.sendResponseHeaders(304, -1);
                return;
            }

            sendResponseWithETag(exchange, 200, json, etag);

        } catch (Exception e) {
            sendResponse(exchange, 404, "{\"error\":\"Order not found\"}");
        }
    }

    private void handleUpdateOrder(HttpExchange exchange) throws Exception {
        String path = exchange.getRequestURI().getPath();
        long id = Long.parseLong(path.substring(path.lastIndexOf('/') + 1));

        String ifMatch = exchange.getRequestHeaders().getFirst("If-Match");
        if (ifMatch == null) {
            sendResponse(exchange, 428, "{\"error\":\"If-Match header required\"}");
            return;
        }

        OrderDTO current;
        try {
            current = dataApi.get("/data/orders/" + id, OrderDTO.class);
        } catch (Exception e) {
            sendResponse(exchange, 404, "{\"error\":\"Order not found\"}");
            return;
        }

        String currentJson = objectMapper.writeValueAsString(current);
        String currentETag = ETagGenerator.generateETag(currentJson);

        if (!ifMatch.equals(currentETag)) {
            String conflict = "{\"error\":\"Precondition Failed\",\"currentETag\":\"" + currentETag + "\"}";
            exchange.getResponseHeaders().set("ETag", currentETag);
            sendResponse(exchange, 412, conflict);
            return;
        }

        InputStream bodyStream = exchange.getRequestBody();
        String body = new String(bodyStream.readAllBytes(), StandardCharsets.UTF_8);
        OrderDTO updatePayload = objectMapper.readValue(body, OrderDTO.class);

        OrderDTO updated = dataApi.put("/data/orders/" + id, updatePayload, OrderDTO.class);

        String updatedJson = objectMapper.writeValueAsString(updated);
        String newETag = ETagGenerator.generateETag(updatedJson);

        sendResponseWithETag(exchange, 200, updatedJson, newETag);
    }

    private void sendResponse(HttpExchange exchange, int code, String body) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(code, body.getBytes().length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(body.getBytes());
        }
    }

    private void sendResponseWithETag(HttpExchange exchange, int code, String json, String etag) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.getResponseHeaders().set("ETag", etag);
        exchange.sendResponseHeaders(code, json.getBytes().length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(json.getBytes());
        }
    }
}