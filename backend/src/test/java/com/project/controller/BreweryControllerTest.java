package com.project.controller;

import com.project.entity.Brewery;
import com.project.service.BreweryDBService;
import io.quarkus.panache.mock.PanacheMock;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@QuarkusTest
public class BreweryControllerTest {

    @InjectMock
    @RestClient
    BreweryDBService breweryDBService;

    @Inject
    BreweryController breweryController;

    @Test
    public void testGetBreweryByCity(){
        Brewery brewery = new Brewery();
        brewery.searchInput = "TestSearchInput";
        brewery.data = "TestData";

        PanacheMock.mock(Brewery.class);
        when(Brewery.findBySearchInput("TestCity")).thenReturn(brewery);

        Brewery result = breweryController.getBreweryByCity("TestCity", 1);

        assertEquals("TestSearchInput", result.searchInput);
        assertEquals("TestData", result.data);
    }

    @Test
    public void testGetBreweryByCityEndpointReturnsNoContent() {
        PanacheMock.mock(Brewery.class);
        when(Brewery.findBySearchInput("NonExistentCity")).thenReturn(null);

        when(breweryDBService.getBreweryByCity("NonExistentCity", 1)).thenReturn("[]");

        Brewery result = breweryController.getBreweryByCity("NonExistentCity", 1);

        assertNull(result);
    }
}
