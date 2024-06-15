package com.project.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.entity.BreweryData;
import com.project.service.BreweryDBService;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;

import static io.restassured.RestAssured.given;

@QuarkusTest
class BreweryDataResourceIntegrationTest {

    @Inject
    @RestClient
    BreweryDBService breweryDBService;

    //führe einen komplett parametrisierten Integrationstest durch
    @ParameterizedTest
    @ValueSource(strings = {"Graz", "San Diego", "Denver", "Austin", "Cincinnati"})
    void testGetBreweryDataByCityParametrizedIntegrationTest(String city) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        int count = 5;

        //erhalte eine Response aus der Open Brewery DB
        String testBreweriesFromRestClient = breweryDBService.getBreweryDataByCity(city, count);
        JsonNode testJson = mapper.readTree(testBreweriesFromRestClient);
        String testData = mapper.writeValueAsString(testJson);

        //erhalte eine Response vom eigenen API Endpoint
        String response = given()
                .pathParam("cityName", city)
                .queryParam("count", count)
                .when().get("/brewery/{cityName}")
                .then()
                .statusCode(200)
                .extract().body().asString();

        //prüfe, ob Brauereien in der Datenbank abgespeichert wurden
        BreweryData testPersistedEntity = BreweryData.findBySearchInput(city);

        JsonNode responseJson = mapper.readTree(response);
        String responseData = responseJson.get("data").asText();

        //gleiche Datenbankergebnis und Response mit der Open Brewery DB Response ab
        Assertions.assertEquals(testData, responseData);
        Assertions.assertEquals(testData, testPersistedEntity.data);
    }
}
