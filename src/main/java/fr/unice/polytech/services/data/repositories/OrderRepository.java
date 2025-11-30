package fr.unice.polytech.services.data.repositories;

import fr.unice.polytech.api.dto.OrderDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class OrderRepository {

    private final List<OrderDTO> orders = new ArrayList<>();
    private final AtomicLong counter = new AtomicLong(1);

    public OrderDTO save(OrderDTO order) {
        //  FIX 1 : Assigner un ID si manquant
        if (order.getId() == null || order.getId() == 0) {
            order.setId(counter.getAndIncrement());
        }

        //  FIX 2 : Définir le status par défaut
        if (order.getStatus() == null || order.getStatus().isEmpty()) {
            order.setStatus("PENDING");
        }

        orders.add(order);
        return order;
    }

    public List<OrderDTO> findAll() {
        return orders;
    }

    public OrderDTO findById(long id) {
        return orders.stream()
                .filter(o -> o.getId() == id)
                .findFirst()
                .orElse(null);
    }

    // FIX 3 : Ajouter une méthode pour mettre à jour une commande
    public OrderDTO update(OrderDTO order) {
        OrderDTO existing = findById(order.getId());
        if (existing != null) {
            orders.remove(existing);
            orders.add(order);
            return order;
        }
        return null;
    }
}