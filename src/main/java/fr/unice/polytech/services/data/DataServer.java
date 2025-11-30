package fr.unice.polytech.services.data;

import com.sun.net.httpserver.HttpServer;
import fr.unice.polytech.services.data.handlers.OrderDataHandler;
import fr.unice.polytech.services.data.handlers.RestaurantDataHandler;
import fr.unice.polytech.services.data.repositories.OrderRepository;
import fr.unice.polytech.services.data.repositories.RestaurantRepository;
import java.io.IOException;
import java.net.InetSocketAddress;

public class DataServer {

    private static final int PORT = 8090;

    private final HttpServer server;
    private final RestaurantRepository restaurantRepo;
    private final OrderRepository orderRepo;

    public DataServer() throws IOException {
        this.restaurantRepo = new RestaurantRepository();
        this.orderRepo = new OrderRepository();

        this.server = HttpServer.create(new InetSocketAddress(PORT), 0);
        registerHandlers();
    }

    private void registerHandlers() {

        // === Restaurants ===
        server.createContext("/data/restaurants",
                new RestaurantDataHandler(restaurantRepo));

        // === Orders ===
        server.createContext("/data/orders",
                new OrderDataHandler(orderRepo));
    }

    public void start() {
        server.setExecutor(null);
        server.start();
        System.out.println("📦 DataService started on port " + PORT);
        System.out.println("Available endpoints:");
        System.out.println("  GET  /data/restaurants");
        System.out.println("  GET  /data/restaurants/{id}");
        System.out.println("  GET  /data/orders");
        System.out.println("  GET  /data/orders/{id}");
        System.out.println("  POST /data/orders");
    }

    public static void main(String[] args) {
        try {
            DataServer ds = new DataServer();
            ds.start();

            System.out.println("\nPress ENTER to stop the server...");
            System.in.read();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
