package fr.unice.polytech.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Generic DTO for paginated API responses
 * Contains data + pagination metadata
 * 
 * @param <T> Type of items in the response
 */
public class PaginatedResponseDTO<T> {
    
    @JsonProperty("data")
    private List<T> data;
    
    @JsonProperty("page")
    private int page;
    
    @JsonProperty("limit")
    private int limit;
    
    @JsonProperty("totalItems")
    private int totalItems;
    
    @JsonProperty("totalPages")
    private int totalPages;
    
    @JsonProperty("hasNextPage")
    private boolean hasNextPage;
    
    @JsonProperty("hasPreviousPage")
    private boolean hasPreviousPage;
    
    // ========== Constructors ==========
    
    public PaginatedResponseDTO() {
        // Required by Jackson
    }
    
    public PaginatedResponseDTO(List<T> data, int page, int limit, int totalItems) {
        this.data = data;
        this.page = page;
        this.limit = limit;
        this.totalItems = totalItems;
        this.totalPages = (int) Math.ceil((double) totalItems / limit);
        this.hasNextPage = page < totalPages;
        this.hasPreviousPage = page > 1;
    }
    
    // ========== Getters/Setters ==========
    
    public List<T> getData() {        return data;    }
    
    public void setData(List<T> data) {        this.data = data;    }
    
    public int getPage() {        return page;    }
    
    public void setPage(int page) {        this.page = page;    }
    
    public int getLimit() {        return limit;    }
    
    public void setLimit(int limit) {        this.limit = limit;    }
    
    public int getTotalItems() {        return totalItems;    }
    
    public void setTotalItems(int totalItems) {       this.totalItems = totalItems;    }
    
    public int getTotalPages() {        return totalPages;    }
    
    public void setTotalPages(int totalPages) {       this.totalPages = totalPages;    }
    
    public boolean isHasNextPage() {        return hasNextPage;    }
    
    public void setHasNextPage(boolean hasNextPage) {        this.hasNextPage = hasNextPage;    }
    
    public boolean isHasPreviousPage() {        return hasPreviousPage;    }
    
    public void setHasPreviousPage(boolean hasPreviousPage) {        this.hasPreviousPage = hasPreviousPage;    }
    
    @Override
    public String toString() {
        return "PaginatedResponseDTO{" +
                "page=" + page +
                ", limit=" + limit +
                ", totalItems=" + totalItems +
                ", totalPages=" + totalPages +
                ", hasNextPage=" + hasNextPage +
                ", hasPreviousPage=" + hasPreviousPage +
                '}';
    }
}