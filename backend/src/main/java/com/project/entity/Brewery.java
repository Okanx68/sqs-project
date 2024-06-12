package com.project.entity;

import com.project.dto.BreweryDTO;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class Brewery extends PanacheEntity {

    public String searchInput;

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

        breweryDTO.searchInput = brewery.searchInput;
        breweryDTO.data = brewery.data;
        return breweryDTO;
    }
}
