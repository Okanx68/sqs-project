package com.project.service;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.QueryParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient
public interface BreweryDBService {

    @GET
    String getBreweryByCity(@QueryParam("by_city") String city, @QueryParam("per_page") int count);
}
