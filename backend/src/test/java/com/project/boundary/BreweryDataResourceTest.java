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

    //Endpoint prüfen
    @Test
    void testGetBreweryDataByCityEndpoint(){
        BreweryData breweryData = new BreweryData();
        breweryData.searchInput = "TestSearchInput";
        breweryData.data = "TestData";
        BreweryDataDTO breweryDataDTO = BreweryData.convertToDTO(breweryData);

        when(breweryDataController.getBreweryDataByCity("TestCity", 1)).thenReturn(breweryDataDTO);

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

    //Endpoint mit einer Stadt prüfen, die nicht in der Open Brewery DB vorhanden ist
    @Test
    void testGetBreweryDataByCityEndpointReturnsNoContent() {
        when(breweryDataController.getBreweryDataByCity("NonExistentCity", 1)).thenReturn(null);

        given()
                .pathParam("cityName", "NonExistentCity")
                .queryParam("count", 1)
                .when()
                .get("/brewery/{cityName}")
                .then()
                .statusCode(204);
    }
}
