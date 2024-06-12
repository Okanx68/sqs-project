package com.project.boundary;

import com.project.controller.BreweryController;
import com.project.dto.BreweryDTO;
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
    public Response getBreweryByCity(@PathParam("cityName") String city, @QueryParam("count") int count){
        BreweryDTO breweryDTO = breweryController.getBreweryByCity(city, count);
        if(breweryDTO == null){
            return Response.noContent().build();
        }
        return Response.ok().entity(breweryDTO).build();

    }
}
