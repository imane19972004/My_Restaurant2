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
    private final AtomicLong counter = new AtomicLong(1); // Pour les restaurants
    private final AtomicLong dishCounter = new AtomicLong(1); // Pour les plats

    public RestaurantRepository() {
        seed();
    }

    private void seed() {
        // ====================== RESTAURANT 1 ======================
        RestaurantDTO r1 = new RestaurantDTO();
        r1.setId(counter.getAndIncrement());
        r1.setName("La Bella Vita");
        r1.setCuisineType(DishType.ITALIAN);
        r1.setPriceRange("€€");
        r1.setEstablishmentType(EstablishmentType.RESTAURANT);
        r1.setOpen(true);

        List<DishDTO> r1Dishes = new ArrayList<>();
        // Starter
        DishDTO bruschetta = new DishDTO();
        bruschetta.setId(dishCounter.getAndIncrement());
        bruschetta.setName("Bruschetta");
        bruschetta.setDescription("Tomato, basil, garlic on toasted bread");
        bruschetta.setPrice(6.50);
        bruschetta.setCategory("STARTER");
        bruschetta.setDishType("ITALIAN");
        bruschetta.setToppings(Arrays.asList(
                new ToppingDTO("Extra Mozzarella", 1.50),
                new ToppingDTO("Prosciutto", 2.00)
        ));
        r1Dishes.add(bruschetta);

        // Main courses
        DishDTO pizza = new DishDTO();
        pizza.setId(dishCounter.getAndIncrement());
        pizza.setName("Margherita Pizza");
        pizza.setDescription("Tomato sauce, mozzarella, basil");
        pizza.setPrice(12.50);
        pizza.setCategory("MAIN_COURSE");
        pizza.setDishType("ITALIAN");
        pizza.setToppings(Arrays.asList(
                new ToppingDTO("Extra Cheese", 1.50),
                new ToppingDTO("Olives", 0.80),
                new ToppingDTO("Mushrooms", 1.00),
                new ToppingDTO("Pepperoni", 2.00)
        ));
        r1Dishes.add(pizza);

        DishDTO carbonara = new DishDTO();
        carbonara.setId(dishCounter.getAndIncrement());
        carbonara.setName("Pasta Carbonara");
        carbonara.setDescription("Creamy bacon pasta with egg and parmesan");
        carbonara.setPrice(14.00);
        carbonara.setCategory("MAIN_COURSE");
        carbonara.setDishType("ITALIAN");
        carbonara.setToppings(Arrays.asList(
                new ToppingDTO("Extra Bacon", 2.50),
                new ToppingDTO("Extra Parmesan", 1.00)
        ));
        r1Dishes.add(carbonara);

        // Dessert
        DishDTO tiramisu = new DishDTO();
        tiramisu.setId(dishCounter.getAndIncrement());
        tiramisu.setName("Tiramisu");
        tiramisu.setDescription("Classic Italian coffee dessert");
        tiramisu.setPrice(6.00);
        tiramisu.setCategory("DESSERT");
        tiramisu.setDishType("ITALIAN");
        tiramisu.setToppings(new ArrayList<>());
        r1Dishes.add(tiramisu);

        r1.setDishes(r1Dishes);
        r1.setOpeningHours(Arrays.asList(
                new OpeningHoursDTO("MONDAY", "11:30", "14:00"),
                new OpeningHoursDTO("MONDAY", "18:30", "22:00"),
                new OpeningHoursDTO("TUESDAY", "11:30", "14:00"),
                new OpeningHoursDTO("TUESDAY", "18:30", "22:00"),
                new OpeningHoursDTO("WEDNESDAY", "11:30", "14:00"),
                new OpeningHoursDTO("WEDNESDAY", "18:30", "22:00"),
                new OpeningHoursDTO("THURSDAY", "11:30", "14:00"),
                new OpeningHoursDTO("THURSDAY", "18:30", "22:00"),
                new OpeningHoursDTO("FRIDAY", "11:30", "14:00"),
                new OpeningHoursDTO("FRIDAY", "18:30", "23:00"),
                new OpeningHoursDTO("SATURDAY", "18:30", "23:00")
        ));
        restaurants.add(r1);

        // ====================== RESTAURANT 2 ======================
        RestaurantDTO r2 = new RestaurantDTO();
        r2.setId(counter.getAndIncrement());
        r2.setName("Sakura Sushi");
        r2.setCuisineType(DishType.JAPANESE);
        r2.setPriceRange("€€");
        r2.setEstablishmentType(EstablishmentType.RESTAURANT);
        r2.setOpen(true);

        List<DishDTO> r2Dishes = new ArrayList<>();
        // Starter
        DishDTO misoSoup = new DishDTO();
        misoSoup.setId(dishCounter.getAndIncrement());
        misoSoup.setName("Miso Soup");
        misoSoup.setDescription("Traditional Japanese soup");
        misoSoup.setPrice(4.50);
        misoSoup.setCategory("STARTER");
        misoSoup.setDishType("JAPANESE");
        misoSoup.setToppings(Arrays.asList(
                new ToppingDTO("Extra Tofu", 1.00),
                new ToppingDTO("Seaweed", 0.50)
        ));
        r2Dishes.add(misoSoup);

        // Main courses
        DishDTO californiaRoll = new DishDTO();
        californiaRoll.setId(dishCounter.getAndIncrement());
        californiaRoll.setName("California Roll");
        californiaRoll.setDescription("Crab, avocado, cucumber");
        californiaRoll.setPrice(13.00);
        californiaRoll.setCategory("MAIN_COURSE");
        californiaRoll.setDishType("JAPANESE");
        californiaRoll.setToppings(Arrays.asList(
                new ToppingDTO("Extra Avocado", 2.00),
                new ToppingDTO("Spicy Mayo", 0.80),
                new ToppingDTO("Wasabi", 0.50)
        ));
        r2Dishes.add(californiaRoll);

        DishDTO salmonSashimi = new DishDTO();
        salmonSashimi.setId(dishCounter.getAndIncrement());
        salmonSashimi.setName("Salmon Sashimi");
        salmonSashimi.setDescription("Fresh salmon slices");
        salmonSashimi.setPrice(16.00);
        salmonSashimi.setCategory("MAIN_COURSE");
        salmonSashimi.setDishType("JAPANESE");
        salmonSashimi.setToppings(Arrays.asList(
                new ToppingDTO("Soy Sauce", 0.00),
                new ToppingDTO("Extra Ginger", 0.50)
        ));
        r2Dishes.add(salmonSashimi);

        r2.setDishes(r2Dishes);
        r2.setOpeningHours(Arrays.asList(
                new OpeningHoursDTO("TUESDAY", "12:00", "14:30"),
                new OpeningHoursDTO("TUESDAY", "19:00", "22:30"),
                new OpeningHoursDTO("WEDNESDAY", "12:00", "14:30"),
                new OpeningHoursDTO("WEDNESDAY", "19:00", "22:30"),
                new OpeningHoursDTO("THURSDAY", "12:00", "14:30"),
                new OpeningHoursDTO("THURSDAY", "19:00", "22:30"),
                new OpeningHoursDTO("FRIDAY", "12:00", "14:30"),
                new OpeningHoursDTO("FRIDAY", "19:00", "23:00"),
                new OpeningHoursDTO("SATURDAY", "12:00", "14:30"),
                new OpeningHoursDTO("SATURDAY", "19:00", "23:00")
        ));
        restaurants.add(r2);

        // ====================== RESTAURANT 3 ======================
        RestaurantDTO r3 = new RestaurantDTO();
        r3.setId(counter.getAndIncrement());
        r3.setName("El Sombrero");
        r3.setCuisineType(DishType.MEXICAN);
        r3.setPriceRange("€");
        r3.setEstablishmentType(EstablishmentType.FOOD_TRUCK);
        r3.setOpen(false);

        List<DishDTO> r3Dishes = new ArrayList<>();
        DishDTO tacos = new DishDTO();
        tacos.setId(dishCounter.getAndIncrement());
        tacos.setName("Tacos Al Pastor");
        tacos.setDescription("Pork and pineapple");
        tacos.setPrice(9.00);
        tacos.setCategory("MAIN_COURSE");
        tacos.setDishType("MEXICAN");
        tacos.setToppings(Arrays.asList(
                new ToppingDTO("Extra Guacamole", 1.50),
                new ToppingDTO("Sour Cream", 0.80)
        ));
        r3Dishes.add(tacos);

        DishDTO nachos = new DishDTO();
        nachos.setId(dishCounter.getAndIncrement());
        nachos.setName("Nachos Supreme");
        nachos.setDescription("Cheese, jalapeños, salsa");
        nachos.setPrice(7.50);
        nachos.setCategory("STARTER");
        nachos.setDishType("MEXICAN");
        nachos.setToppings(Arrays.asList(
                new ToppingDTO("Extra Cheese", 1.00)
        ));
        r3Dishes.add(nachos);

        r3.setDishes(r3Dishes);
        r3.setOpeningHours(new ArrayList<>());
        restaurants.add(r3);

        // ====================== RESTAURANT 4 ======================
        RestaurantDTO r4 = new RestaurantDTO();
        r4.setId(counter.getAndIncrement());
        r4.setName("Bombay Spice");
        r4.setCuisineType(DishType.INDIAN);
        r4.setPriceRange("€€€");
        r4.setEstablishmentType(EstablishmentType.RESTAURANT);
        r4.setOpen(true);

        List<DishDTO> r4Dishes = new ArrayList<>();
        DishDTO samosa = new DishDTO();
        samosa.setId(dishCounter.getAndIncrement());
        samosa.setName("Vegetable Samosa");
        samosa.setDescription("Crispy pastry with spiced vegetables");
        samosa.setPrice(5.50);
        samosa.setCategory("STARTER");
        samosa.setDishType("INDIAN");
        samosa.setToppings(Arrays.asList(
                new ToppingDTO("Mint Chutney", 0.50),
                new ToppingDTO("Tamarind Sauce", 0.50)
        ));
        r4Dishes.add(samosa);

        DishDTO butterChicken = new DishDTO();
        butterChicken.setId(dishCounter.getAndIncrement());
        butterChicken.setName("Butter Chicken");
        butterChicken.setDescription("Creamy tomato curry");
        butterChicken.setPrice(15.00);
        butterChicken.setCategory("MAIN_COURSE");
        butterChicken.setDishType("INDIAN");
        butterChicken.setToppings(Arrays.asList(
                new ToppingDTO("Extra Naan", 2.00),
                new ToppingDTO("Rice", 3.00),
                new ToppingDTO("Extra Spicy", 0.00)
        ));
        r4Dishes.add(butterChicken);

        DishDTO naan = new DishDTO();
        naan.setId(dishCounter.getAndIncrement());
        naan.setName("Garlic Naan");
        naan.setDescription("Garlic flatbread");
        naan.setPrice(3.50);
        naan.setCategory("SIDE");
        naan.setDishType("INDIAN");
        naan.setToppings(new ArrayList<>());
        r4Dishes.add(naan);

        r4.setDishes(r4Dishes);
        r4.setOpeningHours(Arrays.asList(
                new OpeningHoursDTO("MONDAY", "12:00", "14:00"),
                new OpeningHoursDTO("MONDAY", "18:00", "22:00"),
                new OpeningHoursDTO("WEDNESDAY", "12:00", "14:00"),
                new OpeningHoursDTO("WEDNESDAY", "18:00", "22:00"),
                new OpeningHoursDTO("FRIDAY", "12:00", "14:00"),
                new OpeningHoursDTO("FRIDAY", "18:00", "23:00"),
                new OpeningHoursDTO("SATURDAY", "18:00", "23:00")
        ));
        restaurants.add(r4);

        // ====================== RESTAURANT 5 ======================
        RestaurantDTO r5 = new RestaurantDTO();
        r5.setId(counter.getAndIncrement());
        r5.setName("Le Pain Doré");
        r5.setCuisineType(DishType.FRENCH);
        r5.setPriceRange("€€");
        r5.setEstablishmentType(EstablishmentType.RESTAURANT);
        r5.setOpen(true);

        List<DishDTO> r5Dishes = new ArrayList<>();
        DishDTO croissant = new DishDTO();
        croissant.setId(dishCounter.getAndIncrement());
        croissant.setName("Croissant");
        croissant.setDescription("Fresh buttery croissant");
        croissant.setPrice(2.20);
        croissant.setCategory("STARTER");
        croissant.setDishType("FRENCH");
        croissant.setToppings(Arrays.asList(
                new ToppingDTO("Jam", 0.50),
                new ToppingDTO("Butter", 0.30)
        ));
        r5Dishes.add(croissant);

        DishDTO painChocolat = new DishDTO();
        painChocolat.setId(dishCounter.getAndIncrement());
        painChocolat.setName("Pain au Chocolat");
        painChocolat.setDescription("Chocolate-filled pastry");
        painChocolat.setPrice(2.40);
        painChocolat.setCategory("DESSERT");
        painChocolat.setDishType("FRENCH");
        painChocolat.setToppings(new ArrayList<>());
        r5Dishes.add(painChocolat);

        r5.setDishes(r5Dishes);
        r5.setOpeningHours(Arrays.asList(
                new OpeningHoursDTO("MONDAY", "07:00", "14:00"),
                new OpeningHoursDTO("TUESDAY", "07:00", "14:00"),
                new OpeningHoursDTO("WEDNESDAY", "07:00", "14:00"),
                new OpeningHoursDTO("THURSDAY", "07:00", "14:00"),
                new OpeningHoursDTO("FRIDAY", "07:00", "14:00"),
                new OpeningHoursDTO("SATURDAY", "08:00", "13:00")
        ));
        restaurants.add(r5);

        // ====================== RESTAURANT 6 ======================
        RestaurantDTO r6 = new RestaurantDTO();
        r6.setId(counter.getAndIncrement());
        r6.setName("Elios Crous");
        r6.setCuisineType(DishType.AMERICAN);
        r6.setPriceRange("€");
        r6.setEstablishmentType(EstablishmentType.CROUS);
        r6.setOpen(true);

        List<DishDTO> r6Dishes = new ArrayList<>();
        DishDTO burger = new DishDTO();
        burger.setId(dishCounter.getAndIncrement());
        burger.setName("Cheeseburger");
        burger.setDescription("Beef patty with cheese");
        burger.setPrice(8.50);
        burger.setCategory("MAIN_COURSE");
        burger.setDishType("AMERICAN");
        burger.setToppings(Arrays.asList(
                new ToppingDTO("Extra Cheese", 1.00),
                new ToppingDTO("Bacon", 1.50),
                new ToppingDTO("Fries", 2.50)
        ));
        r6Dishes.add(burger);

        DishDTO bbqBurger = new DishDTO();
        bbqBurger.setId(dishCounter.getAndIncrement());
        bbqBurger.setName("BBQ Double Burger");
        bbqBurger.setDescription("BBQ sauce and bacon");
        bbqBurger.setPrice(11.00);
        bbqBurger.setCategory("MAIN_COURSE");
        bbqBurger.setDishType("AMERICAN");
        bbqBurger.setToppings(Arrays.asList(
                new ToppingDTO("Onion Rings", 2.00),
                new ToppingDTO("Extra BBQ Sauce", 0.50)
        ));
        r6Dishes.add(bbqBurger);

        DishDTO soda = new DishDTO();
        soda.setId(dishCounter.getAndIncrement());
        soda.setName("Soda");
        soda.setDescription("Coca-Cola, Sprite, Fanta");
        soda.setPrice(2.00);
        soda.setCategory("DRINK");
        soda.setDishType("AMERICAN");
        soda.setToppings(new ArrayList<>());
        r6Dishes.add(soda);

        r6.setDishes(r6Dishes);
        r6.setOpeningHours(Arrays.asList(
                new OpeningHoursDTO("MONDAY", "11:30", "14:30"),
                new OpeningHoursDTO("MONDAY", "18:00", "20:00"),
                new OpeningHoursDTO("TUESDAY", "11:30", "14:30"),
                new OpeningHoursDTO("TUESDAY", "18:00", "20:00"),
                new OpeningHoursDTO("WEDNESDAY", "11:30", "14:30"),
                new OpeningHoursDTO("WEDNESDAY", "18:00", "20:00"),
                new OpeningHoursDTO("THURSDAY", "11:30", "14:30"),
                new OpeningHoursDTO("THURSDAY", "18:00", "20:00"),
                new OpeningHoursDTO("FRIDAY", "11:30", "14:30"),
                new OpeningHoursDTO("FRIDAY", "18:00", "20:00")
        ));
        restaurants.add(r6);

        // ====================== RESTAURANT 7 ======================
        RestaurantDTO r7 = new RestaurantDTO();
        r7.setId(counter.getAndIncrement());
        r7.setName("Green Leaf Café");
        r7.setCuisineType(DishType.VEGETARIAN);
        r7.setPriceRange("€€");
        r7.setEstablishmentType(EstablishmentType.RESTAURANT);
        r7.setOpen(true);

        List<DishDTO> r7Dishes = new ArrayList<>();
        DishDTO salad = new DishDTO();
        salad.setId(dishCounter.getAndIncrement());
        salad.setName("Quinoa Salad");
        salad.setDescription("Quinoa, kale, avocado, nuts");
        salad.setPrice(9.50);
        salad.setCategory("MAIN_COURSE");
        salad.setDishType("VEGETARIAN");
        salad.setToppings(Arrays.asList(
                new ToppingDTO("Extra Avocado", 1.50),
                new ToppingDTO("Tofu", 2.00)
        ));
        r7Dishes.add(salad);

        DishDTO smoothie = new DishDTO();
        smoothie.setId(dishCounter.getAndIncrement());
        smoothie.setName("Green Smoothie");
        smoothie.setDescription("Spinach, apple, banana");
        smoothie.setPrice(4.50);
        smoothie.setCategory("DRINK");
        smoothie.setDishType("VEGETARIAN");
        smoothie.setToppings(new ArrayList<>());
        r7Dishes.add(smoothie);

        r7.setDishes(r7Dishes);
        r7.setOpeningHours(Arrays.asList(
                new OpeningHoursDTO("MONDAY", "08:00", "16:00"),
                new OpeningHoursDTO("TUESDAY", "08:00", "16:00"),
                new OpeningHoursDTO("WEDNESDAY", "08:00", "16:00"),
                new OpeningHoursDTO("THURSDAY", "08:00", "16:00"),
                new OpeningHoursDTO("FRIDAY", "08:00", "16:00")
        ));
        restaurants.add(r7);

        // ====================== RESTAURANT 8 ======================
        RestaurantDTO r8 = new RestaurantDTO();
        r8.setId(counter.getAndIncrement());
        r8.setName("Dragon Wok");
        r8.setCuisineType(DishType.CHINESE);
        r8.setPriceRange("€€");
        r8.setEstablishmentType(EstablishmentType.RESTAURANT);
        r8.setOpen(true);

        List<DishDTO> r8Dishes = new ArrayList<>();
        DishDTO chowMein = new DishDTO();
        chowMein.setId(dishCounter.getAndIncrement());
        chowMein.setName("Chicken Chow Mein");
        chowMein.setDescription("Noodles with chicken and vegetables");
        chowMein.setPrice(12.00);
        chowMein.setCategory("MAIN_COURSE");
        chowMein.setDishType("CHINESE");
        chowMein.setToppings(Arrays.asList(
                new ToppingDTO("Extra Chicken", 2.50),
                new ToppingDTO("Extra Vegetables", 1.50)
        ));
        r8Dishes.add(chowMein);

        DishDTO springRolls = new DishDTO();
        springRolls.setId(dishCounter.getAndIncrement());
        springRolls.setName("Spring Rolls");
        springRolls.setDescription("Crispy vegetable rolls");
        springRolls.setPrice(5.50);
        springRolls.setCategory("STARTER");
        springRolls.setDishType("CHINESE");
        springRolls.setToppings(new ArrayList<>());
        r8Dishes.add(springRolls);

        r8.setDishes(r8Dishes);
        r8.setOpeningHours(Arrays.asList(
                new OpeningHoursDTO("MONDAY", "11:00", "15:00"),
                new OpeningHoursDTO("MONDAY", "18:00", "22:00"),
                new OpeningHoursDTO("TUESDAY", "11:00", "15:00"),
                new OpeningHoursDTO("TUESDAY", "18:00", "22:00"),
                new OpeningHoursDTO("WEDNESDAY", "11:00", "15:00"),
                new OpeningHoursDTO("WEDNESDAY", "18:00", "22:00"),
                new OpeningHoursDTO("THURSDAY", "11:00", "15:00"),
                new OpeningHoursDTO("THURSDAY", "18:00", "22:00"),
                new OpeningHoursDTO("FRIDAY", "11:00", "15:00"),
                new OpeningHoursDTO("FRIDAY", "18:00", "23:00")
        ));
        restaurants.add(r8);

        // ====================== RESTAURANT 9 ======================
        RestaurantDTO r9 = new RestaurantDTO();
        r9.setId(counter.getAndIncrement());
        r9.setName("Mediterraneo");
        r9.setCuisineType(DishType.MEDITERRANEAN);
        r9.setPriceRange("€€");
        r9.setEstablishmentType(EstablishmentType.RESTAURANT);
        r9.setOpen(true);

        List<DishDTO> r9Dishes = new ArrayList<>();
        DishDTO hummus = new DishDTO();
        hummus.setId(dishCounter.getAndIncrement());
        hummus.setName("Hummus Plate");
        hummus.setDescription("Chickpea dip with pita bread");
        hummus.setPrice(6.00);
        hummus.setCategory("STARTER");
        hummus.setDishType("MEDITERRANEAN");
        hummus.setToppings(Arrays.asList(
                new ToppingDTO("Extra Olive Oil", 0.50),
                new ToppingDTO("Paprika", 0.30)
        ));
        r9Dishes.add(hummus);

        DishDTO falafel = new DishDTO();
        falafel.setId(dishCounter.getAndIncrement());
        falafel.setName("Falafel Wrap");
        falafel.setDescription("Falafel, lettuce, tomato, tahini");
        falafel.setPrice(9.00);
        falafel.setCategory("MAIN_COURSE");
        falafel.setDishType("MEDITERRANEAN");
        falafel.setToppings(Arrays.asList(
                new ToppingDTO("Extra Tahini", 0.50)
        ));
        r9Dishes.add(falafel);

        r9.setDishes(r9Dishes);
        r9.setOpeningHours(Arrays.asList(
                new OpeningHoursDTO("MONDAY", "11:00", "15:00"),
                new OpeningHoursDTO("TUESDAY", "11:00", "15:00"),
                new OpeningHoursDTO("WEDNESDAY", "11:00", "15:00"),
                new OpeningHoursDTO("THURSDAY", "11:00", "15:00"),
                new OpeningHoursDTO("FRIDAY", "11:00", "15:00")
        ));
        restaurants.add(r9);

        // ====================== RESTAURANT 10 ======================
        RestaurantDTO r10 = new RestaurantDTO();
        r10.setId(counter.getAndIncrement());
        r10.setName("Burger Time");
        r10.setCuisineType(DishType.AMERICAN);
        r10.setPriceRange("€");
        r10.setEstablishmentType(EstablishmentType.FOOD_TRUCK);
        r10.setOpen(true);

        List<DishDTO> r10Dishes = new ArrayList<>();
        DishDTO classicBurger = new DishDTO();
        classicBurger.setId(dishCounter.getAndIncrement());
        classicBurger.setName("Classic Burger");
        classicBurger.setDescription("Beef patty, lettuce, tomato");
        classicBurger.setPrice(7.50);
        classicBurger.setCategory("MAIN_COURSE");
        classicBurger.setDishType("AMERICAN");
        classicBurger.setToppings(Arrays.asList(
                new ToppingDTO("Cheese", 1.00),
                new ToppingDTO("Bacon", 1.50)
        ));
        r10Dishes.add(classicBurger);

        DishDTO fries = new DishDTO();
        fries.setId(dishCounter.getAndIncrement());
        fries.setName("French Fries");
        fries.setDescription("Crispy golden fries");
        fries.setPrice(3.00);
        fries.setCategory("SIDE");
        fries.setDishType("AMERICAN");
        fries.setToppings(new ArrayList<>());
        r10Dishes.add(fries);

        r10.setDishes(r10Dishes);
        r10.setOpeningHours(Arrays.asList(
                new OpeningHoursDTO("MONDAY", "11:00", "14:00"),
                new OpeningHoursDTO("TUESDAY", "11:00", "14:00"),
                new OpeningHoursDTO("WEDNESDAY", "11:00", "14:00"),
                new OpeningHoursDTO("THURSDAY", "11:00", "14:00"),
                new OpeningHoursDTO("FRIDAY", "11:00", "14:00")
        ));
        restaurants.add(r10);

        // ====================== RESTAURANT 11 ======================
        RestaurantDTO r11 = new RestaurantDTO();
        r11.setId(counter.getAndIncrement());
        r11.setName("Pasta Fresca");
        r11.setCuisineType(DishType.ITALIAN);
        r11.setPriceRange("€€");
        r11.setEstablishmentType(EstablishmentType.RESTAURANT);
        r11.setOpen(true);

        List<DishDTO> r11Dishes = new ArrayList<>();
        DishDTO lasagna = new DishDTO();
        lasagna.setId(dishCounter.getAndIncrement());
        lasagna.setName("Lasagna");
        lasagna.setDescription("Layered pasta with meat sauce");
        lasagna.setPrice(13.50);
        lasagna.setCategory("MAIN_COURSE");
        lasagna.setDishType("ITALIAN");
        lasagna.setToppings(Arrays.asList(
                new ToppingDTO("Extra Cheese", 1.50)
        ));
        r11Dishes.add(lasagna);

        r11.setDishes(r11Dishes);
        r11.setOpeningHours(Arrays.asList(
                new OpeningHoursDTO("MONDAY", "12:00", "14:30"),
                new OpeningHoursDTO("TUESDAY", "12:00", "14:30"),
                new OpeningHoursDTO("WEDNESDAY", "12:00", "14:30"),
                new OpeningHoursDTO("THURSDAY", "12:00", "14:30"),
                new OpeningHoursDTO("FRIDAY", "12:00", "14:30")
        ));
        restaurants.add(r11);

        // ====================== RESTAURANT 12 ======================
        RestaurantDTO r12 = new RestaurantDTO();
        r12.setId(counter.getAndIncrement());
        r12.setName("Curry Palace");
        r12.setCuisineType(DishType.INDIAN);
        r12.setPriceRange("€€");
        r12.setEstablishmentType(EstablishmentType.RESTAURANT);
        r12.setOpen(true);

        List<DishDTO> r12Dishes = new ArrayList<>();
        DishDTO chickenTikka = new DishDTO();
        chickenTikka.setId(dishCounter.getAndIncrement());
        chickenTikka.setName("Chicken Tikka Masala");
        chickenTikka.setDescription("Spiced chicken in creamy tomato sauce");
        chickenTikka.setPrice(14.50);
        chickenTikka.setCategory("MAIN_COURSE");
        chickenTikka.setDishType("INDIAN");
        chickenTikka.setToppings(Arrays.asList(
                new ToppingDTO("Extra Naan", 2.00),
                new ToppingDTO("Rice", 3.00)
        ));
        r12Dishes.add(chickenTikka);

        DishDTO mangoLassi = new DishDTO();
        mangoLassi.setId(dishCounter.getAndIncrement());
        mangoLassi.setName("Mango Lassi");
        mangoLassi.setDescription("Mango yogurt drink");
        mangoLassi.setPrice(3.50);
        mangoLassi.setCategory("DRINK");
        mangoLassi.setDishType("INDIAN");
        mangoLassi.setToppings(new ArrayList<>());
        r12Dishes.add(mangoLassi);

        r12.setDishes(r12Dishes);
        r12.setOpeningHours(Arrays.asList(
                new OpeningHoursDTO("MONDAY", "11:30", "14:30"),
                new OpeningHoursDTO("TUESDAY", "11:30", "14:30"),
                new OpeningHoursDTO("WEDNESDAY", "11:30", "14:30"),
                new OpeningHoursDTO("THURSDAY", "11:30", "14:30"),
                new OpeningHoursDTO("FRIDAY", "11:30", "14:30")
        ));
        restaurants.add(r12);
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
}
