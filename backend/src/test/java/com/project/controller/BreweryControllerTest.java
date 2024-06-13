package com.project.controller;

import com.project.dto.BreweryDTO;
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
import static org.mockito.ArgumentMatchers.any;
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
        BreweryDTO breweryDTO = Brewery.convertToDTO(brewery);

        PanacheMock.mock(Brewery.class);
        when(Brewery.findBySearchInput("TestCity")).thenReturn(brewery);
        when(Brewery.convertToDTO(brewery)).thenReturn(breweryDTO);

        BreweryDTO result = breweryController.getBreweryByCity("TestCity", 1);

        assertEquals("TestSearchInput", result.getSearchInput());
        assertEquals("TestData", result.getData());
    }

    @Test
    @Transactional
    void testGetBreweryByCityBreweryNotFound() {
        PanacheMock.mock(Brewery.class);
        when(Brewery.findBySearchInput("NotCachedCity")).thenReturn(null);

        String breweryData = "{\"name\":\"TestBrewery\"}";
        when(breweryDBService.getBreweryByCity("NotCachedCity", 1)).thenReturn(breweryData);
        when(Brewery.convertToDTO(any(Brewery.class))).thenCallRealMethod();

        BreweryDTO result = breweryController.getBreweryByCity("NotCachedCity", 1);

        assertEquals("NotCachedCity", result.getSearchInput());
        assertEquals(breweryData, result.getData());
    }

    @Test
    @Transactional
    void testGetBreweryByCityBreweryNotFoundByService() {
        PanacheMock.mock(Brewery.class);
        when(Brewery.findBySearchInput("NotCachedCity")).thenReturn(null);

        when(breweryDBService.getBreweryByCity("NotCachedCity", 1)).thenReturn("[]");

        BreweryDTO result = breweryController.getBreweryByCity("NotCachedCity", 1);

        assertNull(result);
    }
}
