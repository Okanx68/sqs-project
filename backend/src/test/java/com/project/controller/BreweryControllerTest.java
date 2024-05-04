package com.project.controller;

import com.project.entity.Brewery;
import com.project.service.BreweryDBService;
import io.quarkus.panache.mock.PanacheMock;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@QuarkusTest
class BreweryControllerTest {

    @InjectMock
    @RestClient
    BreweryDBService breweryDBService;

    @Inject
    BreweryController breweryController;

    @Test
    void testGetBreweryByCityBreweryFound(){
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
    @Transactional
    void testGetBreweryByCityBreweryNotFound() {
        PanacheMock.mock(Brewery.class);
        when(Brewery.findBySearchInput("NotCachedCity")).thenReturn(null);

        String breweryData = "{\"name\":\"TestBrewery\"}";
        when(breweryDBService.getBreweryByCity("NotCachedCity", 1)).thenReturn(breweryData);

        Brewery result = breweryController.getBreweryByCity("NotCachedCity", 1);

        assertEquals("NotCachedCity", result.searchInput);
        assertEquals(breweryData, result.data);
    }

    @Test
    @Transactional
    void testGetBreweryByCityBreweryNotFoundByService() {
        PanacheMock.mock(Brewery.class);
        when(Brewery.findBySearchInput("NotCachedCity")).thenReturn(null);

        when(breweryDBService.getBreweryByCity("NotCachedCity", 1)).thenReturn("[]");

        Brewery result = breweryController.getBreweryByCity("NotCachedCity", 1);

        assertNull(result);
    }
}
