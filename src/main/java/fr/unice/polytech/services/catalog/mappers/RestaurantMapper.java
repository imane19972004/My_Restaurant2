package fr.unice.polytech.services.catalog.mappers;

import fr.unice.polytech.api.dto.DishDTO;
import fr.unice.polytech.api.dto.OpeningHoursDTO;
import fr.unice.polytech.api.dto.RestaurantDTO;
import fr.unice.polytech.dishes.Dish;
import fr.unice.polytech.dishes.DishCategory;
import fr.unice.polytech.dishes.DishType;
import fr.unice.polytech.restaurants.OpeningHours;
import fr.unice.polytech.restaurants.Restaurant;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

public class RestaurantMapper {
    
    public static RestaurantDTO toDTO(Restaurant restaurant) {
        RestaurantDTO dto = new RestaurantDTO();
        dto.setId((long) restaurant.hashCode());
        dto.setName(restaurant.getRestaurantName());
        dto.setCuisineType(restaurant.getCuisineType() != null ?
                           restaurant.getCuisineType() : DishType.GENERAL);

        List<DishDTO> dishDTOs = restaurant.getDishes().stream()
                .map(RestaurantMapper::dishToDTO)
                .collect(Collectors.toList());
        dto.setDishes(dishDTOs);
        
        List<OpeningHoursDTO> hoursDTOs = restaurant.getOpeningHours().stream()
                .map(RestaurantMapper::openingHoursToDTO)
                .collect(Collectors.toList());
        dto.setOpeningHours(hoursDTOs);
        
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

        // Ajouter les plats avec catégories
        if (dto.getDishes() != null) {
            dto.getDishes().forEach(dishDTO -> {
                restaurant.addDish(
                        dishDTO.getName(),
                        dishDTO.getDescription(),
                        dishDTO.getPrice()
                );
                
                // Set category on the last added dish
                if (dishDTO.getCategory() != null && !restaurant.getDishes().isEmpty()) {
                    try {
                        Dish lastDish = restaurant.getDishes().get(restaurant.getDishes().size() - 1);
                        DishCategory category = DishCategory.valueOf(dishDTO.getCategory());
                        lastDish.setCategory(category);
                    } catch (IllegalArgumentException e) {
                        throw new IllegalArgumentException("Invalid category: " + dishDTO.getCategory(), e);
                    }
                }
            });
        }

        // Ajouter les créneaux d'ouverture
        if (dto.getOpeningHours() != null) {
            dto.getOpeningHours().forEach(slotDTO ->
                    restaurant.addOpeningHours(dtoToOpeningHours(slotDTO))
            );
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