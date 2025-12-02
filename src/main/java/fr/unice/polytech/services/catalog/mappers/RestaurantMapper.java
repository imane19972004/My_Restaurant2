package fr.unice.polytech.services.catalog.mappers;

import fr.unice.polytech.api.dto.*;
import fr.unice.polytech.dishes.*;
import fr.unice.polytech.restaurants.OpeningHours;
import fr.unice.polytech.restaurants.Restaurant;
import fr.unice.polytech.restaurants.TimeSlot;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RestaurantMapper {

    public static RestaurantDTO toDTO(Restaurant restaurant) {
        RestaurantDTO dto = new RestaurantDTO();

        dto.setId((long) restaurant.hashCode());
        dto.setName(restaurant.getRestaurantName());
        dto.setCuisineType(restaurant.getCuisineType() != null ?
                restaurant.getCuisineType() : DishType.GENERAL);

        // ========== Dishes ==========
        if (restaurant.getDishes() != null && !restaurant.getDishes().isEmpty()) {
            List<DishDTO> dishDTOs = restaurant.getDishes().stream()
                    .map(RestaurantMapper::dishToDTO)
                    .collect(Collectors.toList());
            dto.setDishes(dishDTOs);
        } else {
            dto.setDishes(new ArrayList<>());
        }

        // ========== Opening Hours ==========
        if (restaurant.getOpeningHours() != null && !restaurant.getOpeningHours().isEmpty()) {
            List<OpeningHoursDTO> hoursDTOs = restaurant.getOpeningHours().stream()
                    .map(RestaurantMapper::openingHoursToDTO)
                    .collect(Collectors.toList());
            dto.setOpeningHours(hoursDTOs);
        } else {
            dto.setOpeningHours(new ArrayList<>());
        }

        // ========== ✅ TimeSlots avec capacités (LE FIX PRINCIPAL) ==========
        if (restaurant.getCapacityByTimeSlot() != null && !restaurant.getCapacityByTimeSlot().isEmpty()) {
            System.out.println("🔍 RestaurantMapper: Converting " +
                    restaurant.getCapacityByTimeSlot().size() + " TimeSlots for " + restaurant.getRestaurantName());

            List<TimeSlotDTO> timeSlotDTOs = restaurant.getCapacityByTimeSlot().entrySet().stream()
                    .map(entry -> {
                        TimeSlot slot = entry.getKey();
                        Integer capacity = entry.getValue();

                        TimeSlotDTO slotDTO = new TimeSlotDTO();
                        slotDTO.setStartTime(slot.getStartTime().toString());
                        slotDTO.setEndTime(slot.getEndTime().toString());
                        slotDTO.setAvailableCapacity(capacity);
                        slotDTO.setDayOfWeek(slot.getDayOfWeek() != null ? slot.getDayOfWeek().toString() : null);

                        System.out.println("  📅 TimeSlot: " + slotDTO.getStartTime() +
                                " - " + slotDTO.getEndTime() + " (capacity: " + capacity + ")");

                        return slotDTO;
                    })
                    .collect(Collectors.toList());
            dto.setTimeSlots(timeSlotDTOs);

            System.out.println("✅ RestaurantMapper: " + timeSlotDTOs.size() + " TimeSlots converted");
        } else {
            System.out.println("⚠️ RestaurantMapper: No TimeSlots found for " + restaurant.getRestaurantName());
            dto.setTimeSlots(new ArrayList<>());
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

        if (dish.getToppings() != null && !dish.getToppings().isEmpty()) {
            List<ToppingDTO> toppingDTOs = dish.getToppings().stream()
                    .map(t -> new ToppingDTO(t.getName(), t.getPrice()))
                    .collect(Collectors.toList());
            dto.setToppings(toppingDTOs);
        } else {
            dto.setToppings(new ArrayList<>());
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

        System.out.println("🔄 RestaurantMapper.fromDTO: Converting " + dto.getName());

        Restaurant restaurant = new Restaurant.Builder(dto.getName())
                .withCuisineType(dto.getCuisineType())
                .build();

        // ========== Ajouter les plats ==========
        if (dto.getDishes() != null && !dto.getDishes().isEmpty()) {
            dto.getDishes().forEach(dishDTO -> {
                restaurant.addDish(
                        dishDTO.getName(),
                        dishDTO.getDescription() != null ? dishDTO.getDescription() : "",
                        dishDTO.getPrice()
                );

                if (dishDTO.getCategory() != null && !restaurant.getDishes().isEmpty()) {
                    try {
                        Dish lastDish = restaurant.getDishes().get(restaurant.getDishes().size() - 1);
                        DishCategory category = DishCategory.valueOf(dishDTO.getCategory());
                        lastDish.setCategory(category);

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

        // ========== Ajouter les horaires d'ouverture ==========
        if (dto.getOpeningHours() != null && !dto.getOpeningHours().isEmpty()) {
            dto.getOpeningHours().forEach(slotDTO -> {
                try {
                    restaurant.addOpeningHours(dtoToOpeningHours(slotDTO));
                } catch (Exception e) {
                    System.err.println("Invalid opening hours: " + e.getMessage());
                }
            });
        }

        // ========== ✅ Ajouter les TimeSlots avec capacités ==========
        if (dto.getTimeSlots() != null && !dto.getTimeSlots().isEmpty()) {
            System.out.println("🔄 RestaurantMapper.fromDTO: Processing " +
                    dto.getTimeSlots().size() + " TimeSlots");

            dto.getTimeSlots().forEach(slotDTO -> {
                try {
                    LocalTime start = LocalTime.parse(slotDTO.getStartTime());
                    LocalTime end = LocalTime.parse(slotDTO.getEndTime());
                    TimeSlot slot = new TimeSlot(start, end);
                    restaurant.setCapacity(slot, slotDTO.getAvailableCapacity());

                    System.out.println("  📅 Added TimeSlot: " + slotDTO.getStartTime() +
                            " - " + slotDTO.getEndTime() + " (capacity: " + slotDTO.getAvailableCapacity() + ")");
                } catch (Exception e) {
                    System.err.println("❌ Invalid time slot: " + e.getMessage());
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