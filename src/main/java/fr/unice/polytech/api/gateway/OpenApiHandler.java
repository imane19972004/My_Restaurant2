package fr.unice.polytech.api.gateway;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * Handler pour servir la documentation OpenAPI
 */
public class OpenApiHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        
        if (!"GET".equalsIgnoreCase(method)) {
            exchange.sendResponseHeaders(405, -1);
            return;
        }

        try {
            // Lire le fichier openapi.yaml depuis les resources
            InputStream is = getClass().getClassLoader().getResourceAsStream("openapi.yaml");
            
            if (is == null) {
                String error = "OpenAPI specification not found";
                exchange.getResponseHeaders().set("Content-Type", "text/plain");
                exchange.sendResponseHeaders(404, error.length());
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(error.getBytes(StandardCharsets.UTF_8));
                }
                return;
            }

            byte[] content = is.readAllBytes();
            
            // Définir le type de contenu comme YAML
            exchange.getResponseHeaders().set("Content-Type", "application/x-yaml");
            exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
            
            exchange.sendResponseHeaders(200, content.length);
            
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(content);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            String error = "Error loading OpenAPI specification: " + e.getMessage();
            exchange.sendResponseHeaders(500, error.length());
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(error.getBytes(StandardCharsets.UTF_8));
            }
        }
    }
}