package com.project.controller;

import com.project.dto.BreweryDataDTO;
import com.project.entity.BreweryData;
import com.project.service.BreweryDBService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
public class BreweryDataController {

    @Inject
    @RestClient
    BreweryDBService breweryDBService;

    public BreweryDataDTO getBreweryDataByCity(String city, int count){
        //finde zunächst heraus, ob ein Eintrag in der Datenbank vorhanden ist
        BreweryData breweryDataEntry = BreweryData.findBySearchInput(city);

        if(breweryDataEntry == null){
            //falls kein Eintrag in der Datenbank vorhanden ist, führe einen Request an die Open Brewery DB durch
            String data = breweryDBService.getBreweryDataByCity(city, count);

            if(!"[]".equals(data)) {
                //speichere die Open Brewery DB Response in der Datenbank ab
                breweryDataEntry = new BreweryData();
                breweryDataEntry.searchInput = city;
                breweryDataEntry.data = data;

                breweryDataEntry.persist();
            }
        }

        return BreweryData.convertToDTO(breweryDataEntry);
    }
}
