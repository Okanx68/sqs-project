package com.project.controller;

import com.project.dto.BreweryDataDTO;
import com.project.entity.BreweryData;
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
class BreweryDataControllerTest {

    @InjectMock
    @RestClient
    BreweryDBService breweryDBService;

    @Inject
    BreweryDataController breweryDataController;

    //teste Methode mit gefundenen Brauereien im Cache (cache hit)
    @Test
    void testGetBreweryDataByCityBreweriesFoundInOwnDatabase(){
        BreweryData breweryData = new BreweryData();
        breweryData.searchInput = "TestSearchInput";
        breweryData.breweries = "TestData";
        BreweryDataDTO breweryDataDTO = BreweryData.convertToDTO(breweryData);
        PanacheMock.mock(BreweryData.class);

        when(BreweryData.findBySearchInput("TestCity")).thenReturn(breweryData);
        when(BreweryData.convertToDTO(breweryData)).thenReturn(breweryDataDTO);

        BreweryDataDTO result = breweryDataController.getBreweryDataByCity("TestCity", 1);

        assertEquals("TestSearchInput", result.getSearchInput());
        assertEquals("TestData", result.getBreweries());
    }

    //teste Methode mit gefundenen Brauereien in der Open Brewery DB (cache miss)
    @Test
    @Transactional
    void testGetBreweryDataByCityBreweriesNotFoundInOwnDatabase() {
        String breweryData = "{\"name\":\"TestBrewery\"}";
        PanacheMock.mock(BreweryData.class);

        when(BreweryData.findBySearchInput("NotCachedCity")).thenReturn(null);
        when(breweryDBService.getBreweriesByCity("NotCachedCity", 1)).thenReturn(breweryData);
        when(BreweryData.convertToDTO(any(BreweryData.class))).thenCallRealMethod();

        BreweryDataDTO result = breweryDataController.getBreweryDataByCity("NotCachedCity", 1);

        assertEquals("NotCachedCity", result.getSearchInput());
        assertEquals(breweryData, result.getBreweries());
    }

    //teste Methode ohne gefundene Brauereien in der Open Brewery DB
    @Test
    @Transactional
    void testGetBreweryDataByCityBreweriesNotFoundByService() {
        PanacheMock.mock(BreweryData.class);

        when(BreweryData.findBySearchInput("NotCachedCity")).thenReturn(null);
        when(breweryDBService.getBreweriesByCity("NotCachedCity", 1)).thenReturn("[]");

        BreweryDataDTO result = breweryDataController.getBreweryDataByCity("NotCachedCity", 1);

        assertNull(result);
    }
}
