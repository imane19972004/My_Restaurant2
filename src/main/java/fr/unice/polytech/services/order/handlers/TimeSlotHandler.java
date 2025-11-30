package fr.unice.polytech.services.order.handlers;


import fr.unice.polytech.utils.ETagGenerator;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import fr.unice.polytech.api.dto.TimeSlotDTO;
import fr.unice.polytech.restaurants.Restaurant;
import fr.unice.polytech.restaurants.RestaurantManager;
import fr.unice.polytech.restaurants.TimeSlot;
import fr.unice.polytech.services.order.mappers.OrderMapper;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Handler for TimeSlot endpoints
 * 
 * GET /api/timeslots?restaurantId={id}
 * 
 * TD requirement: "visualisant les heures de livraisons possibles 
 * qui évoluent en fonction de la commande et des autres commandes"
 */
public class TimeSlotHandler implements HttpHandler {
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestaurantManager restaurantManager;
    
    public TimeSlotHandler(RestaurantManager restaurantManager) {
        this.restaurantManager = restaurantManager;
    }
    
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
            
            // Find restaurant
            Restaurant restaurant = restaurantManager.getAllRestaurants().stream()
                .filter(r -> r.hashCode() == restaurantId)
                .findFirst()
                .orElse(null);
            
            if (restaurant == null) {
                sendResponse(exchange, 404, "{\"error\": \"Restaurant not found\"}");
                return;
            }
            
            // Get available time slots
            List<TimeSlot> availableSlots = restaurantManager.getAvailableTimeSlots(restaurant);
            
            // Convert to DTOs with capacity info
            List<TimeSlotDTO> slotDTOs = availableSlots.stream()
                .map(slot -> {
                    int capacity = restaurant.getCapacity(slot);
                    return OrderMapper.timeSlotToDTO(slot, capacity);
                })
                .filter(dto -> dto.getAvailableCapacity() > 0)
                .collect(Collectors.toList());
            
            String jsonResponse = objectMapper.writeValueAsString(slotDTOs);
            
            // ✨ NOUVEAU : Générer ETag
            String etag = ETagGenerator.generateETag(jsonResponse);
            
            // ✨ NOUVEAU : Vérifier If-None-Match
            String ifNoneMatch = exchange.getRequestHeaders().getFirst("If-None-Match");
            
            if (ifNoneMatch != null && ifNoneMatch.equals(etag)) {
                System.out.println("✅ ETag match → 304 Not Modified (timeslots)");
                exchange.getResponseHeaders().set("ETag", etag);
                exchange.getResponseHeaders().set("Cache-Control", "public, max-age=60");
                exchange.sendResponseHeaders(304, -1);
                exchange.close();
            } else {
                System.out.println("🆕 New timeslots → 200 OK with ETag: " + etag);
                sendResponseWithETag(exchange, 200, jsonResponse, etag);
            }
            
        } catch (NumberFormatException e) {
            sendResponse(exchange, 400, "{\"error\": \"Invalid restaurant ID\"}");
        }
    }
    /**
     * Send HTTP response with ETag header
     */
    private void sendResponseWithETag(HttpExchange exchange, int statusCode, String response, String etag) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.getResponseHeaders().set("ETag", etag);
        
        if (statusCode == 200) {
            exchange.getResponseHeaders().set("Cache-Control", "public, max-age=60");
            System.out.println("✅ Cache-Control header ajouté : public, max-age=60");
            System.out.println("✅ ETag header ajouté : " + etag);
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
        
        //  NOUVEAU : Ajouter Cache-Control header
        if (statusCode == 200) {
            // Cache les créneaux pendant 1 minute (60 secondes) - plus dynamique
            exchange.getResponseHeaders().set("Cache-Control", "public, max-age=60");
            System.out.println("✅ Cache-Control header ajouté : public, max-age=60");
        } else {
            exchange.getResponseHeaders().set("Cache-Control", "no-store");
            System.out.println("⚠️ Cache-Control header ajouté : no-store (erreur)");
        }
        
        exchange.sendResponseHeaders(statusCode, response.getBytes().length);
        
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }


}
