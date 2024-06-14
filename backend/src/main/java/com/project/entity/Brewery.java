package com.project.entity;

import com.project.dto.BreweryDTO;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Entity
@Schema(description = "Entity representing a list of breweries in a specified city")
public class Brewery extends PanacheEntity {

    @Schema(description = "The search input used to find the breweries", example = "Austin")
    public String searchInput;

    @Schema(description = "The data related to the brewery search")
    @Column(columnDefinition = "TEXT")
    public String data;

    public static Brewery findBySearchInput(String searchInput){
        return find("searchInput", searchInput).firstResult();
    }

    public static BreweryDTO convertToDTO(Brewery brewery) {
        BreweryDTO breweryDTO = new BreweryDTO();

        if(brewery == null){
            return null;
        }

        breweryDTO.setSearchInput(brewery.searchInput);
        breweryDTO.setData(brewery.data);
        return breweryDTO;
    }
}
