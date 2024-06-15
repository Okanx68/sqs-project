package com.project.entity;

import com.project.dto.BreweryDataDTO;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Entity
@Schema(description = "Entity representing a list of breweries in a specified city")
public class BreweryData extends PanacheEntity {

    @Schema(description = "The search input used to find the breweries", example = "Austin")
    public String searchInput;

    @Schema(description = "The breweries related to the brewery search")
    @Column(columnDefinition = "TEXT")
    public String breweries;

    public static BreweryData findBySearchInput(String searchInput){
        return find("searchInput", searchInput).firstResult();
    }

    //wandle Brewery in BreweryDTO um
    public static BreweryDataDTO convertToDTO(BreweryData breweryData) {
        BreweryDataDTO breweryDataDTO = new BreweryDataDTO();

        if(breweryData == null){
            return null;
        }

        breweryDataDTO.setSearchInput(breweryData.searchInput);
        breweryDataDTO.setBreweries(breweryData.breweries);
        return breweryDataDTO;
    }
}
