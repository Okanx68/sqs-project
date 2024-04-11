package com.project.boundary;

import com.project.controller.BreweryController;
import com.project.entity.Brewery;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
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
    public Response getBreweryByCity(@PathParam("cityName") String name, @QueryParam("count") int count){
        Brewery brewery = breweryController.getBreweryByCity(name, count);
        if(brewery == null){
            return Response.noContent().build();
        }
        return Response.ok(200).entity(brewery).build();

    }
}
