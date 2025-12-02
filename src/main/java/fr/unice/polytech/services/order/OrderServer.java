package fr.unice.polytech.services.order;

import com.sun.net.httpserver.HttpServer;
import fr.unice.polytech.orderManagement.OrderManager;
import fr.unice.polytech.services.order.handlers.OrderHandler;
import fr.unice.polytech.services.order.handlers.PaymentHandler;
import fr.unice.polytech.services.order.handlers.TimeSlotHandler;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Order Service - REST API for orders, timeslots, and payments
 * Port: 8082
 *
 * Endpoints:
 * - POST /api/orders
 * - GET  /api/orders/{id}
 * - GET  /api/timeslots?restaurantId={id}
 * - POST /api/payment
 */
public class OrderServer {

    private static final int PORT = 8082;
    private final HttpServer server;
    private final OrderManager orderManager;

    public OrderServer() throws IOException {
        this.orderManager = new OrderManager();

        server = HttpServer.create(new InetSocketAddress(PORT), 0);
        registerHandlers();
    }

    private void registerHandlers() {
        // Order endpoints
        server.createContext("/api/orders", new OrderHandler());

        // TimeSlot endpoints - PLUS BESOIN DE PASSER RestaurantManager
        server.createContext("/api/timeslots", new TimeSlotHandler());

        // Payment endpoints (proxy)
        server.createContext("/api/payment", new PaymentHandler(orderManager));
    }

    public void start() {
        server.setExecutor(null);
        server.start();
        System.out.println("🛒 Order Service started on port " + PORT);
        System.out.println("📍 Available endpoints:");
        System.out.println("   POST http://localhost:" + PORT + "/api/orders");
        System.out.println("   GET  http://localhost:" + PORT + "/api/orders/{id}");
        System.out.println("   GET  http://localhost:" + PORT + "/api/timeslots?restaurantId={id}");
        System.out.println("   POST http://localhost:" + PORT + "/api/payment");
    }

    public void stop() {
        server.stop(0);
        System.out.println("❌ Order Service stopped");
    }

    public OrderManager getOrderManager() {
        return orderManager;
    }

    public static void main(String[] args) {
        try {
            OrderServer orderServer = new OrderServer();
            orderServer.start();

            System.out.println("\n⏸️  Press Enter to stop the server...");
            System.in.read();
            orderServer.stop();

        } catch (IOException e) {
            System.err.println("❌ Failed to start Order Service: " + e.getMessage());
            e.printStackTrace();
        }
    }
}