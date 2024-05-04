package com.project.integration;

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
        String testBreweryString = new String(Files.readAllBytes(Paths.get("src/test/resources/testBrewery.json")));

        String response = given()
                .pathParam("cityName", "Graz")
                .queryParam("count", 20)
                .when().get("/brewery/{cityName}")
                .then()
                .statusCode(200)
                .extract().body().asString();

        Assertions.assertEquals(response, testBreweryString);
    }
}
