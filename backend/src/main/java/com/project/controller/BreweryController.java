package com.project.controller;

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

    public Brewery getBreweryByCity(String city, int count){
        Brewery breweryEntry = Brewery.findBySearchInput(city);

        if(breweryEntry == null){
            String data = breweryDBService.getBreweryByCity(city, count);

            if(!"[]".equals(data)) {
                breweryEntry = new Brewery();
                breweryEntry.searchInput = city;
                breweryEntry.data = data;

                breweryEntry.persist();
            }
        }

        return breweryEntry;
    }
}
