package fr.unice.polytech.services.catalog.handlers;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import fr.unice.polytech.restaurants.Restaurant;
import fr.unice.polytech.services.catalog.mappers.RestaurantMapper;
import fr.unice.polytech.services.shared.DataApiClient;
import fr.unice.polytech.utils.ETagGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import fr.unice.polytech.api.dto.PaginatedResponseDTO;
import fr.unice.polytech.api.dto.RestaurantDTO;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

public class RestaurantHandler implements HttpHandler {

    private final DataApiClient dataApi = new DataApiClient("http://localhost:8090");
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        try {
            if ("GET".equals(method)) {
                if (path.matches("/api/restaurants/\\d+")) {
                    handleGetRestaurantById(exchange);
                } else {
                    handleGetRestaurants(exchange);
                }
            } else {
                sendResponse(exchange, 405, "{\"error\": \"Method not allowed\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendResponse(exchange, 500, "{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    private void handleGetRestaurants(HttpExchange exchange) throws Exception {
        URI uri = exchange.getRequestURI();
        Map<String, String> queryParams = parseQueryParams(uri.getQuery());

        int page = Integer.parseInt(queryParams.getOrDefault("page", "1"));
        int limit = Integer.parseInt(queryParams.getOrDefault("limit", "10"));

        List<RestaurantDTO> allDtos = List.of(dataApi.get("/data/restaurants", RestaurantDTO[].class));
        List<Restaurant> restaurants = allDtos.stream()
                .map(RestaurantMapper::fromDTO)
                .collect(Collectors.toList());

        if (queryParams.containsKey("cuisineType")) {
            String cuisineType = queryParams.get("cuisineType");
            restaurants = restaurants.stream()
                    .filter(r -> r.getCuisineType().toString().equalsIgnoreCase(cuisineType))
                    .collect(Collectors.toList());
        }

        if (queryParams.containsKey("category")) {
            String category = queryParams.get("category");
            restaurants = restaurants.stream()
                    .filter(r -> r.getDishes().stream()
                            .anyMatch(dish -> dish.getCategory() != null &&
                                    dish.getCategory().toString().equalsIgnoreCase(category)))
                    .collect(Collectors.toList());
        }

        if (queryParams.containsKey("availableNow") && "true".equalsIgnoreCase(queryParams.get("availableNow"))) {
            restaurants = restaurants.stream()
                    .filter(this::isCurrentlyOpen)
                    .collect(Collectors.toList());
        }

        int totalItems = restaurants.size();
        int startIndex = Math.max((page - 1) * limit, 0);
        int endIndex = Math.min(startIndex + limit, totalItems);

        List<Restaurant> paginated = restaurants.subList(startIndex, endIndex);
        List<RestaurantDTO> paginatedDTO = paginated.stream()
                .map(RestaurantMapper::toDTO)
                .collect(Collectors.toList());

        PaginatedResponseDTO<RestaurantDTO> response = new PaginatedResponseDTO<>(paginatedDTO, page, limit, totalItems);
        String json = objectMapper.writeValueAsString(response);

        String etag = ETagGenerator.generateETag(json);
        String ifNoneMatch = exchange.getRequestHeaders().getFirst("If-None-Match");

        if (etag.equals(ifNoneMatch)) {
            exchange.getResponseHeaders().set("ETag", etag);
            exchange.sendResponseHeaders(304, -1);
            return;
        }

        sendResponseWithETag(exchange, 200, json, etag);
    }

    private boolean isCurrentlyOpen(Restaurant restaurant) {
        if (restaurant.getOpeningHours() == null || restaurant.getOpeningHours().isEmpty()) {
            return false;
        }

        LocalDateTime now = LocalDateTime.now();
        DayOfWeek currentDay = now.getDayOfWeek();
        LocalTime currentTime = now.toLocalTime();

        return restaurant.getOpeningHours().stream()
                .anyMatch(hours -> {
                    if (hours.getDay() != currentDay) {
                        return false;
                    }
                    LocalTime opening = hours.getOpeningTime();
                    LocalTime closing = hours.getClosingTime();
                    if (closing.isAfter(opening) || closing.equals(opening)) {
                        return !currentTime.isBefore(opening) && !currentTime.isAfter(closing);
                    } else {
                        return !currentTime.isBefore(opening) || !currentTime.isAfter(closing);
                    }
                });
    }

    private void handleGetRestaurantById(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String idStr = path.substring(path.lastIndexOf('/') + 1);

        try {
            long id = Long.parseLong(idStr);

            // ✅ FIX : Récupérer le DTO depuis DataService
            RestaurantDTO dto = dataApi.get("/data/restaurants/" + id, RestaurantDTO.class);

            if (dto == null) {
                sendResponse(exchange, 404, "{\"error\": \"Restaurant not found\"}");
                return;
            }

            // ✅ FIX CRITIQUE : Vérifier que TOUTES les propriétés sont présentes
            if (dto.getDishes() == null) {
                dto.setDishes(new ArrayList<>());
            }

            if (dto.getOpeningHours() == null) {
                dto.setOpeningHours(new ArrayList<>());
            }

            // ✅ FIX : Vérifier que chaque plat a une liste de toppings
            dto.getDishes().forEach(dish -> {
                if (dish.getToppings() == null) {
                    dish.setToppings(new ArrayList<>());
                }
            });

            String json = objectMapper.writeValueAsString(dto);
            String etag = ETagGenerator.generateETag(json);
            String ifNoneMatch = exchange.getRequestHeaders().getFirst("If-None-Match");

            if (etag.equals(ifNoneMatch)) {
                exchange.getResponseHeaders().set("ETag", etag);
                exchange.sendResponseHeaders(304, -1);
                return;
            }

            sendResponseWithETag(exchange, 200, json, etag);

        } catch (NumberFormatException e) {
            sendResponse(exchange, 404, "{\"error\": \"Invalid restaurant ID format\"}");
        } catch (Exception e) {
            e.printStackTrace();
            if (e.getMessage() != null && e.getMessage().contains("404")) {
                sendResponse(exchange, 404, "{\"error\": \"Restaurant not found\"}");
            } else {
                sendResponse(exchange, 500, "{\"error\": \"Internal server error\"}");
            }
        }
    }

    private Map<String, String> parseQueryParams(String query) {
        Map<String, String> params = new HashMap<>();
        if (query == null || query.isEmpty()) return params;

        for (String param : query.split("&")) {
            String[] keyValue = param.split("=", 2);
            if (keyValue.length == 2) {
                params.put(
                        URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8),
                        URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8)
                );
            }
        }
        return params;
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    private void sendResponseWithETag(HttpExchange exchange, int statusCode, String response, String etag) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.getResponseHeaders().set("ETag", etag);
        exchange.getResponseHeaders().set("Cache-Control", "public, max-age=300");
        byte[] bytes = response.getBytes();
        exchange.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}



/*package fr.unice.polytech.services.catalog.handlers;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import fr.unice.polytech.restaurants.Restaurant;
import fr.unice.polytech.services.catalog.mappers.RestaurantMapper;
import fr.unice.polytech.services.shared.DataApiClient;
import fr.unice.polytech.utils.ETagGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import fr.unice.polytech.api.dto.PaginatedResponseDTO;
import fr.unice.polytech.api.dto.RestaurantDTO;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

public class RestaurantHandler implements HttpHandler {

    private final DataApiClient dataApi = new DataApiClient("http://localhost:8090");
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        try {
            if ("GET".equals(method)) {
                if (path.matches("/api/restaurants/\\d+")) {
                    handleGetRestaurantById(exchange);
                } else {
                    handleGetRestaurants(exchange);
                }
            } else {
                sendResponse(exchange, 405, "{\"error\": \"Method not allowed\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendResponse(exchange, 500, "{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    private void handleGetRestaurants(HttpExchange exchange) throws Exception {
        URI uri = exchange.getRequestURI();
        Map<String, String> queryParams = parseQueryParams(uri.getQuery());

        int page = Integer.parseInt(queryParams.getOrDefault("page", "1"));
        int limit = Integer.parseInt(queryParams.getOrDefault("limit", "10"));

        List<RestaurantDTO> allDtos = List.of(dataApi.get("/data/restaurants", RestaurantDTO[].class));
        List<Restaurant> restaurants = allDtos.stream()
                .map(RestaurantMapper::fromDTO)
                .collect(Collectors.toList());

        if (queryParams.containsKey("cuisineType")) {
            String cuisineType = queryParams.get("cuisineType");
            restaurants = restaurants.stream()
                    .filter(r -> r.getCuisineType().toString().equalsIgnoreCase(cuisineType))
                    .collect(Collectors.toList());
        }

        if (queryParams.containsKey("category")) {
            String category = queryParams.get("category");
            restaurants = restaurants.stream()
                    .filter(r -> r.getDishes().stream()
                            .anyMatch(dish -> dish.getCategory() != null &&
                                    dish.getCategory().toString().equalsIgnoreCase(category)))
                    .collect(Collectors.toList());
        }

        if (queryParams.containsKey("availableNow") && "true".equalsIgnoreCase(queryParams.get("availableNow"))) {
            restaurants = restaurants.stream()
                    .filter(this::isCurrentlyOpen)
                    .collect(Collectors.toList());
        }

        int totalItems = restaurants.size();
        int startIndex = Math.max((page - 1) * limit, 0);
        int endIndex = Math.min(startIndex + limit, totalItems);

        List<Restaurant> paginated = restaurants.subList(startIndex, endIndex);
        List<RestaurantDTO> paginatedDTO = paginated.stream()
                .map(RestaurantMapper::toDTO)
                .collect(Collectors.toList());

        PaginatedResponseDTO<RestaurantDTO> response = new PaginatedResponseDTO<>(paginatedDTO, page, limit, totalItems);
        String json = objectMapper.writeValueAsString(response);

        String etag = ETagGenerator.generateETag(json);
        String ifNoneMatch = exchange.getRequestHeaders().getFirst("If-None-Match");

        if (etag.equals(ifNoneMatch)) {
            exchange.getResponseHeaders().set("ETag", etag);
            exchange.sendResponseHeaders(304, -1);
            return;
        }

        sendResponseWithETag(exchange, 200, json, etag);
    }

    private boolean isCurrentlyOpen(Restaurant restaurant) {
        if (restaurant.getOpeningHours() == null || restaurant.getOpeningHours().isEmpty()) {
            return false;
        }

        LocalDateTime now = LocalDateTime.now();
        DayOfWeek currentDay = now.getDayOfWeek();
        LocalTime currentTime = now.toLocalTime();

        return restaurant.getOpeningHours().stream()
                .anyMatch(hours -> {
                    if (hours.getDay() != currentDay) {
                        return false;
                    }
                    LocalTime opening = hours.getOpeningTime();
                    LocalTime closing = hours.getClosingTime();
                    if (closing.isAfter(opening) || closing.equals(opening)) {
                        // Normal case: opening and closing on same day
                        return !currentTime.isBefore(opening) && !currentTime.isAfter(closing);
                    } else {
                        // Overnight case: closing is after midnight
                        return !currentTime.isBefore(opening) || !currentTime.isAfter(closing);
                    }
                });

    }

    private void handleGetRestaurantById(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String idStr = path.substring(path.lastIndexOf('/') + 1);

        try {
            long id = Long.parseLong(idStr);
            RestaurantDTO dto = dataApi.get("/data/restaurants/" + id, RestaurantDTO.class);

            if (dto == null) {
                sendResponse(exchange, 404, "{\"error\": \"Restaurant not found\"}");
                return;
            }

            String json = objectMapper.writeValueAsString(dto);
            String etag = ETagGenerator.generateETag(json);
            String ifNoneMatch = exchange.getRequestHeaders().getFirst("If-None-Match");

            if (etag.equals(ifNoneMatch)) {
                exchange.getResponseHeaders().set("ETag", etag);
                exchange.sendResponseHeaders(304, -1);
                return;
            }

            sendResponseWithETag(exchange, 200, json, etag);
        } catch (NumberFormatException e) {
            // AJOUTÉ : Renvoyer 404 si l'ID n'est pas un nombre valide
            sendResponse(exchange, 404, "{\"error\": \"Invalid restaurant ID format\"}");
        } catch (Exception e) {
            e.printStackTrace();
            // MODIFIÉ : Vérifier si c'est une erreur 404 du DataService
            if (e.getMessage() != null && e.getMessage().contains("404")) {
                sendResponse(exchange, 404, "{\"error\": \"Restaurant not found\"}");
            } else {
                sendResponse(exchange, 500, "{\"error\": \"Internal server error\"}");
            }
        }
    }

    private Map<String, String> parseQueryParams(String query) {
        Map<String, String> params = new HashMap<>();
        if (query == null || query.isEmpty()) return params;

        for (String param : query.split("&")) {
            String[] keyValue = param.split("=", 2);
            if (keyValue.length == 2) {
                params.put(
                    URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8),
                    URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8)
                );
            }
        }
        return params;
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    private void sendResponseWithETag(HttpExchange exchange, int statusCode, String response, String etag) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.getResponseHeaders().set("ETag", etag);
        exchange.getResponseHeaders().set("Cache-Control", "public, max-age=300");
        byte[] bytes = response.getBytes();
        exchange.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}*/