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
    
    // Liste des TimeSlots avec capacités
    @JsonProperty("timeSlots")
    private List<TimeSlotDTO> timeSlots;

    public RestaurantDTO() {
        this.dishes = new ArrayList<>();
        this.openingHours = new ArrayList<>();
        this.timeSlots = new ArrayList<>();
    }

    public RestaurantDTO(Long id, String name, DishType cuisineType) {
        this.id = id;
        this.name = name;
        this.cuisineType = cuisineType;
        this.dishes = new ArrayList<>();
        this.openingHours = new ArrayList<>();
        this.timeSlots = new ArrayList<>();
    }

    // ========== Getters/Setters ==========

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPriceRange() { return priceRange; }
    public void setPriceRange(String range) { this.priceRange = range; }

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

    public List<DishDTO> getDishes() {
        return dishes != null ? dishes : new ArrayList<>();
    }

    public void setDishes(List<DishDTO> dishes) {
        this.dishes = dishes != null ? dishes : new ArrayList<>();
    }

    public List<OpeningHoursDTO> getOpeningHours() {
        return openingHours != null ? openingHours : new ArrayList<>();
    }

    public void setOpeningHours(List<OpeningHoursDTO> openingHours) {
        this.openingHours = openingHours != null ? openingHours : new ArrayList<>();
    }
    
    //  NOUVEAU : Getters/Setters pour TimeSlots
    public List<TimeSlotDTO> getTimeSlots() {
        return timeSlots != null ? timeSlots : new ArrayList<>();
    }

    public void setTimeSlots(List<TimeSlotDTO> timeSlots) {
        this.timeSlots = timeSlots != null ? timeSlots : new ArrayList<>();
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public boolean isOpen() {
        return open;
    }

    @Override
    public String toString() {
        return "RestaurantDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", cuisineType='" + cuisineType + '\'' +
                ", dishes=" + (dishes != null ? dishes.size() + " dishes" : "no dishes") +
                ", timeSlots=" + (timeSlots != null ? timeSlots.size() + " slots" : "no slots") +
                '}';
    }
}