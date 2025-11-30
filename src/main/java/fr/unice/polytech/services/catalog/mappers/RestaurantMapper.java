package fr.unice.polytech.services.catalog.mappers;

import fr.unice.polytech.api.dto.DishDTO;
import fr.unice.polytech.api.dto.OpeningHoursDTO;
import fr.unice.polytech.api.dto.RestaurantDTO;
import fr.unice.polytech.api.dto.ToppingDTO;
import fr.unice.polytech.dishes.Dish;
import fr.unice.polytech.dishes.DishCategory;
import fr.unice.polytech.dishes.DishType;
import fr.unice.polytech.dishes.Topping;
import fr.unice.polytech.restaurants.OpeningHours;
import fr.unice.polytech.restaurants.Restaurant;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RestaurantMapper {

    public static RestaurantDTO toDTO(Restaurant restaurant) {
        RestaurantDTO dto = new RestaurantDTO();

        // ✅ FIX CRITIQUE : TOUJOURS assigner un ID
        dto.setId((long) restaurant.hashCode());
        dto.setName(restaurant.getRestaurantName());
        dto.setCuisineType(restaurant.getCuisineType() != null ?
                restaurant.getCuisineType() : DishType.GENERAL);

        // ✅ FIX CRITIQUE : TOUJOURS initialiser dishes (même si vide)
        if (restaurant.getDishes() != null && !restaurant.getDishes().isEmpty()) {
            List<DishDTO> dishDTOs = restaurant.getDishes().stream()
                    .map(RestaurantMapper::dishToDTO)
                    .collect(Collectors.toList());
            dto.setDishes(dishDTOs);
        } else {
            dto.setDishes(new ArrayList<>()); // Liste vide par défaut
        }

        // ✅ FIX CRITIQUE : TOUJOURS initialiser openingHours (même si vide)
        if (restaurant.getOpeningHours() != null && !restaurant.getOpeningHours().isEmpty()) {
            List<OpeningHoursDTO> hoursDTOs = restaurant.getOpeningHours().stream()
                    .map(RestaurantMapper::openingHoursToDTO)
                    .collect(Collectors.toList());
            dto.setOpeningHours(hoursDTOs);
        } else {
            dto.setOpeningHours(new ArrayList<>()); // Liste vide par défaut
        }

        return dto;
    }

    private static DishDTO dishToDTO(Dish dish) {
        DishDTO dto = new DishDTO();
        dto.setId((long) dish.hashCode());
        dto.setName(dish.getName());
        dto.setDescription(dish.getDescription());
        dto.setPrice(dish.getPrice());
        dto.setCategory(dish.getCategory() != null ? dish.getCategory().toString() : null);
        dto.setDishType(dish.getCuisineType() != null ? dish.getCuisineType().toString() : "GENERAL");

        // ✅ FIX : TOUJOURS initialiser toppings (même si vide)
        if (dish.getToppings() != null && !dish.getToppings().isEmpty()) {
            List<ToppingDTO> toppingDTOs = dish.getToppings().stream()
                    .map(t -> new ToppingDTO(t.getName(), t.getPrice()))
                    .collect(Collectors.toList());
            dto.setToppings(toppingDTOs);
        } else {
            dto.setToppings(new ArrayList<>()); // Liste vide par défaut
        }

        return dto;
    }

    private static OpeningHoursDTO openingHoursToDTO(OpeningHours hours) {
        return new OpeningHoursDTO(
                hours.getDay().toString(),
                hours.getOpeningTime().toString(),
                hours.getClosingTime().toString()
        );
    }

    public static Restaurant fromDTO(RestaurantDTO dto) {
        if (dto == null) return null;

        Restaurant restaurant = new Restaurant.Builder(dto.getName())
                .withCuisineType(dto.getCuisineType())
                .build();

        // ✅ Ajouter les plats avec catégories
        if (dto.getDishes() != null && !dto.getDishes().isEmpty()) {
            dto.getDishes().forEach(dishDTO -> {
                restaurant.addDish(
                        dishDTO.getName(),
                        dishDTO.getDescription() != null ? dishDTO.getDescription() : "",
                        dishDTO.getPrice()
                );

                // Set category on the last added dish
                if (dishDTO.getCategory() != null && !restaurant.getDishes().isEmpty()) {
                    try {
                        Dish lastDish = restaurant.getDishes().get(restaurant.getDishes().size() - 1);
                        DishCategory category = DishCategory.valueOf(dishDTO.getCategory());
                        lastDish.setCategory(category);

                        // ✅ Ajouter les toppings
                        if (dishDTO.getToppings() != null && !dishDTO.getToppings().isEmpty()) {
                            dishDTO.getToppings().forEach(toppingDTO -> {
                                Topping topping = new Topping(toppingDTO.getName(), toppingDTO.getPrice());
                                lastDish.addTopping(topping);
                            });
                        }
                    } catch (IllegalArgumentException e) {
                        System.err.println("Invalid category: " + dishDTO.getCategory());
                    }
                }
            });
        }

        // ✅ Ajouter les créneaux d'ouverture
        if (dto.getOpeningHours() != null && !dto.getOpeningHours().isEmpty()) {
            dto.getOpeningHours().forEach(slotDTO -> {
                try {
                    restaurant.addOpeningHours(dtoToOpeningHours(slotDTO));
                } catch (Exception e) {
                    System.err.println("Invalid opening hours: " + e.getMessage());
                }
            });
        }

        return restaurant;
    }

    private static OpeningHours dtoToOpeningHours(OpeningHoursDTO dto) {
        DayOfWeek day = DayOfWeek.valueOf(dto.getDay());
        LocalTime openingTime = LocalTime.parse(dto.getOpeningTime());
        LocalTime closingTime = LocalTime.parse(dto.getClosingTime());

        return new OpeningHours(day, openingTime, closingTime);
    }
}