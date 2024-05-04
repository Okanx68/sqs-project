package com.project.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.given;

@QuarkusTest
class BreweryResourceIntegrationTest {

    @Test
    void testGetBreweryByCityIntegrationTest() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode testBreweryJson = mapper.readTree(Files.readAllBytes(Paths.get("src/test/resources/testBrewery.json")));
        JsonNode expectedData = testBreweryJson.get("data");

        String response = given()
                .pathParam("cityName", "Graz")
                .queryParam("count", 20)
                .when().get("/brewery/{cityName}")
                .then()
                .statusCode(200)
                .extract().body().asString();

        JsonNode responseJson = mapper.readTree(response);
        JsonNode responseData = responseJson.get("data");

        Assertions.assertEquals(expectedData, responseData);
    }
}
