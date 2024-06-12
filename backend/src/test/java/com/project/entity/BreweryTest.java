package com.project.entity;

import com.project.dto.BreweryDTO;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
class BreweryTest {

    @Test
    @Transactional
    void testFindBySearchInputReturnsBrewery(){
        Brewery brewery = new Brewery();
        brewery.searchInput = "TestSearchInput";
        brewery.persist();

        Brewery foundBrewery = Brewery.findBySearchInput("TestSearchInput");

        assertEquals("TestSearchInput", foundBrewery.searchInput);
    }

    @Test
    void testConvertToDTO() {
        Brewery brewery = new Brewery();
        brewery.searchInput = "TestSearchInput";
        brewery.data = "TestData";

        BreweryDTO breweryDTO = Brewery.convertToDTO(brewery);

        assertEquals(brewery.searchInput, breweryDTO.searchInput);
        assertEquals(brewery.data, breweryDTO.data);
    }
}
