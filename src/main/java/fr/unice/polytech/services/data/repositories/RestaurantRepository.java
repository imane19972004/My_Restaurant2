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

    public RestaurantRepository() {
        seed();
    }

    private void seed() {
        RestaurantDTO r1 = new RestaurantDTO();
        r1.setId(counter.getAndIncrement());
        r1.setName("La Bella Vita");
        r1.setCuisineType(DishType.ITALIAN);
        r1.setPriceRange("€€");
        r1.setEstablishmentType(EstablishmentType.RESTAURANT);
        r1.setOpen(true);

        List<DishDTO> r1Dishes = new ArrayList<>();
        
        // STARTER
        DishDTO bruschetta = new DishDTO();
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

        // MAIN_COURSE
        DishDTO pizza = new DishDTO();
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

        // DESSERT
        DishDTO tiramisu = new DishDTO();
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
        RestaurantDTO r2 = new RestaurantDTO();
        r2.setId(counter.getAndIncrement());
        r2.setName("Sakura Sushi");
        r2.setCuisineType(DishType.JAPANESE);
        r2.setPriceRange("€€");
        r2.setEstablishmentType(EstablishmentType.RESTAURANT);
        r2.setOpen(true);

        List<DishDTO> r2Dishes = new ArrayList<>();

        // STARTER
        DishDTO misoSoup = new DishDTO();
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

        // MAIN_COURSE
        DishDTO californiaRoll = new DishDTO();
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
        RestaurantDTO r3 = new RestaurantDTO();
        r3.setId(counter.getAndIncrement());
        r3.setName("El Sombrero");
        r3.setCuisineType(DishType.MEXICAN);
        r3.setPriceRange("€");
        r3.setEstablishmentType(EstablishmentType.FOOD_TRUCK);
        r3.setOpen(false);

        List<DishDTO> r3Dishes = new ArrayList<>();

        DishDTO tacos = new DishDTO();
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
        r3.setOpeningHours(new ArrayList<>()); // Closed - no hours
        restaurants.add(r3);

        RestaurantDTO r4 = new RestaurantDTO();
        r4.setId(counter.getAndIncrement());
        r4.setName("Bombay Spice");
        r4.setCuisineType(DishType.INDIAN);
        r4.setPriceRange("€€€");
        r4.setEstablishmentType(EstablishmentType.RESTAURANT);
        r4.setOpen(true);

        List<DishDTO> r4Dishes = new ArrayList<>();

        // STARTER
        DishDTO samosa = new DishDTO();
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

        // MAIN_COURSE
        DishDTO butterChicken = new DishDTO();
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

        // SIDE
        DishDTO naan = new DishDTO();
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

        RestaurantDTO r5 = new RestaurantDTO();
        r5.setId(counter.getAndIncrement());
        r5.setName("Le Pain Doré");
        r5.setCuisineType(DishType.FRENCH);
        r5.setPriceRange("€€");
        r5.setEstablishmentType(EstablishmentType.RESTAURANT);
        r5.setOpen(true);

        List<DishDTO> r5Dishes = new ArrayList<>();

        DishDTO croissant = new DishDTO();
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

        RestaurantDTO r6 = new RestaurantDTO();
        r6.setId(counter.getAndIncrement());
        r6.setName("Elios Crous");
        r6.setCuisineType(DishType.AMERICAN);
        r6.setPriceRange("€");
        r6.setEstablishmentType(EstablishmentType.CROUS);
        r6.setOpen(true);

        List<DishDTO> r6Dishes = new ArrayList<>();

        DishDTO burger = new DishDTO();
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

        // DRINK
        DishDTO soda = new DishDTO();
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

        RestaurantDTO r7 = new RestaurantDTO();
        r7.setId(counter.getAndIncrement());
        r7.setName("Green Leaf Café");
        r7.setCuisineType(DishType.VEGETARIAN);
        r7.setPriceRange("€€");
        r7.setEstablishmentType(EstablishmentType.CROUS);
        r7.setOpen(true);

        List<DishDTO> r7Dishes = new ArrayList<>();

        DishDTO veganBowl = new DishDTO();
        veganBowl.setName("Vegan Bowl");
        veganBowl.setDescription("Quinoa, beans, avocado");
        veganBowl.setPrice(10.90);
        veganBowl.setCategory("MAIN_COURSE");
        veganBowl.setDishType("VEGETARIAN");
        veganBowl.setToppings(Arrays.asList(
            new ToppingDTO("Extra Avocado", 2.00),
            new ToppingDTO("Hummus", 1.50),
            new ToppingDTO("Seeds Mix", 1.00)
        ));
        r7Dishes.add(veganBowl);

        DishDTO tofuStirFry = new DishDTO();
        tofuStirFry.setName("Tofu Stir Fry");
        tofuStirFry.setDescription("Tofu with vegetables");
        tofuStirFry.setPrice(9.50);
        tofuStirFry.setCategory("MAIN_COURSE");
        tofuStirFry.setDishType("VEGETARIAN");
        tofuStirFry.setToppings(Arrays.asList(
            new ToppingDTO("Extra Tofu", 2.00),
            new ToppingDTO("Peanut Sauce", 0.80)
        ));
        r7Dishes.add(tofuStirFry);

        r7.setDishes(r7Dishes);
        r7.setOpeningHours(Arrays.asList(
            new OpeningHoursDTO("MONDAY", "11:00", "15:00"),
            new OpeningHoursDTO("TUESDAY", "11:00", "15:00"),
            new OpeningHoursDTO("WEDNESDAY", "11:00", "15:00"),
            new OpeningHoursDTO("THURSDAY", "11:00", "15:00"),
            new OpeningHoursDTO("FRIDAY", "11:00", "15:00")
        ));
        restaurants.add(r7);

        RestaurantDTO r8 = new RestaurantDTO();
        r8.setId(counter.getAndIncrement());
        r8.setName("Dragon Wok");
        r8.setCuisineType(DishType.CHINESE);
        r8.setPriceRange("€");
        r8.setEstablishmentType(EstablishmentType.RESTAURANT);
        r8.setOpen(false);

        List<DishDTO> r8Dishes = new ArrayList<>();

        DishDTO kungPao = new DishDTO();
        kungPao.setName("Kung Pao Chicken");
        kungPao.setDescription("Spicy stir-fry");
        kungPao.setPrice(9.50);
        kungPao.setCategory("MAIN_COURSE");
        kungPao.setDishType("CHINESE");
        kungPao.setToppings(new ArrayList<>());
        r8Dishes.add(kungPao);

        r8.setDishes(r8Dishes);
        r8.setOpeningHours(new ArrayList<>()); // Closed
        restaurants.add(r8);

        RestaurantDTO r9 = new RestaurantDTO();
        r9.setId(counter.getAndIncrement());
        r9.setName("Beirut Mezze");
        r9.setCuisineType(DishType.ITALIAN);
        r9.setPriceRange("€€");
        r9.setEstablishmentType(EstablishmentType.FOOD_TRUCK);
        r9.setOpen(true);

        List<DishDTO> r9Dishes = new ArrayList<>();

        DishDTO hummus = new DishDTO();
        hummus.setName("Hummus");
        hummus.setDescription("Chickpea cream");
        hummus.setPrice(5.00);
        hummus.setCategory("STARTER");
        hummus.setDishType("ITALIAN");
        hummus.setToppings(Arrays.asList(
            new ToppingDTO("Extra Pita", 1.00),
            new ToppingDTO("Olive Oil", 0.50)
        ));
        r9Dishes.add(hummus);

        DishDTO shawarma = new DishDTO();
        shawarma.setName("Shawarma Plate");
        shawarma.setDescription("Chicken, rice, tahini");
        shawarma.setPrice(12.00);
        shawarma.setCategory("MAIN_COURSE");
        shawarma.setDishType("ITALIAN");
        shawarma.setToppings(Arrays.asList(
            new ToppingDTO("Extra Tahini", 1.00),
            new ToppingDTO("Pickles", 0.50)
        ));
        r9Dishes.add(shawarma);

        r9.setDishes(r9Dishes);
        r9.setOpeningHours(Arrays.asList(
            new OpeningHoursDTO("THURSDAY", "12:00", "15:00"),
            new OpeningHoursDTO("THURSDAY", "18:00", "21:00"),
            new OpeningHoursDTO("FRIDAY", "12:00", "15:00"),
            new OpeningHoursDTO("FRIDAY", "18:00", "22:00"),
            new OpeningHoursDTO("SATURDAY", "12:00", "22:00")
        ));
        restaurants.add(r9);

        RestaurantDTO r10 = new RestaurantDTO();
        r10.setId(counter.getAndIncrement());
        r10.setName("Sunny Brunch");
        r10.setCuisineType(DishType.FRENCH);
        r10.setPriceRange("€€");
        r10.setEstablishmentType(EstablishmentType.RESTAURANT);
        r10.setOpen(true);

        List<DishDTO> r10Dishes = new ArrayList<>();

        DishDTO avocadoToast = new DishDTO();
        avocadoToast.setName("Avocado Toast");
        avocadoToast.setDescription("Sourdough bread, avocado");
        avocadoToast.setPrice(9.00);
        avocadoToast.setCategory("MAIN_COURSE");
        avocadoToast.setDishType("FRENCH");
        avocadoToast.setToppings(Arrays.asList(
            new ToppingDTO("Poached Egg", 2.00),
            new ToppingDTO("Smoked Salmon", 3.50)
        ));
        r10Dishes.add(avocadoToast);

        DishDTO pancakes = new DishDTO();
        pancakes.setName("Pancakes & Syrup");
        pancakes.setDescription("3 fluffy pancakes");
        pancakes.setPrice(7.50);
        pancakes.setCategory("DESSERT");
        pancakes.setDishType("FRENCH");
        pancakes.setToppings(Arrays.asList(
            new ToppingDTO("Nutella", 1.50),
            new ToppingDTO("Berries", 2.00)
        ));
        r10Dishes.add(pancakes);

        r10.setDishes(r10Dishes);
        r10.setOpeningHours(Arrays.asList(
            new OpeningHoursDTO("SATURDAY", "09:00", "15:00"),
            new OpeningHoursDTO("SUNDAY", "09:00", "15:00")
        ));
        restaurants.add(r10);

        RestaurantDTO r11 = new RestaurantDTO();
        r11.setId(counter.getAndIncrement());
        r11.setName("Sea Breeze Grill");
        r11.setCuisineType(DishType.MEXICAN);
        r11.setPriceRange("€€€");
        r11.setEstablishmentType(EstablishmentType.RESTAURANT);
        r11.setOpen(true);

        List<DishDTO> r11Dishes = new ArrayList<>();

        DishDTO seaBass = new DishDTO();
        seaBass.setName("Grilled Sea Bass");
        seaBass.setDescription("Fresh fish with herbs");
        seaBass.setPrice(22.00);
        seaBass.setCategory("MAIN_COURSE");
        seaBass.setDishType("MEXICAN");
        seaBass.setToppings(Arrays.asList(
            new ToppingDTO("Lemon Butter", 1.50),
            new ToppingDTO("Side Vegetables", 3.00)
        ));
        r11Dishes.add(seaBass);

        DishDTO calamari = new DishDTO();
        calamari.setName("Calamari Plate");
        calamari.setDescription("Fried calamari");
        calamari.setPrice(14.50);
        calamari.setCategory("STARTER");
        calamari.setDishType("MEXICAN");
        calamari.setToppings(Arrays.asList(
            new ToppingDTO("Aioli Sauce", 1.00),
            new ToppingDTO("Lemon", 0.00)
        ));
        r11Dishes.add(calamari);

        r11.setDishes(r11Dishes);
        r11.setOpeningHours(Arrays.asList(
            new OpeningHoursDTO("WEDNESDAY", "19:00", "23:00"),
            new OpeningHoursDTO("THURSDAY", "19:00", "23:00"),
            new OpeningHoursDTO("FRIDAY", "19:00", "23:30"),
            new OpeningHoursDTO("SATURDAY", "19:00", "23:30")
        ));
        restaurants.add(r11);

        RestaurantDTO r12 = new RestaurantDTO();
        r12.setId(counter.getAndIncrement());
        r12.setName("Pasta Corner");
        r12.setCuisineType(DishType.ITALIAN);
        r12.setPriceRange("€");
        r12.setEstablishmentType(EstablishmentType.RESTAURANT);
        r12.setOpen(true);

        List<DishDTO> r12Dishes = new ArrayList<>();

        DishDTO penneArrabiata = new DishDTO();
        penneArrabiata.setName("Penne Arrabiata");
        penneArrabiata.setDescription("Tomato and chili");
        penneArrabiata.setPrice(8.00);
        penneArrabiata.setCategory("MAIN_COURSE");
        penneArrabiata.setDishType("ITALIAN");
        penneArrabiata.setToppings(Arrays.asList(
            new ToppingDTO("Extra Chili", 0.00),
            new ToppingDTO("Parmesan", 1.00)
        ));
        r12Dishes.add(penneArrabiata);

        DishDTO tagliaPesto = new DishDTO();
        tagliaPesto.setName("Tagliatelle Pesto");
        tagliaPesto.setDescription("Basil pesto");
        tagliaPesto.setPrice(9.50);
        tagliaPesto.setCategory("MAIN_COURSE");
        tagliaPesto.setDishType("ITALIAN");
        tagliaPesto.setToppings(Arrays.asList(
            new ToppingDTO("Pine Nuts", 1.50),
            new ToppingDTO("Cherry Tomatoes", 1.00)
        ));
        r12Dishes.add(tagliaPesto);

        r12.setDishes(r12Dishes);
        r12.setOpeningHours(Arrays.asList(
            new OpeningHoursDTO("MONDAY", "11:00", "15:00"),
            new OpeningHoursDTO("MONDAY", "18:00", "21:00"),
            new OpeningHoursDTO("TUESDAY", "11:00", "15:00"),
            new OpeningHoursDTO("TUESDAY", "18:00", "21:00"),
            new OpeningHoursDTO("WEDNESDAY", "11:00", "15:00"),
            new OpeningHoursDTO("WEDNESDAY", "18:00", "21:00"),
            new OpeningHoursDTO("THURSDAY", "11:00", "15:00"),
            new OpeningHoursDTO("THURSDAY", "18:00", "21:00"),
            new OpeningHoursDTO("FRIDAY", "11:00", "15:00"),
            new OpeningHoursDTO("FRIDAY", "18:00", "22:00")
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