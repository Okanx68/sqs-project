package com.project.entity;

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
}
