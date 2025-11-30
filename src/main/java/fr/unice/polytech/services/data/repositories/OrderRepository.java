package fr.unice.polytech.services.data.repositories;

import fr.unice.polytech.api.dto.OrderDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class OrderRepository {

    private final List<OrderDTO> orders = new ArrayList<>();
    private final AtomicLong counter = new AtomicLong(1);

    public OrderDTO save(OrderDTO order) {
        if (order.getId() == 0) {
            order.setId(counter.getAndIncrement());
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
}
