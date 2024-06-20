package com.project.boundary;

import com.project.controller.BreweryDataController;
import com.project.dto.BreweryDataDTO;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;


@Path("/breweries")
@Produces(MediaType.APPLICATION_JSON)
public class BreweryDataResource {

    @Inject
    BreweryDataController breweryDataController;

    @GET
    @Path("/{cityName}")
    @Operation(summary = "Get breweries by city", description = "Returns a list of breweries located in the specified city.")
    @Parameter(
            name = "cityName",
            required = true,
            description = "The name of the city to search for breweries.",
            schema = @Schema(type = SchemaType.STRING)
    )
    @Parameter(
            name = "count",
            required = true,
            description = "The maximum number of breweries to return.",
            schema = @Schema(type = SchemaType.INTEGER, defaultValue = "20")
    )
    @APIResponses(value = {
            @APIResponse(
                    responseCode = "200",
                    description = "A list of breweries",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(type = SchemaType.ARRAY, implementation = BreweryDataDTO.class)
                    )
            ),
            @APIResponse(
                    responseCode = "204",
                    description = "No content, no breweries found"
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "Bad Request"
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "Not Found"
            ),
            @APIResponse(
                    responseCode = "500",
                    description = "Internal server error"
            )
    })
    @Transactional
    public Response getBreweryDataByCity(@PathParam("cityName") String city, @QueryParam("count") int count){
        BreweryDataDTO breweryDataDTO = breweryDataController.getBreweryDataByCity(city, count);

        if(breweryDataDTO == null){
            return Response.noContent().build();
        }

        return Response.ok().entity(breweryDataDTO).build();
    }
}
