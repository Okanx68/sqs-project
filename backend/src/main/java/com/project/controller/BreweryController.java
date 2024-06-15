package com.project.controller;

import com.project.dto.BreweryDTO;
import com.project.entity.Brewery;
import com.project.service.BreweryDBService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
public class BreweryController {

    @Inject
    @RestClient
    BreweryDBService breweryDBService;

    public BreweryDTO getBreweryByCity(String city, int count){
        //finde zunächst heraus, ob ein Eintrag in der Datenbank vorhanden ist
        Brewery breweryEntry = Brewery.findBySearchInput(city);

        if(breweryEntry == null){
            //falls kein Eintrag in der Datenbank vorhanden ist, führe einen Request an die Open Brewery DB durch
            String data = breweryDBService.getBreweryByCity(city, count);

            if(!"[]".equals(data)) {
                //speichere die Open Brewery DB Response in der Datenbank ab
                breweryEntry = new Brewery();
                breweryEntry.searchInput = city;
                breweryEntry.data = data;

                breweryEntry.persist();
            }
        }

        return Brewery.convertToDTO(breweryEntry);
    }
}
