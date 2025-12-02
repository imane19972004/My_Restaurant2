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

/**
 * ✅ Récupère les TimeSlots STOCKÉS dans le restaurant (avec capacités réelles)
 */
public class TimeSlotHandler implements HttpHandler {
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final DataApiClient dataApi = new DataApiClient("http://localhost:8090");
    
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        
        try {
            if ("GET".equals(method)) {
                handleGetTimeSlots(exchange);
            } else {
                sendResponse(exchange, 405, "{\"error\": \"Method not allowed\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendResponse(exchange, 500, "{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    private void handleGetTimeSlots(HttpExchange exchange) throws IOException {
        URI uri = exchange.getRequestURI();
        Map<String, String> queryParams = parseQueryParams(uri.getQuery());

        String restaurantIdStr = queryParams.get("restaurantId");
        if (restaurantIdStr == null || restaurantIdStr.isEmpty()) {
            sendResponse(exchange, 400, "{\"error\": \"Restaurant ID is required\"}");
            return;
        }

        try {
            long restaurantId = Long.parseLong(restaurantIdStr);

            // ✅ RÉCUPÉRER LE RESTAURANT DEPUIS LE DATA SERVICE
            RestaurantDTO restaurant = dataApi.get("/data/restaurants/" + restaurantId, RestaurantDTO.class);
            
            if (restaurant == null) {
                sendResponse(exchange, 404, "{\"error\": \"Restaurant not found\"}");
                return;
            }

            // ✅ RÉCUPÉRER LES TIMESLOTS DÉJÀ STOCKÉS DANS LE RESTAURANT
            List<TimeSlotDTO> slots = restaurant.getTimeSlots();
            
            if (slots == null || slots.isEmpty()) {
                System.out.println("⚠️ Aucun TimeSlot défini pour le restaurant " + restaurant.getName());
                slots = new ArrayList<>();
            }

            System.out.println("✅ " + slots.size() + " créneaux trouvés pour " + restaurant.getName());

            String jsonResponse = objectMapper.writeValueAsString(slots);
            String etag = ETagGenerator.generateETag(jsonResponse);
            String ifNoneMatch = exchange.getRequestHeaders().getFirst("If-None-Match");

            if (ifNoneMatch != null && ifNoneMatch.equals(etag)) {
                exchange.getResponseHeaders().set("ETag", etag);
                exchange.getResponseHeaders().set("Cache-Control", "public, max-age=60");
                exchange.sendResponseHeaders(304, -1);
                exchange.close();
            } else {
                sendResponseWithETag(exchange, 200, jsonResponse, etag);
            }

        } catch (NumberFormatException e) {
            sendResponse(exchange, 400, "{\"error\": \"Invalid restaurant ID\"}");
        } catch (Exception e) {
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
        
        exchange.sendResponseHeaders(statusCode, response.getBytes().length);
        
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
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
        
        if (statusCode == 200) {
            exchange.getResponseHeaders().set("Cache-Control", "public, max-age=60");
        } else {
            exchange.getResponseHeaders().set("Cache-Control", "no-store");
        }
        
        exchange.sendResponseHeaders(statusCode, response.getBytes().length);
        
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}