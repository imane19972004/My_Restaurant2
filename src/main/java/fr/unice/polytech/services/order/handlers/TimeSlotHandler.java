package fr.unice.polytech.services.order.handlers;

import fr.unice.polytech.utils.ETagGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import fr.unice.polytech.api.dto.RestaurantDTO;
import fr.unice.polytech.api.dto.TimeSlotDTO;
import fr.unice.polytech.services.shared.DataApiClient;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.*;

public class TimeSlotHandler implements HttpHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final DataApiClient dataApi = new DataApiClient("http://localhost:8090");

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        System.out.println("\n🕐 TimeSlotHandler received: " + method + " " +
                exchange.getRequestURI().getPath());

        try {
            if ("GET".equals(method)) {
                handleGetTimeSlots(exchange);
            } else {
                sendResponse(exchange, 405, "{\"error\": \"Method not allowed\"}");
            }
        } catch (Exception e) {
            System.err.println("❌ TimeSlotHandler error: " + e.getMessage());
            e.printStackTrace();
            sendResponse(exchange, 500, "{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    private void handleGetTimeSlots(HttpExchange exchange) throws IOException {
        URI uri = exchange.getRequestURI();
        Map<String, String> queryParams = parseQueryParams(uri.getQuery());

        System.out.println("📋 Query params: " + queryParams);

        String restaurantIdStr = queryParams.get("restaurantId");
        if (restaurantIdStr == null || restaurantIdStr.isEmpty()) {
            System.err.println("❌ Missing restaurantId parameter");
            sendResponse(exchange, 400, "{\"error\": \"Restaurant ID is required\"}");
            return;
        }

        try {
            long restaurantId = Long.parseLong(restaurantIdStr);
            System.out.println("🔍 Looking for restaurant ID: " + restaurantId);

            // ✅ RÉCUPÉRER LE RESTAURANT DEPUIS LE DATA SERVICE
            RestaurantDTO restaurant = dataApi.get("/data/restaurants/" + restaurantId, RestaurantDTO.class);

            if (restaurant == null) {
                System.err.println("❌ Restaurant not found");
                sendResponse(exchange, 404, "{\"error\": \"Restaurant not found\"}");
                return;
            }

            System.out.println("✅ Found restaurant: " + restaurant.getName());

            // ✅ RÉCUPÉRER LES TIMESLOTS DU RESTAURANT
            List<TimeSlotDTO> slots = restaurant.getTimeSlots();

            if (slots == null) {
                System.out.println("⚠️ TimeSlots is null, initializing empty list");
                slots = new ArrayList<>();
            }

            System.out.println("📅 TimeSlots found: " + slots.size());

            if (!slots.isEmpty()) {
                System.out.println("📅 TimeSlot details:");
                for (int i = 0; i < Math.min(slots.size(), 5); i++) {
                    TimeSlotDTO slot = slots.get(i);
                    System.out.println("  - " + slot.getStartTime() + " to " +
                            slot.getEndTime() + " (capacity: " + slot.getAvailableCapacity() + ")");
                }
                if (slots.size() > 5) {
                    System.out.println("  ... and " + (slots.size() - 5) + " more");
                }
            } else {
                System.out.println("⚠️ No TimeSlots defined for restaurant " + restaurant.getName());
            }

            String jsonResponse = objectMapper.writeValueAsString(slots);
            System.out.println("📤 Sending response with " + slots.size() + " TimeSlots");

            String etag = ETagGenerator.generateETag(jsonResponse);
            String ifNoneMatch = exchange.getRequestHeaders().getFirst("If-None-Match");

            if (ifNoneMatch != null && ifNoneMatch.equals(etag)) {
                System.out.println("✅ ETag match - returning 304");
                exchange.getResponseHeaders().set("ETag", etag);
                exchange.getResponseHeaders().set("Cache-Control", "public, max-age=60");
                exchange.sendResponseHeaders(304, -1);
                exchange.close();
            } else {
                System.out.println("✅ Sending 200 with data");
                sendResponseWithETag(exchange, 200, jsonResponse, etag);
            }

        } catch (NumberFormatException e) {
            System.err.println("❌ Invalid restaurant ID format: " + restaurantIdStr);
            sendResponse(exchange, 400, "{\"error\": \"Invalid restaurant ID\"}");
        } catch (Exception e) {
            System.err.println("❌ Error retrieving TimeSlots: " + e.getMessage());
            e.printStackTrace();
            sendResponse(exchange, 500, "{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    private void sendResponseWithETag(HttpExchange exchange, int statusCode, String response, String etag) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.getResponseHeaders().set("ETag", etag);

        if (statusCode == 200) {
            exchange.getResponseHeaders().set("Cache-Control", "public, max-age=60");
        } else {
            exchange.getResponseHeaders().set("Cache-Control", "no-store");
        }

        byte[] bytes = response.getBytes();
        exchange.sendResponseHeaders(statusCode, bytes.length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }

        System.out.println("✅ Response sent successfully");
    }

    private Map<String, String> parseQueryParams(String query) {
        Map<String, String> params = new HashMap<>();
        if (query == null || query.isEmpty()) {
            return params;
        }

        for (String param : query.split("&")) {
            String[] pair = param.split("=");
            if (pair.length == 2) {
                params.put(pair[0], pair[1]);
            }
        }
        return params;
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.getResponseHeaders().set("Cache-Control", "no-store");

        byte[] bytes = response.getBytes();
        exchange.sendResponseHeaders(statusCode, bytes.length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}