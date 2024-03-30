package com.project.controller;

import com.project.entity.Brewery;
import com.project.service.BreweryDBService;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@Singleton
public class BreweryController {

    @Inject
    @RestClient
    BreweryDBService breweryDBService;

    public Brewery getBreweryByCity(String name){
        Brewery breweryEntry = Brewery.findBySearchInput(name);

        if(breweryEntry != null){
            return breweryEntry;
        } else{
            String data = breweryDBService.getBreweryByCity(name, "3");

            breweryEntry = new Brewery();
            breweryEntry.searchInput = name;
            breweryEntry.data = data;

            breweryEntry.persist();
            return breweryEntry;
        }

    }
}
