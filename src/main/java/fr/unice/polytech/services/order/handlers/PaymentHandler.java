package fr.unice.polytech.services.order.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import fr.unice.polytech.api.dto.OrderDTO;
import fr.unice.polytech.services.shared.DataApiClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class PaymentHandler implements HttpHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final DataApiClient dataApi = new DataApiClient("http://localhost:8090");

    public PaymentHandler(Object orderManager) {}

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        try {
            if ("POST".equals(method)) {
                handleProcessPayment(exchange);
            } else {
                sendResponse(exchange, 405, "{\"error\": \"Method not allowed\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendResponse(exchange, 500, "{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    private void handleProcessPayment(HttpExchange exchange) throws Exception {
        InputStream requestBody = exchange.getRequestBody();
        String body = new String(requestBody.readAllBytes(), StandardCharsets.UTF_8);

        JsonNode jsonNode = objectMapper.readTree(body);

        if (!jsonNode.has("orderId") || !jsonNode.has("paymentMethod")) {
            sendResponse(exchange, 400, "{\"error\": \"Order ID and payment method required\"}");
            return;
        }

        long orderId = jsonNode.get("orderId").asLong();
        String paymentMethodStr = jsonNode.get("paymentMethod").asText();

        OrderDTO order;
        try {
            order = dataApi.get("/data/orders/" + orderId, OrderDTO.class);
        } catch (Exception e) {
            sendResponse(exchange, 404, "{\"error\": \"Order not found\"}");
            return;
        }

        if (!"PENDING".equals(order.getStatus())) {
            sendResponse(exchange, 400, "{\"error\": \"Order is not pending\"}");
            return;
        }

        boolean paymentSuccess = simulatePayment(paymentMethodStr);

        if (paymentSuccess) {
            order.setStatus("VALIDATED");
        } else {
            order.setStatus("CANCELED");
        }

        OrderDTO updatedOrder = dataApi.put("/data/orders/" + orderId, order, OrderDTO.class);

        Map<String, Object> response = new HashMap<>();
        response.put("orderId", updatedOrder.getId());
        response.put("status", updatedOrder.getStatus());
        response.put("message", paymentSuccess ?
                "Payment successful. Order validated." :
                "Payment failed. Order canceled.");
        response.put("paymentMethod", paymentMethodStr);

        String jsonResponse = objectMapper.writeValueAsString(response);

        int statusCode = paymentSuccess ? 200 : 402;
        sendResponse(exchange, statusCode, jsonResponse);
    }

    private boolean simulatePayment(String paymentMethod) {
        if ("INTERNAL".equalsIgnoreCase(paymentMethod)) {
            return true;
        }

        if ("EXTERNAL".equalsIgnoreCase(paymentMethod)) {
            return Math.random() < 0.8;
        }

        return false;
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, response.getBytes().length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}