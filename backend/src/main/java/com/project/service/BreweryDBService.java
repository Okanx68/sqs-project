package com.project.service;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.QueryParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient
public interface BreweryDBService {

    //führe einen Request an die Open Brewery DB durch, um Brauereien für eine bestimmte Stadt zu erhalten
    @GET
    String getBreweriesByCity(@QueryParam("by_city") String city, @QueryParam("per_page") int count);

}
