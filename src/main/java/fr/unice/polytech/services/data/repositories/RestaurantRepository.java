package fr.unice.polytech.services.data.repositories;

import fr.unice.polytech.api.dto.RestaurantDTO;
import fr.unice.polytech.dishes.DishType;
import fr.unice.polytech.restaurants.EstablishmentType;
import fr.unice.polytech.services.catalog.mappers.RestaurantMapper;
import fr.unice.polytech.restaurants.Restaurant;
import fr.unice.polytech.restaurants.TimeSlot;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class RestaurantRepository {

    private final List<RestaurantDTO> restaurants = new ArrayList<>();
    private final AtomicLong counter = new AtomicLong(1);
    private final AtomicLong dishCounter = new AtomicLong(1);

    public RestaurantRepository() {
        seed();
        System.out.println("📦 RestaurantRepository initialized with " + restaurants.size() + " restaurants");
    }

    private void seed() {
        // ====================== RESTAURANT 1 ======================
        Restaurant r1Domain = new Restaurant.Builder("La Bella Vita")
            .withCuisineType(DishType.ITALIAN)
            .build();
        
        // ✅ AJOUTER DES TIMESLOTS AVEC CAPACITÉ
        addTimeSlots(r1Domain, "11:30", "14:00", 5);  // Midi: capacité 5
        addTimeSlots(r1Domain, "18:30", "22:00", 8);  // Soir: capacité 8
        
        // Ajouter les plats
        r1Domain.addDish("Bruschetta", "Tomato, basil, garlic on toasted bread", 6.50);
        r1Domain.addDish("Margherita Pizza", "Tomato sauce, mozzarella, basil", 12.50);
        r1Domain.addDish("Pasta Carbonara", "Creamy bacon pasta", 14.00);
        r1Domain.addDish("Tiramisu", "Classic Italian dessert", 6.00);
        
        RestaurantDTO r1 = RestaurantMapper.toDTO(r1Domain);
        r1.setId(counter.getAndIncrement());
        r1.setPriceRange("€€");
        r1.setEstablishmentType(EstablishmentType.RESTAURANT);
        r1.setOpen(true);
        restaurants.add(r1);

        // ====================== RESTAURANT 2 ======================
        Restaurant r2Domain = new Restaurant.Builder("Sakura Sushi")
            .withCuisineType(DishType.JAPANESE)
            .build();
        
        addTimeSlots(r2Domain, "12:00", "14:30", 6);
        addTimeSlots(r2Domain, "19:00", "22:30", 10);
        
        r2Domain.addDish("Miso Soup", "Traditional Japanese soup", 4.50);
        r2Domain.addDish("California Roll", "Crab, avocado, cucumber", 13.00);
        r2Domain.addDish("Salmon Sashimi", "Fresh salmon slices", 16.00);
        
        RestaurantDTO r2 = RestaurantMapper.toDTO(r2Domain);
        r2.setId(counter.getAndIncrement());
        r2.setPriceRange("€€");
        r2.setEstablishmentType(EstablishmentType.RESTAURANT);
        r2.setOpen(true);
        restaurants.add(r2);

        // ====================== RESTAURANT 3 ======================
        Restaurant r3Domain = new Restaurant.Builder("El Sombrero")
            .withCuisineType(DishType.MEXICAN)
            .build();
        
        // Restaurant fermé - AUCUN TIMESLOT
        
        r3Domain.addDish("Tacos Al Pastor", "Pork and pineapple", 9.00);
        r3Domain.addDish("Nachos Supreme", "Cheese, jalapeños", 7.50);
        
        RestaurantDTO r3 = RestaurantMapper.toDTO(r3Domain);
        r3.setId(counter.getAndIncrement());
        r3.setPriceRange("€");
        r3.setEstablishmentType(EstablishmentType.FOOD_TRUCK);
        r3.setOpen(false);
        restaurants.add(r3);

        // ====================== RESTAURANT 4 ======================
        Restaurant r4Domain = new Restaurant.Builder("Bombay Spice")
            .withCuisineType(DishType.INDIAN)
            .build();
        
        addTimeSlots(r4Domain, "12:00", "14:00", 4);
        addTimeSlots(r4Domain, "18:00", "22:00", 7);
        
        r4Domain.addDish("Vegetable Samosa", "Crispy pastry", 5.50);
        r4Domain.addDish("Butter Chicken", "Creamy tomato curry", 15.00);
        r4Domain.addDish("Garlic Naan", "Garlic flatbread", 3.50);
        
        RestaurantDTO r4 = RestaurantMapper.toDTO(r4Domain);
        r4.setId(counter.getAndIncrement());
        r4.setPriceRange("€€€");
        r4.setEstablishmentType(EstablishmentType.RESTAURANT);
        r4.setOpen(true);
        restaurants.add(r4);

        // ... Ajoute les autres restaurants de la même manière
    }

    /**
     * ✅ Méthode helper pour ajouter des TimeSlots à un restaurant
     * Génère des créneaux de 30 minutes entre startTime et endTime
     */
    private void addTimeSlots(Restaurant restaurant, String startTime, String endTime, int capacity) {
        LocalTime start = LocalTime.parse(startTime);
        LocalTime end = LocalTime.parse(endTime);
        LocalTime current = start;
        
        while (current.plusMinutes(30).isBefore(end) || current.plusMinutes(30).equals(end)) {
            TimeSlot slot = new TimeSlot(current, current.plusMinutes(30));
            restaurant.setCapacity(slot, capacity);
            current = current.plusMinutes(30);
        }
    }

    public List<RestaurantDTO> findAll() {
        return restaurants;
    }

    public RestaurantDTO findById(long id) {
        return restaurants.stream()
                .filter(r -> r.getId() == id)
                .findFirst()
                .orElse(null);
    }
    
    /**
     * ✅ NOUVELLE MÉTHODE : Récupérer les TimeSlots d'un restaurant
     */
    public List<TimeSlot> getTimeSlots(long restaurantId) {
        RestaurantDTO dto = findById(restaurantId);
        if (dto == null) return new ArrayList<>();
        
        // Convertir DTO -> Domain pour accéder aux TimeSlots
        Restaurant domain = RestaurantMapper.fromDTO(dto);
        return domain.getAvailableTimeSlots();
    }
}