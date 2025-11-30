package fr.unice.polytech.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.unice.polytech.dishes.DishType;
import fr.unice.polytech.restaurants.EstablishmentType;

import java.util.ArrayList;
import java.util.List;

public class RestaurantDTO {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("priceRange")
    private String priceRange;

    @JsonProperty("open")
    private boolean open;

    @JsonProperty("establishmentType")
    private EstablishmentType establishmentType;

    @JsonProperty("cuisineType")
    private DishType cuisineType;

    @JsonProperty("dishes")
    private List<DishDTO> dishes;

    @JsonProperty("openingHours")
    private List<OpeningHoursDTO> openingHours;

    // ✅ FIX CRITIQUE : TOUJOURS initialiser les listes
    public RestaurantDTO() {
        this.dishes = new ArrayList<>();
        this.openingHours = new ArrayList<>();
    }

    public RestaurantDTO(Long id, String name, DishType cuisineType) {
        this.id = id;
        this.name = name;
        this.cuisineType = cuisineType;
        this.dishes = new ArrayList<>();
        this.openingHours = new ArrayList<>();
    }

    // ========== Getters/Setters ==========

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getPriceRange() { return priceRange; }

    public EstablishmentType getEstablishmentType() { return establishmentType; }

    public void setEstablishmentType(EstablishmentType establishmentType) {
        this.establishmentType = establishmentType;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public DishType getCuisineType() { return cuisineType; }

    public void setCuisineType(DishType cuisineType) {
        this.cuisineType = cuisineType;
    }

    // ✅ FIX CRITIQUE : Retourner TOUJOURS une liste (jamais null)
    public List<DishDTO> getDishes() {
        return dishes != null ? dishes : new ArrayList<>();
    }

    // ✅ FIX CRITIQUE : Accepter null et le convertir en liste vide
    public void setDishes(List<DishDTO> dishes) {
        this.dishes = dishes != null ? dishes : new ArrayList<>();
    }

    // ✅ FIX CRITIQUE : Retourner TOUJOURS une liste (jamais null)
    public List<OpeningHoursDTO> getOpeningHours() {
        return openingHours != null ? openingHours : new ArrayList<>();
    }

    // ✅ FIX CRITIQUE : Accepter null et le convertir en liste vide
    public void setOpeningHours(List<OpeningHoursDTO> openingHours) {
        this.openingHours = openingHours != null ? openingHours : new ArrayList<>();
    }

    @Override
    public String toString() {
        return "RestaurantDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", cuisineType='" + cuisineType + '\'' +
                ", dishes=" + (dishes != null ? dishes.size() + " dishes" : "no dishes") +
                '}';
    }

    public void setPriceRange(String range) {
        this.priceRange = range;
    }

    public void setOpen(boolean b) {
        this.open = b;
    }

    public boolean isOpen() {
        return open;
    }
}