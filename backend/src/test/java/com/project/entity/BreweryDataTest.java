package com.project.entity;

import com.project.dto.BreweryDataDTO;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@QuarkusTest
class BreweryDataTest {

    // prüfe, ob die Datenbank Einträge abspeichert
    @Test
    @Transactional
    void testFindBySearchInputReturnsBreweryData(){
        BreweryData breweryData = new BreweryData();
        breweryData.searchInput = "TestSearchInput";
        breweryData.persist();

        BreweryData foundBreweryData = BreweryData.findBySearchInput("TestSearchInput");

        assertEquals("TestSearchInput", foundBreweryData.searchInput);
    }

    // prüfe, ob Brewery in BreweryDTO umgewandelt wird
    @Test
    void testConvertToDTO() {
        BreweryData breweryData = new BreweryData();
        breweryData.searchInput = "TestSearchInput";
        breweryData.breweries = "TestBreweries";

        BreweryDataDTO breweryDataDTO = BreweryData.convertToDTO(breweryData);

        assertEquals(breweryData.searchInput, breweryDataDTO.getSearchInput());
        assertEquals(breweryData.breweries, breweryDataDTO.getBreweries());
    }

    // prüfe, ob Umwandlung in DTO mit fehlender Entity fehlschlägt
    @Test
    void testConvertToDTOBreweryDataIsNull() {
        BreweryData breweryData = null;

        assertNull(BreweryData.convertToDTO(breweryData));
    }
}
