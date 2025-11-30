package fr.unice.polytech.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.unice.polytech.dishes.DishType;
import fr.unice.polytech.restaurants.EstablishmentType;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO for transferring restaurant data via REST APIs
 * Used by Catalog Service to send restaurant information to frontend 
 * Corresponds to TD requirement: "consulter les restaurants et leurs menus"
 */
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
    private DishType cuisineType; // ITALIAN, FRENCH, JAPANESE, etc.
    
    @JsonProperty("dishes")
    private List<DishDTO> dishes;
    
    @JsonProperty("openingHours")
    private List<OpeningHoursDTO> openingHours;
    
    // ========== Constructors ==========
    
    public RestaurantDTO() {
        // Required by Jackson for deserialization

        //  AJOUTÉ : Initialiser les listes pour éviter null
        this.dishes = new ArrayList<>();
        this.openingHours = new ArrayList<>();
    }
    
    public RestaurantDTO(Long id, String name, DishType cuisineType) {
        this.id = id;
        this.name = name;
        this.cuisineType = cuisineType;

        this.dishes = new ArrayList<>();  //  Initialiser
        this.openingHours = new ArrayList<>();  //  Initialiser
    }
    
    // ========== Getters/Setters (required by Jackson) ==========
    
    public Long getId() {        return id;    }
    
    public void setId(Long id) {        this.id = id;    }

    public String getPriceRange() {        return priceRange;    }

    public EstablishmentType getEstablishmentType() {        return establishmentType;    }

    public void setEstablishmentType(EstablishmentType establishmentType) {
        this.establishmentType = establishmentType;
    }

    public String getName() {        return name;    }
    
    public void setName(String name) {        this.name = name;    }
    
    public DishType getCuisineType() {        return cuisineType;    }
    
    public void setCuisineType(DishType cuisineType) {
        this.cuisineType = cuisineType;
    }
    
    public List<DishDTO> getDishes() {       return dishes != null ? dishes : new ArrayList<>();    }
    
    public void setDishes(List<DishDTO> dishes) {
        this.dishes = dishes != null ? dishes : new ArrayList<>();
    }
    
    public List<OpeningHoursDTO> getOpeningHours() {
        return openingHours;
    }
    
    public void setOpeningHours(List<OpeningHoursDTO> openingHours) {
        this.openingHours = openingHours != null ? openingHours : new ArrayList<>();  //  Sécurité
    }
    
    // ========== toString for debugging ==========
    
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