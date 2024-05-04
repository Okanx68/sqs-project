package com.project.boundary;

import com.project.controller.BreweryController;
import com.project.entity.Brewery;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;

@QuarkusTest
@Transactional
class BreweryResourceTest {

    @InjectMock
    BreweryController breweryController;

    @Test
    void testGetBreweryByCityEndpoint(){
        Brewery brewery = new Brewery();
        brewery.searchInput = "TestSearchInput";
        brewery.data = "TestData";

        when(breweryController.getBreweryByCity("TestCity", 1)).thenReturn(brewery);

        given()
                .pathParam("cityName", "TestCity")
                .queryParam("count", 1)
                .when()
                .get("/brewery/{cityName}")
                .then()
                .statusCode(200)
                .body("searchInput", is("TestSearchInput"),
                        "data", is("TestData"));
    }

    @Test
    void testGetBreweryByCityEndpointReturnsNoContent() {
        when(breweryController.getBreweryByCity("NonExistentCity", 1)).thenReturn(null);

        given()
                .pathParam("cityName", "NonExistentCity")
                .queryParam("count", 1)
                .when()
                .get("/brewery/{cityName}")
                .then()
                .statusCode(204);
    }
}
