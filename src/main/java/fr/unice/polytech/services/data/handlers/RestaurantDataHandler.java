package fr.unice.polytech.services.data.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import fr.unice.polytech.services.data.repositories.RestaurantRepository;
import fr.unice.polytech.api.dto.RestaurantDTO;
import java.io.IOException;
import java.io.OutputStream;

public class RestaurantDataHandler implements HttpHandler {

    private final ObjectMapper mapper = new ObjectMapper();
    private final RestaurantRepository repository;

    public RestaurantDataHandler(RestaurantRepository repository) {
        this.repository = repository;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();

        if (!method.equals("GET")) {
            send(exchange, 405, "{\"error\":\"Method not allowed\"}");
            return;
        }

        // ✅ GET /data/restaurants/{id}
        if (path.matches("/data/restaurants/\\d+")) {
            try {
                long id = Long.parseLong(path.substring(path.lastIndexOf("/") + 1));
                RestaurantDTO dto = repository.findById(id);

                if (dto == null) {
                    // ✅ CRITIQUE : Retourner 404 si restaurant introuvable
                    send(exchange, 404, "{\"error\":\"Restaurant not found\"}");
                    return;
                }

                send(exchange, 200, mapper.writeValueAsString(dto));

            } catch (NumberFormatException e) {
                // ✅ CRITIQUE : ID invalide → 404
                send(exchange, 404, "{\"error\":\"Invalid restaurant ID format\"}");
            } catch (Exception e) {
                e.printStackTrace();
                send(exchange, 500, "{\"error\":\"Internal server error\"}");
            }
            return;
        }

        // ✅ GET /data/restaurants (all)
        try {
            send(exchange, 200, mapper.writeValueAsString(repository.findAll()));
        } catch (Exception e) {
            e.printStackTrace();
            send(exchange, 500, "{\"error\":\"Internal server error\"}");
        }
    }

    private void send(HttpExchange ex, int code, String body) throws IOException {
        ex.getResponseHeaders().set("Content-Type", "application/json");
        ex.sendResponseHeaders(code, body.getBytes().length);
        try (OutputStream os = ex.getResponseBody()) {
            os.write(body.getBytes());
        }
    }
}