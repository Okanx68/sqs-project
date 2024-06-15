package com.project.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(description = "Data Transfer Object representing a list of breweries in a specified city")
public class BreweryDataDTO {

    @Schema(description = "The search input used to find the breweries", example = "Austin")
    private String searchInput;

    @Schema(description = "The breweries related to the brewery search")
    private String breweries;

    public String getSearchInput() {
        return searchInput;
    }

    public void setSearchInput(String searchInput) {
        this.searchInput = searchInput;
    }

    public String getBreweries() {
        return breweries;
    }

    public void setBreweries(String breweries) {
        this.breweries = breweries;
    }
}
