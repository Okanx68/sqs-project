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

    public Brewery getBreweryByCity(String name, int count){
        Brewery breweryEntry = Brewery.findBySearchInput(name);

        if(breweryEntry == null){
            String data = breweryDBService.getBreweryByCity(name, count);

            if(!data.isEmpty()) {
                breweryEntry = new Brewery();
                breweryEntry.searchInput = name;
                breweryEntry.data = data;

                breweryEntry.persist();
            }
        }

        return breweryEntry;
    }
}
