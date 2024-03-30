package com.project.boundary;

import com.project.controller.BreweryController;
import com.project.entity.Brewery;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


@Path("/brewery")
@Produces(MediaType.APPLICATION_JSON)
public class BreweryResource {

    @Inject
    BreweryController breweryController;

    @GET
    @Path("/{cityName}")
    @Transactional
    public Response getBreweryByCity(@PathParam("cityName") String name){

        Brewery brewery = breweryController.getBreweryByCity(name);
        return Response.ok(200).entity(brewery).build();

    }

}
