package com.project.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.entity.Brewery;
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
class BreweryResourceIntegrationTest {

    @Inject
    @RestClient
    BreweryDBService breweryDBService;

    @ParameterizedTest
    @ValueSource(strings = {"Graz", "San Diego", "Denver", "Austin", "Cincinnati"})
    void testGetBreweryByCityParametrizedIntegrationTest(String city) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        int count = 5;

        String testBreweriesFromRestClient = breweryDBService.getBreweryByCity(city, count);
        JsonNode testJson = mapper.readTree(testBreweriesFromRestClient);
        String testData = mapper.writeValueAsString(testJson);

        String response = given()
                .pathParam("cityName", city)
                .queryParam("count", count)
                .when().get("/brewery/{cityName}")
                .then()
                .statusCode(200)
                .extract().body().asString();

        Brewery testPersistedEntity = Brewery.findBySearchInput(city);

        JsonNode responseJson = mapper.readTree(response);
        String responseData = responseJson.get("data").asText();

        Assertions.assertEquals(testData, responseData);
        Assertions.assertEquals(testData, testPersistedEntity.data);
    }
}
