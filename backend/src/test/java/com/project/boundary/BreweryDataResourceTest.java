package com.project.boundary;

import com.project.controller.BreweryDataController;
import com.project.dto.BreweryDataDTO;
import com.project.entity.BreweryData;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;

@QuarkusTest
@Transactional
class BreweryDataResourceTest {

    @InjectMock
    BreweryDataController breweryDataController;

    // Endpunkt prüfen
    @Test
    void testGetBreweryDataByCityEndpoint(){
        String testCityName = "TestCity";
        int count = 1;
        BreweryData breweryData = new BreweryData();
        breweryData.searchInput = "TestSearchInput";
        breweryData.breweries = "TestBreweries";
        BreweryDataDTO breweryDataDTO = BreweryData.convertToDTO(breweryData);

        when(breweryDataController.getBreweryDataByCity(testCityName, count)).thenReturn(breweryDataDTO);

        given()
                .pathParam("cityName", testCityName)
                .queryParam("count", count)
                .when()
                .get("/breweries/{cityName}")
                .then()
                .statusCode(200)
                .body("searchInput", is("TestSearchInput"),
                        "breweries", is("TestBreweries"));
    }

    // Endpunkt mit einer Stadt prüfen, die nicht in der Open Brewery DB vorhanden ist
    @Test
    void testGetBreweryDataByCityEndpointReturnsNoContent() {
        String testNonExistentCityName = "NonExistentCity";
        int count = 1;

        when(breweryDataController.getBreweryDataByCity(testNonExistentCityName, count)).thenReturn(null);

        given()
                .pathParam("cityName", testNonExistentCityName)
                .queryParam("count", count)
                .when()
                .get("/breweries/{cityName}")
                .then()
                .statusCode(204);
    }
}
