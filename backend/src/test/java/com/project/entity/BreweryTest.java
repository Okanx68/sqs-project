package com.project.entity;

import com.project.dto.BreweryDTO;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@QuarkusTest
class BreweryTest {

    //prüfe, ob Datenbank Einträge abspeichert
    @Test
    @Transactional
    void testFindBySearchInputReturnsBrewery(){
        Brewery brewery = new Brewery();
        brewery.searchInput = "TestSearchInput";
        brewery.persist();

        Brewery foundBrewery = Brewery.findBySearchInput("TestSearchInput");

        assertEquals("TestSearchInput", foundBrewery.searchInput);
    }

    //prüfe, ob Brewery in BreweryDTO umgewandelt wird
    @Test
    void testConvertToDTO() {
        Brewery brewery = new Brewery();
        brewery.searchInput = "TestSearchInput";
        brewery.data = "TestData";

        BreweryDTO breweryDTO = Brewery.convertToDTO(brewery);

        assertEquals(brewery.searchInput, breweryDTO.getSearchInput());
        assertEquals(brewery.data, breweryDTO.getData());
    }

    //prüfe, ob Umwandlung fehlschlägt
    @Test
    void testConvertToDTOBreweryIsNull() {
        Brewery brewery = null;

        assertNull(Brewery.convertToDTO(brewery));
    }
}
