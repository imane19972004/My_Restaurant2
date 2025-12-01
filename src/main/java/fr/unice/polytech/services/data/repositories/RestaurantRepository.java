package fr.unice.polytech.services.data.repositories;

import fr.unice.polytech.api.dto.DishDTO;
import fr.unice.polytech.api.dto.OpeningHoursDTO;
import fr.unice.polytech.api.dto.RestaurantDTO;
import fr.unice.polytech.api.dto.ToppingDTO;
import fr.unice.polytech.dishes.DishType;
import fr.unice.polytech.restaurants.EstablishmentType;

import java.util.ArrayList;
import java.util.Arrays;
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
        RestaurantDTO r1 = new RestaurantDTO();
        r1.setId(counter.getAndIncrement());  // ID = 1
        r1.setName("La Bella Vita");
        r1.setCuisineType(DishType.ITALIAN);
        r1.setPriceRange("€€");
        r1.setEstablishmentType(EstablishmentType.RESTAURANT);
        r1.setOpen(true);

        List<DishDTO> r1Dishes = new ArrayList<>();
        r1Dishes.add(createDish("Bruschetta", "Tomato, basil, garlic on toasted bread", 6.50, "STARTER", "ITALIAN",
                Arrays.asList(new ToppingDTO("Extra Mozzarella", 1.50), new ToppingDTO("Prosciutto", 2.00))));
        r1Dishes.add(createDish("Margherita Pizza", "Tomato sauce, mozzarella, basil", 12.50, "MAIN_COURSE", "ITALIAN",
                Arrays.asList(new ToppingDTO("Extra Cheese", 1.50), new ToppingDTO("Olives", 0.80),
                        new ToppingDTO("Mushrooms", 1.00), new ToppingDTO("Pepperoni", 2.00))));
        r1Dishes.add(createDish("Pasta Carbonara", "Creamy bacon pasta with egg and parmesan", 14.00, "MAIN_COURSE", "ITALIAN",
                Arrays.asList(new ToppingDTO("Extra Bacon", 2.50), new ToppingDTO("Extra Parmesan", 1.00))));
        r1Dishes.add(createDish("Tiramisu", "Classic Italian coffee dessert", 6.00, "DESSERT", "ITALIAN", new ArrayList<>()));

        r1.setDishes(r1Dishes);
        r1.setOpeningHours(Arrays.asList(
                new OpeningHoursDTO("MONDAY", "11:30", "14:00"), new OpeningHoursDTO("MONDAY", "18:30", "22:00"),
                new OpeningHoursDTO("TUESDAY", "11:30", "14:00"), new OpeningHoursDTO("TUESDAY", "18:30", "22:00"),
                new OpeningHoursDTO("WEDNESDAY", "11:30", "14:00"), new OpeningHoursDTO("WEDNESDAY", "18:30", "22:00"),
                new OpeningHoursDTO("THURSDAY", "11:30", "14:00"), new OpeningHoursDTO("THURSDAY", "18:30", "22:00"),
                new OpeningHoursDTO("FRIDAY", "11:30", "14:00"), new OpeningHoursDTO("FRIDAY", "18:30", "23:00"),
                new OpeningHoursDTO("SATURDAY", "18:30", "23:00")
        ));
        restaurants.add(r1);

        // ====================== RESTAURANT 2 ======================
        RestaurantDTO r2 = new RestaurantDTO();
        r2.setId(counter.getAndIncrement());  // ID = 2
        r2.setName("Sakura Sushi");
        r2.setCuisineType(DishType.JAPANESE);
        r2.setPriceRange("€€");
        r2.setEstablishmentType(EstablishmentType.RESTAURANT);
        r2.setOpen(true);

        List<DishDTO> r2Dishes = new ArrayList<>();
        r2Dishes.add(createDish("Miso Soup", "Traditional Japanese soup", 4.50, "STARTER", "JAPANESE",
                Arrays.asList(new ToppingDTO("Extra Tofu", 1.00), new ToppingDTO("Seaweed", 0.50))));
        r2Dishes.add(createDish("California Roll", "Crab, avocado, cucumber", 13.00, "MAIN_COURSE", "JAPANESE",
                Arrays.asList(new ToppingDTO("Extra Avocado", 2.00), new ToppingDTO("Spicy Mayo", 0.80), new ToppingDTO("Wasabi", 0.50))));
        r2Dishes.add(createDish("Salmon Sashimi", "Fresh salmon slices", 16.00, "MAIN_COURSE", "JAPANESE",
                Arrays.asList(new ToppingDTO("Soy Sauce", 0.00), new ToppingDTO("Extra Ginger", 0.50))));

        r2.setDishes(r2Dishes);
        r2.setOpeningHours(Arrays.asList(
                new OpeningHoursDTO("TUESDAY", "12:00", "14:30"), new OpeningHoursDTO("TUESDAY", "19:00", "22:30"),
                new OpeningHoursDTO("WEDNESDAY", "12:00", "14:30"), new OpeningHoursDTO("WEDNESDAY", "19:00", "22:30"),
                new OpeningHoursDTO("THURSDAY", "12:00", "14:30"), new OpeningHoursDTO("THURSDAY", "19:00", "22:30"),
                new OpeningHoursDTO("FRIDAY", "12:00", "14:30"), new OpeningHoursDTO("FRIDAY", "19:00", "23:00"),
                new OpeningHoursDTO("SATURDAY", "12:00", "14:30"), new OpeningHoursDTO("SATURDAY", "19:00", "23:00")
        ));
        restaurants.add(r2);

        // ====================== RESTAURANT 3 ======================
        RestaurantDTO r3 = new RestaurantDTO();
        r3.setId(counter.getAndIncrement());  // ID = 3
        r3.setName("El Sombrero");
        r3.setCuisineType(DishType.MEXICAN);
        r3.setPriceRange("€");
        r3.setEstablishmentType(EstablishmentType.FOOD_TRUCK);
        r3.setOpen(false);

        List<DishDTO> r3Dishes = new ArrayList<>();
        r3Dishes.add(createDish("Tacos Al Pastor", "Pork and pineapple", 9.00, "MAIN_COURSE", "MEXICAN",
                Arrays.asList(new ToppingDTO("Extra Guacamole", 1.50), new ToppingDTO("Sour Cream", 0.80))));
        r3Dishes.add(createDish("Nachos Supreme", "Cheese, jalapeños, salsa", 7.50, "STARTER", "MEXICAN",
                Arrays.asList(new ToppingDTO("Extra Cheese", 1.00))));

        r3.setDishes(r3Dishes);
        r3.setOpeningHours(new ArrayList<>());
        restaurants.add(r3);

        // ====================== RESTAURANT 4 ======================
        RestaurantDTO r4 = new RestaurantDTO();
        r4.setId(counter.getAndIncrement());  // ID = 4
        r4.setName("Bombay Spice");
        r4.setCuisineType(DishType.INDIAN);
        r4.setPriceRange("€€€");
        r4.setEstablishmentType(EstablishmentType.RESTAURANT);
        r4.setOpen(true);

        List<DishDTO> r4Dishes = new ArrayList<>();
        r4Dishes.add(createDish("Vegetable Samosa", "Crispy pastry with spiced vegetables", 5.50, "STARTER", "INDIAN",
                Arrays.asList(new ToppingDTO("Mint Chutney", 0.50), new ToppingDTO("Tamarind Sauce", 0.50))));
        r4Dishes.add(createDish("Butter Chicken", "Creamy tomato curry", 15.00, "MAIN_COURSE", "INDIAN",
                Arrays.asList(new ToppingDTO("Extra Naan", 2.00), new ToppingDTO("Rice", 3.00), new ToppingDTO("Extra Spicy", 0.00))));
        r4Dishes.add(createDish("Garlic Naan", "Garlic flatbread", 3.50, "SIDE", "INDIAN", new ArrayList<>()));

        r4.setDishes(r4Dishes);
        r4.setOpeningHours(Arrays.asList(
                new OpeningHoursDTO("MONDAY", "12:00", "14:00"), new OpeningHoursDTO("MONDAY", "18:00", "22:00"),
                new OpeningHoursDTO("WEDNESDAY", "12:00", "14:00"), new OpeningHoursDTO("WEDNESDAY", "18:00", "22:00"),
                new OpeningHoursDTO("FRIDAY", "12:00", "14:00"), new OpeningHoursDTO("FRIDAY", "18:00", "23:00"),
                new OpeningHoursDTO("SATURDAY", "18:00", "23:00")
        ));
        restaurants.add(r4);

        // ====================== RESTAURANT 5 ======================
        RestaurantDTO r5 = new RestaurantDTO();
        r5.setId(counter.getAndIncrement());  // ID = 5
        r5.setName("Le Pain Doré");
        r5.setCuisineType(DishType.FRENCH);
        r5.setPriceRange("€€");
        r5.setEstablishmentType(EstablishmentType.RESTAURANT);
        r5.setOpen(true);

        List<DishDTO> r5Dishes = new ArrayList<>();
        r5Dishes.add(createDish("Croissant", "Fresh buttery croissant", 2.20, "STARTER", "FRENCH",
                Arrays.asList(new ToppingDTO("Jam", 0.50), new ToppingDTO("Butter", 0.30))));
        r5Dishes.add(createDish("Pain au Chocolat", "Chocolate-filled pastry", 2.40, "DESSERT", "FRENCH", new ArrayList<>()));

        r5.setDishes(r5Dishes);
        r5.setOpeningHours(Arrays.asList(
                new OpeningHoursDTO("MONDAY", "07:00", "14:00"), new OpeningHoursDTO("TUESDAY", "07:00", "14:00"),
                new OpeningHoursDTO("WEDNESDAY", "07:00", "14:00"), new OpeningHoursDTO("THURSDAY", "07:00", "14:00"),
                new OpeningHoursDTO("FRIDAY", "07:00", "14:00"), new OpeningHoursDTO("SATURDAY", "08:00", "13:00")
        ));
        restaurants.add(r5);

        // ====================== RESTAURANT 6 ======================
        RestaurantDTO r6 = new RestaurantDTO();
        r6.setId(counter.getAndIncrement());  // ID = 6
        r6.setName("Elios Crous");
        r6.setCuisineType(DishType.AMERICAN);
        r6.setPriceRange("€");
        r6.setEstablishmentType(EstablishmentType.CROUS);
        r6.setOpen(true);

        List<DishDTO> r6Dishes = new ArrayList<>();
        r6Dishes.add(createDish("Cheeseburger", "Beef patty with cheese", 8.50, "MAIN_COURSE", "AMERICAN",
                Arrays.asList(new ToppingDTO("Extra Cheese", 1.00), new ToppingDTO("Bacon", 1.50), new ToppingDTO("Fries", 2.50))));
        r6Dishes.add(createDish("BBQ Double Burger", "BBQ sauce and bacon", 11.00, "MAIN_COURSE", "AMERICAN",
                Arrays.asList(new ToppingDTO("Onion Rings", 2.00), new ToppingDTO("Extra BBQ Sauce", 0.50))));
        r6Dishes.add(createDish("Soda", "Coca-Cola, Sprite, Fanta", 2.00, "DRINK", "AMERICAN", new ArrayList<>()));

        r6.setDishes(r6Dishes);
        r6.setOpeningHours(Arrays.asList(
                new OpeningHoursDTO("MONDAY", "11:30", "14:30"), new OpeningHoursDTO("MONDAY", "18:00", "20:00"),
                new OpeningHoursDTO("TUESDAY", "11:30", "14:30"), new OpeningHoursDTO("TUESDAY", "18:00", "20:00"),
                new OpeningHoursDTO("WEDNESDAY", "11:30", "14:30"), new OpeningHoursDTO("WEDNESDAY", "18:00", "20:00"),
                new OpeningHoursDTO("THURSDAY", "11:30", "14:30"), new OpeningHoursDTO("THURSDAY", "18:00", "20:00"),
                new OpeningHoursDTO("FRIDAY", "11:30", "14:30"), new OpeningHoursDTO("FRIDAY", "18:00", "20:00")
        ));
        restaurants.add(r6);

        // ====================== RESTAURANT 7 ======================
        RestaurantDTO r7 = new RestaurantDTO();
        r7.setId(counter.getAndIncrement());  // ID = 7
        r7.setName("Green Leaf Café");
        r7.setCuisineType(DishType.VEGETARIAN);
        r7.setPriceRange("€€");
        r7.setEstablishmentType(EstablishmentType.RESTAURANT);
        r7.setOpen(true);

        List<DishDTO> r7Dishes = new ArrayList<>();
        r7Dishes.add(createDish("Quinoa Salad", "Quinoa, kale, avocado, nuts", 9.50, "MAIN_COURSE", "VEGETARIAN",
                Arrays.asList(new ToppingDTO("Extra Avocado", 1.50), new ToppingDTO("Tofu", 2.00))));
        r7Dishes.add(createDish("Green Smoothie", "Spinach, apple, banana", 4.50, "DRINK", "VEGETARIAN", new ArrayList<>()));

        r7.setDishes(r7Dishes);
        r7.setOpeningHours(Arrays.asList(
                new OpeningHoursDTO("MONDAY", "08:00", "16:00"), new OpeningHoursDTO("TUESDAY", "08:00", "16:00"),
                new OpeningHoursDTO("WEDNESDAY", "08:00", "16:00"), new OpeningHoursDTO("THURSDAY", "08:00", "16:00"),
                new OpeningHoursDTO("FRIDAY", "08:00", "16:00")
        ));
        restaurants.add(r7);

        // ====================== RESTAURANT 8 ======================
        RestaurantDTO r8 = new RestaurantDTO();
        r8.setId(counter.getAndIncrement());  // ID = 8
        r8.setName("Dragon Wok");
        r8.setCuisineType(DishType.CHINESE);
        r8.setPriceRange("€€");
        r8.setEstablishmentType(EstablishmentType.RESTAURANT);
        r8.setOpen(true);

        List<DishDTO> r8Dishes = new ArrayList<>();
        r8Dishes.add(createDish("Chicken Chow Mein", "Noodles with chicken and vegetables", 12.00, "MAIN_COURSE", "CHINESE",
                Arrays.asList(new ToppingDTO("Extra Chicken", 2.50), new ToppingDTO("Extra Vegetables", 1.50))));
        r8Dishes.add(createDish("Spring Rolls", "Crispy vegetable rolls", 5.50, "STARTER", "CHINESE", new ArrayList<>()));

        r8.setDishes(r8Dishes);
        r8.setOpeningHours(Arrays.asList(
                new OpeningHoursDTO("MONDAY", "11:00", "15:00"), new OpeningHoursDTO("MONDAY", "18:00", "22:00"),
                new OpeningHoursDTO("TUESDAY", "11:00", "15:00"), new OpeningHoursDTO("TUESDAY", "18:00", "22:00"),
                new OpeningHoursDTO("WEDNESDAY", "11:00", "15:00"), new OpeningHoursDTO("WEDNESDAY", "18:00", "22:00"),
                new OpeningHoursDTO("THURSDAY", "11:00", "15:00"), new OpeningHoursDTO("THURSDAY", "18:00", "22:00"),
                new OpeningHoursDTO("FRIDAY", "11:00", "15:00"), new OpeningHoursDTO("FRIDAY", "18:00", "23:00")
        ));
        restaurants.add(r8);

        // ====================== RESTAURANT 9 ======================
        RestaurantDTO r9 = new RestaurantDTO();
        r9.setId(counter.getAndIncrement());  // ID = 9
        r9.setName("Mediterraneo");
        r9.setCuisineType(DishType.MEDITERRANEAN);
        r9.setPriceRange("€€");
        r9.setEstablishmentType(EstablishmentType.RESTAURANT);
        r9.setOpen(true);

        List<DishDTO> r9Dishes = new ArrayList<>();
        r9Dishes.add(createDish("Hummus Plate", "Chickpea dip with pita bread", 6.00, "STARTER", "MEDITERRANEAN",
                Arrays.asList(new ToppingDTO("Extra Olive Oil", 0.50), new ToppingDTO("Paprika", 0.30))));
        r9Dishes.add(createDish("Falafel Wrap", "Falafel, lettuce, tomato, tahini", 9.00, "MAIN_COURSE", "MEDITERRANEAN",
                Arrays.asList(new ToppingDTO("Extra Tahini", 0.50))));

        r9.setDishes(r9Dishes);
        r9.setOpeningHours(Arrays.asList(
                new OpeningHoursDTO("MONDAY", "11:00", "15:00"), new OpeningHoursDTO("TUESDAY", "11:00", "15:00"),
                new OpeningHoursDTO("WEDNESDAY", "11:00", "15:00"), new OpeningHoursDTO("THURSDAY", "11:00", "15:00"),
                new OpeningHoursDTO("FRIDAY", "11:00", "15:00")
        ));
        restaurants.add(r9);

        // ====================== RESTAURANT 10 ======================
        RestaurantDTO r10 = new RestaurantDTO();
        r10.setId(counter.getAndIncrement());  // ID = 10
        r10.setName("Burger Time");
        r10.setCuisineType(DishType.AMERICAN);
        r10.setPriceRange("€");
        r10.setEstablishmentType(EstablishmentType.FOOD_TRUCK);
        r10.setOpen(true);

        List<DishDTO> r10Dishes = new ArrayList<>();
        r10Dishes.add(createDish("Classic Burger", "Beef patty, lettuce, tomato", 7.50, "MAIN_COURSE", "AMERICAN",
                Arrays.asList(new ToppingDTO("Cheese", 1.00), new ToppingDTO("Bacon", 1.50))));
        r10Dishes.add(createDish("French Fries", "Crispy golden fries", 3.00, "SIDE", "AMERICAN", new ArrayList<>()));

        r10.setDishes(r10Dishes);
        r10.setOpeningHours(Arrays.asList(
                new OpeningHoursDTO("MONDAY", "11:00", "14:00"), new OpeningHoursDTO("TUESDAY", "11:00", "14:00"),
                new OpeningHoursDTO("WEDNESDAY", "11:00", "14:00"), new OpeningHoursDTO("THURSDAY", "11:00", "14:00"),
                new OpeningHoursDTO("FRIDAY", "11:00", "14:00")
        ));
        restaurants.add(r10);

        // ====================== RESTAURANT 11 ======================
        RestaurantDTO r11 = new RestaurantDTO();
        r11.setId(counter.getAndIncrement());  // ID = 11
        r11.setName("Pasta Fresca");
        r11.setCuisineType(DishType.ITALIAN);
        r11.setPriceRange("€€");
        r11.setEstablishmentType(EstablishmentType.RESTAURANT);
        r11.setOpen(true);

        List<DishDTO> r11Dishes = new ArrayList<>();
        r11Dishes.add(createDish("Lasagna", "Layered pasta with meat sauce", 13.50, "MAIN_COURSE", "ITALIAN",
                Arrays.asList(new ToppingDTO("Extra Cheese", 1.50))));

        r11.setDishes(r11Dishes);
        r11.setOpeningHours(Arrays.asList(
                new OpeningHoursDTO("MONDAY", "12:00", "14:30"), new OpeningHoursDTO("TUESDAY", "12:00", "14:30"),
                new OpeningHoursDTO("WEDNESDAY", "12:00", "14:30"), new OpeningHoursDTO("THURSDAY", "12:00", "14:30"),
                new OpeningHoursDTO("FRIDAY", "12:00", "14:30")
        ));
        restaurants.add(r11);

        // ====================== RESTAURANT 12 ======================
        RestaurantDTO r12 = new RestaurantDTO();
        r12.setId(counter.getAndIncrement());  // ID = 12
        r12.setName("Curry Palace");
        r12.setCuisineType(DishType.INDIAN);
        r12.setPriceRange("€€");
        r12.setEstablishmentType(EstablishmentType.RESTAURANT);
        r12.setOpen(true);

        List<DishDTO> r12Dishes = new ArrayList<>();
        r12Dishes.add(createDish("Chicken Tikka Masala", "Spiced chicken in creamy tomato sauce", 14.50, "MAIN_COURSE", "INDIAN",
                Arrays.asList(new ToppingDTO("Extra Naan", 2.00), new ToppingDTO("Rice", 3.00))));
        r12Dishes.add(createDish("Mango Lassi", "Mango yogurt drink", 3.50, "DRINK", "INDIAN", new ArrayList<>()));

        r12.setDishes(r12Dishes);
        r12.setOpeningHours(Arrays.asList(
                new OpeningHoursDTO("MONDAY", "11:30", "14:30"), new OpeningHoursDTO("TUESDAY", "11:30", "14:30"),
                new OpeningHoursDTO("WEDNESDAY", "11:30", "14:30"), new OpeningHoursDTO("THURSDAY", "11:30", "14:30"),
                new OpeningHoursDTO("FRIDAY", "11:30", "14:30")
        ));
        restaurants.add(r12);
    }

    /**
     * ✅ Méthode helper pour créer des DishDTO complets
     */
    private DishDTO createDish(String name, String description, double price,
                               String category, String dishType, List<ToppingDTO> toppings) {
        DishDTO dish = new DishDTO();
        dish.setId(dishCounter.getAndIncrement());
        dish.setName(name);
        dish.setDescription(description);
        dish.setPrice(price);
        dish.setCategory(category);
        dish.setDishType(dishType);
        dish.setToppings(toppings != null ? toppings : new ArrayList<>());
        return dish;
    }

    public List<RestaurantDTO> findAll() {
        System.out.println("📋 RestaurantRepository.findAll() returning " + restaurants.size() + " restaurants");
        return restaurants;
    }

    public RestaurantDTO findById(long id) {
        System.out.println("🔍 RestaurantRepository.findById(" + id + ")");
        RestaurantDTO found = restaurants.stream()
                .filter(r -> r.getId() == id)
                .findFirst()
                .orElse(null);

        if (found != null) {
            System.out.println("✅ Found restaurant: " + found.getName() + " with " + found.getDishes().size() + " dishes");
        } else {
            System.err.println("❌ Restaurant not found with ID: " + id);
            System.out.println("Available IDs: " + restaurants.stream().map(r -> r.getId()).toList());
        }

        return found;
    }
}