package fr.unice.polytech.services.data.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import fr.unice.polytech.services.data.repositories.OrderRepository;
import fr.unice.polytech.api.dto.OrderDTO;
import java.io.IOException;
import java.io.OutputStream;

public class OrderDataHandler implements HttpHandler {

    private final ObjectMapper mapper = new ObjectMapper();
    private final OrderRepository repository;

    public OrderDataHandler(OrderRepository repository) {
        this.repository = repository;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        if (method.equals("GET")) {

            if (path.matches("/data/orders/\\d+")) {
                long id = Long.parseLong(path.substring(path.lastIndexOf("/") + 1));
                OrderDTO dto = repository.findById(id);

                if (dto == null) {
                    send(exchange, 404, "{\"error\":\"Order not found\"}");
                    return;
                }

                send(exchange, 200, mapper.writeValueAsString(dto));
                return;
            }

            send(exchange, 200, mapper.writeValueAsString(repository.findAll()));
            return;
        }

        if (method.equals("POST")) {
            OrderDTO dto = mapper.readValue(exchange.getRequestBody(), OrderDTO.class);
            dto = repository.save(dto);

            send(exchange, 201, mapper.writeValueAsString(dto));
            return;
        }

        send(exchange, 405, "{\"error\":\"Method not allowed\"}");
    }

    private void send(HttpExchange ex, int code, String body) throws IOException {
        ex.getResponseHeaders().set("Content-Type", "application/json");
        ex.sendResponseHeaders(code, body.getBytes().length);
        try (OutputStream os = ex.getResponseBody()) {
            os.write(body.getBytes());
        }
    }
}
